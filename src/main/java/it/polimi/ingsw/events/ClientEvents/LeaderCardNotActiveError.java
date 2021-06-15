package it.polimi.ingsw.events.ClientEvents;

import it.polimi.ingsw.events.ControllerEvents.ControllerEvent;

/**
 * Error sent to the client when he tried to use a non active leader card
 */
public class LeaderCardNotActiveError extends ClientEvent{
    private final String leaderCardID;

    /**
     * Constructor for the class
     * @param playerID The player that tried to use a non active leader card
     * @param leaderCardID The ID of the leader card that should be active
     */
    public LeaderCardNotActiveError(String playerID, String leaderCardID) {
        super(playerID);
        this.leaderCardID = leaderCardID;
    }

    /**
     * Getter for the ID of the leader card that should be active
     * @return The ID of the leader card that should be active
     */
    public String getLeaderCardID() {
        return leaderCardID;
    }
}
