package it.polimi.ingsw.events.clientEvents;

import java.util.HashMap;

/**
 * Event signaling the change of state of a player
 */
public class PlayerStateEvent extends ClientEvent{
    HashMap<String, Boolean> leaderCards;

    /**
     * Constructor for the class
     * @param playerId The ID of the player
     * @param leaderCards The IDs of the leader cards that the player owns, together with their state
     */
    public PlayerStateEvent(String playerId, HashMap<String, Boolean> leaderCards) {
        super(playerId);
        this.leaderCards = new HashMap<>(leaderCards);
    }

    /**
     * Getter for the IDs of the leader cards that the player owns, together with their state
     * @return The IDs of the leader cards that the player owns, together with their state
     */
    public HashMap<String, Boolean> getLeaderCards() {
        return new HashMap<>(leaderCards);
    }
}
