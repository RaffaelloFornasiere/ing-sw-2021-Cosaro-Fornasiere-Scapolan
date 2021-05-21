package it.polimi.ingsw.controller.modelChangeHandlers;

import it.polimi.ingsw.events.Event;
import it.polimi.ingsw.utilities.Observer;
import it.polimi.ingsw.virtualview.ClientHandlerSender;
import it.polimi.ingsw.virtualview.RequestsElaborator;

import java.util.HashMap;

public abstract class MatchObserver implements Observer {
    protected HashMap<String, ClientHandlerSender> networkData;

    public MatchObserver(HashMap<String, ClientHandlerSender> networkData){
        this.networkData = (HashMap<String, ClientHandlerSender>) networkData.clone();
    }

    protected void sendToAll(Event event){
        for(String playerID: networkData.keySet())
            networkData.get(playerID).sendEvent(event);
    }
}
