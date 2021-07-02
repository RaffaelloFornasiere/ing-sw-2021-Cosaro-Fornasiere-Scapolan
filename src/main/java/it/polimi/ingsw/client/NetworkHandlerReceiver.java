package it.polimi.ingsw.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import it.polimi.ingsw.events.EventRegistry;
import it.polimi.ingsw.events.clientEvents.ServerDisconnectionEvent;
import it.polimi.ingsw.events.Event;
import it.polimi.ingsw.events.HeartbeatEvent;
import it.polimi.ingsw.utilities.GsonInheritanceAdapter;
import it.polimi.ingsw.utilities.LockWrap;
import it.polimi.ingsw.utilities.MessageWrapper;

import java.io.IOException;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class NetworkHandlerReceiver{
    private final EventRegistry eventRegistry;
    private Scanner scanner;
    private String userID;

    /**
     * Constructor for the class
     * @param server the socket from which the messages will be received
     */
    public NetworkHandlerReceiver(Socket server) {
        if (server == null)
            throw new NullPointerException();
        try {
            scanner = new Scanner(server.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        userID = "";
        eventRegistry = new EventRegistry();
    }


    /**
     * Continuously reads the socket and if an event gets sent trough it, it decodes it and notifies it to an EventRegistry
     */
    public void receive() {
        GsonBuilder jsonBuilder = new GsonBuilder();
        jsonBuilder.registerTypeAdapter(Event.class, new GsonInheritanceAdapter<Event>());
        Gson gson = jsonBuilder.create();
        boolean done = false;
        while (!done) {
            String json;
            try {
                json = MessageWrapper.unwrap(scanner.nextLine());
                if(!json.contains("\"CLASSNAME\":\"it.polimi.ingsw.events.HeartbeatEvent\""))
                    System.out.println(json);
                Event event = gson.fromJson(json, Event.class);
                if (event.getClass() != HeartbeatEvent.class) {
                    userID = event.getPlayerId();
                    eventRegistry.sendEvent(event);
                }
            } catch (JsonSyntaxException ignore) {
            } catch (NoSuchElementException | IllegalStateException e){
                LockWrap<Boolean> a = new LockWrap<>(null, null);

                System.out.println("catch");
                a.getWaitIfLocked();
                eventRegistry.sendEvent(new ServerDisconnectionEvent(userID));
                done = true;
            }
        }
    }

    /**
     * Getter for the event registry that should handle all the received events
     * @return The event registry that should handle all the received events
     */
    public EventRegistry getEventRegistry() {
        return eventRegistry;
    }

    /**
     * Closes the scanner reading the socket
     */
    public void closeConnection() {
        scanner.close();
    }
}
