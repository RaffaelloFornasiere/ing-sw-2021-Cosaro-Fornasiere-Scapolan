package it.polimi.ingsw.client;

import com.google.gson.Gson;
import it.polimi.ingsw.controller.EventRegistry;
import it.polimi.ingsw.events.Event;
import it.polimi.ingsw.ui.cli.Color;

public class LocalSender implements Sender{

    private final EventRegistry eventRegistry;

    public LocalSender(EventRegistry eventRegistry){
        this.eventRegistry = eventRegistry;
    }

    @Override
    public void sendObject(Event e){
        System.out.println(Color.BLUE.getAnsiCode() + e.getEventName() + Color.reset() + new Gson().toJson(e));
        new Thread(()->eventRegistry.sendEvent(e)).start();
    }
}
