package it.polimi.ingsw.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import it.polimi.ingsw.controller.EventRegistry;
import it.polimi.ingsw.events.*;
import it.polimi.ingsw.events.Event;
import it.polimi.ingsw.utilities.GsonInheritanceAdapter;
import it.polimi.ingsw.utilities.PropertyChangeSubject;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Scanner;

public class NetworkHandlerReceiver{
    EventRegistry eventRegistry = new EventRegistry();
    Scanner scanner;

    public NetworkHandlerReceiver(Socket server) {
        if (server == null)
            throw new NullPointerException();
        try {
            scanner = new Scanner(server.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @SuppressWarnings("InfiniteLoopStatement")
    public void receive() {
        GsonBuilder jsonBuilder = new GsonBuilder();
        jsonBuilder.registerTypeAdapter(Event.class, new GsonInheritanceAdapter<Event>());
        Gson gson = jsonBuilder.create();
        while (true) {
            String json;
            if (scanner.hasNext()) {
                json = scanner.nextLine();
                System.out.println(json);
                Event event = gson.fromJson(json, Event.class);
                eventRegistry.sendEvent(event);
            }
        }
    }

    public EventRegistry getEventRegistry() {
        return eventRegistry;
    }
}
