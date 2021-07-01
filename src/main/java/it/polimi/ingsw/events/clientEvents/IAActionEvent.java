package it.polimi.ingsw.events.clientEvents;

import it.polimi.ingsw.model.singlePlayer.SoloActionToken;

/**
 * Event sent to the player when Lorenzo il Magnifico does it's turn
 */
public class IAActionEvent extends ClientEvent{
    private final SoloActionToken action;

    /**
     * Constructor for the class
     * @param playerId The player ID
     * @param action The token representing the action Lorenzo does this turn
     */
    public IAActionEvent(String playerId, SoloActionToken action) {
        super(playerId);
        this.action = action;
    }

    /**
     * Getter for the token representing the action Lorenzo does this turn
     * @return The token representing the action Lorenzo does this turn
     */
    public SoloActionToken getAction() {
        return action;
    }
}
