package it.polimi.ingsw.ui.cli;

import it.polimi.ingsw.events.ClientEvents.DepositLeaderPowerStateEvent;
import it.polimi.ingsw.events.ClientEvents.DepotState;
import it.polimi.ingsw.events.ControllerEvents.MatchEvents.*;
import it.polimi.ingsw.events.Event;
import it.polimi.ingsw.exceptions.NotPresentException;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.FaithTrack.PopeFavorCard;
import it.polimi.ingsw.model.LeaderCards.DepositLeaderPower;
import it.polimi.ingsw.model.LeaderCards.LeaderPower;
import it.polimi.ingsw.ui.PlayerState;
import it.polimi.ingsw.ui.UI;
import it.polimi.ingsw.utilities.Pair;

import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CLI extends UI {
    private static PrintWriter out = new PrintWriter(System.out, true);
    private static Scanner in = new Scanner(System.in);

    private FaithTrackView faithTrack;
    private HashMap<String, PlayerState> playerStates;
    private ArrayList<String> players;
    private DevCardGridView devCardGridView;
    private MarketView market;


    public CLI() {
        players = new ArrayList<>();
        playerStates = new HashMap<>();
    }

    public static boolean isAffirmative(String s) {
        return s.equalsIgnoreCase("YES") ||
                s.equalsIgnoreCase("Y");
    }

    public void displayBadRequest(String playerID, String description, Event cause) {
        out.println(" Player" + playerID + ", your request, caused by event:" + cause.getEventName() + " cannot be satisfied:"
                + description);
    }

    public void displayCantAffordError(String id) {
        out.println(thisPlayer + ", IT SEEMS YOU DON'T HAVE THE REQUIRED RESOURCES TO BUY " + id.toUpperCase() + ", TRY AGAIN");
        DrawableObject obj = new DrawableObject(new DevCardView(id).toString(), 50, 0);
        DrawableObject resources = new DrawableObject(playerStates.get(thisPlayer).dashBoard.resourceNumberToString(), 0, (int) obj.getHeight() + 3);
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

    public static ArrayList<Integer> displaySelectionForm(ArrayList<Pair<String, String>> option_itsColor, Panel displayPanel, int numberOfOptionsToChose, String message) {
        String resetColor = Color.WHITE.getAnsiCode();
        StringBuilder builder = new StringBuilder();
        builder.append(message);
        builder.append("YOU HAVE TO CHOOSE " + numberOfOptionsToChose +
                " OF THE FOLLOWING OPTIONS \n");

        if (displayPanel != null) {
            displayPanel.show();
        }

        builder.append(generateForm(option_itsColor, resetColor));

        DrawableObject obj = new DrawableObject(builder.toString(), 0, 0);
        Panel panel = new Panel(1000, (int) obj.getHeight() + 3, out);
        panel.addItem(obj);
        panel.show();

        int size = option_itsColor.size();
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
        String answer = in.next().toUpperCase();
        in.nextLine();
        if (isAffirmative(answer)) {
            return inputs;
        } else {
            return displaySelectionForm(option_itsColor, displayPanel, numberOfOptionsToChose, message);
        }

    }

    /*private static String generateFormORIGINAL(ArrayList<Pair<String, String>> option_itsColor, String resetColor) {
        StringBuilder builder = new StringBuilder();
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

        return builder.toString();
    }*/

    private static String generateForm(ArrayList<Pair<String, String>> option_itsColor, String resetColor) {
        StringBuilder builderTop = new StringBuilder();
        StringBuilder builderBot = new StringBuilder();
        StringBuilder builderEnd = new StringBuilder();

        int maxLines = option_itsColor.stream().map(Pair::getKey).map(s -> s.chars().filter(ch -> ch == '\n').map(ch -> 1).sum()).max(Integer::compareTo).orElse(-1) + 1;
        ArrayList<StringBuilder> buildersMid = new ArrayList<>();
        for (int i = 0; i < maxLines; i++) {
            buildersMid.add(new StringBuilder());
        }

        AtomicInteger n = new AtomicInteger(1);
        option_itsColor.forEach(option -> {
            String[] rows = option.getKey().split("\n");
            int maxLength = Arrays.stream(rows).map(String::length).max(Integer::compare).orElse(0);

            builderTop.append("╔═" + resetColor);
            IntStream.range(0, maxLength).forEach(letter -> builderTop.append("═"));
            builderTop.append(resetColor + "═╗  ");

            String color = option.getValue();
            for (int i = 0; i < maxLines; i++) {
                buildersMid.get(i).append("║ " + color);
                if (i < rows.length) {
                    buildersMid.get(i).append(rows[i]);
                    for (int j = 0; j < maxLength - rows[i].length(); j++) {
                        buildersMid.get(i).append(" ");
                    }
                } else {
                    for (int j = 0; j < maxLength; j++) {
                        buildersMid.get(i).append(" ");
                    }
                }
                buildersMid.get(i).append(resetColor + " ║  ");
            }

            builderBot.append("╚═" + resetColor);
            IntStream.range(0, maxLength).forEach(letter -> builderBot.append("═"));
            builderBot.append(resetColor + "═╝  ");

            builderEnd.append("  " + resetColor);
            builderEnd.append(n.get());
            IntStream.range(0, maxLength - 1).forEach(letter -> builderEnd.append(" "));
            builderEnd.append(resetColor + "    ");
            n.getAndIncrement();
        });
        builderTop.append('\n');
        for (StringBuilder stringBuilder : buildersMid) {
            builderTop.append(stringBuilder).append('\n');
        }
        builderTop.append(builderBot).append('\n');
        builderTop.append(builderEnd);

        return builderTop.toString();
    }

    public static ArrayList<Integer> displaySelectionFormVariableChoices(ArrayList<Pair<String, String>> option_itsColor, Panel displayPanel, int maxNumberOfOptionsToChose, String message) {
        String resetColor = Color.reset();
        StringBuilder builder = new StringBuilder();
        builder.append(message);
        builder.append("YOU HAVE TO CHOOSE AT MOST " + maxNumberOfOptionsToChose +
                " OF THE FOLLOWING OPTIONS \n");

        if (displayPanel != null) {
            displayPanel.show();
        }

        builder.append(generateForm(option_itsColor, resetColor));

        DrawableObject obj = new DrawableObject(builder.toString(), 0, 0);
        Panel panel = new Panel(1000, (int) obj.getHeight() + 3, out);
        panel.addItem(obj);
        panel.show();

        int size = option_itsColor.size();
        int m = maxNumberOfOptionsToChose;
        out.println("ENTER THE NUMBER OF THE SELECTED ITEMS. ENTER '0' OR 'STOP' TO STOP ");
        String inputString;
        ArrayList<Integer> inputs = new ArrayList<>();
        while (m > 0) {
            inputString = in.next();
            if (inputString.matches("-?\\d+")) {
                int input = Integer.parseInt(inputString);
                if (inputs.contains(input)) {
                    out.println("YOU HAVE ALREADY CHOSEN THIS RESOURCE");
                } else if (input < 0 || input > size) {
                    out.println("THIS NUMBER OF CHOICE DOESN'T EXIST, TRY WITH A NUMBER BETWEEN 1 AND " + size);
                } else if (input == 0) {
                    m = 0;
                } else {
                    inputs.add(input);
                    m--;
                }
            } else {
                if (inputString.toUpperCase().equals("STOP")) m = 0;
                else out.println("PLEASE INSERT A NUMBER");
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
        in.nextLine();
        if (isAffirmative(response)) {
            return inputs;
        } else {
            return displaySelectionFormVariableChoices(option_itsColor, displayPanel, maxNumberOfOptionsToChose, message);
        }

    }

    public static ArrayList<Integer> displaySelectionFormMultipleChoices(ArrayList<Pair<String, String>> option_itsColor, Panel displayPanel, int numberOfOptionsToChose, String message) {
        String resetColor = Color.WHITE.getAnsiCode();
        StringBuilder builder = new StringBuilder();
        builder.append(message);
        builder.append("YOU HAVE TO CHOOSE " + numberOfOptionsToChose +
                " OF THE FOLLOWING OPTIONS \n(YOU CAN CHOOSE THE SAME OPTION MULTIPLE TIMES) \n");

        if (displayPanel != null) {
            displayPanel.show();
        }

        builder.append(generateForm(option_itsColor, resetColor));

        DrawableObject obj = new DrawableObject(builder.toString(), 0, 0);
        Panel panel = new Panel(1000, (int) obj.getHeight() + 3, out);
        panel.addItem(obj);
        panel.show();

        int size = option_itsColor.size();
        int m = numberOfOptionsToChose;
        out.println("ENTER THE NUMBER OF THE SELECTED ITEMS");
        String inputString;
        ArrayList<Integer> inputs = new ArrayList<>();
        while (m > 0) {
            inputString = in.next();
            if (inputString.matches("-?\\d+")) {
                int input = Integer.parseInt(inputString);
                if (input < 1 || input > size) {
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
        String answer = in.next().toUpperCase();
        in.nextLine();
        if (isAffirmative(answer)) {
            return inputs;
        } else {
            return displaySelectionForm(option_itsColor, displayPanel, numberOfOptionsToChose, message);
        }

    }

// test of displaySelectionForm()

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
        if (isAffirmative(response)) {
            //generate response event
        }

    }*/


    public void displayChooseMultipleExtraResourcePowerForm(ArrayList<Resource> resourceTypes, int numberOfResources) {
        out.println("SOME ACTIVE EXTRA RESOURCE POWER ALLOWS YOU \n " +
                "TO ADD " + numberOfResources + " RESOURCES TO YOUR STORE! \n ");
        choseResources(resourceTypes, numberOfResources);
    }

    public void displayDashBoardState(String playerId) {
        out.println("One dashboard has been updated!");
        DashBoardView dashBoardView = playerStates.get(playerId).dashBoard;
        if (dashBoardView != null) {
            dashBoardView.displayAll(playerId.toUpperCase());
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
        if (isAffirmative(response)) {
           System.out.println("good");
        } else  System.out.println("bad");
    }*/


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
          if (isAffirmative(response)) {
              //put in dashboard event
              out.println("resources put in dashboard");
          }

      }
  */

    public void displayDevCardSlotError(String devCardID, int cardSlot) {
        out.println("SORRY, " + devCardID + " CANNOT BE ADDED TO THE SELECTED SLOT, SINCE IT \n  DOES NOT FULFILL THE SPECIFIED LEVEL REQUIREMENTS");
        DevCardView card = new DevCardView(devCardID);
        out.println(card.toString());
        playerStates.get(thisPlayer).dashBoard.displayDevCardSlots();
    }

    public void displayIncompatiblePowersError(String leaderCardID, int leaderPowerIndex) {
        LeaderCardView card = playerStates.get(thisPlayer).leaderCards.get(leaderCardID);
        System.out.println(card.getLeaderPowerName(leaderPowerIndex) + "IN" + leaderCardID.toUpperCase() + " IS NOT COMPATIBLE WITH OTHER SELECTED POWERS");
        System.out.println(card.toString());

    }

    public void displayLeaderCardNotActiveError(String leaderCardID) {
        LeaderCardView card = playerStates.get(thisPlayer).leaderCards.get(leaderCardID);
        System.out.println("SORRY, " + leaderCardID.toUpperCase() + " IS NOT ACTIVE, YOU CAN'T USE IT");
        System.out.println(card.toString());
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

//    public Event displayMatchState(String playerId, boolean lastRound, TurnState turnState) {
//        Event event = null;
//        int c = turnState.getStateCode();
//        boolean leaderActionDone= false;
//        switch (c) {
//            case 2: { //start
//                if (thisPlayer == playerId) {
//                    out.println(playerId + " " + turnState.getDescription());
//                } else {
//                    int inputIndex = -1;
//                    ArrayList<Action> allActions = Arrays.stream(Action.values()).collect(Collectors.toCollection(ArrayList::new));
//                    ArrayList<Pair<String, String>> possibleActions = new ArrayList<>();
//
//                    for (Action a : allActions) {
//                        possibleActions.add(new Pair(a.getDescription(), Color.WHITE.getAnsiCode()));
//                        inputIndex = displaySelectionForm(possibleActions, null, 1, "YOU CAN DO THE FOLLOWING ACTIONS: CHOOSE ONE").get(0);
//                    }
//                    Action selectedAction = allActions.get(inputIndex);
//                    switch (selectedAction.getActionCode()) {
//                        case 1://BUY_DEVCARD
//                            out.println(selectedAction.getDescription());
//                            askForDevCard();
//
//                        case 2: {//TAKE_RESOURCES_FROM_MARKET
//                            out.println(selectedAction.getDescription());
//                            askForMarketRow();
//                        }
//                        case 3://PRODUCE
//                            out.println(selectedAction.getDescription());
//
//                        case 4://LEADER_ACTION
//                            out.println(selectedAction.getDescription());
//
//                    }
//
//                }
//            }
//            ;
//            case 3: {//AFTER_LEADER_CARD_ACTION
//                if (thisPlayer == playerId) {
//                    out.println(playerId + " " + turnState.getDescription());
//                } else {
//
//                }
//            }
//            ;
//            case 4: { //AFTER_MAIN_ACTION
//                if (thisPlayer == playerId) {
//                    out.println(playerId + " " + turnState.getDescription());
//                } else {
//
//                }
//            }
//            ;
//            case 5: { //END_OF_TURN
//
//            }
//            ;
//            case 6: {//WAITING_FOR_SOMETHING
//
//            }
//            ;
//            case 7: {//MATCH_ENDED
//
//            }
//            ;
//
//        }
//        return event;
//    }


    public static String productionPowerToString(ProductionPower productionPower, String color) {
        StringBuilder build = new StringBuilder();
        build.append(color + "╔════" + color + color + "═════════╗" + Color.reset() + "\n");
        for (Resource resource : productionPower.getConsumedResources().keySet()) {
            build.append(color + "║     " + CLI.colorResource(resource) + productionPower.getConsumedResources().get(resource) + " " + CLI.shapeResource(resource) + color + "     ║" + Color.reset() + "\n");
        }
        if (productionPower.getRequiredResourceOfChoice() != 0) {
            build.append(color + "║     " + color + +productionPower.getRequiredResourceOfChoice() + " " + "?" + color + "     ║" + Color.reset() + "\n");
        }
        build.append(color + "║   " + color + "--->>> " + color + "   ║ " + Color.reset() + "\n");
        for (Resource resource : productionPower.getProducedResources().keySet()) {
            build.append(color + "║     " + CLI.colorResource(resource) + productionPower.getProducedResources().get(resource) + " " + CLI.shapeResource(resource) + color + "     ║" + Color.reset() + "\n");
        }
        if (productionPower.getFaithPointsProduced() != 0) {
            build.append(color + "║     " + color + +productionPower.getFaithPointsProduced() + " " + "+" + color + "     ║" + Color.reset() + "\n");
        }
        if (productionPower.getProducedResourceOfChoice() != 0) {
            build.append(color + "║     " + color + +productionPower.getProducedResourceOfChoice() + " " + "?" + color + "     ║" + Color.reset() + "\n");
        }
        build.append(color + "╚════" + color + color + "═════════╝" + Color.reset() + "\n");
        return build.toString();
    }

    public String displayResourcesInHashMap(HashMap<Resource, Integer> hashMapToDisplay) {
        StringBuilder builder0 = new StringBuilder();
        hashMapToDisplay.keySet().forEach(resource -> {
            String color = colorResource(resource);
            String shape = shapeResource(resource);
            builder0.append(color + resource.toString() + ": ");
            IntStream.range(0, hashMapToDisplay.get(resource)).forEach(n -> builder0.append(color + shape + " "));
            builder0.append(Color.WHITE.getAnsiCode() + " --> " + hashMapToDisplay.get(resource) + "\n");
        });
        return builder0.toString();
    }

    public void displayAvailableDeposits(DashBoardView thisDashboard) {
        StringBuilder builder = new StringBuilder();
        builder.append("\n\n");
        builder.append("THESE ARE THE AVAILABLE PLACES \n:" + thisDashboard.warehouseToString() + "\n\n");
        playerStates.get(thisPlayer).getLeaderCards().values().forEach(card -> builder.append(card.depositPowersToString()));
        DrawableObject obj = new DrawableObject(builder.toString(), 2, 0);
        Panel panel = new Panel(1000, (int) obj.getHeight() + 2, out);
        panel.addItem(obj);
        panel.show();
    }

    @Override
    public NewResourcesOrganizationEvent getWarehouseDisplacement(HashMap<Resource, Integer> resourcesToOrganize) {
        //Parameters to giveback to the NewResourcesOrganizationEvent
        ArrayList<DepotState> depotStates;
        ArrayList<DepositLeaderPowerStateEvent> leaderPowersState = new ArrayList<>();
        HashMap<Resource, Integer> discardedResources = new HashMap<>();
        //-----------------------------------------------------
        resourcesToOrganize.keySet().forEach(entry -> discardedResources.put(entry, resourcesToOrganize.get(entry)));
        DashBoardView thisDashboard = playerStates.get(thisPlayer).getDashBoard();
        //Image of the current state of depots
        ArrayList<DepotState> currentDepotStates = thisDashboard.getWarehouse();
        //Initially the depotState to give back to model is the current state of this dashboard.
        depotStates = currentDepotStates;
        //Takes only the active LeaderCards
        ArrayList<LeaderCardView> leaderCardViews = playerStates.get(thisPlayer).getLeaderCards().values().stream().collect(Collectors.toCollection(ArrayList::new));
        //Selects only DepositLeaderPowers active
        ArrayList<InfoPower> thisDepositLeaderPowers = new ArrayList<>();

        //TODO here must be changed how selected is handled
        leaderCardViews.stream().forEach(cardView -> {
            if (cardView.isActive()) {
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
        int totalNumberOfResourcesToOrganize = resourcesToOrganize.values().stream().reduce(0, (prev, next) -> prev + next);
        if (totalNumberOfResourcesToOrganize == 0) {
            out.println("THERE ARE NO RESOURCES TO STORE");
        } else {
            displayAvailableDeposits(thisDashboard);
            builder0.append(" YOU  CAN NOW STORE THE FOLLOWING RESOURCES \n\n");
            builder0.append(displayResourcesInHashMap(resourcesToOrganize));
            out.println(builder0.toString());
            System.out.println("IF YOU DON'T WANT TO STORE ANY RESOURCES OF THESE,PLEASE TYPE D FOR 'DONE' OTHERWISE TYPE C FOR 'CONTINUE'");
        }
        if (totalNumberOfResourcesToOrganize == 0 || in.next().toUpperCase().equals("D")) {
            //GENERATE NEW NewResourcesOrganizationEvent
            out.println("Done");


            thisDepositLeaderPowers.stream().forEach(infoPower -> {
                leaderPowersState.add(new DepositLeaderPowerStateEvent(thisPlayer, infoPower.getCardId(), infoPower.powerIndex, infoPower.getPower().getCurrentResources()));
            });
            NewResourcesOrganizationEvent event = new NewResourcesOrganizationEvent(thisPlayer, depotStates, leaderPowersState, discardedResources);

            return event;

        } else {
            // prepare panel to display current state of all deposits.

            //this is the moment where the user is asked if they want to switch depot.
            boolean allEmpty = depotStates.stream().map(depot -> depot.getCurrentQuantity() == 0).reduce(true, (prev, next) -> (prev && next));
            if (!allEmpty) {
                out.println("DO YOU WANT TO SWITCH SOME DEPOTS? Y/N");
                String y_n = in.next();
                while (y_n.toUpperCase().equals("Y")) {
                    int count = 2;
                    int[] indexes = {-1, -1};
                    DepotResultMessage done;

                    out.println("WHICH DEPOTS? INSERT TWO NUMBERS");
                    while (count > 0) {
                        String inp = in.next();
                        if (!inp.matches("-?\\d+")) {
                            out.println("YOU HAVE TO INSERT NUMBERS");
                            continue;
                        }
                        int chosenDepotIndex = Integer.parseInt(inp);
                        if (chosenDepotIndex - 1 < 0 || chosenDepotIndex > depotStates.size()) {
                            out.println("THIS DEPOT DOESN'T EXIST. INSERT A NUMBER BETWEEN 1 AND " + depotStates.size());
                            continue;
                        }
                        indexes[2 - count] = chosenDepotIndex - 1;
                        count--;

                    }
                    out.println(indexes[0] + " " + indexes[1]);
                    done = thisDashboard.switchDepots(indexes[0], indexes[1]);
                    out.println(done.getMessage());
                    out.println(thisDashboard.warehouseToString());
                    out.println("DO YOU WANT TO SWITCH SOME DEPOTS? Y/N");
                    y_n = in.next();
                }
            }

            // select one resource at a time among those available
            ArrayList<Pair<String, String>> resourcesOptions = new ArrayList<>();
            ArrayList<Resource> justResources = new ArrayList<>();

            resourcesToOrganize.keySet().forEach(resType -> {
                IntStream.range(0, resourcesToOrganize.get(resType)).forEach(n -> {
                    resourcesOptions.add(new Pair<>(shapeResource(resType), colorResource(resType)));
                    justResources.add(resType);
                });
            });
            int inputResource;
            if (totalNumberOfResourcesToOrganize == 1) {
                String white = Color.WHITE.getAnsiCode();
                out.println("YOU HAVE JUST ONE RESOURCE TO STORE: \n" +
                        white + "╔" + white + "═══" + white + "╗\n" +
                        white + "║ " + colorResource(justResources.get(0)) + shapeResource(justResources.get(0)) + white + " ║\n" +
                        white + "╚" + white + "═══" + white + "╝");
                inputResource = 0;
                // the chosen resource needs to be assigned to one of the deposits
            } else {
                String s = "YOU HAVE " + totalNumberOfResourcesToOrganize + " AVAILABLE FOR STORING, LET'S STORE ONE AT A TIME\n";
                // the chosen resource needs to be assigned to one of the deposits
                inputResource = displaySelectionForm(resourcesOptions, null, 1, s).get(0);
            }
            System.out.println("WHERE DO YOU WANT TO PUT THIS RESOURCE ( " + resourcesOptions.get(inputResource).getValue() + resourcesOptions.get(inputResource).getKey() + Color.reset() + " )?");

            // prepare the selection form for deposits.
            ArrayList<Pair<String, String>> depositOptions = new ArrayList<>();
            IntStream.range(0, currentDepotStates.size()).forEach(n -> {
                if (currentDepotStates.get(n).getCurrentQuantity() == 0)
                    depositOptions.add(new Pair<String, String>("DEPOT " + (n + 1), Color.WHITE.getAnsiCode()));
                else
                    depositOptions.add(new Pair<String, String>("DEPOT " + (n + 1), colorResource(currentDepotStates.get(n).getResourceType())));
            });
            IntStream.range(0, thisDepositLeaderPowers.size()).forEach(n -> {
                depositOptions.add(new Pair<String, String>("LEADER POWER " + (n + 1), Color.WHITE.getAnsiCode()));
            });

            //result of transition
            AtomicBoolean successful = new AtomicBoolean(false);
            //inputDeposit is the result of the selection of one destination among deposits
            int inputDeposit = displaySelectionForm(depositOptions, null, 1, "THESE ARE THE AVAILABLE DEPOSITS\n").get(0);
            //if the selected index belongs to depots...
            if (inputDeposit < currentDepotStates.size()) {
                DepotResultMessage result = thisDashboard.tryAddResource(justResources.get(inputResource), currentDepotStates.get(inputDeposit));
                successful.set(result.getSuccessfull());
                out.println(result.getMessage());
            }
            //or if it belongs to leader card extra depots...
            else {
                int index = inputDeposit - currentDepotStates.size();
                HashMap<Resource, Integer> resInput = new HashMap<>();
                resInput.put(justResources.get(inputResource), 1);


                int finalInputResource = inputResource;
                leaderCardViews.stream().forEach(cardView -> {
                    if (cardView.getIdCard().equals(thisDepositLeaderPowers.get(index).getCardId())) {
                        DepotResultMessage result = cardView.updateDepositLeaderPower(thisDepositLeaderPowers.get(index).getPowerIndex(), justResources.get(finalInputResource));
                        successful.set(result.getSuccessfull());
                        out.println(result.getMessage());
                    }
                });
            }
            if (successful.get()) {
                resourcesToOrganize.put(justResources.get(inputResource), (resourcesToOrganize.get(justResources.get(inputResource)) - 1));
            }
            return getWarehouseDisplacement(resourcesToOrganize);
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

//    public static void main(String[] args) {
//        HashMap<String, DashBoardView> dashboards = new HashMap<>();
//        DepotState depot = new DepotState(Resource.COIN, 3, 3);
//        DepotState depot2 = new DepotState(Resource.SERVANT, 4, 2);
//        DepotState depot3 = new DepotState(Resource.SHIELD, 6, 4);
//        ArrayList<DepotState> totalLevels = new ArrayList();
//        totalLevels.add(depot);
//        totalLevels.add(depot2);
//        totalLevels.add(depot3);
//
//        HashMap<Resource, Integer> str = new HashMap<>();
//        str.put(Resource.COIN, 6);
//        str.put(Resource.ROCK, 7);
//
//        ArrayList<String> cards = new ArrayList<>();
//        cards.add("DevCard10");
//        cards.add("DevCard40");
//        cards.add("DevCard16");
//
//        String thisPlayer = "PAOLO";
//
//        DashBoardView d = new DashBoardView(cards, str, totalLevels, thisPlayer);
//        dashboards.put(thisPlayer, d);
//
//        HashMap<String, HashMap<String, LeaderCardView>> leaderCards = new HashMap<>();
//        HashMap<String, LeaderCardView> leaderCardsSet = new HashMap<>();
//        LeaderCardView card1 = new LeaderCardView("LeaderCard5");
//        card1.setSelected(true);
//        leaderCardsSet.put("LeaderCard5", card1);
//        LeaderCardView card2 = new LeaderCardView("LeaderCard6");
//        card2.setSelected(true);
//        leaderCardsSet.put("LeaderCard6", card2);
//        leaderCards.put(thisPlayer, leaderCardsSet);
//
//        HashMap<Resource, Integer> resourcesToOrganize = new HashMap<>();
//        resourcesToOrganize.put(Resource.SHIELD, 3);
//        resourcesToOrganize.put(Resource.ROCK, 5);
//        resourcesToOrganize.put(Resource.COIN, 2);
//        resourcesToOrganize.put(Resource.SERVANT, 4);
//
//
//        //Parameters to giveback to the NewResourcesOrganizationEvent
//        ArrayList<DepotState> depotStates;
//        ArrayList<DepositLeaderPowerStateEvent> leaderPowersState;
//        HashMap<Resource, Integer> discardedResources = new HashMap<>();
//        //-----------------------------------------------------
//        //Image of the current state of depots
//        ArrayList<DepotState> currentDepotStates = dashboards.get(thisPlayer).getWarehouse();
//        //Initially the depotState to give back to model is the current state of this dashboard.
//        depotStates = currentDepotStates;
//        //Takes only the active LeaderCards
//        ArrayList<LeaderCardView> leaderCardViews = leaderCards.get(thisPlayer).values().stream().collect(Collectors.toCollection(ArrayList::new));
//        //Selects only DepositLeaderPowers active
//        ArrayList<InfoPower> thisDepositLeaderPowers = new ArrayList<>();
//
//        leaderCardViews.stream().forEach(cardView -> {
//            if (cardView.getSelected()) {
//                IntStream.range(0, cardView.getLeaderPowersActive().size()).forEach(index -> {
//                    LeaderPower power = cardView.getLeaderPowersActive().get(index);
//                    if (power instanceof DepositLeaderPower)
//                        thisDepositLeaderPowers.add(new InfoPower((DepositLeaderPower) power, cardView.getIdCard(), index));
//                });
//            }
//        });
//        // Initially the DepositLeaderPower states to give back to the model are the current states of leaderPower.
//
//        //display the resources available for storing
//        StringBuilder builder0 = new StringBuilder();
//        builder0.append(" YOU  CAN NOW STORE THE FOLLOWING RESOURCES \n\n");
//        resourcesToOrganize.keySet().forEach(resource -> {
//            String color = colorResource(resource);
//            String shape = shapeResource(resource);
//            builder0.append(color + resource.toString() + ": ");
//            IntStream.range(0, resourcesToOrganize.get(resource)).forEach(n -> builder0.append(color + shape + " "));
//            builder0.append(Color.WHITE.getAnsiCode() + " --> " + resourcesToOrganize.get(resource) + "\n");
//        });
//        out.println(builder0.toString());
//        System.out.println("IF YOU DON'T WANT TO STORE ANY RESOURCES OF THESE,PLEASE TYPE D FOR 'DONE' OTHERWISE TYPE C FOR 'CONTINUE'");
//        if (in.next().toUpperCase().equals("D")) {
//            //GENERATE NEW NewResourcesOrganizationEvent
//            out.println("Done");
//            out.println(depotStates.size());
//            out.println(thisDepositLeaderPowers.size());
//            resourcesToOrganize.keySet().forEach(entry -> discardedResources.put(entry, resourcesToOrganize.get(entry)));
//            out.println(discardedResources.size());
//
//        } else {
//            // prepare panel to display current state of all deposits.
//            StringBuilder builder = new StringBuilder();
//            builder.append("\n\n");
//            builder.append("THESE ARE THE AVAILABLE PLACES \n:" + dashboards.get(thisPlayer).warehouseToString() + "\n\n");
//            leaderCards.get(thisPlayer).values().forEach(card -> builder.append(card.depositPowersToString()));
//            DrawableObject obj = new DrawableObject(builder.toString(), 2, 0);
//            Panel panel = new Panel(1000, (int) obj.getHeight() + 2, out);
//            panel.addItem(obj);
//            panel.show();
//            //this is the moment where the user is asked if they want to switch depot.
//            out.println("DO YOU WANT TO SWITCH SOME DEPOTS? Y/N");
//            String y_n = in.next();
//            while (y_n.toUpperCase().equals("Y")) {
//                int count = 2;
//                int[] indexes = {-1, -1};
//                DepotResultMessage done;
//
//                out.println("WHICH DEPOTS? INSERT TWO NUMBERS");
//                while (count > 0) {
//                    String inp = in.next();
//                    if (!inp.matches("-?\\d+")) {
//                        out.println("YOU HAVE TO INSERT NUMBERS");
//                        continue;
//                    }
//                    int chosenDepotIndex = Integer.parseInt(inp);
//                    if (chosenDepotIndex - 1 < 0 || chosenDepotIndex > depotStates.size()) {
//                        out.println("THIS DEPOT DOESN'T EXIST. INSERT A NUMBER BETWEEN 1 AND " + depotStates.size());
//                        continue;
//                    }
//                    indexes[2 - count] = chosenDepotIndex - 1;
//                    count--;
//
//                }
//                out.println(indexes[0] + " " + indexes[1]);
//                done = d.switchDepots(indexes[0], indexes[1]);
//                out.println(done.getMessage());
//                out.println(d.warehouseToString());
//                out.println("DO YOU WANT TO SWITCH SOME DEPOTS? Y/N");
//                y_n = in.next();
//            }
//
//            // select one resource at a time among those available
//            ArrayList<Pair<String, String>> resourcesOptions = new ArrayList<>();
//            ArrayList<Resource> justResources = new ArrayList<>();
//
//            resourcesToOrganize.keySet().forEach(resType -> {
//                IntStream.range(0, resourcesToOrganize.get(resType)).forEach(n -> {
//                    resourcesOptions.add(new Pair<>(shapeResource(resType), colorResource(resType)));
//                    justResources.add(resType);
//                });
//            });
//            String s = "THESE ARE THE RESOURCES AVAILABLE FOR STORING, LET'S STORE ONE AT A TIME\n";
//            // the chosen resource needs to be assigned to one of the deposits
//            int inputResource = displaySelectionForm(resourcesOptions, null, 1, s).get(0);
//            System.out.println("WHERE DO YOU WANT TO PUT THIS RESOURCE ( " + resourcesOptions.get(inputResource).getValue() + resourcesOptions.get(inputResource).getKey() + Color.reset() + " )?");
//
//            // prepare the selection form for deposits.
//            ArrayList<Pair<String, String>> depositOptions = new ArrayList<>();
//            IntStream.range(0, currentDepotStates.size()).forEach(n -> depositOptions.add(new Pair<String, String>("DEPOT " + (n + 1), colorResource(currentDepotStates.get(n).getResourceType()))));
//
//            IntStream.range(0, thisDepositLeaderPowers.size()).forEach(n -> {
//                depositOptions.add(new Pair<String, String>("LEADER POWER " + (n + 1), Color.WHITE.getAnsiCode()));
//            });
//
//            //result of transition
//            AtomicBoolean successfull = new AtomicBoolean(false);
//            //inputDeposit is the result of the selection of one destination among deposits
//            int inputDeposit = displaySelectionForm(depositOptions, null, 1, "THESE ARE THE AVAILABLE DEPOSITS\n").get(0);
//            //if the selected index belongs to depots...
//            if (inputDeposit < currentDepotStates.size()) {
//                DepotResultMessage result = currentDepotStates.get(inputDeposit).tryAddResource(justResources.get(inputResource));
//                successfull.set(result.getSuccessfull());
//                out.println(result.getMessage());
//            }
//            //or if it belongs to leader card extra depots...
//            else {
//                int index = inputDeposit - currentDepotStates.size();
//                HashMap<Resource, Integer> resInput = new HashMap<>();
//                resInput.put(justResources.get(inputResource), 1);
//
//                String cardId = thisDepositLeaderPowers.get(index).getCardId();
//                leaderCardViews.stream().forEach(cardView -> {
//                    if (cardView.getIdCard() == thisDepositLeaderPowers.get(index).getCardId()) {
//                        DepotResultMessage result = cardView.updateDepositLeaderPower(thisDepositLeaderPowers.get(index).getPowerIndex(), justResources.get(inputResource));
//                        successfull.set(result.getSuccessfull());
//                        out.println(result.getMessage());
//                    }
//                });
//            }
//            if (successfull.get()) {
//                resourcesToOrganize.put(justResources.get(inputResource), (resourcesToOrganize.get(justResources.get(inputResource)) - 1));
//                //displayOrganizeResourcesForm( resourcesToOrganize);
//                out.println(resourcesToOrganize.toString());
//                out.println(currentDepotStates.get(0).getCurrentQuantity());
//                out.println(currentDepotStates.get(1).getCurrentQuantity());
//                out.println(currentDepotStates.get(2).getCurrentQuantity());
//                out.println(card1.getLeaderPowersActive().get(0));
//                out.println(((DepositLeaderPower) card1.getLeaderPowersActive().get(0)).getCurrentResources());
//                out.println(((DepositLeaderPower) card2.getLeaderPowersActive().get(0)).getCurrentResources());
//
//
//            } else {
//                out.println(resourcesToOrganize.toString());
//                out.println(currentDepotStates.get(0).getCurrentQuantity());
//                out.println(currentDepotStates.get(1).getCurrentQuantity());
//                out.println(currentDepotStates.get(2).getCurrentQuantity());
//                out.println(((DepositLeaderPower) card1.getLeaderPowersActive().get(0)).getCurrentResources());
//                out.println(((DepositLeaderPower) card2.getLeaderPowersActive().get(0)).getCurrentResources());
//            }
//        }
//
//    }


    public static String colorResource(Resource res) {
        if (res == Resource.SERVANT) return Color.MAGENTA.getAnsiCode();
        if (res == Resource.SHIELD) return Color.BLUE.getAnsiCode();
        if (res == Resource.COIN) return Color.YELLOW.getAnsiCode();
        else return Color.GREY.getAnsiCode();
    }

    public static String shapeResource(Resource res) {
        if (res == Resource.SERVANT) return "■";
        if (res == Resource.SHIELD) return "◆";
        if (res == Resource.COIN) return "●";
        else return "▼";
    }

    @Override
    public void printMessage(String message) {
        out.println(message);
    }

    @Override
    public void printError(String error) {
        out.println(Color.RED.getAnsiCode() + error + Color.reset());
    }

    @Override
    public void printWarning(String warning) {
        out.println(Color.YELLOW.getAnsiCode() + warning + Color.reset());
    }

    @Override
    public boolean askSingleplayer() {
        ArrayList<Pair<String, String>> options = new ArrayList<>();
        options.add(new Pair<>("Multiplayer", Color.reset()));
        options.add(new Pair<>("Singleplayer", Color.reset()));

        Integer choice = displaySelectionForm(options, null, 1, "WHICH MODE WOULD YOU LIKE TO PLAY?").get(0);

        return choice==1;
    }

    @Override
    public InetAddress askIP() {
        InetAddress inetAddress = null;
        while (inetAddress == null) {
            out.println("Insert the server IP address");
            String IP = in.nextLine();

            if (IP.equals("0")) //TODO only for testing purpose, must be removed
                IP = "127.0.0.1";

            try {
                inetAddress = InetAddress.getByName(IP);
            } catch (UnknownHostException e) {
                printError("Please, insert a valid IP address");
            }
        }
        return inetAddress;
    }

    @Override
    public boolean askIfNewLobby() {
        ArrayList<Pair<String, String>> options = new ArrayList<>();
        options.add(new Pair<>("Create Lobby", Color.reset()));
        options.add(new Pair<>("Join Lobby", Color.reset()));

        ArrayList<Integer> choices = displaySelectionForm(options, null, 1, "");
        return choices.get(0) == 0;
    }

    @Override
    public String askUserID() {
        out.println("Insert username");
        this.thisPlayer = in.nextLine();
        players.add(this.thisPlayer);
        playerStates.put(this.thisPlayer, new PlayerState());
        faithTrack = new FaithTrackView(players);
        return this.thisPlayer;
    }

    @Override
    public String askLeaderID() {
        out.println("Insert the username of the leader of the lobby you want to join");
        out.println("Put * to join a random lobby");
        return in.nextLine();
    }

    @Override
    public void displayLobbyState(String leaderID, ArrayList<String> otherPlayersID) {
        if (!players.contains(leaderID)) players.add(leaderID);
        playerStates.putIfAbsent(leaderID, new PlayerState());
        for (String playerID : otherPlayersID) {
            if (!players.contains(playerID)) players.add(playerID);
            playerStates.putIfAbsent(playerID, new PlayerState());
        }
        faithTrack = new FaithTrackView(players);

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
        Panel panel = new Panel(500, obj.getHeight() + 3, out);
        panel.addItem(obj);
        panel.show();

        if (this.thisPlayer.equals(leaderID) && players.size() > 1) {
            new Thread(this::askForGameStart).start();
        }
    }

    private ArrayList<String> toSetupPlayers = null;

    @Override
    public void displayWaitingForPlayerToSetupState(String playerID) {
        if (toSetupPlayers == null) toSetupPlayers = (ArrayList<String>) players.clone();
        toSetupPlayers.remove(playerID);

        if (toSetupPlayers.isEmpty() || toSetupPlayers.contains(thisPlayer)) return;

        out.println("Waiting for");
        for (String p : toSetupPlayers) out.println(p);
        out.println("to finish their setup");
    }

    private void askForGameStart() {
        out.println("Type \"start\" to start the game");
        String s;
        try {
            do {
                s = in.nextLine().toUpperCase();
            } while (!s.equals("START"));
            client.startMatch();
        } catch (Exception ignore) {
        }
    }

    @Override
    public ArrayList<String> choseInitialLeaderCards(ArrayList<String> leaderCardsIDs, int numberOFLeaderCardsToChose) {
        ArrayList<Integer> indexes = leaderCardsIDs.stream().map(name -> Integer.parseInt(name.substring(10))).collect(Collectors.toCollection(ArrayList::new));

        if (numberOFLeaderCardsToChose <= 0) return new ArrayList<>();

        int numberOFLeaderCardsToChoseLeft = numberOFLeaderCardsToChose;

        out.println(thisPlayer.toUpperCase() + ", CHOOSE " + numberOFLeaderCardsToChoseLeft + " AMONG THESE LEADER CARDS:");
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
        while (numberOFLeaderCardsToChoseLeft > 0) {
            out.println("YOU HAVE " + numberOFLeaderCardsToChoseLeft + " CARDS LEFT TO CHOOSE,\n PLEASE TYPE THE NUMBER OF ONE CARD: ");
            int input = in.nextInt();
            while (!indexes.contains(input)) {
                out.println("WRONG NUMBER, PLEASE RETRY TYPING THE NUMBER OF ONE CARD: ");
                input = in.nextInt();
            }
            while (cardIndexes.contains(input)) {
                out.println("YOU HAVE ALREADY SELECTED THIS NUMBER, PLEASE RETRY TYPING THE NUMBER OF ONE CARD: ");
                input = in.nextInt();
            }
            cardIndexes.add(input);
            numberOFLeaderCardsToChoseLeft--;
        }
        out.println("YOU HAVE CHOSEN THESE LEADER CARDS:");
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
        if (isAffirmative(response)) {
            playerStates.get(thisPlayer).leaderCards.putAll(leaderCardsChosen);
            return new ArrayList<>(leaderCardsChosen.keySet());
        } else return choseInitialLeaderCards(leaderCardsIDs, numberOFLeaderCardsToChose);
    }

    @Override
    public HashMap<Resource, Integer> choseResources(ArrayList<Resource> resourceType, int numberOFResources) {
        HashMap<Resource, Integer> initialResources = new HashMap<>();
        for (Resource res : resourceType) {
            initialResources.put(res, 0);
        }
        if (numberOFResources <= 0) return initialResources;

        int numberOFResourcesLeft = numberOFResources;

        out.println(thisPlayer.toUpperCase() + ", YOU HAVE TO CHOOSE " + numberOFResourcesLeft + " RESOURCES AMONG:");
        resourceType.stream().forEach(el -> out.println(colorResource(el) + el.toString().toUpperCase() + "\n" + Color.reset()));

        for (Resource res : resourceType) {
            if (res != resourceType.get(resourceType.size() - 1) && numberOFResourcesLeft > 0) {
                out.println("HOW MANY " + colorResource(res) + res.toString() + Color.reset() + " WOULD YOU LIKE? INSERT A NUMBER (" + numberOFResourcesLeft + " LEFT)");
                int n = in.nextInt();
                while (numberOFResourcesLeft - n < 0) {
                    out.println("RETRY, HOW MANY " + colorResource(res) + res.toString() + Color.reset() + "S WOULD YOU LIKE? INSERT A NUMBER");
                    n = in.nextInt();
                }
                initialResources.put(res, n);
                numberOFResourcesLeft = numberOFResourcesLeft - n;
            }
        }
        if (numberOFResourcesLeft >= 0) {
            if (numberOFResourcesLeft == 0) out.println("YOU HAVE " + numberOFResourcesLeft + " RESOURCES LEFT");
            else {
                out.println("YOU HAVE " + numberOFResourcesLeft + " RESOURCES LEFT: THESE WILL BE " + colorResource(resourceType.get(resourceType.size() - 1)) + resourceType.get(resourceType.size() - 1).toString() + Color.reset());
                initialResources.put(resourceType.get(resourceType.size() - 1), numberOFResourcesLeft);
            }
        }
        out.println("YOU HAVE CHOSEN THESE RESOURCES:");
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
        if (isAffirmative(response)) {
            //put in dashboard event
            out.println("resources put in dashboard");
            return initialResources;
        } else return choseResources(resourceType, numberOFResources);
    }

    @Override
    public void setPersonalProductionPower(String playerId, ProductionPower personalProductionPower) {
        if (playerStates.get(playerId).dashBoard == null) {
            playerStates.get(playerId).dashBoard = new DashBoardView(new ArrayList<>(), new HashMap<>(), new ArrayList<>(), playerId);
        }
        playerStates.get(playerId).dashBoard.setPersonalProductionPower(personalProductionPower);

    }

    @Override
    public void updateFaithTrack(String playerID, int position, HashMap<String, HashMap<Integer, PopeFavorCard>> popeFavorCards) {
        faithTrack.updatePlayerPosition(playerID, position);
        faithTrack.updateFavorPopeCard(popeFavorCards);
    }

    @Override
    public void updateDashboard(String playerId, ArrayList<String> topDevCards, HashMap<Resource, Integer> strongBox, ArrayList<DepotState> warehouse) {
        DashBoardView dashBoardView = playerStates.get(playerId).dashBoard;
        if (dashBoardView == null) {
            playerStates.get(playerId).dashBoard = new DashBoardView(topDevCards, strongBox, warehouse, playerId);
        } else {
            dashBoardView.updateTopDevCards(topDevCards);
            dashBoardView.updateStrongBox(strongBox);
            dashBoardView.updateWarehouse(warehouse);
        }
    }

    @Override
    public void updateLeaderCardsState(String playerId, HashMap<String, Boolean> leaderCards) {
        HashMap<String, LeaderCardView> leaderCardsViews = playerStates.get(playerId).leaderCards;

        for (String leaderCardID : leaderCards.keySet()) {
            LeaderCardView leaderCardView = leaderCardsViews.get(leaderCardID);
            if (leaderCardView == null) {
                leaderCardsViews.put(leaderCardID, new LeaderCardView(leaderCardID));
                leaderCardView = leaderCardsViews.get(leaderCardID);
            }
            leaderCardView.setActive(leaderCards.get(leaderCardID));
        }
    }

    @Override
    public void updateMarket(int rows, int cols, Marble[][] marketStatus, Marble marbleLeft) {
        market = new MarketView(marbleLeft, marketStatus, rows, cols);
    }

    @Override
    public void updateDevCardGrid(String[][] topDevCardIDs) {
        devCardGridView = new DevCardGridView(topDevCardIDs);
    }

    @Override
    public BuyResourcesEvent askForMarketRow() {
        Direction dir = null;
        int index = 0;
        String output = "YOU HAVE TO CHOOSE THE RESOURCES FROM ONE ROW OR ONE COLUMN OF THE MARKET\n" +
                "WOULD YOU LIKE A ROW OR A COLUMN?\n";
        ArrayList<Pair<String, String>> direction = new ArrayList<>();
        direction.add(new Pair<String, String>(Direction.ROW.toString(), Color.WHITE.getAnsiCode()));
        direction.add(new Pair<String, String>(Direction.COLUMN.toString(), Color.WHITE.getAnsiCode()));
        out.println(market.toString());
        int result = displaySelectionForm(direction, null, 1, output).get(0);
        if (result == 0) {//ROW
            dir = Direction.ROW;
            ArrayList<Pair<String, String>> indexRow = new ArrayList<>();
            for (int i = 0; i < market.getRows(); i++) {
                indexRow.add(new Pair<String, String>("ROW " + (i + 1), Color.WHITE.getAnsiCode()));

            }
            index = displaySelectionForm(indexRow, null, 1, " ").get(0);
        }
        if (result == 1) {//COLUMN
            dir = Direction.COLUMN;
            ArrayList<Pair<String, String>> indexColumn = new ArrayList<>();
            for (int i = 0; i < market.getCols(); i++) {
                indexColumn.add(new Pair<String, String>("COLUMN " + (i + 1), Color.WHITE.getAnsiCode()));

            }
            index = displaySelectionForm(indexColumn, null, 1, " ").get(0);
        }
        out.println("YOU HAVE CHOSEN " + dir.toString() + (index + 1));
        return new BuyResourcesEvent(thisPlayer, dir, index);
    }

    @Override
    public BuyDevCardsEvent askForDevCard() {
        int slot = -1;
        String devCard = "";

        devCardGridView.display();
        ArrayList<Pair<String, String>> gridSelection = new ArrayList<>();
        String[][] grid = devCardGridView.getTopDevCardIDs();
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                gridSelection.add(new Pair<String, String>(grid[i][j], Color.WHITE.getAnsiCode()));
            }
        }
        int input1 = displaySelectionForm(gridSelection, null, 1, "DEVELOPMENT CARDS AVAILABLE : \n").get(0);
        devCard = gridSelection.get(input1).getKey();

        playerStates.get(thisPlayer).getDashBoard().displayDevCardSlots();
        ArrayList<Pair<String, String>> selectionArray = new ArrayList<>();
        AtomicInteger slotIndex = new AtomicInteger(1);
        playerStates.get(thisPlayer).getDashBoard().getTopDevCards().stream().forEach(n -> {
            selectionArray.add(new Pair<String, String>("SLOT " + slotIndex, Color.WHITE.getAnsiCode()));
            slotIndex.getAndIncrement();
        });
        int input2 = displaySelectionForm(selectionArray, null, 1, "THESE ARE THE SLOTS TO WHICH YOU CAN ADD THE BOUGHT CARD : \n").get(0);
        slot = input2;
        BuyDevCardsEvent event = new BuyDevCardsEvent(thisPlayer, devCard, slot);


        return event;
    }

    /*public static void main(String[] args) {
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

        HashMap<String, LeaderCardView> leaderCardsSet = new HashMap<>();
        LeaderCardView card1 = new LeaderCardView("LeaderCard5");
        card1.setSelected(true);
        leaderCardsSet.put("LeaderCard5", card1);
        LeaderCardView card2 = new LeaderCardView("LeaderCard6");
        card2.setSelected(true);
        leaderCardsSet.put("LeaderCard6", card2);

        HashMap<String, PlayerState> playerStates = new HashMap<>();

        PlayerState PL1 = new PlayerState(d, leaderCardsSet);
        PlayerState PL2 = new PlayerState(d, leaderCardsSet);
        PlayerState PL3 = new PlayerState(d, leaderCardsSet);

        playerStates.put("LISA", PL1);
        playerStates.put("PAOLO", PL2);
        playerStates.put("GIGI", PL3);

        String[][] grid0 = {
                {"DevCard1", "DevCard2", "DevCard3", "DevCard10"},
                {"DevCard4", "DevCard5", "DevCard6", "DevCard11"},
                {"DevCard7", "DevCard8", "DevCard9", "DevCard12"},
                {"DevCard13", "DevCard14", "DevCard15", "DevCard16"}
        };
        DevCardGridView devCardGridView = new DevCardGridView(grid0);


        int slot = -1;
        String devCard = "";

        devCardGridView.display();
        ArrayList<Pair<String, String>> gridSelection = new ArrayList<>();
        String[][] grid = devCardGridView.getTopDevCardIDs();
        for (int i = 0; i < grid[0].length; i++) {
            for (int j = 0; j < grid.length; j++) {
                gridSelection.add(new Pair<String, String>(grid[i][j], Color.WHITE.getAnsiCode()));
            }
        }
        int input1 = displaySelectionForm(gridSelection, null, 1, "  DEVELOPMENT CARDS AVAILABLE : \n").get(0);
        devCard = gridSelection.get(input1).getKey();

        playerStates.get(thisPlayer).getDashBoard().displayDevCardSlots();
        ArrayList<Pair<String, String>> selectionArray = new ArrayList<>();
        playerStates.get(thisPlayer).getDashBoard().displayDevCardSlots();
        AtomicInteger slotIndex = new AtomicInteger(1);
        playerStates.get(thisPlayer).getDashBoard().getTopDevCards().stream().forEach(n -> {
            selectionArray.add(new Pair<String, String>("SLOT " + slotIndex, Color.WHITE.getAnsiCode()));
            slotIndex.getAndIncrement();
        });
        int input2 = displaySelectionForm(selectionArray, null, 1, " THESE ARE THE SLOTS TO WHICH YOU CAN ADD THE BOUGHT CARD : \n").get(0);
        slot = input2;
        BuyDevCardsEvent event = new BuyDevCardsEvent(thisPlayer, devCard, slot);
        out.println(event.getPlayerId() + "\n" + event.getCardSlot() + "\n" + event.getDevCardID());
        out.println("event sent");


    }*/

    @Override
    public ActivateProductionEvent askForProductionPowersToUse() {
        playerStates.get(thisPlayer).getDashBoard().displayPersonalProductionPower(out);
        playerStates.get(thisPlayer).getDashBoard().displayDevCardSlots();

        ArrayList<Pair<String, String>> opt = new ArrayList<>();
        opt.add(new Pair<>("Personal production power", Color.reset()));
        for (String devCardID : playerStates.get(thisPlayer).getDashBoard().getTopDevCards()) {
            if(devCardID!=null) opt.add(new Pair<>(devCardID, Color.reset()));
        }

        ArrayList<Integer> powerChosen = displaySelectionFormVariableChoices(opt, null, opt.size(), "Choose which production power to activate");

        return new ActivateProductionEvent(thisPlayer, powerChosen.stream().filter(x -> x != 0).map(i -> opt.get(i).getKey()).collect(Collectors.toCollection(ArrayList::new)), powerChosen.contains(0));
    }

    @Override
    public String askForLeaderCardToDiscard() throws NotPresentException {
        ArrayList<LeaderCardView> leaderCardViews = playerStates.get(thisPlayer).getLeaderCards().values().stream().filter(lcv -> !lcv.isActive()).collect(Collectors.toCollection(ArrayList::new));

        if (leaderCardViews.size() == 0) throw new NotPresentException("No leader card can be discarded");

        ArrayList<Pair<String, String>> choices = leaderCardViews.stream().map(LeaderCardView::getIdCard).map(s -> new Pair<>(s, Color.reset())).collect(Collectors.toCollection(ArrayList::new));

        ArrayList<DrawableObject> drawableLeaderCards = leaderCardViews.stream().map(LeaderCardView::toString)
                .map(s -> new DrawableObject(s, 0, 0)).collect(Collectors.toCollection(ArrayList::new));

        int choice = displaySelectionForm(choices, new Panel(drawableLeaderCards, out, true), 1, "Choose a leader card to discard").get(0);

        return choices.get(choice).getKey();
    }

    @Override
    public String askForLeaderCardToActivate() throws NotPresentException {
        ArrayList<LeaderCardView> leaderCardViews = playerStates.get(thisPlayer).getLeaderCards().values().stream().filter(lcv -> !lcv.isActive()).collect(Collectors.toCollection(ArrayList::new));

        if (leaderCardViews.size() == 0) throw new NotPresentException("No leader card can be activated");

        ArrayList<Pair<String, String>> choices = leaderCardViews.stream().map(LeaderCardView::getIdCard).map(s -> new Pair<>(s, Color.reset())).collect(Collectors.toCollection(ArrayList::new));

        ArrayList<DrawableObject> drawableLeaderCards = leaderCardViews.stream().map(LeaderCardView::toString)
                .map(s -> new DrawableObject(s, 0, 0)).collect(Collectors.toCollection(ArrayList::new));

        int choice = displaySelectionForm(choices, new Panel(drawableLeaderCards, out, true), 1, "Choose a leader card to activate").get(0);

        return choices.get(choice).getKey();
    }

    @Override
    public ArrayList<LeaderPowerSelectStateEvent> askForLeaderCardToSelectOrDeselect() throws NotPresentException {
        ArrayList<LeaderCardView> leaderCardViews = playerStates.get(thisPlayer).getLeaderCards().values().stream().filter(LeaderCardView::isActive).collect(Collectors.toCollection(ArrayList::new));

        if (leaderCardViews.size() == 0) throw new NotPresentException("No leader card is active");

        ArrayList<Pair<String, String>> options = new ArrayList<>();
        ArrayList<String> leaderCardIDs = new ArrayList<>();
        ArrayList<Integer> leaderPowerIndexes = new ArrayList<>();
        ArrayList<Boolean> leaderPowersSelectedState = new ArrayList<>();

        for (LeaderCardView leaderCardView : leaderCardViews) {
            for (int i : leaderCardView.getSelectablePowersIndexes()) {
                options.add(new Pair<>(leaderCardView.getIdCard() + "\nPower " + i, Color.reset()));
                leaderCardIDs.add(leaderCardView.getIdCard());
                leaderPowerIndexes.add(i);
                leaderPowersSelectedState.add(leaderCardView.getSelected(i));
            }
        }

        ArrayList<DrawableObject> drawableLeaderCards = leaderCardViews.stream().map(LeaderCardView::toString)
                .map(s -> new DrawableObject(s, 0, 0)).collect(Collectors.toCollection(ArrayList::new));

        ArrayList<Integer> choices = displaySelectionFormVariableChoices(options, new Panel(drawableLeaderCards, out, true), options.size(), "Choose the leader powers to select");

        ArrayList<LeaderPowerSelectStateEvent> ret = new ArrayList<>();
        for (int choice : choices) {
            ret.add(new LeaderPowerSelectStateEvent(thisPlayer, leaderCardIDs.get(choice), leaderPowerIndexes.get(choice), !leaderPowersSelectedState.get(choice)));
        }
        return ret;
    }

    public int displayOthers() {
        ArrayList<Pair<String, String>> options;
        options = Arrays.stream(DisplayOptions.values()).map(option -> new Pair<String, String>(option.getTitle(), Color.WHITE.getAnsiCode())).collect(Collectors.toCollection(ArrayList::new));
        options.add(new Pair<>("NOTHING", Color.WHITE.getAnsiCode()));
        int input = displaySelectionForm(options, null, 1, "WOULD YOU LIKE TO SEE SOMETHING ABOUT THE GAME ? ?\n").get(0);

        if (input < DisplayOptions.values().length) {
            out.println(DisplayOptions.values()[input].getMessage());
            switch (DisplayOptions.values()[input]) {
                case DISPLAY_MARKET -> {
                    out.println(market.toString());
                    break;
                }
                case DISPLAY_DASHBOARD -> {
                    ArrayList<Pair<String, String>> allPlayers;
                    allPlayers = players.stream().map(player -> {
                        if (player.equals(thisPlayer)) return "YOURS";
                        else return player;
                    }).map(player -> new Pair<String, String>(player, Color.WHITE.getAnsiCode())).collect(Collectors.toCollection(ArrayList::new));
                    int inputPlayer = displaySelectionForm(allPlayers, null, 1, "WHOSE DASHBORAD WOULD YOU LIKE TO SEE? \n").get(0);
                    playerStates.get(players.get(inputPlayer)).getDashBoard().displayAll(players.get(inputPlayer));
                    break;
                }
                case DISPLAY_FAITH_TRACK -> {
                    this.faithTrack.display("Check the incremented positions and \n acquired PopeFavorCards !", thisPlayer);
                    break;
                }
                case DISPLAY_LEADER_CARD -> {
                    ArrayList<Pair<String, String>> allPlayers;
                    allPlayers = players.stream().map(player -> {
                        if (player.equals(thisPlayer)) return "YOURS";
                        else return player;
                    }).map(player -> new Pair<String, String>(player, Color.WHITE.getAnsiCode())).collect(Collectors.toCollection(ArrayList::new));
                    int inputPlayer = displaySelectionForm(allPlayers, null, 1, "WHOSE LEADER CARDS WOULD YOU LIKE TO SEE? \n").get(0);

                    out.println("[31;1;4m" + players.get(inputPlayer) + "'S SET OF LEADER CARDS\n" + Color.reset());
                    ArrayList<DrawableObject> objs = new ArrayList<>();
                    AtomicInteger offSet = new AtomicInteger();
                    AtomicInteger height = new AtomicInteger();
                    playerStates.get(players.get(inputPlayer)).getLeaderCards().forEach((cardId, cardView) -> {
                        DrawableObject obj = new DrawableObject(cardView.toString(), 0, 20 * offSet.get());
                        objs.add(obj);
                        offSet.getAndIncrement();
                        height.set(Integer.max(height.get(), obj.getHeight()));
                    });
                    Panel cardPanel = new Panel(objs, out, false);
                    cardPanel.show();

                    break;
                }
                case DISPLAY_DEV_CARD_GRID -> {
                    devCardGridView.display();
                    break;
                }
            }

            out.println("WOULD YOU LIKE TO SEE MORE? Y/N");
            String answer = in.next().toUpperCase();
            in.nextLine();
            if (answer.equals("YES") || answer.equals("Y")) {
                return displayOthers();
            } else {
                out.println("done");
                return 1;
            }
        } else {
            out.println("YOU CHOSE TO SEE ANYTHING");
            return 1;
        }
    }

    @Override
    public ArrayList<Event> askForNextAction(String playerID, boolean lastRound, TurnState turnState) {

        ArrayList<Event> events = new ArrayList<>();

        if (!thisPlayer.equals(playerID)) {
            out.println(playerID + " " + turnState.getDescription());
            displayOthers();
        } else {
            switch (turnState) {
                case START -> {
                    int inputIndex;
                    ArrayList<Action> allActions = Arrays.stream(Action.values()).collect(Collectors.toCollection(ArrayList::new));
                    ArrayList<Pair<String, String>> possibleActions = new ArrayList<>();

                    for (Action a : allActions) {
                        possibleActions.add(new Pair(a.getDescription(), Color.WHITE.getAnsiCode()));

                    }
                    inputIndex = displaySelectionForm(possibleActions, null, 1, "POSSIBLE ACTIONS: \n").get(0);
                    Action selectedAction = allActions.get(inputIndex);
                    out.println(selectedAction.getDescription());

                    switch (selectedAction) {
                        case BUY_DEVCARD -> {
                            events.add(askForDevCard());
                            break;
                        }
                        case TAKE_RESOURCES_FROM_MARKET -> {
                            events.add(askForMarketRow());
                            break;
                        }
                        case PRODUCE -> {
                            events.add(askForProductionPowersToUse());
                            break;
                        }
                        case LEADER_ACTION -> {
                            ArrayList<Pair<String, String>> options = new ArrayList<>();
                            options.add(new Pair<String, String>("DELETE LEADER CARD", Color.WHITE.getAnsiCode()));
                            options.add(new Pair<String, String>("ACTIVATE LEADER CARD", Color.WHITE.getAnsiCode()));

                            int chosenAction = displaySelectionForm(options, null, 1, "THESE ARE THE POSSIBLE LEADER ACTIONS YOU CAN DO: \n").get(0);
                            if (chosenAction == 0) {
                                String leaderCardToDiscard = null;
                                try {
                                    leaderCardToDiscard = askForLeaderCardToDiscard();
                                } catch (NotPresentException e) {
                                    e.printStackTrace();
                                }
                                events.add(new DiscardLeaderCardEvent(thisPlayer, leaderCardToDiscard));
                            } else {
                                String leaderCardToActivate = null;
                                try {
                                    leaderCardToActivate = askForLeaderCardToActivate();
                                } catch (NotPresentException e) {
                                    e.printStackTrace();
                                }
                                events.add(new ActivateLeaderCardEvent(thisPlayer, leaderCardToActivate));
                            }
                            break;
                        }
                        case DISPLAY_SMTH -> {
                            displayOthers();
                            events.add(askForNextAction(playerID, lastRound, turnState).get(0));
                            break;
                        }
                        case SELECT_LEADER_CARD -> {
                            try {
                                askForLeaderCardToSelectOrDeselect().forEach(ev -> events.add(ev));
                            } catch (NotPresentException e) {
                                e.printStackTrace();
                            }
                            break;
                        }

                    }

                    break;
                }


                case AFTER_LEADER_CARD_ACTION -> {
                    int inputIndex = -1;
                    ArrayList<Action> allActions = Arrays.stream(Action.values()).filter(action -> action != Action.LEADER_ACTION).collect(Collectors.toCollection(ArrayList::new));
                    ArrayList<Pair<String, String>> possibleActions = new ArrayList<>();

                    for (Action a : allActions) {
                        possibleActions.add(new Pair(a.getDescription(), Color.WHITE.getAnsiCode()));

                    }
                    inputIndex = displaySelectionForm(possibleActions, null, 1, "POSSIBLE ACTIONS: \n").get(0);
                    Action selectedAction = allActions.get(inputIndex);
                    out.println(selectedAction.getDescription());

                    switch (selectedAction) {
                        case BUY_DEVCARD -> {
                            events.add(askForDevCard());
                            break;
                        }
                        case TAKE_RESOURCES_FROM_MARKET -> {
                            events.add(askForMarketRow());
                            break;
                        }
                        case PRODUCE -> {
                            events.add(askForProductionPowersToUse());
                            break;
                        }
                        case DISPLAY_SMTH -> {
                            displayOthers();
                            events.add(askForNextAction(playerID, lastRound, turnState).get(0));
                            break;
                        }
                        case SELECT_LEADER_CARD -> {
                            try {
                                askForLeaderCardToSelectOrDeselect().forEach(ev -> events.add(ev));
                            } catch (NotPresentException e) {
                                e.printStackTrace();
                            }
                            break;
                        }

                    }
                    break;
                }


                case AFTER_MAIN_ACTION -> {
                    int inputIndex = -1;
                    ArrayList<Action> allActions = Arrays.stream(Action.values()).filter(action -> action == Action.LEADER_ACTION || action == Action.DISPLAY_SMTH).collect(Collectors.toCollection(ArrayList::new));

                    ArrayList<Pair<String, String>> possibleActions = new ArrayList<>();

                    for (Action a : allActions) {
                        possibleActions.add(new Pair(a.getDescription(), Color.WHITE.getAnsiCode()));

                    }
                    possibleActions.add(new Pair<String, String>("FINISH TURN", Color.WHITE.getAnsiCode()));
                    inputIndex = displaySelectionForm(possibleActions, null, 1, "POSSIBLE ACTIONS: \n").get(0);
                    if (inputIndex < allActions.size()) {
                        Action selectedAction = allActions.get(inputIndex);
                        out.println(selectedAction.getDescription());


                        switch (selectedAction) {
                            case LEADER_ACTION -> {
                                ArrayList<Pair<String, String>> options = new ArrayList<>();
                                options.add(new Pair<String, String>("DELETE LEADER CARD", Color.WHITE.getAnsiCode()));
                                options.add(new Pair<String, String>("ACTIVATE LEADER CARD", Color.WHITE.getAnsiCode()));
                                int chosenAction = displaySelectionForm(options, null, 1, "THESE ARE THE POSSIBLE LEADER ACTIONS YOU CAN DO: \n").get(0);
                                if (chosenAction == 0) {
                                    String leaderCardToDiscard = null;
                                    try {
                                        leaderCardToDiscard = askForLeaderCardToDiscard();
                                    } catch (NotPresentException e) {
                                        e.printStackTrace();
                                    }
                                    events.add(new DiscardLeaderCardEvent(thisPlayer, leaderCardToDiscard));
                                } else {
                                    String leaderCardToActivate = null;
                                    try {
                                        leaderCardToActivate = askForLeaderCardToActivate();
                                    } catch (NotPresentException e) {
                                        e.printStackTrace();
                                    }
                                    events.add(new ActivateLeaderCardEvent(thisPlayer, leaderCardToActivate));
                                }
                                break;
                            }
                            case DISPLAY_SMTH -> {
                                displayOthers();
                                events.add(askForNextAction(playerID, lastRound, turnState).get(0));
                                break;
                            }
                        }
                    } else {
                        events.add(new EndTurnEvent(thisPlayer));
                    }
                    break;
                }


                case END_OF_TURN -> {
                    displayOthers();
                    out.println("WOULD YOU LIKE TO SEE SOME OTHER PLAYER'S/ YOUR STATE? Y/N");
                    String response = in.next().toUpperCase();
                    if (isAffirmative(response)) {
                        displayOthers();
                    }
                    out.println("YOUR TURN HAS ENDED");
                    events.add(new EndTurnEvent(thisPlayer));
                    break;
                }


                case WAITING_FOR_SOMETHING -> {
                    break;
                }
                case MATCH_ENDED -> {
                    out.println("MATCH HAS ENDED");
                    break;
                }

            }

        }
        out.println("returned event");
        return events;
    }

    @Override
    public void updateLeaderCardDepositState(String playerID, String leaderCardID, int leaderPowerIndex, HashMap<
            Resource, Integer> storedResources) {
        playerStates.get(playerID).getLeaderCards().get(leaderCardID).updateDepositLeaderPower(leaderPowerIndex, storedResources);
    }

    @Override
    public void updateLeaderPowersSelectedState(String playerId, String
            leaderCardID, ArrayList<Boolean> powerSelectedStates) {
        LeaderCardView leaderCardView = playerStates.get(playerId).getLeaderCards().get(leaderCardID);
        for (int i = 0; i < powerSelectedStates.size(); i++) {
            leaderCardView.setPowerSelectionState(i, powerSelectedStates.get(i));
        }
    }

    public HashMap<Resource, Integer> getDepositLeaderPowerTotalResources() {
        HashMap<Resource, Integer> totalRes = new HashMap<>();
        Arrays.stream(Resource.values()).forEach(res -> totalRes.put(res, 0));
        playerStates.get(thisPlayer).getLeaderCards().values().stream().filter(cardView -> cardView.isActive()).map(card -> card.getTotalResourcesInDepositLeaderPowers()).forEach(hashMap -> hashMap.forEach((key, value) -> totalRes.put(key, totalRes.get(key) + value)));
        return totalRes;
    }

    @Override
    public ChosenResourcesEvent askWhereToTakeResourcesFrom(HashMap<Resource, Integer> required,
                                                            int freeChoicesResources) {
        //clone required
        HashMap<Resource, Integer> clonedRequired = (HashMap<Resource, Integer>) required.clone();
        HashMap<Resource, Integer> emptyHashmap = new HashMap<>();
        Arrays.stream(Resource.values()).forEach(re -> emptyHashmap.put(re, 0));
        //prepare parameters to build event to return
        HashMap<Resource, Integer> allChosen = new HashMap<>(emptyHashmap);
        HashMap<Resource, Integer> chosenFromWarehouse = new HashMap<>(emptyHashmap);

        HashMap<Resource, Integer> chosenFromLeaderPower = new HashMap<>(emptyHashmap);

        DashBoardView thisDashboard = playerStates.get(thisPlayer).getDashBoard();
        //Image of the current state of depots
        ArrayList<DepotState> currentDepotStates = thisDashboard.getWarehouse();

        //Takes only the active LeaderCards
        ArrayList<LeaderCardView> leaderCardViews = playerStates.get(thisPlayer).getLeaderCards().values().stream().collect(Collectors.toCollection(ArrayList::new));
        //Selects only DepositLeaderPowers active
        ArrayList<InfoPower> thisDepositLeaderPowers = new ArrayList<>();

        leaderCardViews.stream().forEach(cardView -> {
            if (cardView.isActive()) {
                IntStream.range(0, cardView.getLeaderPowersActive().size()).forEach(index -> {
                    LeaderPower power = cardView.getLeaderPowersActive().get(index);
                    if (power instanceof DepositLeaderPower)
                        thisDepositLeaderPowers.add(new InfoPower((DepositLeaderPower) power, cardView.getIdCard(), index));
                });
            }
        });
        //display all deposits
//        out.println("THIS IS THE CURRENT STATE OF DEPOSITS: \n");
//        displayAvailableDeposits(thisDashboard);
//        out.println(thisDashboard.strongBoxToString());

        //display how many resources the user has to take
        StringBuilder builder = new StringBuilder();
        builder.append("YOU HAVE TO TAKE THE FOLLOWING RESOURCES FROM YOUR DEPOSITS: \n");
        builder.append(displayResourcesInHashMap(required) + "\n");
        if (freeChoicesResources != 0) {
            builder.append("ALSO, YOU HAVE " + freeChoicesResources + " OPTIONAL RESOURCES YOU CAN CHOOSE:\n");
            choseResources(Arrays.stream(Resource.values()).collect(Collectors.toCollection(ArrayList::new)), freeChoicesResources).forEach((key, value) -> required.put(key, required.get(key) + value));
            builder.append("SO NOW, THE RESOURCES YOU HAVE TO TAKE FROM THE DEPOSITS HAVE BECOME THE FOLLOWING: \n");
            builder.append(displayResourcesInHashMap(required));
        }
        out.println(builder.toString());
        //hashmaps that take count of total resources in each category of deposit(warehouse, leader deposit, strongbox)
        HashMap<Resource, Integer> warehouseHashMap = thisDashboard.totalResourcesInWarehouse();
        HashMap<Resource, Integer> leaderDepositsHashMap = getDepositLeaderPowerTotalResources();
        HashMap<Resource, Integer> strongBoxHashMap = (HashMap<Resource, Integer>) thisDashboard.getStrongBox().clone();

        //until there will be resources to take, the user will be asked to chose where to take from
        while (!required.keySet().stream().map(resource -> required.get(resource) == 0).reduce(true, (prev, foll) -> prev && foll)) {

            //ask where to take resources from
            ArrayList<Pair<String, String>> depositCategory = new ArrayList<>();
            depositCategory.add(new Pair<>("WAREHOUSE", Color.WHITE.getAnsiCode()));
            if (thisDepositLeaderPowers.size() > 0)
                depositCategory.add(new Pair<>("EXTRA DEPOSITS", Color.WHITE.getAnsiCode()));
            depositCategory.add(new Pair<>("STRONGBOX", Color.WHITE.getAnsiCode()));
            ArrayList<Integer> inputs = displaySelectionFormVariableChoices(depositCategory, null, depositCategory.size(), "WHERE WOULD YOU LIKE TO TAKE THE RESOURCES FROM?\n ");
            inputs.stream().forEach(input -> {
                out.print("HAVE A LOOK AT THE CURRENT TOTAL QUANTITY OF RESOURCES IN THE " + depositCategory.get(input).getKey() + "\n");

                switch (input) {
                    case 0 -> {
                        out.println(displayResourcesInHashMap(warehouseHashMap));
                        break;
                    }
                    case 1 -> {
                        out.println(displayResourcesInHashMap(leaderDepositsHashMap));
                        break;
                    }
                    case 2 -> {
                        out.println(displayResourcesInHashMap(strongBoxHashMap));
                        break;
                    }
                }

                out.println("WHICH KIND OF RESOURCES WOULD YOU LIKE TO REMOVE FROM " + depositCategory.get(input).getKey() + "?\n");
                ArrayList<Resource> justResources = required.keySet().stream().collect(Collectors.toCollection(ArrayList::new));
                ArrayList<Pair<String, String>> finalResourcesOptions = justResources.stream().map(res -> new Pair<>(res.toString() + " " + shapeResource(res), colorResource(res))).collect(Collectors.toCollection(ArrayList::new));
                ArrayList<Integer> inputs2 = displaySelectionFormVariableChoices(finalResourcesOptions, null, 1, " ");
                inputs2.stream().forEach(index -> {
                    out.println("HOW MANY " + justResources.get(index).toString() + " WOULD YOU LIKE TO REMOVE FROM THE " + depositCategory.get(input).getKey() + "?  INSERT A NUMBER\n");
                    int quantityToTake = required.get(justResources.get(index));
                    boolean successful = false;
                    boolean validInput = false;
                    int chosenNumber = -1;
                    String inp = in.nextLine();
                    while (!validInput) {
                        if (!inp.matches("-?\\d+")) {
                            out.println("YOU HAVE TO INSERT NUMBERS");
                            inp = in.nextLine();
                            continue;
                        }
                        chosenNumber = Integer.parseInt(inp);
                        if (chosenNumber < 0 || quantityToTake - chosenNumber < 0) {
                            out.println("YOU HAVE " + quantityToTake + " " + finalResourcesOptions.get(index).getKey() + " SO CHOSE A NUMBER BETWEEN 0 AND " + quantityToTake + "\n");
                            inp = in.nextLine();
                            continue;
                        }
                        validInput = true;
                    }

                    switch (input) {
                        case 0 -> {//WAREHOUSE

                            if (!currentDepotStates.stream().map(depot -> depot.getCurrentQuantity() != 0 && depot.getResourceType() == justResources.get(index)).reduce(false, (prev, foll) -> prev || foll)) {
                                out.println(DepotResultMessage.INVALID_RES_WAREHOUSE.getMessage());
                                successful = DepotResultMessage.INVALID_RES_WAREHOUSE.getSuccessfull();
                                out.println("QUANTITY OF RESOURCES IN THE WAREHOUSE REMAINS THE SAME\n");

                            } else if (chosenNumber > warehouseHashMap.get(justResources.get(index))) {
                                out.println(DepotResultMessage.REACHED_MIN_CAP_WAREHOUSE.getMessage());
                                successful = DepotResultMessage.REACHED_MIN_CAP_WAREHOUSE.getSuccessfull();
                                out.println("QUANTITY OF RESOURCES IN THE WAREHOUSE REMAINS THE SAME\n");

                            } else {
                                out.println(DepotResultMessage.SUCCESSFUL_GENERIC.getMessage());
                                successful = DepotResultMessage.SUCCESSFUL_GENERIC.getSuccessfull();
                                chosenFromWarehouse.put(justResources.get(index), chosenFromWarehouse.get(justResources.get(index)) + chosenNumber);
                                allChosen.put(justResources.get(index), allChosen.get(justResources.get(index)) + chosenNumber);
                                warehouseHashMap.put(justResources.get(index), required.get(justResources.get(index)) - chosenNumber);

                            }
                            displayResourcesInHashMap(warehouseHashMap);
                            break;
                        }
                        case 1 -> {//DPOSIT LEADER POWER
                            if (chosenNumber > leaderDepositsHashMap.get(justResources.get(index))) {
                                out.println(DepotResultMessage.UNSUCCESSFUL_SUB_FROM_LEADER.getMessage());
                                successful = DepotResultMessage.UNSUCCESSFUL_SUB_FROM_LEADER.getSuccessfull();
                                out.println("QUANTITY OF RESOURCES IN THE LEADER DEPOSITS REMAINS THE SAME\n");
                            } else {
                                out.println(DepotResultMessage.SUCCESSFUL_GENERIC.getMessage());
                                successful = DepotResultMessage.SUCCESSFUL_GENERIC.getSuccessfull();
                                chosenFromWarehouse.put(justResources.get(index), chosenFromWarehouse.get(justResources.get(index)) + chosenNumber);
                                allChosen.put(justResources.get(index), allChosen.get(justResources.get(index)) + chosenNumber);
                                leaderDepositsHashMap.put(justResources.get(index), required.get(justResources.get(index)) - chosenNumber);
                            }
                            displayResourcesInHashMap(leaderDepositsHashMap);

                            break;
                        }
                        case 2 -> {//STRONGBOX
                            if (!thisDashboard.getStrongBox().containsKey(justResources.get(index))) {
                                out.println(DepotResultMessage.INVALID_RES_STRONGBOX.getMessage());
                                successful = DepotResultMessage.INVALID_RES_STRONGBOX.getSuccessfull();
                                out.println("QUANTITY OF RESOURCES IN THE STRONGBOX REMAINS THE SAME\n");
                            } else if (chosenNumber > strongBoxHashMap.get(justResources.get(index))) {
                                out.println(DepotResultMessage.REACHED_MIN_CAP_STRONGBOX.getMessage());
                                successful = DepotResultMessage.REACHED_MIN_CAP_STRONGBOX.getSuccessfull();
                                out.println("QUANTITY OF RESOURCES IN THE STRONGBOX REMAINS THE SAME\n");
                            } else {
                                out.println(DepotResultMessage.SUCCESSFUL_GENERIC.getMessage());
                                successful = DepotResultMessage.SUCCESSFUL_GENERIC.getSuccessfull();
                                allChosen.put(justResources.get(index), allChosen.get(justResources.get(index)) + chosenNumber);
                                leaderDepositsHashMap.put(justResources.get(index), required.get(justResources.get(index)) - chosenNumber);

                            }
                            displayResourcesInHashMap(leaderDepositsHashMap);
                            break;
                        }

                    }//end of switch
                    if (successful)
                        required.put(justResources.get(index), required.get(justResources.get(index)) - chosenNumber);

                });

            });
            if (required.keySet().stream().map(resource -> required.get(resource) == 0).reduce(true, (prev, foll) -> prev && foll))
                out.println("YOU'RE DONE, ALL RESOURCES HAVE BEEN TAKEN\n");
            else {
                out.println("YOU STILL HAVE SOME RESOURCES LEFT TO TAKE FROM DEPOSITS");
                out.println(displayResourcesInHashMap(required));
            }
        }
        out.println("THIS IS THE FINAL RESULT OF YOUR CHOICE :\n");
        out.println("Resources chosen from Warehouse: \n");
        displayResourcesInHashMap(warehouseHashMap);
        out.println("Resources chosen from Leader deposits: \n");
        displayResourcesInHashMap(leaderDepositsHashMap);
        out.println("Resources chosen from Strongbox: \n");
        displayResourcesInHashMap(strongBoxHashMap);
        out.println("IF YOU'RE NOT SATISFIED WITH YOUR CHOICES, WOULD YOU LIKE TO REDO? Y/N\n");
        String answer = in.next().toUpperCase();
        in.nextLine();
        if (isAffirmative(answer)) {
            return askWhereToTakeResourcesFrom(clonedRequired, freeChoicesResources);
        } else
            return new ChosenResourcesEvent(thisPlayer, allChosen, chosenFromWarehouse, chosenFromLeaderPower);
    }

    @Override
    public HashMap<Resource, Integer> chooseResources(int requiredResourcesOFChoice, ArrayList<
            Resource> allowedResourcesTypes) {
        ArrayList<Pair<String, String>> options = new ArrayList<>();
        for (Resource r : allowedResourcesTypes) {
            options.add(new Pair<>(shapeResource(r), colorResource(r)));
        }

        ArrayList<Integer> choices = displaySelectionFormMultipleChoices(options, null, requiredResourcesOFChoice, "");

        HashMap<Resource, Integer> ret = new HashMap<>();
        for (Integer i : choices) {
            Resource r = allowedResourcesTypes.get(i);
            ret.put(r, ret.getOrDefault(r, 0) + 1);
        }
        return ret;
    }
}


