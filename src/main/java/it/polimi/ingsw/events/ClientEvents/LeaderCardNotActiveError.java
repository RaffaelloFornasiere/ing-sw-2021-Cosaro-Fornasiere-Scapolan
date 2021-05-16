package it.polimi.ingsw.events.ClientEvents;

import it.polimi.ingsw.events.ControllerEvents.ControllerEvent;

public class LeaderCardNotActiveError extends ClientEvent{
    private String leaderCardID;

    /**
     * constructor for the class
     * @param playerID the player that generated(directly or indirectly) this event
     * @param leaderCardID the ID of the leader card that should be active
     */
    public LeaderCardNotActiveError(String playerID, String leaderCardID) {
        super(playerID);
        this.leaderCardID = leaderCardID;
    }

    /**
     * getter for the ID of the leader card that should be active
     * @return the ID of the leader card that should be active
     */
    public String getLeaderCardIndex() {
        return leaderCardID;
    }
}
