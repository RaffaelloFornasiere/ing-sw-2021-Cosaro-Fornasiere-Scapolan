package it.polimi.ingsw.events.ClientEvents;

import it.polimi.ingsw.model.Resource;

import java.util.ArrayList;

public class ChoseMultipleExtraResourcePowerEvent extends ClientEvent{
    private ArrayList<Resource> resourceTypes;
    private int numberOfResources;

    public ChoseMultipleExtraResourcePowerEvent(String playerId, ArrayList<Resource> resourceTypes, int numberOfResources) {
        super(playerId);
        this.resourceTypes = (ArrayList<Resource>) resourceTypes.clone();
        this.numberOfResources = numberOfResources;
    }

    public ArrayList<Resource> getResourceTypes() {
        return (ArrayList<Resource>) resourceTypes.clone();
    }

    public int getNumberOfResources() {
        return numberOfResources;
    }
}
