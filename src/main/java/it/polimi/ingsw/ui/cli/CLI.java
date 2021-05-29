package it.polimi.ingsw.ui.cli;

import it.polimi.ingsw.client.NetworkAdapter;
import it.polimi.ingsw.events.Event;
import it.polimi.ingsw.model.Resource;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CLI {
    private final NetworkAdapter client;
    private static PrintWriter out = new PrintWriter(System.out, true);
    private static Scanner in = new Scanner(System.in);

    private String thisPlayer;
    private FaithTrackView faithTrack;
    private HashMap<String, PlayerState> playerStates;
    private ArrayList<String> players;
    private DevCardGridView devCardGridView;
    //private HashMap<String, ArrayList<LeaderCardView>> leaderCards;
    private MarketView market;

    public CLI(NetworkAdapter client) {
        this.client = client;
        players = new ArrayList<>();
        //players = players.stream().map(name -> name.toUpperCase()).collect(Collectors.toCollection(ArrayList::new));
        faithTrack = new FaithTrackView(players);
        playerStates = new HashMap<>();
        /*for (String player : players) {
            playerStates.put(player, new PlayerState(null, new ArrayList<>()));
        }*/

    }


    public void displayBadRequest(String playerID, String description, Event cause) {
        out.println(" Player" + playerID + ", your request, caused by event:" + cause.getEventName() + " cannot be satisfied:"
                + description);
    }

    public void displayDashBoardState(String playerId) {
        out.println("One dashboard has been updated!");
        DashBoardView dashBoardView = playerStates.get(playerId).dashBoard;
        if (dashBoardView != null) {
            dashBoardView.display(playerId.toUpperCase());
        }

    }

    ;

    public void displayDevCardGridState() {
        out.println("The market has been updated!");
        if (market != null) {
            DrawableObject obj = new DrawableObject(market.toString(), 10, 2);
            Panel panel = new Panel(100, (int) obj.getHeight() + 10, System.out);
            panel.addItem(obj);
            panel.show();
        }
    }

    ;

    public void displayFaithTrack(String causeMessage, String causePlayer) {
        out.println("The faithTrack has been updated!");
        faithTrack.display(causeMessage, causePlayer);
    }

    ;

    public void displayInitialChoiceForm(ArrayList<String> leaderCardsIDs, int numberOFLeaderCardsToChose, int numberOFResourcesToChose) {
        out.println(thisPlayer.toUpperCase() + ", CHOOSE " + numberOFLeaderCardsToChose + " AMONG THESE LEADER CARDS:");
        Panel panel = new Panel(1000, 50, out);
        AtomicInteger n = new AtomicInteger();
        leaderCardsIDs.stream().forEach(name -> {
            LeaderCardView card = new LeaderCardView(name);
            DrawableObject obj1 = new DrawableObject(card.toString(), 40 * (n.get()) + 10, 0);
            n.getAndIncrement();
            panel.addItem(obj1);
        });
        panel.show();
        ArrayList<Integer> cardIndexes = new ArrayList<Integer>();
        while (numberOFLeaderCardsToChose > 0) {
            out.println("YOU HAVE " + numberOFLeaderCardsToChose + " CARDS LEFT TO CHOOSE,\n PLEASE TYPE THE NUMBER OF ONE CARD: ");
            cardIndexes.add(in.nextInt());
            numberOFLeaderCardsToChose--;
        }
        out.println("YOU HAVE CHOSEN THIS LEADER CARDS:");
        Panel panel2 = new Panel(1000, 50, out);
        n.set(0);
        ArrayList<LeaderCardView> leaderCardsChosen = new ArrayList<>();
        cardIndexes.stream().forEach(index -> {
            LeaderCardView card = new LeaderCardView("LeaderCard" + index);
            leaderCardsChosen.add(card);
            DrawableObject obj1 = new DrawableObject(card.toString(), 40 * (n.get()) + 10, 0);
            n.getAndIncrement();
            panel.addItem(obj1);
        });
        panel.show();

        out.println("DO YOU AGREE? yes/no");
        String response = in.next().toUpperCase();
        if (response.equals("YES")) {
            playerStates.get(thisPlayer).leaderCards.addAll(leaderCardsChosen);
            displayInitialResourcesChoiceForm(numberOFResourcesToChose);
        } else displayInitialChoiceForm(leaderCardsIDs, numberOFLeaderCardsToChose, numberOFResourcesToChose);
    }


    public void displayInitialResourcesChoiceForm(int numberOFResources) {
        HashMap<Resource, Integer> initialResources = new HashMap<>();
        for (Resource res : Resource.values()) {
            initialResources.put(res, 0);
        }
        out.println(thisPlayer.toUpperCase() + ", YOU HAVE TO CHOOSE " + numberOFResources + " RESOURCES:");

        for (Resource res : Resource.values()) {
            if (res != Resource.SHIELD && numberOFResources > 0) {
                out.println("HOW MANY " + res.toString() + " WOULD YOU LIKE? INSERT A NUMBER (" + numberOFResources + "LEFT)");
                int n = in.nextInt();
                while(numberOFResources - n <0)
                {
                    out.println("RETRY, HOW MANY " + res.toString() + "S WOULD YOU LIKE? INSERT A NUMBER");
                    n = in.nextInt();
                }
                initialResources.put(res, n);
                numberOFResources = numberOFResources - n;
            }
        }
        if (numberOFResources >=0) {
            if(numberOFResources==0) out.println("YOU HAVE " + numberOFResources + " RESOURCES LEFT");
            else {
                out.println("YOU HAVE " + numberOFResources + " RESOURCES LEFT: THESE WILL BE " + Resource.SHIELD.toString());
                initialResources.put(Resource.SHIELD, numberOFResources);
            }
        }
        out.println("YOU HAVE CHOSEN THIS LEADER CARDS:");
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
        } else displayInitialResourcesChoiceForm(numberOFResources);
    }

  /*  public static void main(String[] args) {
        String thisPlayer = "paolo";
        int numberOFResources = 7;
        HashMap<Resource, Integer> initialResources = new HashMap<>();
        for (Resource res : Resource.values()) {
            initialResources.put(res, 0);
        }
        out.println(thisPlayer.toUpperCase() + ", YOU HAVE TO CHOOSE " + numberOFResources + " RESOURCES:");

        for (Resource res : Resource.values()) {
            if (res != Resource.SHIELD && numberOFResources > 0) {
                out.println("HOW MANY " + res.toString() + " WOULD YOU LIKE? INSERT A NUMBER (" + numberOFResources + "LEFT)");
                int n = in.nextInt();
                while(numberOFResources - n <0)
                {
                    out.println("RETRY, HOW MANY " + res.toString() + "S WOULD YOU LIKE? INSERT A NUMBER");
                    n = in.nextInt();
                }
                initialResources.put(res, n);
                numberOFResources = numberOFResources - n;
            }
        }
        if (numberOFResources >=0) {
            if(numberOFResources==0) out.println("YOU HAVE " + numberOFResources + " RESOURCES LEFT");
            else {
                out.println("YOU HAVE " + numberOFResources + " RESOURCES LEFT: THESE WILL BE " + Resource.SHIELD.toString());
                initialResources.put(Resource.SHIELD, numberOFResources);
            }
        }

        out.println("YOU HAVE CHOSEN THIS RESOURCES:");
        StringBuilder builder = new StringBuilder();

        initialResources.keySet().forEach(resource -> {
            String color = Color.BLUE.getAnsiCode();
            String shape = "■";
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

    /**
     * Method that allows the display of the form for entering server data
     */
    void displaySetupForm() {
    }

    ;

    /**
     * Method that allows the display the failure of the setup with server
     */
    void displaySetupFailure() {
    }

    ;

    /**
     * Method that allows the display of the form for entering user data
     */
    void displayLogin() {
    }

    ;

    /**
     * Method that allows the display of successfully login of the user
     */
    void displayLoginSuccessful() {
    }

    ;

    /**
     * Method that allows the display of user login failure
     *
     * @param details the details of the failure
     */
    void displayLoginFailure(String details) {
    }

    ;

    /**
     * Method that allows you to view the arrival of a new player in the match
     *
     * @param playerJoined    the name of the new player
     * @param remainingPlayer the players required remaining to start the match
     */
    void displayUserJoined(String playerJoined, Integer remainingPlayer) {
    }

    ;

    /**
     * Method that allows you to view the arrival of a new player in the match in a existing lobby
     *
     * @param otherPlayer     the name of the new others player
     * @param remainingPlayer the players required remaining to start the match
     */
    void displayAddedToQueue(List<String> otherPlayer, Integer remainingPlayer) {
    }

    ;

    /**
     * Method that allows you to propose to the user the recovery of an old game
     */
    void displayProposeRestoreMatch() {
    }

    ;

    /**
     * Method that allows you to see that the game is starting
     */
    void displayStartingMatch() {
    }

    ;

    /**
     * Method that allows you to view the disconnection of a player
     *
     * @param username the name of disconnected player
     */
    void displayDisconnectedUser(String username) {
    }

    ;

    /**
     * Method that notifies disconnection from the server
     */
    void displayDisconnected() {
    }

    ;

    /**
     * Method that allows the display of a generic message
     */
    void displayGenericMessage(String message) {
    }

    ;
