package it.polimi.ingsw.ui.cli;

import it.polimi.ingsw.client.NetworkAdapter;
import it.polimi.ingsw.events.Event;
import it.polimi.ingsw.model.Resource;

import java.io.PrintWriter;
import java.util.*;
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
    private HashMap<String, HashMap<String,LeaderCardView>> leaderCards;
    private MarketView market;

    public CLI(NetworkAdapter client) {
        this.client = client;
        players = new ArrayList<>();
        players = players.stream().map(name -> name.toUpperCase()).collect(Collectors.toCollection(ArrayList::new));
        faithTrack = new FaithTrackView(players);
        dashboards = new HashMap<>();
        leaderCards = new HashMap<>();
        for (String player : players) {
            leaderCards.put(player, new HashMap<String,LeaderCardView>());
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

    /* public void displaySelectionForm(ArrayList<Resource> resourceTypes, int numberOfResources) {
       StringBuilder builder = new StringBuilder();
         builder.append("SOME ACTIVE EXTRA RESOURCE POWER ALLOWS YOU \n " +
                 "TO ADD " + numberOfResources + " RESOURCES TO YOUR STORE \n " +
                 " YOU CAN CHOOSE THOSE YOU WANT AMONG:\n");
         resourceTypes.stream().forEach(resource -> {
             builder.append(colorResource(resource)+resource.toString().toUpperCase()+"\n");
         });
         resourceTypes.stream().forEach(resource -> {
             builder.append("HOW MANY "+ resource.toString()+" DO YOU WANT?");
             String color = colorResource(resource);
             String shape = shapeResource(resource);
             builder.append(color + "╔═══╗" + " ");
         });
         builder.append(Color.reset() + "\n");
         resourceTypes.stream().forEach(resource -> {
             String color = colorResource(resource);
             String shape = shapeResource(resource);
             builder.append(color + "║ " + shape + " ║" + " ");
         });
         builder.append(Color.reset() + "\n");
         resourceTypes.stream().forEach(resource -> {
             String color = colorResource(resource);
             String shape = shapeResource(resource);
             builder.append(color + "╚═══╝" + " ");
         });
         builder.append(Color.reset() + "\n");
         AtomicInteger n = new AtomicInteger();
         resourceTypes.stream().forEach(resource -> {
             String color = colorResource(resource);
             String shape = shapeResource(resource);
             builder.append(color + "  " + n + "  " + " ");
             n.getAndIncrement();
         });
         builder.append(Color.reset() + "\n");
         int m = n.intValue();
         out.println("ENTER THE NUMBER OF RESOURCES, TO FINISH PRESS -1");
         int input = in.nextInt();
         ArrayList<Integer> inputs = new ArrayList<>();
         while (m > 0) {
             input = in.nextInt();
             if (inputs.contains(input)) {
                 out.println("YOU HAVE ALREADY CHOSEN THIS RESOURCE");
             } else inputs.add(input);
             m--;
         }

         out.println("YOU HAVE CHOSEN TO ADD THE FOLLOWING EXTRA RESOURCES TO YOUR DEPOSIT:\n");
         StringBuilder builder2 = new StringBuilder();

         inputs.forEach(index -> {
             String color = colorResource(resourceTypes.get(index));
             String shape = shapeResource(resourceTypes.get(index));
             builder2.append(color + "╔═══╗" + " ");
         });
         builder2.append(Color.reset() + "\n");
         inputs.stream().forEach(index -> {
             String color = colorResource(resourceTypes.get(index));
             String shape = shapeResource(resourceTypes.get(index));
             builder2.append(color + "║ " + shape + " ║" + " ");
         });
         builder2.append(Color.reset() + "\n");
         inputs.stream().forEach(index -> {
             String color = colorResource(resourceTypes.get(index));
             String shape = shapeResource(resourceTypes.get(index));
             builder2.append(color + "╚═══╝" + " ");
         });
         builder2.append(Color.reset() + "\n");
         AtomicInteger g = new AtomicInteger();
         inputs.stream().forEach(index -> {
             String color = colorResource(resourceTypes.get(index));
             String shape = shapeResource(resourceTypes.get(index));
             builder2.append(color + "  " + g + "  " + " ");
             g.getAndIncrement();
         });
         builder2.append(Color.reset() + "\n");

         out.println("DO YOU AGREE? yes/no");
         String response = in.next().toUpperCase();
         if (response.equals("YES")) {
             //generate response event
         } else displayChoseMultipleExtraResourcePowerForm(resourceTypes, numberOfResources);
     }

     public static void main(String[] args) {
         out.println("SOME ACTIVE EXTRA RESOURCE POWER ALLOWS YOU \n " +
                 "TO ADD SOME RESOURCES TO YOUR STORE, IF YOU LIKE \n " +
                 "CHOOSE THOSE YOU WANT AMONG THIS:\n");

         StringBuilder builder = new StringBuilder();
         ArrayList<Resource> resourceTypes = new ArrayList<>();
         resourceTypes.add(Resource.COIN);
         resourceTypes.add(Resource.COIN);
         resourceTypes.add(Resource.ROCK);
         resourceTypes.add(Resource.SERVANT);
         resourceTypes.add(Resource.SHIELD);

         resourceTypes.stream().forEach(resource -> {
             String color = colorResource(resource);
             String shape = shapeResource(resource);
             builder.append(color + "╔═══╗" + " ");
         });
         builder.append(Color.reset() + "\n");
         resourceTypes.stream().forEach(resource -> {
             String color = colorResource(resource);
             String shape = shapeResource(resource);
             builder.append(color + "║ " + shape + " ║" + " ");
         });
         builder.append(Color.reset() + "\n");
         resourceTypes.stream().forEach(resource -> {
             String color = colorResource(resource);
             String shape = shapeResource(resource);
             builder.append(color + "╚═══╝" + " ");
         });
         builder.append(Color.reset() + "\n");
         AtomicInteger n = new AtomicInteger();
         resourceTypes.stream().forEach(resource -> {
             String color = colorResource(resource);
             String shape = shapeResource(resource);
             builder.append(color + "  " + n + "  " + " ");
             n.getAndIncrement();
         });
         builder.append(Color.reset() + "\n");
         out.println(builder.toString());
         int m = n.intValue();
         out.println("ENTER THE NUMBER OF RESOURCES, TO FINISH PRESS -1");
         int input = in.nextInt();
         ArrayList<Integer> inputs = new ArrayList<>();
         while (m > 0) {
             input = in.nextInt();
             if (inputs.contains(input)) {
                 out.println("YOU HAVE ALREADY CHOSEN THIS RESOURCE");
             } else inputs.add(input);
             m--;
         }

         out.println("YOU HAVE CHOSEN TO ADD THE FOLLOWING EXTRA RESOURCES TO YOUR DEPOSIT:\n");
         StringBuilder builder2 = new StringBuilder();

         inputs.forEach(index -> {
             String color = colorResource(resourceTypes.get(index));
             String shape = shapeResource(resourceTypes.get(index));
             builder2.append(color + "╔═══╗" + " ");
         });
         builder2.append(Color.reset() + "\n");
         inputs.stream().forEach(index -> {
             String color = colorResource(resourceTypes.get(index));
             String shape = shapeResource(resourceTypes.get(index));
             builder2.append(color + "║ " + shape + " ║" + " ");
         });
         builder2.append(Color.reset() + "\n");
         inputs.stream().forEach(index -> {
             String color = colorResource(resourceTypes.get(index));
             String shape = shapeResource(resourceTypes.get(index));
             builder2.append(color + "╚═══╝" + " ");
         });
         builder2.append(Color.reset() + "\n");
         AtomicInteger g = new AtomicInteger();
         inputs.stream().forEach(index -> {
             String color = colorResource(resourceTypes.get(index));
             String shape = shapeResource(resourceTypes.get(index));
             builder2.append(color + "  " + g + "  " + " ");
             g.getAndIncrement();
         });
         builder2.append(Color.reset() + "\n");

         out.println("DO YOU AGREE? yes/no");
         String response = in.next().toUpperCase();
         if (response.equals("YES")) {
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
        HashMap<String,LeaderCardView> leaderCardsChosen = new HashMap<>();
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
    public void updateDepositLeaderPowerState( String leaderCardID, int leaderPowerIndex, HashMap<Resource, Integer> storedResources) {
    out.println(thisPlayer+" , THE DEPOSIT LEADER POWER OF "+  leaderCardID.toUpperCase()+"\n HAS BEEN UPDATED! CHECK IT OUT!");
    leaderCards.get(thisPlayer).get(leaderCardID).updateDepositLeaderPower(leaderPowerIndex, storedResources);
    out.println(leaderCards.get(thisPlayer).get(leaderCardID).toString());
    }

    public void displayDevCardSlotError( String devCardID, int cardSlot){
        out.println("SORRY, "+devCardID+" CANNOT BE ADDED TO THE SELECTED SLOT, SINCE IT \n  DOES NOT FULFILL THE SPECIFIED LEVEL REQUIREMENTS");
        DevCardView card= new DevCardView(devCardID);
        out.println(card.toString());
        dashboards.get(thisPlayer).displayDevCardSlots();
    }
    public void displayIncompatiblePowersError(){

    }
    public void displayLeaderCardNotActiveError(){

    }
    public void displayLobbyState(){

    }
    public void displayMatchState(){

    }

    public void displayOrganizeResourcesForm(){

    }
    public void displayPlayerState(){

    }
    public void displayRequirementsNotMetError(){

    }
     public void displaySetupDone(){

     }

     public void displaySimpleChoseResourcesForm(){

     }


    private static String colorResource(Resource res) {
        if (res == Resource.SERVANT) return Color.BLUE.getAnsiCode();
        if (res == Resource.SHIELD) return Color.RED.getAnsiCode();
        if (res == Resource.COIN) return Color.GREEN.getAnsiCode();
        else return Color.YELLOW.getAnsiCode();
    }

    private static String shapeResource(Resource res) {
        if (res == Resource.SERVANT) return "■";
        if (res == Resource.SHIELD) return "◆";
        if (res == Resource.COIN) return "●";
        else return "▼";
    }


}
