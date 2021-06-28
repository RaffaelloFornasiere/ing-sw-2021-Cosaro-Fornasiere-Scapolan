package it.polimi.ingsw.controller.modelChangeHandlers;

import it.polimi.ingsw.client.Sender;
import it.polimi.ingsw.events.Event;
import it.polimi.ingsw.utilities.Observer;

import java.util.HashMap;

public abstract class MatchObserver implements Observer {
    protected HashMap<String, Sender> networkData;

    /**
     * Constructor for the class
     * @param networkData The Senders of all the player involved into the match
     */
    public MatchObserver(HashMap<String, Sender> networkData){
        this.networkData = networkData;
    }

    /**
     * Sends to all the player involved in the match the same event
     * @param event The event to send
     */
    protected void sendToAll(Event event){
        for(String playerID: networkData.keySet())
            networkData.get(playerID).sendObject(event);
    }
}
