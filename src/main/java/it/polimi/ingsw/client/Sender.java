package it.polimi.ingsw.client;

import it.polimi.ingsw.events.Event;

import java.io.IOException;

public interface Sender {
    /**
     * Methods that sends an event
     * @param e the event to send
     */
    void sendObject(Event e);
}
