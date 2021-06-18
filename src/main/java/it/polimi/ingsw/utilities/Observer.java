package it.polimi.ingsw.utilities;

public interface Observer {
    /**
     * Function called when the observed object notifies it's observers
     * @param o The object that changed
     */
    void update(Object o);
}
