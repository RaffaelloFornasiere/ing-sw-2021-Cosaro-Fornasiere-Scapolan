package it.polimi.ingsw.events.ControllerEvents;

import it.polimi.ingsw.events.Event;

public class StartMatchEvent extends ControllerEvent {
    /**
     * constructor of the class
     *
     * @param playerId the player that generated(directly or indirectly) this event
     */
    public StartMatchEvent(String playerId) {
        super(playerId);
    }
}
