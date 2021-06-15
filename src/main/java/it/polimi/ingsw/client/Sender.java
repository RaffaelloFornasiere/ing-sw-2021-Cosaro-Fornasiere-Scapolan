package it.polimi.ingsw.client;

import it.polimi.ingsw.events.Event;

import java.io.IOException;

public interface Sender {
    void sendObject(Event e);
}
