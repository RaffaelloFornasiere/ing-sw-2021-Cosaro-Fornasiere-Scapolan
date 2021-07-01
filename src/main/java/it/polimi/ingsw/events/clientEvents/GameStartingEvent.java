package it.polimi.ingsw.events.clientEvents;

import java.util.ArrayList;

/**
 * Event sent to the players when the game starts to be set up
 */
public class GameStartingEvent extends ClientEvent{
    private final ArrayList<String> playerIDs;

    /**
     * Constructor for the class
     * @param playerID The player that generated the event
     * @param playerIDs The IDs of the players in the match
     */
    public GameStartingEvent(String playerID, ArrayList<String> playerIDs){
        super(playerID);
        this.playerIDs = new ArrayList<>(playerIDs);
    }

    /**
     * Getter for the IDs of the players in the match
     * @return The IDs of the players in the match
     */
    public ArrayList<String> getLeaderId() {
        return new ArrayList<>(playerIDs);
    }
}
