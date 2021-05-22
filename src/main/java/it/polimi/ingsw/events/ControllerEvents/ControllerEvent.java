package it.polimi.ingsw.events.ControllerEvents;

import it.polimi.ingsw.events.Event;

/**
 * Event that will be handled by the server
 */
public abstract class ControllerEvent extends Event{
    /**
     * Constructor of the class
     * @param playerId the player that generated(directly or indirectly) this event
     */
    public ControllerEvent(String playerId) {
        super(playerId);
    }
}
