package it.polimi.ingsw.events.ControllerEvents.MatchEvents;

import it.polimi.ingsw.events.ControllerEvents.ControllerEvent;
import it.polimi.ingsw.events.Event;

public abstract class MatchEvent extends ControllerEvent {
    /**
     * constructor for the class
     * @param playerID the player that generated(directly or indirectly) this event
     */
    public MatchEvent(String playerID){
        super(playerID);
    }
}