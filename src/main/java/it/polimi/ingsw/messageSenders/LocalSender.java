package it.polimi.ingsw.messageSenders;

import com.google.gson.Gson;
import it.polimi.ingsw.events.EventRegistry;
import it.polimi.ingsw.events.Event;
import it.polimi.ingsw.ui.cli.Color;

/**
 * Class for sending events locally
 */
public class LocalSender implements Sender {

    private final EventRegistry eventRegistry;
    private boolean closed;

    /**
     * Constructor for the class
     * @param eventRegistry the registry for the handlers tat will be called when an event is sent
     */
    public LocalSender(EventRegistry eventRegistry){
        this.eventRegistry = eventRegistry;
        this.closed = false;
    }

    @Override
    public void sendObject(Event e){
        if(!closed) {
            System.out.println(Color.BLUE.getAnsiCode() + e.getEventName() + Color.reset() + new Gson().toJson(e));
            new Thread(() -> eventRegistry.sendEvent(e)).start();
        }
    }

    @Override
    public void closeConnection() {
        this.closed = true;
    }
}
