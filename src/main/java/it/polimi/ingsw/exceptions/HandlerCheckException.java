package it.polimi.ingsw.exceptions;

import it.polimi.ingsw.events.Event;

public class HandlerCheckException extends Exception{
    private final Event eventToSend;

    public HandlerCheckException(Event event){
        this.eventToSend = event;
    }

    public Event getEventToSend() {
        return eventToSend;
    }
}
