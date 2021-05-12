package it.polimi.ingsw.utilities;

import java.util.ArrayList;

public abstract class Observable {
    private ArrayList<Observer> observers;

    public Observable(){
        observers = new ArrayList<>();
    }

    public void notifyObservers(){
        for(Observer observer: observers)
            observer.update(this);
    }

    public void addObserver(Observer observer){
        if(!observers.contains(observer))
            observers.add(observer);
    }

    public void removeObserver(Observer observer){
        observers.remove(observer);
    }
}
