package it.polimi.ingsw.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.events.Event;
import it.polimi.ingsw.utilities.GsonInheritanceAdapter;
import it.polimi.ingsw.utilities.PropertyChangeSubject;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class NetworkHandlerReceiver implements Runnable, PropertyChangeSubject {
    BufferedReader input;
    NetworkAdapter networkAdapter;
    PropertyChangeSupport support = new PropertyChangeSupport(this);
    public NetworkHandlerReceiver(Socket server, NetworkAdapter adapter)
    {
        if (server == null)
            throw new NullPointerException();
        try {
            input = new BufferedReader(new InputStreamReader(server.getInputStream()));
        } catch (IOException e) {
        }
        networkAdapter = adapter;
    }


    @Override
    @SuppressWarnings("InfiniteLoopStatement")
    public void run() {
        while(true)
        {
            try {
                StringBuilder builder = new StringBuilder();
                while(input.ready())
                {
                    builder.append(input.read());
                }
                if(builder.isEmpty())
                {
                    GsonBuilder jsonBuilder = new GsonBuilder();
                    jsonBuilder.registerTypeAdapter(Event.class, new GsonInheritanceAdapter<Event>());
                    Gson gson = jsonBuilder.create();
                    Event event = gson.fromJson(builder.toString(), Event.class);
                    support.firePropertyChange(event.getEventName(), null, event);
                }
            } catch (IOException e) {
                e.printStackTrace();
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
}
