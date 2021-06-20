package it.polimi.ingsw.utilities;

import java.util.ArrayList;

/**
 * Observable object
 */
public abstract class Observable {
    private final ArrayList<Observer> observers;

    /**
     *
     */
    public Observable(){
        observers = new ArrayList<>();
    }

    /**
     * Calls the update method of all the observers
     */
    public void notifyObservers(){
        for(Observer observer: observers)
            observer.update(this);
    }

    /**
     * Adds an observer
     * @param observer The observer to add
     */
    public void addObserver(Observer observer){
        if(!observers.contains(observer))
            observers.add(observer);
    }

    /**
     * Removes an observer
     * @param observer The observer to remove
     */
    public void removeObserver(Observer observer){
        observers.remove(observer);
    }

    /**
     * Gets all the registered observers
     * @return The registered observer
     */
    public ArrayList<Observer> getObservers(){
        return new ArrayList<>(observers);
    }
}
