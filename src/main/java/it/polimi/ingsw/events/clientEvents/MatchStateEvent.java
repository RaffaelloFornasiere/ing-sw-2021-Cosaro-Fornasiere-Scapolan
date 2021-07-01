package it.polimi.ingsw.events.clientEvents;

import it.polimi.ingsw.model.TurnState;

/**
 * Event signaling changes in the match state
 */
public class MatchStateEvent extends ClientEvent{
    private final boolean lastRound;
    private final TurnState turnState;

    /**
     * Constructor for the class
     * @param playerId The ID of the player currently taking it's turn
     * @param lastRound If it is the last round
     * @param turnState The current point of the turn
     */
    public MatchStateEvent(String playerId, boolean lastRound, TurnState turnState) {
        super(playerId);
        this.lastRound = lastRound;
        this.turnState = turnState;
    }

    /**
     * @return If it is the last round
     */
    public boolean isLastRound() {
        return lastRound;
    }

    /**
     * Getter for the current point of the turn
     * @return The current point of the turn
     */
    public TurnState getTurnState() {
        return turnState;
    }
}
