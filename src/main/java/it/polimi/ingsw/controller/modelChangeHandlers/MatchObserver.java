package it.polimi.ingsw.controller.modelChangeHandlers;

import it.polimi.ingsw.events.Event;
import it.polimi.ingsw.utilities.Observer;
import it.polimi.ingsw.virtualview.RequestsElaborator;

import java.util.HashMap;

public abstract class MatchObserver implements Observer {
    protected HashMap<String, RequestsElaborator> networkData;

    public MatchObserver(HashMap<String, RequestsElaborator> networkData){
        this.networkData = (HashMap<String, RequestsElaborator>) networkData.clone();
    }

    protected void sendToAll(Event event){
        for(String playerID: networkData.keySet())
            networkData.get(playerID).getClientHandlerSender().sendEvent(event);
    }
}
