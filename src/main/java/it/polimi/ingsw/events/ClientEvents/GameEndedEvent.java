package it.polimi.ingsw.events.ClientEvents;

import java.util.ArrayList;

/**
 * Event sent to the clients when the game is over
 */
public class GameEndedEvent extends ClientEvent {
    ArrayList<FinalPlayerState> finalPlayerStates;

    /**
     * Constructor for the class
     * @param playerID The player that this event was sent to
     * @param finalPlayerStates The achievement of a player at the end of the match, in ascending order,
     *                          which means from last to first place
     */
    public GameEndedEvent(String playerID, ArrayList<FinalPlayerState> finalPlayerStates) {
        super(playerID);
        this.finalPlayerStates = new ArrayList<>(finalPlayerStates);
    }

    /**
     * Getter for the achievement of a player at the end of the match, in ascending order, which means from last to first place
     * @return The achievement of a player at the end of the match, in ascending order, which means from last to first place
     */
    public ArrayList<FinalPlayerState> getFinalPlayerStates() {
        return new ArrayList<>(finalPlayerStates);
    }
}
