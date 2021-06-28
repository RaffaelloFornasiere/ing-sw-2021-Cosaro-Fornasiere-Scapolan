package it.polimi.ingsw.events.ControllerEvents.MatchEvents;

import it.polimi.ingsw.events.ControllerEvents.ControllerEvent;

/**
 * Events that will be handled by the server and that refer to a specific match
 */
public abstract class MatchEvent extends ControllerEvent {
    /**
     * Constructor for the class
     * @param playerID The player that generated(directly or indirectly) this event
     */
    public MatchEvent(String playerID){
        super(playerID);
    }
}