//------------------------------------------------------------------------------------------------------

    /**
     * Method that allows the display of the form for the choice of the initial LeaderCard
     *
     * @param availableCards a list with the available cards
     */
    void displayChoicePersonalLeaderCard(List<String> availableCards) {

    }

    ;


    /**
     * Method that notifies the automatic assignment of the resources
     *
     * @param resources the assigned card
     */
    void displayStartingResources(ArrayList<Resource> resources) {
    }

    ;

//---------------------------------------------------------------------------------------------------------

    /**
     * Method that gives freedom of action to the player who is in turn.
     * In the CLI also allows who is in turn to choose an action between takeResources, buyDevCard,  produce.
     */
    void displayWhoseTurnIsIt() {

    }

    ;

    /**
     * Method that asks you if you want to activate a LeaderCard.
     * and allows the display of the form for the choice of LeaderCards to activate in cli
     */
    void displayAskForLeaderCardActivation() {
    }

    ;


    /**
     * Method that allows the display of the form for the choice of LeaderCard to use currently
     */
    void displayLeaderCardSelection() {
    }

    ;

    ///torna indietro
//----------------------------------------------------------------------------------------------------

    /**
     * Method that allows you to choose a row/column from market
     */
    void displayChoiceOfMarketResources() {
    }

    ;


    /**
     * Method that displays the form to assign a depot to each resource obtained
     */
    void displayAskForResourceDestinationInDepot() {
    }

    ;

    /**
     * Method that allows the display of the resources in depots.
     */
    void displayDepotUpdated() {
    }

    ;
