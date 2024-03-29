package it.polimi.ingsw.messageSenders;

import it.polimi.ingsw.events.Event;

public interface Sender {
    /**
     * Methods that sends an event
     * @param e the event to send
     */
    void sendObject(Event e);

    /**
     * Method responsible for closing the connection with the entity the object are being sent
     */
    void closeConnection();
}
