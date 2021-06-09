package it.polimi.ingsw.ui;

import it.polimi.ingsw.client.NetworkAdapter;
import it.polimi.ingsw.events.ClientEvents.DepotState;
import it.polimi.ingsw.events.ControllerEvents.MatchEvents.ActivateProductionEvent;
import it.polimi.ingsw.events.ControllerEvents.MatchEvents.BuyDevCardsEvent;
import it.polimi.ingsw.events.ControllerEvents.MatchEvents.BuyResourcesEvent;
import it.polimi.ingsw.events.ControllerEvents.MatchEvents.NewResourcesOrganizationEvent;
import it.polimi.ingsw.events.Event;
import it.polimi.ingsw.model.FaithTrack.PopeFavorCard;
import it.polimi.ingsw.model.LeaderCards.LeaderCard;
import it.polimi.ingsw.model.Marble;
import it.polimi.ingsw.model.ProductionPower;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.model.TurnState;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * provides a common interface to
 */
public abstract class UI {
    private boolean turnActive;
    protected NetworkAdapter client;
    protected String thisPlayer;

    public void setClient(NetworkAdapter networkAdapter) {
        this.client = networkAdapter;
    }

    //DON'T USE, NEEDED FOR DEBUGGING
    @Deprecated
    public void setThisPlayer(String thisPlayer) {
        this.thisPlayer = thisPlayer;
    }


    //generics
    abstract public void printMessage(String message);

    abstract public void printError(String error);

    abstract public void printWarning(String warning);

    public abstract InetAddress askIP();

    public abstract boolean askIfNewLobby();

    public abstract String askUserID();

    public abstract String askLeaderID();

    public abstract void displayLobbyState(String leaderID, ArrayList<String> otherPLayersID);

    public abstract void displayWaitingForPlayerToSetupState(String playerID);

    abstract public void beginGame();

    abstract public void setUserTurnActive(boolean active);

    abstract public void ack();

    //game related

    public abstract ArrayList<String> choseInitialLeaderCards(ArrayList<String> leaderCardsIDs, int numberOFLeaderCardsToChose);

    public abstract HashMap<Resource, Integer> choseResources(ArrayList<Resource> resourceType, int numberOFResources);

    public abstract void setPersonalProductionPower(String playerId, ProductionPower personalProductionPower);

    public abstract void updateFaithTrack(String playerID, int position, HashMap<String, HashMap<Integer, PopeFavorCard>> popeFavorCards);

    public abstract void updateDashboard(String playerId, ArrayList<String> topDevCards, HashMap<Resource, Integer> strongBox, ArrayList<DepotState> warehouse);

    public abstract void updateLeaderCardsState(String playerId, HashMap<String, Boolean> leaderCards);

    public abstract void updateMarket(int rows, int cols, Marble[][] marketStatus, Marble marbleLeft);

    public abstract void updateDevCardGrid(String[][] topDevCardIDs);

    public abstract BuyResourcesEvent askForMarketRow();

    public abstract BuyDevCardsEvent askForDevCard();

    public abstract ActivateProductionEvent askForDevCardToProduce();

    public abstract String askForLeaderCardToDiscard();

    public abstract String askForLeaderCardToActivate();

    public abstract Event askForNextAction(String PlayerID, boolean lastRound, TurnState turnState);


    /**
     * Asks the user to select which leader powers use with which marbles
     *
     * @param marbles       white marbles obtained from market
     * @param leaderCardIDs active leader cards
     * @return a hash with the required matching
     */
    abstract public HashMap<Marble, LeaderCard> getLeaderCardMarbleMatching(ArrayList<Marble> marbles, ArrayList<String> leaderCardIDs);

    /**
     * Asks the user to give the displacement of marbles
     *
     * @param resources resources to put in whareouse
     * @return 3 arraylist with the marbles
     */
    abstract public NewResourcesOrganizationEvent getWarehouseDisplacement(HashMap<Resource, Integer> resources);

    /**
     * ask which active leader cards use
     *
     * @param leaderCards active leader cards
     * @return the array containing the leader cards that the user wants to use
     */
    abstract public ArrayList<LeaderCard> useLeaderCardPowers(ArrayList<LeaderCard> leaderCards);


    abstract public ArrayList<ArrayList<Resource>> getResourcesSelection(ArrayList<Resource> required);
}
