package it.polimi.ingsw.virtualview;

import it.polimi.ingsw.events.Event;
import it.polimi.ingsw.events.ControllerEvents.MatchEvents.MatchEvent;
import it.polimi.ingsw.utilities.PropertyChangeSubject;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashMap;

public class VirtualView implements PropertyChangeSubject {
    private PropertyChangeSupport support = new PropertyChangeSupport(this);

    public VirtualView(){}

    public void sendEvent(Event event){
        support.firePropertyChange(event.getEventName(), null, event);
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