//------------------------------------------------------------------------------------------------------

    /**
     * Method that allows you to choose an available devCard to buy from DevCardGrid
     */
    void displayChoiceOfAvailableDevCards() {
    }

    ;

    /**
     * Method that displays the form to assign the bought card a destination in dashboard.
     */
    void displayAskForDevCardDestinationInSlots() {
    }

    ;

    /**
     * Method that allows the display of the slots in dashboard.
     */
    void displayDevCardSlotsUpdated() {
    }

    ;
//--------------------------------------------------------------------------------------------------------

    /**
     * Method that displays the form that allows you to choose a card for production, including the base production
     */
    void displayChoiceOfAvailableProductions() {
    }

    ;

//----------------------------------------------------------------------------------------------------------------------

    /**
     * Method that displays the new position in FaithTrack
     */
    void displayFaithTrackUpdated() {
    }

    ;

    /**
     * Method that displays the new  favorPopeCard acquired.
     */
    void displayFavorPopeCardUpdated() {
    }

    ;

//--------------------------------------------------------------------------------------------------------------------------


    /**
     * Method that allows you to view players' updates within the match
     */
    void displayPlayersUpdated() {
    }

    ;
//---------------------------------------------------------------------------------------------------

    /**
     * Method of displaying the victory message
     */
    void displayWinnerMessage() {
    }

    ;
    //--------------------------------------------------------

    /**
     * Method that notifies the end of current turn
     */
    void displayEndTurn() {
    }

    ;

    /**
     * Method that allows you to display the lobby
     */
    void displayLobbyCreated(String playersWaiting) {
    }

    ;

    /**
     * Method that allows the display of a recovered match
     */
    void displayRestoredMatch() {
    }

    private String colorResource(Resource res) {
        if (res == Resource.SERVANT) return Color.BLUE.getAnsiCode();
        if (res == Resource.SHIELD) return Color.RED.getAnsiCode();
        if (res == Resource.COIN) return Color.GREEN.getAnsiCode();
        else return Color.YELLOW.getAnsiCode();
    }

    private String shapeResource(Resource res) {
        if (res == Resource.SERVANT) return "■";
        if (res == Resource.SHIELD) return "◆";
        if (res == Resource.COIN) return "●";
        else return "▼";
    }


}
