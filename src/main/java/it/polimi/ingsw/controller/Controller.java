package it.polimi.ingsw.controller;


import it.polimi.ingsw.events.BuyResourcesEvent;
import it.polimi.ingsw.model.Marble;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class Controller {
    MarketManager marketManager;


    Controller() {

    }


    public void BuyResourcesEvent(PropertyChangeEvent evt) {
        BuyResourcesEvent event = (BuyResourcesEvent) evt.getNewValue();
        var resources = marketManager.buy(
                event.getDirection(),
                event.getIndex());

        if(resources.contains(Marble.RED))
        {

        }

    }
}
