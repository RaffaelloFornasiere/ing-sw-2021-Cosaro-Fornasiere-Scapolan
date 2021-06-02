package it.polimi.ingsw.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
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

public class NetworkHandlerReceiver implements PropertyChangeSubject {
    BufferedReader input;
    PropertyChangeSupport support = new PropertyChangeSupport(this);
    Scanner scanner;

    public NetworkHandlerReceiver(Socket server) {
        if (server == null)
            throw new NullPointerException();
        try {
            //input = new BufferedReader(new InputStreamReader(server.getInputStream()));
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
                support.firePropertyChange(event.getEventName(), null, event);
            }
        }
    }

    @Override
    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        if (propertyName == null || propertyName.equals("")) {
            addPropertyChangeListener(listener);
            return;
        }
        support.addPropertyChangeListener(propertyName, listener);
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    @Override
    public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        if (propertyName == null || "".equals(propertyName)) {
            removePropertyChangeListener(listener);
            return;
        }
        support.removePropertyChangeListener(propertyName, listener);
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }

    @Override
    public PropertyChangeListener[] getAllPropertyChangeListener(){
        return support.getPropertyChangeListeners();
    }
}
