package it.polimi.ingsw.ui;

import it.polimi.ingsw.client.NetworkAdapter;
import it.polimi.ingsw.events.ClientEvents.DepotState;
import it.polimi.ingsw.events.ControllerEvents.MatchEvents.*;
import it.polimi.ingsw.events.Event;
import it.polimi.ingsw.exceptions.NotPresentException;
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

    public abstract boolean askSingleplayer();

    public abstract InetAddress askIP();

    public abstract boolean askIfNewLobby();

    public abstract String askUserID();

    public abstract String askLeaderID();

    public abstract void displayLobbyState(String leaderID, ArrayList<String> otherPLayersID);

    public abstract void displayWaitingForPlayerToSetupState(String playerID);

    //game related


    public void invalidateUsername(){}

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

    public abstract ActivateProductionEvent askForProductionPowersToUse();

    public abstract String askForLeaderCardToDiscard() throws NotPresentException;

    public abstract String askForLeaderCardToActivate() throws NotPresentException;

    public abstract ArrayList<LeaderPowerSelectStateEvent> askForLeaderCardToSelectOrDeselect() throws NotPresentException;

    public abstract Event askForNextAction(String PlayerID, boolean lastRound, TurnState turnState);

    public abstract void updateLeaderCardDepositState(String playerID, String leaderCardID, int leaderPowerIndex, HashMap<Resource, Integer> storedResources);

    public abstract void updateLeaderPowersSelectedState(String playerId, String leaderCardID, ArrayList<Boolean> powerSelectedStates);


    /**
     * Asks the user to give the displacement of marbles USED
     *
     * @param resources resources to put in whareouse
     * @return 3 arraylist with the marbles
     */
    abstract public NewResourcesOrganizationEvent getWarehouseDisplacement(HashMap<Resource, Integer> resources);


    abstract public ChosenResourcesEvent askWhereToTakeResourcesFrom(HashMap<Resource, Integer> required, int  freeChoicesResources);

    public abstract HashMap<Resource, Integer> chooseResources(int requiredResourcesOFChoice, ArrayList<Resource> allowedResourcesTypes);
}
