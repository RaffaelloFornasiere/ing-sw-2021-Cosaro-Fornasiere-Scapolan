package it.polimi.ingsw.exceptions;

import it.polimi.ingsw.events.Event;

/**
 * Checked exception thrown when a handler for an event encounters some problems
 */
public class HandlerCheckException extends Exception{
    private final Event eventToSend;

    /**
     * Constructor for the class
     * @param event the event that caused the exception
     */
    public HandlerCheckException(Event event){
        this.eventToSend = event;
    }

    /**
     * Getter for the event that caused the exception
     * @return The event that caused the exception
     */
    public Event getEventToSend() {
        return eventToSend;
    }
}
