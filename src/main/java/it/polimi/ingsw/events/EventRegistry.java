package it.polimi.ingsw.events;

import it.polimi.ingsw.events.Event;
import it.polimi.ingsw.utilities.PropertyChangeSubject;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class EventRegistry implements PropertyChangeSubject {
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    /**
     * Notifies the arrival of an event and triggers the registered handlers that have as an associated string the name of the event
     * @param event The event that arrived
     */
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

    @Override
    public PropertyChangeListener[] getAllPropertyChangeListener(){
        return support.getPropertyChangeListeners();
    }
}
