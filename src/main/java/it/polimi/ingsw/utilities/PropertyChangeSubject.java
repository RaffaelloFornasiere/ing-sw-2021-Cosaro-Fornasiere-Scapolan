package it.polimi.ingsw.utilities;

import java.beans.PropertyChangeListener;

/**
 * Interface containing all the methods needed for the registration of a PropertyChangeListener for PropertyChangeSupport
 */
public interface PropertyChangeSubject {

     /**
      * Registers a function(PropertyChangeListener) together with an associated string
      * @param propertyName The associated string
      * @param listener The function
      */
     void addPropertyChangeListener(String propertyName, PropertyChangeListener listener);

     /**
      * Registers a function(PropertyChangeListener)
      * @param listener The function to register
      */
     void addPropertyChangeListener(PropertyChangeListener listener);

     /**
      * Removes an association string - PropertyChangeListener
      * @param propertyName The string
      * @param listener The PropertyChangeListener
      */
     void removePropertyChangeListener(String propertyName, PropertyChangeListener listener);

     /**
      * Removes all the associations string - PropertyChangeListener for one PropertyChangeListener
      * @param listener The PropertyChangeListener
      */
     void removePropertyChangeListener(PropertyChangeListener listener);

     /**
      * Returns all the PropertyChangeListener registered in the PropertyChangeSupport
      * @return All the PropertyChangeListener registered in the PropertyChangeSupport
      */
     PropertyChangeListener[] getAllPropertyChangeListener();
}
