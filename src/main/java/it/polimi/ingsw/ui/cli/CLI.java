package it.polimi.ingsw.ui.cli;

import it.polimi.ingsw.client.NetworkAdapter;
import it.polimi.ingsw.events.ClientEvents.DepositLeaderPowerStateEvent;
import it.polimi.ingsw.events.ClientEvents.DepotState;
import it.polimi.ingsw.events.Event;
import it.polimi.ingsw.model.LeaderCards.DepositLeaderPower;
import it.polimi.ingsw.model.LeaderCards.LeaderPower;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.utilities.Pair;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CLI {
    private final NetworkAdapter client;
    private static PrintWriter out = new PrintWriter(System.out, true);
    private static Scanner in = new Scanner(System.in);

    private String thisPlayer;
    private FaithTrackView faithTrack;
    private HashMap<String, DashBoardView> dashboards;
    private ArrayList<String> players;
    private DevCardGridView devCardGridView;
    private HashMap<String, HashMap<String, LeaderCardView>> leaderCards;
    private MarketView market;

    public CLI(NetworkAdapter client) {
        this.client = client;
        players = new ArrayList<>();
        players = players.stream().map(name -> name.toUpperCase()).collect(Collectors.toCollection(ArrayList::new));
        faithTrack = new FaithTrackView(players);
        dashboards = new HashMap<>();
        leaderCards = new HashMap<>();
        for (String player : players) {
            leaderCards.put(player, new HashMap<String, LeaderCardView>());
            dashboards.put(player, null);
        }

    }


    public void displayBadRequest(String playerID, String description, Event cause) {
        out.println(" Player" + playerID + ", your request, caused by event:" + cause.getEventName() + " cannot be satisfied:"
                + description);
    }

    public void displayCantAffordError(String id) {
        out.println(thisPlayer + ", IT SEEMS YOU DON'T HAVE THE REQUIRED RESOURCES TO BUY " + id.toUpperCase() + ", TRY AGAIN");
        DrawableObject obj = new DrawableObject(new DevCardView(id).toString(), 50, 0);
        DrawableObject resources = new DrawableObject(dashboards.get(thisPlayer).resourceNumberToString(), 0, (int) obj.getHeight() + 3);
        Panel panel = new Panel(250, (int) obj.getHeight() + (int) resources.getHeight() + 4, out);
        panel.addItem(obj);
        panel.addItem(resources);
        panel.show();

    }

  /*public static void main(String[] args) {
        DepotState depot = new DepotState(Resource.COIN, 3, 2);
        DepotState depot2 = new DepotState(Resource.SERVANT, 4, 2);
        DepotState depot3 = new DepotState(Resource.SHIELD, 6, 4);
        ArrayList<DepotState> totalLevels = new ArrayList();
        totalLevels.add(depot);
        totalLevels.add(depot2);
        totalLevels.add(depot3);

        HashMap<Resource, Integer> str = new HashMap<>();
        str.put(Resource.COIN, 6);
        str.put(Resource.ROCK, 7);

        ArrayList<String> cards = new ArrayList<>();
        cards.add("DevCard10");
        cards.add("DevCard40");
        cards.add("DevCard16");

        String player = "PAOLO";

        DashBoardView d = new DashBoardView(cards, str, totalLevels, player);
        String id= "DevCard10";
        out.println("LISA"+ ", IT SEEMS YOU DON'T HAVE THE REQUIRED RESOURCES TO BUY "+id.toUpperCase()+", TRY AGAIN");
        DrawableObject obj= new DrawableObject(new DevCardView(id).toString(),50, 0);
        DrawableObject resources= new DrawableObject(d.resourceNumberToString(),0, (int)obj.getHeight()+3 );
        Panel panel= new Panel(250, (int)obj.getHeight()+(int)resources.getHeight()+4, out);
        panel.addItem(obj);
        panel.addItem(resources);
        panel.show();
    }*/

    public static ArrayList<Integer> displaySelectionForm(ArrayList<Pair<String, String>> option_itsColor, Panel displayPanel, int numberOfOptionsToChose) {
        String resetColor = Color.WHITE.getAnsiCode();
        StringBuilder builder = new StringBuilder();
        builder.append("YOU HAVE TO CHOOSE " + numberOfOptionsToChose +
                " OF THE FOLLOWING OPTIONS \n");

        if (displayPanel != null) {
            displayPanel.show();
        }

        AtomicInteger n = new AtomicInteger(1);
        option_itsColor.stream().forEach(option -> {

            builder.append("╔═" + resetColor);
            IntStream.range(0, option.getKey().length()).forEach(letter -> builder.append("═"));
            builder.append(resetColor + "═╗  ");
        });
        builder.append("\n");

        option_itsColor.stream().forEach(option -> {
            String color = option.getValue();
            builder.append("║ " + color);
            builder.append(option.getKey());
            builder.append(resetColor + " ║  ");
        });
        builder.append("\n");

        option_itsColor.stream().forEach(option -> {
            builder.append("╚═" + resetColor);
            IntStream.range(0, option.getKey().length()).forEach(letter -> builder.append("═"));
            builder.append(resetColor + "═╝  ");
        });
        builder.append("\n");

        option_itsColor.stream().forEach(option -> {
            builder.append("  " + resetColor);
            builder.append(n.get());
            IntStream.range(0, option.getKey().length() - 1).forEach(letter -> builder.append(" "));
            builder.append(resetColor + "    ");
            n.getAndIncrement();
        });
        builder.append("\n");

        DrawableObject obj = new DrawableObject(builder.toString(), 50, 0);
        Panel panel = new Panel(1000, (int) obj.getHeight() + 3, out);
        panel.addItem(obj);
        panel.show();

        int size = n.intValue() - 1;
        int m = numberOfOptionsToChose;
        out.println("ENTER THE NUMBER OF THE SELECTED ITEMS");
        String inputString;
        ArrayList<Integer> inputs = new ArrayList<>();
        while (m > 0) {
            inputString = in.next();
            if (inputString.matches("-?\\d+")) {
                int input = Integer.parseInt(inputString);
                if (inputs.contains(input)) {
                    out.println("YOU HAVE ALREADY CHOSEN THIS RESOURCE");
                } else if (input < 1 || input > size) {
                    out.println("THIS NUMBER OF CHOICE DOESN'T EXIST, TRY WITH A NUMBER BETWEEN 1 AND " + size);
                } else {
                    inputs.add(input);
                    m--;
                }
            } else {
                out.println("PLEASE INSERT A NUMBER");
            }
        }

        out.println("YOU HAVE CHOSEN: \n");
        StringBuilder builder2 = new StringBuilder();
        inputs = inputs.stream().map(integer -> integer - 1).collect(Collectors.toCollection(ArrayList::new));

        inputs.forEach(index -> {
            builder2.append(index + 1 + " -> " + option_itsColor.get(index).getValue() + option_itsColor.get(index).getKey() + Color.reset() + "\n");
        });
        out.println(builder2.toString());

        out.println("DO YOU AGREE? yes/no");
        String response = in.next().toUpperCase();
        if (response.equals("YES")) {
            return inputs;
        } else return displaySelectionForm(option_itsColor, displayPanel, numberOfOptionsToChose);

    }


   /* public static void main(String[] args) {
        int numberOfOptionsToChose = 1;
        ArrayList<Pair<String, String>> options = new ArrayList<>();
        ArrayList<String> resourceTypes = new ArrayList<>();
//        options.add(new Pair<String, String>(shapeResource(Resource.COIN), colorResource(Resource.COIN)));
//        options.add(new Pair<String, String>(shapeResource(Resource.SHIELD), colorResource(Resource.SHIELD)));
//        options.add(new Pair<String, String>(shapeResource(Resource.SERVANT), colorResource(Resource.SERVANT)));
//        options.add(new Pair<String, String>(shapeResource(Resource.COIN), colorResource(Resource.COIN)));
//        options.add(new Pair<String, String>(shapeResource(Resource.ROCK), colorResource(Resource.ROCK)));
//
//        options.add(new Pair<String, String>("FRANK", colorResource(Resource.COIN)));
//        options.add(new Pair<String, String>("GIO", colorResource(Resource.SHIELD)));
//        options.add(new Pair<String, String>("PETER 200", colorResource(Resource.SERVANT)));
//        options.add(new Pair<String, String>("lIZ", colorResource(Resource.COIN)));
//        options.add(new Pair<String, String>("katy100", colorResource(Resource.ROCK)));

        Panel displayPanel = new Panel(1000, 15, System.out);
        IntStream.range(1, 4).forEach(n -> {
            LeaderCardView card = new LeaderCardView("LeaderCard" + n);
            options.add(new Pair<String, String>("LeaderCard" + n, Color.BLUE.getAnsiCode()));
            DrawableObject obj1 = new DrawableObject(card.toString(), 40 * (n - 1)+30, 0);
            displayPanel.addItem(obj1);
        });


        String resetColor = Color.WHITE.getAnsiCode();
        StringBuilder builder = new StringBuilder();
        builder.append("YOU HAVE TO CHOOSE " + numberOfOptionsToChose +
                " OF THE FOLLOWING OPTIONS \n");

        if (displayPanel != null) {
            displayPanel.show();
        }

        AtomicInteger n = new AtomicInteger(1);
        options.stream().forEach(option -> {

            builder.append("╔═" + resetColor);
            IntStream.range(0, option.getKey().length()).forEach(letter -> builder.append("═"));
            builder.append(resetColor + "═╗  ");
        });
        builder.append("\n");

        options.stream().forEach(option -> {
            String color = option.getValue();
            builder.append("║ " + color);
            builder.append(option.getKey());
            builder.append(resetColor + " ║  ");
        });
        builder.append("\n");

        options.stream().forEach(option -> {
            builder.append("╚═" + resetColor);
            IntStream.range(0, option.getKey().length()).forEach(letter -> builder.append("═"));
            builder.append(resetColor + "═╝  ");
        });
        builder.append("\n");

        options.stream().forEach(option -> {
            builder.append("  " + resetColor);
            builder.append(n.get());
            IntStream.range(0, option.getKey().length() - 1).forEach(letter -> builder.append(" "));
            builder.append(resetColor + "    ");
            n.getAndIncrement();
        });
        builder.append("\n");

        DrawableObject obj = new DrawableObject(builder.toString(), 50, 0);
        Panel panel = new Panel(1000, (int) obj.getHeight() + 3, out);
        panel.addItem(obj);
        panel.show();

        int size = n.intValue() - 1;
        int m = numberOfOptionsToChose;
        out.println("ENTER THE NUMBER OF THE SELECTED ITEMS");
        int input;
        ArrayList<Integer> inputs = new ArrayList<>();
        while (m > 0) {
                input = in.nextInt();
                if (inputs.contains(input)) {
                    out.println("YOU HAVE ALREADY CHOSEN THIS RESOURCE");
                } else if (input < 1 || input > size) {
                    out.println("THIS NUMBER OF CHOICE DOESN'T EXIST, TRY WITH A NUMBER BETWEEN 1 AND " + size);
                } else {
                    inputs.add(input);
                    m--;
                }

        }

        out.println("YOU HAVE CHOSEN: \n");
        StringBuilder builder2 = new StringBuilder();

        inputs.forEach(index -> {
            builder2.append(index + " -> " + options.get(index-1).getKey() + "\n");
        });
        System.out.println(builder2.toString());

        out.println("DO YOU AGREE? yes/no");
        String response = in.next().toUpperCase();
        if (response.equals("YES")) {
            //generate response event
        }

    }*/


    public void displayChooseMultipleExtraResourcePowerForm(ArrayList<Resource> resourceTypes, int numberOfResources) {
        out.println("SOME ACTIVE EXTRA RESOURCE POWER ALLOWS YOU \n " +
                "TO ADD " + numberOfResources + " RESOURCES TO YOUR STORE! \n ");
        displayResourcesChoiceForm(resourceTypes, numberOfResources);
    }

    public void displayDashBoardState(String playerId) {
        out.println("One dashboard has been updated!");
        if (dashboards.get(playerId) != null) {
            dashboards.get(playerId).displayAll(playerId.toUpperCase());
        }

    }


    public void displayDevCardGridState() {
        out.println("The market has been updated!");
        if (market != null) {
            DrawableObject obj = new DrawableObject(market.toString(), 10, 2);
            Panel panel = new Panel(100, (int) obj.getHeight() + 10, System.out);
            panel.addItem(obj);
            panel.show();
        }
    }


    public void displayFaithTrack(String causeMessage, String causePlayer) {
        out.println("The faithTrack has been updated!");
        faithTrack.display(causeMessage, causePlayer);
    }


    public void displayInitialChoiceForm(ArrayList<String> leaderCardsIDs, int numberOFLeaderCardsToChose, int numberOFResourcesToChose) {
        ArrayList<Integer> indexes = leaderCardsIDs.stream().map(name -> Integer.parseInt(name.substring(10))).collect(Collectors.toCollection(ArrayList::new));


        out.println(thisPlayer.toUpperCase() + ", CHOOSE " + numberOFLeaderCardsToChose + " AMONG THESE LEADER CARDS:");
        Panel panel = new Panel(1000, 15, out);
        AtomicInteger n = new AtomicInteger();
        leaderCardsIDs.stream().forEach(name -> {
            LeaderCardView card = new LeaderCardView(name);
            DrawableObject obj1 = new DrawableObject(card.toString(), 50 * (n.get()) + 10, 0);
            n.getAndIncrement();
            panel.addItem(obj1);
        });
        panel.show();
        ArrayList<Integer> cardIndexes = new ArrayList<Integer>();
        while (numberOFLeaderCardsToChose > 0) {
            out.println("YOU HAVE " + numberOFLeaderCardsToChose + " CARDS LEFT TO CHOOSE,\n PLEASE TYPE THE NUMBER OF ONE CARD: ");
            int input = in.nextInt();
            while (input < indexes.get(0) || input > indexes.get(indexes.size() - 1)) {
                out.println("WRONG NUMBER, PLEASE RETRY TYPING THE NUMBER OF ONE CARD: ");
                input = in.nextInt();
            }
            if (cardIndexes.contains(input)) {
                out.println("YOU HAVE ALREADY SELECTED THIS NUMBER, PLEASE RETRY TYPING THE NUMBER OF ONE CARD: ");
                input = in.nextInt();
            }
            cardIndexes.add(input);
            numberOFLeaderCardsToChose--;
        }
        out.println("YOU HAVE CHOSEN THIS LEADER CARDS:");
        Panel panel2 = new Panel(1000, 15, out);
        n.set(0);
        HashMap<String, LeaderCardView> leaderCardsChosen = new HashMap<>();
        cardIndexes.stream().forEach(index -> {
            LeaderCardView card = new LeaderCardView("LeaderCard" + index);
            leaderCardsChosen.put("LeaderCard" + index, card);
            DrawableObject obj1 = new DrawableObject(card.toString(), 40 * (n.get()) + 10, 0);
            n.getAndIncrement();
            panel2.addItem(obj1);
        });
        panel2.show();

        out.println("DO YOU AGREE? yes/no");
        String response = in.next().toUpperCase();
        if (response.equals("YES")) {
            leaderCards.put(thisPlayer, leaderCardsChosen);
            ArrayList<Resource> allResources = Arrays.stream(Resource.values()).collect(Collectors.toCollection(ArrayList::new));
            displayResourcesChoiceForm(allResources, numberOFResourcesToChose);
        } else displayInitialChoiceForm(leaderCardsIDs, numberOFLeaderCardsToChose, numberOFResourcesToChose);
    }
  /*  public static void main(String[] args) {
        String player = "PAOLO";

        ArrayList<String> ids = new ArrayList<>();
        ids.add("LeaderCard10");
        ids.add("LeaderCard11");
        ids.add("LeaderCard12");
        ids.add("LeaderCard13");

        ArrayList<Integer> indexes= ids.stream().map(name->Integer.parseInt(name.substring(10))).collect(Collectors.toCollection(ArrayList::new));

        int numberOFLeaderCardsToChose=3;

        out.println(player.toUpperCase() + ", CHOOSE " + numberOFLeaderCardsToChose + " AMONG THESE LEADER CARDS:");
        Panel panel = new Panel(1000, 15, out);
        AtomicInteger n = new AtomicInteger();
        ids.stream().forEach(name -> {
            LeaderCardView card = new LeaderCardView(name);
            DrawableObject obj1 = new DrawableObject(card.toString(), 50 * (n.get()) + 10, 0);
            n.getAndIncrement();
            panel.addItem(obj1);
        });
        panel.show();
        ArrayList<Integer> cardIndexes = new ArrayList<Integer>();
        while (numberOFLeaderCardsToChose > 0) {
            out.println("YOU HAVE " + numberOFLeaderCardsToChose + " CARDS LEFT TO CHOOSE,\n PLEASE TYPE THE NUMBER OF ONE CARD: ");
            int input=in.nextInt();
            while(input<indexes.get(0) || input > indexes.get(indexes.size()-1)){
                out.println("WRONG NUMBER, PLEASE RETRY TYPING THE NUMBER OF ONE CARD: ");
                input=in.nextInt();
            }
            if(cardIndexes.contains(input)){
                out.println("YOU HAVE ALREADY SELECTED THIS NUMBER, PLEASE RETRY TYPING THE NUMBER OF ONE CARD: ");
                input=in.nextInt();
            }
            cardIndexes.add(input);
            numberOFLeaderCardsToChose--;
        }
        out.println("YOU HAVE CHOSEN THIS LEADER CARDS:");
        Panel panel2 = new Panel(1000, 15, out);
        n.set(0);
        ArrayList<LeaderCardView> leaderCardsChosen = new ArrayList<>();
        cardIndexes.stream().forEach(index -> {
            LeaderCardView card = new LeaderCardView("LeaderCard" + index);
            leaderCardsChosen.add(card);
            DrawableObject obj1 = new DrawableObject(card.toString(), 40 * (n.get()) + 10, 0);
            n.getAndIncrement();
            panel2.addItem(obj1);
        });
        panel2.show();

        out.println("DO YOU AGREE? yes/no");
        String response = in.next().toUpperCase();
        if (response.equals("YES")) {
           System.out.println("good");
        } else  System.out.println("bad");
    }*/


    public void displayResourcesChoiceForm(ArrayList<Resource> resourceType, int numberOFResources) {
        HashMap<Resource, Integer> initialResources = new HashMap<>();
        for (Resource res : resourceType) {
            initialResources.put(res, 0);
        }
        out.println(thisPlayer.toUpperCase() + ", YOU HAVE TO CHOOSE " + numberOFResources + " RESOURCES AMONG:");
        resourceType.stream().forEach(el -> out.println(colorResource(el) + el.toString().toUpperCase() + "\n" + Color.reset()));

        for (Resource res : resourceType) {
            if (res != resourceType.get(resourceType.size() - 1) && numberOFResources > 0) {
                out.println("HOW MANY " + colorResource(res) + res.toString() + Color.reset() + " WOULD YOU LIKE? INSERT A NUMBER (" + numberOFResources + " LEFT)");
                int n = in.nextInt();
                while (numberOFResources - n < 0) {
                    out.println("RETRY, HOW MANY " + colorResource(res) + res.toString() + Color.reset() + "S WOULD YOU LIKE? INSERT A NUMBER");
                    n = in.nextInt();
                }
                initialResources.put(res, n);
                numberOFResources = numberOFResources - n;
            }
        }
        if (numberOFResources >= 0) {
            if (numberOFResources == 0) out.println("YOU HAVE " + numberOFResources + " RESOURCES LEFT");
            else {
                out.println("YOU HAVE " + numberOFResources + " RESOURCES LEFT: THESE WILL BE " + colorResource(resourceType.get(resourceType.size() - 1)) + resourceType.get(resourceType.size() - 1).toString() + Color.reset());
                initialResources.put(resourceType.get(resourceType.size() - 1), numberOFResources);
            }
        }
        out.println("YOU HAVE CHOSEN THIS RESOURCES:");
        StringBuilder builder = new StringBuilder();

        initialResources.keySet().forEach(resource -> {
            String color = colorResource(resource);
            String shape = shapeResource(resource);
            builder.append(color + resource.toString() + ": ");
            IntStream.range(0, initialResources.get(resource)).forEach(n -> builder.append(color + shape + " "));
            builder.append(Color.reset() + "\n");
        });
        out.println(builder.toString());
        out.println("DO YOU AGREE? yes/no");
        String response = in.next().toUpperCase();
        if (response == "YES") {
            //put in dashboard event
            out.println("resources put in dashboard");
        } else displayResourcesChoiceForm(resourceType, numberOFResources);
    }

    /*  public static void main(String[] args) {
          String thisPlayer = "paolo";
          int numberOFResources = 7;
          ArrayList<Resource> resourceType = new ArrayList<>();
          resourceType.add(Resource.COIN);
          resourceType.add(Resource.ROCK);
         // resourceType.add(Resource.SERVANT);
          resourceType.add(Resource.SHIELD);

          HashMap<Resource, Integer> initialResources = new HashMap<>();
          for (Resource res : resourceType) {
              initialResources.put(res, 0);
          }
          out.println(thisPlayer.toUpperCase() + ", YOU HAVE TO CHOOSE " + numberOFResources + " RESOURCES AMONG:");
          resourceType.stream().forEach(el->out.println( colorResource(el)+ el.toString().toUpperCase()+"\n"+ Color.reset()));

          for (Resource res : resourceType) {
              if (res != resourceType.get(resourceType.size() - 1) && numberOFResources > 0) {
                  out.println("HOW MANY " +colorResource(res) + res.toString()+ Color.reset()  + " WOULD YOU LIKE? INSERT A NUMBER (" + numberOFResources + " LEFT)");
                  int n = in.nextInt();
                  while (numberOFResources - n < 0) {
                      out.println("RETRY, HOW MANY "+colorResource(res) + res.toString()+ Color.reset() + "S WOULD YOU LIKE? INSERT A NUMBER");
                      n = in.nextInt();
                  }
                  initialResources.put(res, n);
                  numberOFResources = numberOFResources - n;
              }
          }
          if (numberOFResources >= 0) {
              if (numberOFResources == 0) out.println("YOU HAVE " + numberOFResources + " RESOURCES LEFT");
              else {
                  out.println("YOU HAVE " + numberOFResources + " RESOURCES LEFT: THESE WILL BE " +colorResource(resourceType.get(resourceType.size() - 1))+ resourceType.get(resourceType.size() - 1).toString()+ Color.reset());
                  initialResources.put(resourceType.get(resourceType.size() - 1), numberOFResources);
              }
          }
          out.println("YOU HAVE CHOSEN THIS RESOURCES:");
          StringBuilder builder = new StringBuilder();

          initialResources.keySet().forEach(resource -> {
              String color = colorResource(resource);
              String shape = shapeResource(resource);
              builder.append(color + resource.toString() + ": ");
              IntStream.range(0, initialResources.get(resource)).forEach(n -> builder.append(color + shape + " "));
              builder.append(Color.reset() + "\n");
          });
          out.println(builder.toString());
          out.println("DO YOU AGREE? yes/no");
          String response = in.next().toUpperCase();
          if (response.equals("YES")) {
              //put in dashboard event
              out.println("resources put in dashboard");
          }

      }
  */
    public void updateDepositLeaderPowerState(String leaderCardID, int leaderPowerIndex, HashMap<Resource, Integer> storedResources) {
        out.println(thisPlayer + " , THE DEPOSIT LEADER POWER OF " + leaderCardID.toUpperCase() + "\n HAS BEEN UPDATED! CHECK IT OUT!");
        leaderCards.get(thisPlayer).get(leaderCardID).updateDepositLeaderPower(leaderPowerIndex, storedResources);
        out.println(leaderCards.get(thisPlayer).get(leaderCardID).toString());
    }

    public void displayDevCardSlotError(String devCardID, int cardSlot) {
        out.println("SORRY, " + devCardID + " CANNOT BE ADDED TO THE SELECTED SLOT, SINCE IT \n  DOES NOT FULFILL THE SPECIFIED LEVEL REQUIREMENTS");
        DevCardView card = new DevCardView(devCardID);
        out.println(card.toString());
        dashboards.get(thisPlayer).displayDevCardSlots();
    }

    public void displayIncompatiblePowersError(String leaderCardID, int leaderPowerIndex) {
        LeaderCardView card = leaderCards.get(thisPlayer).get(leaderCardID);
        System.out.println(card.getLeaderPowerName(leaderPowerIndex) + "IN" + leaderCardID.toUpperCase() + " IS NOT COMPATIBLE WITH OTHER SELECTED POWERS");
        System.out.println(card.toString());

    }

    public void displayLeaderCardNotActiveError(String leaderCardID) {
        LeaderCardView card = leaderCards.get(thisPlayer).get(leaderCardID);
        System.out.println("SORRY, " + leaderCardID.toUpperCase() + " IS NOT ACTIVE, YOU CAN'T USE IT");
        System.out.println(card.toString());
    }

    public void displayLobbyState(String leaderID, ArrayList<String> otherPlayersID) {
        System.out.println("THIS IS THE CURRENT STATE OF THE LOBBY:");
        StringBuilder builder = new StringBuilder();
        builder.append(Color.RED.getAnsiCode() + "╔═");
        IntStream.range(0, leaderID.length()).forEach(letter -> builder.append("═"));
        builder.append("═╗  " + Color.WHITE.getAnsiCode());

        otherPlayersID.stream().forEach(name -> {
            builder.append("╔═");
            IntStream.range(0, name.length()).forEach(letter -> builder.append("═"));
            builder.append("═╗  ");
        });
        builder.append("\n");

        builder.append(Color.RED.getAnsiCode() + "║ ");
        builder.append(leaderID);
        builder.append(" ║  " + Color.WHITE.getAnsiCode());

        otherPlayersID.stream().forEach(name -> {
            builder.append("║ ");
            builder.append(name);
            builder.append(" ║  ");
        });
        builder.append("\n");

        builder.append(Color.RED.getAnsiCode() + "╚═");
        IntStream.range(0, leaderID.length()).forEach(letter -> builder.append("═"));
        builder.append("═╝  " + Color.WHITE.getAnsiCode());

        otherPlayersID.stream().forEach(name -> {
            builder.append("╚═");
            IntStream.range(0, name.length()).forEach(letter -> builder.append("═"));
            builder.append("═╝  ");
        });

        DrawableObject obj = new DrawableObject(builder.toString(), 50, 0);
        Panel panel = new Panel(500, (int) obj.getHeight() + 3, out);
        panel.addItem(obj);
        panel.show();
    }
/*
    public static void main(String[] args) {
        String leaderID = "Steve100";
        ArrayList<String> otherPlayersID = new ArrayList<>();
        otherPlayersID.add("Michael");
        otherPlayersID.add("FILIP");
        otherPlayersID.add("GIAN20034");


        System.out.println("THIS IS THE CURRENT STATE OF THE LOBBY:");
        StringBuilder builder = new StringBuilder();
        builder.append(Color.RED.getAnsiCode() + "╔═");
        IntStream.range(0, leaderID.length()).forEach(letter -> builder.append("═"));
        builder.append("═╗  " + Color.WHITE.getAnsiCode());

        otherPlayersID.stream().forEach(name -> {
            builder.append("╔═");
            IntStream.range(0, name.length()).forEach(letter -> builder.append("═"));
            builder.append("═╗  ");
        });
        builder.append("\n");

        builder.append(Color.RED.getAnsiCode() + "║ ");
        builder.append(leaderID);
        builder.append(" ║  " + Color.WHITE.getAnsiCode());

        otherPlayersID.stream().forEach(name -> {
            builder.append("║ ");
            builder.append(name);
            builder.append(" ║  ");
        });
        builder.append("\n");

        builder.append(Color.RED.getAnsiCode() + "╚═");
        IntStream.range(0, leaderID.length()).forEach(letter -> builder.append("═"));
        builder.append("═╝  " + Color.WHITE.getAnsiCode());

        otherPlayersID.stream().forEach(name -> {
            builder.append("╚═");
            IntStream.range(0, name.length()).forEach(letter -> builder.append("═"));
            builder.append("═╝  ");
        });

        DrawableObject obj = new DrawableObject(builder.toString(), 50, 0);
        Panel panel = new Panel(500, (int) obj.getHeight() + 3, out);
        panel.addItem(obj);
        panel.show();
    }
*/

    public void displayMatchState() {

    }

    public void displayOrganizeResourcesForm(HashMap<Resource, Integer> resourcesToOrganize) {

        //Parameters to giveback to the NewResourcesOrganizationEvent
        ArrayList<DepotState> depotStates;
        ArrayList<DepositLeaderPowerStateEvent> leaderPowersState;
        HashMap<Resource, Integer> discardedResources = new HashMap<>();
        //-----------------------------------------------------
        //Image of the current state of depots
        ArrayList<DepotState> currentDepotStates = dashboards.get(thisPlayer).getWarehouse();
        //Initially the depotState to give back to model is the current state of this dashboard.
        depotStates = currentDepotStates;
        //Takes only the active LeaderCards
        ArrayList<LeaderCardView> leaderCardViews = leaderCards.get(thisPlayer).values().stream().collect(Collectors.toCollection(ArrayList::new));
        //Selects only DepositLeaderPowers active
        ArrayList<InfoPower> thisDepositLeaderPowers = new ArrayList<>();

        leaderCardViews.stream().forEach(cardView -> {
            if (cardView.getSelected()) {
                IntStream.range(0, cardView.getLeaderPowersActive().size()).forEach(index -> {
                    LeaderPower power = cardView.getLeaderPowersActive().get(index);
                    if (power instanceof DepositLeaderPower)
                        thisDepositLeaderPowers.add(new InfoPower((DepositLeaderPower) power, cardView.getIdCard(), index));
                });
            }
        });
        // Initially the DepositLeaderPower states to give back to the model are the current states of leaderPower.

        //display the resources available for storing
        StringBuilder builder0 = new StringBuilder();
        builder0.append(" YOU  CAN NOW STORE THE FOLLOWING RESOURCES \n\n");
        resourcesToOrganize.keySet().forEach(resource -> {
            String color = colorResource(resource);
            String shape = shapeResource(resource);
            builder0.append(color + resource.toString() + ": ");
            IntStream.range(0, resourcesToOrganize.get(resource)).forEach(n -> builder0.append(color + shape + " "));
            builder0.append(Color.WHITE.getAnsiCode() + " --> " + resourcesToOrganize.get(resource) + "\n");
        });
        out.println(builder0.toString());
        System.out.println("IF YOU DON'T WANT TO STORE ANY RESOURCES OF THESE,PLEASE TYPE 'DONE' OTHERWISE TYPE 'CONTINUE'");
        if (in.next().toUpperCase().equals("DONE")) {
            //GENERATE NEW NewResourcesOrganizationEvent
            out.println("Done");
            out.println(depotStates.size());
            out.println(thisDepositLeaderPowers.size());
            resourcesToOrganize.keySet().forEach(entry -> discardedResources.put(entry, resourcesToOrganize.get(entry)));
            out.println(discardedResources.size());

        } else {
            // prepare panel to display current state of all deposits.
            StringBuilder builder = new StringBuilder();
            builder.append("\n\n");
            builder.append("THESE ARE THE AVAILABLE PLACES \n:" + dashboards.get(thisPlayer).warehouseToString() + "\n\n");
            leaderCards.get(thisPlayer).values().forEach(card -> builder.append(card.depositPowersToString()));
            DrawableObject obj = new DrawableObject(builder.toString(), 2, 0);
            Panel panel = new Panel(1000, (int) obj.getHeight() + 2, out);
            panel.addItem(obj);

            // select one resource at a time among those available
            ArrayList<Pair<String, String>> resourcesOptions = new ArrayList<>();
            ArrayList<Resource> justResources = new ArrayList<>();

            resourcesToOrganize.keySet().forEach(resType -> {
                IntStream.range(0, resourcesToOrganize.get(resType)).forEach(n -> {
                    resourcesOptions.add(new Pair<>(shapeResource(resType), colorResource(resType)));
                    justResources.add(resType);
                });
            });
            // the chosen resource needs to be assigned to one of the deposits
            int inputResource = displaySelectionForm(resourcesOptions, panel, 1).get(0);
            System.out.println("WHERE DO YOU WANT TO PUT THIS RESOURCE ( " + resourcesOptions.get(inputResource).getValue() + resourcesOptions.get(inputResource).getKey() + Color.reset() + " )?");

            // prepare the selection form for deposits.
            ArrayList<Pair<String, String>> depositOptions = new ArrayList<>();
            IntStream.range(0, currentDepotStates.size()).forEach(n -> depositOptions.add(new Pair<String, String>("DEPOT " + (n + 1), colorResource(currentDepotStates.get(n).getResourceType()))));

            IntStream.range(0, thisDepositLeaderPowers.size()).forEach(n -> {
                depositOptions.add(new Pair<String, String>("LEADER POWER " + (n+1), Color.WHITE.getAnsiCode()));
            });

            //result of transition
            AtomicBoolean successfull = new AtomicBoolean(false);
            //inputDeposit is the result of the selection of one destination among deposits
            int inputDeposit = displaySelectionForm(depositOptions, null, 1).get(0);
            //if the selected index belongs to depots...
            if (inputDeposit < currentDepotStates.size()) {
                DepotResultMessage result = currentDepotStates.get(inputDeposit).tryAddResource(justResources.get(inputResource));
                successfull.set(result.getSuccessfull());
                out.println(result.getMessage());
            }
            //or if it belongs to leader card extra depots...
            else {
                int index = inputDeposit - currentDepotStates.size();
                HashMap<Resource, Integer> resInput = new HashMap<>();
                resInput.put(justResources.get(inputResource), 1);

                String cardId = thisDepositLeaderPowers.get(index).getCardId();
                leaderCardViews.stream().forEach(cardView -> {
                    if (cardView.getIdCard() == thisDepositLeaderPowers.get(index).getCardId()) {
                        DepotResultMessage result = cardView.updateDepositLeaderPower(thisDepositLeaderPowers.get(index).getPowerIndex(), justResources.get(inputResource));
                        successfull.set(result.getSuccessfull());
                        out.println(result.getMessage());
                    }
                });
            }
            if (successfull.get()) {
                resourcesToOrganize.put(justResources.get(inputResource), (resourcesToOrganize.get(justResources.get(inputResource)) - 1));
                displayOrganizeResourcesForm(resourcesToOrganize);
            }
        }
    }

    private static class InfoPower {
        private DepositLeaderPower power;
        private String cardId;
        private int powerIndex;

        public InfoPower(DepositLeaderPower power, String cardId, int powerIndex) {
            this.power = power;
            this.powerIndex = powerIndex;
            this.cardId = cardId;
        }

        public DepositLeaderPower getPower() {
            return power;
        }

        public String getCardId() {
            return cardId;
        }

        public int getPowerIndex() {
            return powerIndex;
        }


    }

    public static void main(String[] args) {
        HashMap<String, DashBoardView> dashboards = new HashMap<>();
        DepotState depot = new DepotState(Resource.COIN, 3, 3);
        DepotState depot2 = new DepotState(Resource.SERVANT, 4, 2);
        DepotState depot3 = new DepotState(Resource.SHIELD, 6, 4);
        ArrayList<DepotState> totalLevels = new ArrayList();
        totalLevels.add(depot);
        totalLevels.add(depot2);
        totalLevels.add(depot3);

        HashMap<Resource, Integer> str = new HashMap<>();
        str.put(Resource.COIN, 6);
        str.put(Resource.ROCK, 7);

        ArrayList<String> cards = new ArrayList<>();
        cards.add("DevCard10");
        cards.add("DevCard40");
        cards.add("DevCard16");

        String thisPlayer = "PAOLO";

        DashBoardView d = new DashBoardView(cards, str, totalLevels, thisPlayer);
        dashboards.put(thisPlayer, d);

        HashMap<String, HashMap<String, LeaderCardView>> leaderCards = new HashMap<>();
        HashMap<String, LeaderCardView> leaderCardsSet = new HashMap<>();
        LeaderCardView card1 = new LeaderCardView("LeaderCard5");
        card1.setSelected(true);
        leaderCardsSet.put("LeaderCard5", card1);
        LeaderCardView card2 = new LeaderCardView("LeaderCard6");
        card2.setSelected(true);
        leaderCardsSet.put("LeaderCard6", card2);
        leaderCards.put(thisPlayer, leaderCardsSet);

        HashMap<Resource, Integer> resourcesToOrganize = new HashMap<>();
        resourcesToOrganize.put(Resource.SHIELD, 3);
        resourcesToOrganize.put(Resource.ROCK, 5);
        resourcesToOrganize.put(Resource.COIN, 2);
        resourcesToOrganize.put(Resource.SERVANT, 4);


        //Parameters to giveback to the NewResourcesOrganizationEvent
        ArrayList<DepotState> depotStates;
        ArrayList<DepositLeaderPowerStateEvent> leaderPowersState;
        HashMap<Resource, Integer> discardedResources = new HashMap<>();
        //-----------------------------------------------------
        //Image of the current state of depots
        ArrayList<DepotState> currentDepotStates = dashboards.get(thisPlayer).getWarehouse();
        //Initially the depotState to give back to model is the current state of this dashboard.
        depotStates = currentDepotStates;
        //Takes only the active LeaderCards
        ArrayList<LeaderCardView> leaderCardViews = leaderCards.get(thisPlayer).values().stream().collect(Collectors.toCollection(ArrayList::new));
        //Selects only DepositLeaderPowers active
        ArrayList<InfoPower> thisDepositLeaderPowers = new ArrayList<>();

        leaderCardViews.stream().forEach(cardView -> {
            if (cardView.getSelected()) {
                IntStream.range(0, cardView.getLeaderPowersActive().size()).forEach(index -> {
                    LeaderPower power = cardView.getLeaderPowersActive().get(index);
                    if (power instanceof DepositLeaderPower)
                        thisDepositLeaderPowers.add(new InfoPower((DepositLeaderPower) power, cardView.getIdCard(), index));
                });
            }
        });
        // Initially the DepositLeaderPower states to give back to the model are the current states of leaderPower.

        //display the resources available for storing
        StringBuilder builder0 = new StringBuilder();
        builder0.append(" YOU  CAN NOW STORE THE FOLLOWING RESOURCES \n\n");
        resourcesToOrganize.keySet().forEach(resource -> {
            String color = colorResource(resource);
            String shape = shapeResource(resource);
            builder0.append(color + resource.toString() + ": ");
            IntStream.range(0, resourcesToOrganize.get(resource)).forEach(n -> builder0.append(color + shape + " "));
            builder0.append(Color.WHITE.getAnsiCode() + " --> " + resourcesToOrganize.get(resource) + "\n");
        });
        out.println(builder0.toString());
        System.out.println("IF YOU DON'T WANT TO STORE ANY RESOURCES OF THESE,PLEASE TYPE 'DONE' OTHERWISE TYPE 'CONTINUE'");
        if (in.next().toUpperCase().equals("DONE")) {
            //GENERATE NEW NewResourcesOrganizationEvent
            out.println("Done");
            out.println(depotStates.size());
            out.println(thisDepositLeaderPowers.size());
            resourcesToOrganize.keySet().forEach(entry -> discardedResources.put(entry, resourcesToOrganize.get(entry)));
            out.println(discardedResources.size());

        } else {
            // prepare panel to display current state of all deposits.
            StringBuilder builder = new StringBuilder();
            builder.append("\n\n");
            builder.append("THESE ARE THE AVAILABLE PLACES \n:" + dashboards.get(thisPlayer).warehouseToString() + "\n\n");
            leaderCards.get(thisPlayer).values().forEach(card -> builder.append(card.depositPowersToString()));
            DrawableObject obj = new DrawableObject(builder.toString(), 2, 0);
            Panel panel = new Panel(1000, (int) obj.getHeight() + 2, out);
            panel.addItem(obj);

            // select one resource at a time among those available
            ArrayList<Pair<String, String>> resourcesOptions = new ArrayList<>();
            ArrayList<Resource> justResources = new ArrayList<>();

            resourcesToOrganize.keySet().forEach(resType -> {
                IntStream.range(0, resourcesToOrganize.get(resType)).forEach(n -> {
                    resourcesOptions.add(new Pair<>(shapeResource(resType), colorResource(resType)));
                    justResources.add(resType);
                });
            });
            // the chosen resource needs to be assigned to one of the deposits
            int inputResource = displaySelectionForm(resourcesOptions, panel, 1).get(0);
            System.out.println("WHERE DO YOU WANT TO PUT THIS RESOURCE ( " + resourcesOptions.get(inputResource).getValue() + resourcesOptions.get(inputResource).getKey() + Color.reset() + " )?");

            // prepare the selection form for deposits.
            ArrayList<Pair<String, String>> depositOptions = new ArrayList<>();
            IntStream.range(0, currentDepotStates.size()).forEach(n -> depositOptions.add(new Pair<String, String>("DEPOT " + (n + 1), colorResource(currentDepotStates.get(n).getResourceType()))));

            IntStream.range(0, thisDepositLeaderPowers.size()).forEach(n -> {
                depositOptions.add(new Pair<String, String>("LEADER POWER " + (n+1), Color.WHITE.getAnsiCode()));
            });

            //result of transition
            AtomicBoolean successfull = new AtomicBoolean(false);
            //inputDeposit is the result of the selection of one destination among deposits
            int inputDeposit = displaySelectionForm(depositOptions, null, 1).get(0);
            //if the selected index belongs to depots...
            if (inputDeposit < currentDepotStates.size()) {
                DepotResultMessage result = currentDepotStates.get(inputDeposit).tryAddResource(justResources.get(inputResource));
                successfull.set(result.getSuccessfull());
                out.println(result.getMessage());
            }
            //or if it belongs to leader card extra depots...
            else {
                int index = inputDeposit - currentDepotStates.size();
                HashMap<Resource, Integer> resInput = new HashMap<>();
                resInput.put(justResources.get(inputResource), 1);

                String cardId = thisDepositLeaderPowers.get(index).getCardId();
                leaderCardViews.stream().forEach(cardView -> {
                    if (cardView.getIdCard() == thisDepositLeaderPowers.get(index).getCardId()) {
                        DepotResultMessage result = cardView.updateDepositLeaderPower(thisDepositLeaderPowers.get(index).getPowerIndex(), justResources.get(inputResource));
                        successfull.set(result.getSuccessfull());
                        out.println(result.getMessage());
                    }
                });
            }
            if (successfull.get()) {
                resourcesToOrganize.put(justResources.get(inputResource), (resourcesToOrganize.get(justResources.get(inputResource)) - 1));
                //displayOrganizeResourcesForm( resourcesToOrganize);
                out.println(resourcesToOrganize.toString());
                out.println(currentDepotStates.get(0).getCurrentQuantity());
                out.println(currentDepotStates.get(1).getCurrentQuantity());
                out.println(currentDepotStates.get(2).getCurrentQuantity());
                out.println(card1.getLeaderPowersActive().get(0));
                out.println(((DepositLeaderPower) card1.getLeaderPowersActive().get(0)).getCurrentResources());
                out.println(((DepositLeaderPower) card2.getLeaderPowersActive().get(0)).getCurrentResources());


            } else {
                out.println(resourcesToOrganize.toString());
                out.println(currentDepotStates.get(0).getCurrentQuantity());
                out.println(currentDepotStates.get(1).getCurrentQuantity());
                out.println(currentDepotStates.get(2).getCurrentQuantity());
                out.println(((DepositLeaderPower) card1.getLeaderPowersActive().get(0)).getCurrentResources());
                out.println(((DepositLeaderPower) card2.getLeaderPowersActive().get(0)).getCurrentResources());
            }
        }
    }


    public void displayPlayerState() {

    }

    public void displayRequirementsNotMetError() {

    }

    public void displaySetupDone() {

    }

    public void displaySimpleChoseResourcesForm() {

    }


  public static String colorResource(Resource res) {
        if (res == Resource.SERVANT) return Color.BLUE.getAnsiCode();
        if (res == Resource.SHIELD) return Color.RED.getAnsiCode();
        if (res == Resource.COIN) return Color.GREEN.getAnsiCode();
        else return Color.YELLOW.getAnsiCode();
    }

    public static String shapeResource(Resource res) {
        if (res == Resource.SERVANT) return "■";
        if (res == Resource.SHIELD) return "◆";
        if (res == Resource.COIN) return "●";
        else return "▼";
    }


}
