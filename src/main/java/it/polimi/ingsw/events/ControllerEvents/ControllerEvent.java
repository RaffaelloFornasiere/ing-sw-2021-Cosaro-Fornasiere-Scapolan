package it.polimi.ingsw.events.ControllerEvents;

import it.polimi.ingsw.events.Event;

public abstract class ControllerEvent extends Event{
    /**
     * constructor of the class
     *
     * @param playerId the player that generated(directly or indirectly) this event
     */
    public ControllerEvent(String playerId) {
        super(playerId);
    }
}
