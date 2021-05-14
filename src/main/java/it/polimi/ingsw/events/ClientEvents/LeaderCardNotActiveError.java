package it.polimi.ingsw.events.ClientEvents;

import it.polimi.ingsw.events.ControllerEvents.ControllerEvent;

public class LeaderCardNotActiveError extends ClientEvent{
    private int leaderCardIndex;

    /**
     * constructor for the class
     * @param playerID the player that generated(directly or indirectly) this event
     * @param leaderCardIndex the index of the leader card that should be active
     */
    public LeaderCardNotActiveError(String playerID, int leaderCardIndex) {
        super(playerID);
        this.leaderCardIndex = leaderCardIndex;
    }

    /**
     * getter for the index of the leader card that should be active
     * @return the index of the leader card that should be active
     */
    public int getLeaderCardIndex() {
        return leaderCardIndex;
    }
}
