package it.polimi.ingsw.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import it.polimi.ingsw.controller.EventRegistry;
import it.polimi.ingsw.events.ClientEvents.ServerDisconnectionEvent;
import it.polimi.ingsw.events.Event;
import it.polimi.ingsw.events.HeartbeatEvent;
import it.polimi.ingsw.utilities.GsonInheritanceAdapter;
import it.polimi.ingsw.utilities.MessageWrapper;

import java.io.IOException;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class NetworkHandlerReceiver{
    EventRegistry eventRegistry = new EventRegistry();
    Scanner scanner;
    private String userID;

    public NetworkHandlerReceiver(Socket server) {
        if (server == null)
            throw new NullPointerException();
        try {
            scanner = new Scanner(server.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        userID = "";
    }


    public void receive() {
        GsonBuilder jsonBuilder = new GsonBuilder();
        jsonBuilder.registerTypeAdapter(Event.class, new GsonInheritanceAdapter<Event>());
        Gson gson = jsonBuilder.create();
        while (true) {
            String json;
            try {
                if (scanner.hasNext()) {
                    json = MessageWrapper.unwrap(scanner.nextLine());
                    System.out.println(json);
                    Event event = gson.fromJson(json, Event.class);
                    if(event.getClass() != HeartbeatEvent.class) {
                        userID = event.getPlayerId();
                        eventRegistry.sendEvent(event);
                    }
                }
            } catch (JsonSyntaxException ignore) {
            } catch (NoSuchElementException | IllegalStateException e){
                eventRegistry.sendEvent(new ServerDisconnectionEvent(userID));
                break;
            }
        }
    }

    public EventRegistry getEventRegistry() {
        return eventRegistry;
    }

    public void closeConnection() {
        scanner.close();
    }
}
