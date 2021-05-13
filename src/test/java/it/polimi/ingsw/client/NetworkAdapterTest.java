package it.polimi.ingsw.client;

import it.polimi.ingsw.events.ControllerEvents.MatchEvents.MatchEvent;
import it.polimi.ingsw.events.Event;
import it.polimi.ingsw.utilities.PropertyChangeSubject;
import junit.framework.TestCase;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.net.InetAddress;

public class NetworkAdapterTest extends TestCase {


    public void testConstructor()
    {
        PropertyChangeSubject subject = new PropertyChangeSubject() {
            private PropertyChangeSupport support = new PropertyChangeSupport(this);
            public void sendEvent(Event event){
                if (MatchEvent.class.isAssignableFrom(event.getClass())) {
                    support.firePropertyChange(event.getEventName(), null, event);
                } else {
                    support.firePropertyChange(event.getEventName(), null, event);
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
        };

        try {
            NetworkAdapter nt = new NetworkAdapter(subject, InetAddress.getByName("192.168.0.172"));
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }

    }

    public void testConnectToServer() {
    }

    public void testEnterMatch() {
    }

    public void testCreateMatch() {
    }

    public void testBuyResources() {
    }

    public void testBuyDevCards() {
    }

    public void testActivateProduction() {
    }

    public void testActivateLeaderCard() {
    }
}