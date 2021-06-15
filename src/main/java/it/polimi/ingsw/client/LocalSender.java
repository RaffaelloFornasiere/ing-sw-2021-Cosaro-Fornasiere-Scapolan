package it.polimi.ingsw.client;

import it.polimi.ingsw.controller.EventRegistry;
import it.polimi.ingsw.events.Event;

public class LocalSender implements Sender{

    private final EventRegistry eventRegistry;

    public LocalSender(EventRegistry eventRegistry){
        this.eventRegistry = eventRegistry;
    }

    @Override
    public void sendObject(Event e){
        new Thread(()->eventRegistry.sendEvent(e)).start();
    }
}
