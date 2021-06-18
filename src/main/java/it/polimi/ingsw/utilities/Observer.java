package it.polimi.ingsw.utilities;

/**
 * interface for the method that will be called by the observed object when it notifies it's observers
 */
public interface Observer {
    /**
     * Function called when the observed object notifies it's observers
     * @param o The object that changed
     */
    void update(Object o);
}
