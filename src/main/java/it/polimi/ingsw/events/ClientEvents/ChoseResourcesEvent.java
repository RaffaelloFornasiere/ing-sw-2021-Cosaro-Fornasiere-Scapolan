package it.polimi.ingsw.events.ClientEvents;

import it.polimi.ingsw.model.Resource;

import java.util.HashMap;

public class ChoseResourcesEvent extends SimpleChoseResourcesEvent {
    private HashMap<Resource, Integer> requiredResources;

    public ChoseResourcesEvent(String playerId, HashMap<Resource, Integer> requiredResources, int requiredResourcesOFChoice) {
        super(playerId, requiredResourcesOFChoice);
        this.requiredResources = (HashMap<Resource, Integer>) requiredResources.clone();
    }

    public HashMap<Resource, Integer> getRequiredResources() {
        return (HashMap<Resource, Integer>) requiredResources.clone();
    }
}
