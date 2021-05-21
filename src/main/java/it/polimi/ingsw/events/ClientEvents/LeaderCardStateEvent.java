package it.polimi.ingsw.events.ClientEvents;

import java.util.ArrayList;

/**
 * Event signaling a change of the state of a leader card
 */
public class LeaderCardStateEvent extends ClientEvent{
    private final String leaderCardID;
    private final ArrayList<Boolean> powerSelectedStates;

    /**
     * Constructor for the class
     * @param playerId The ID of the player owning the leader card
     * @param leaderCardID The ID of the leader card
     * @param powerSelectedStates The selected state of the powers of the leader card
     */
    @SuppressWarnings("unchecked")
    public LeaderCardStateEvent(String playerId, String leaderCardID, ArrayList<Boolean> powerSelectedStates) {
        super(playerId);
        this.leaderCardID = leaderCardID;
        this.powerSelectedStates = (ArrayList<Boolean>) powerSelectedStates.clone();
    }

    /**
     * Getter for the ID of the leader card
     * @return The ID of the leader card
     */
    public String getLeaderCardID() {
        return leaderCardID;
    }

    /**
     * Getter for the selected state of the powers of the leader card
     * @return The selected state of the powers of the leader card
     */
    @SuppressWarnings("unchecked")
    public ArrayList<Boolean> getPowerSelectedStates() {
        return (ArrayList<Boolean>) powerSelectedStates.clone();
    }
}
