package it.polimi.ingsw.ui;

import it.polimi.ingsw.model.LeaderCards.LeaderCard;
import it.polimi.ingsw.model.Marble;
import it.polimi.ingsw.model.Resource;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * provides a common interface to
 */
public abstract class UI {
    private boolean turnActive;


    //generics
    abstract public void beginGame();
    abstract public void setUserTurnActive(boolean active);
    abstract public void printMessage(String message);
    abstract public void printError(String error);
    abstract public void ack();

    //game related

    /**
     * Asks the user to select which leader powers use with which marbles
     * @param marbles white marbles obtained from market
     * @param leaderCardIDs active leader cards
     * @return a hash with the required matching
     */
    abstract public HashMap<Marble, LeaderCard> getLeaderCardMarbleMatching(ArrayList<Marble> marbles, ArrayList<String> leaderCardIDs);

    /**
     * Asks the user to give the displacement of marbles
     * @param resources resources to put in whareouse
     * @return 3 arraylist with the marbles
     */
    abstract public HashMap<Resource, Integer> getWarehouseDisplacement(ArrayList<Resource> resources);

    /**
     * ask which active leader cards use
     * @param leaderCards active leader cards
     * @return the array containing the leader cards that the user wants to use
     */
    abstract public ArrayList<LeaderCard> useLeaderCardPowers(ArrayList<LeaderCard> leaderCards);



    abstract public ArrayList<ArrayList<Resource>> getResourcesSelection(ArrayList<Resource> required);
}
