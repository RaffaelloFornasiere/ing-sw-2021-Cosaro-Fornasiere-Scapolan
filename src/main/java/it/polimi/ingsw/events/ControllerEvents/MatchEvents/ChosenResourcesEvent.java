package it.polimi.ingsw.events.ControllerEvents.MatchEvents;

import it.polimi.ingsw.model.Resource;

import java.util.HashMap;

public class ChosenResourcesEvent extends SimpleChosenResourcesEvent{

    private HashMap<Resource, Integer> selectedResourcesFromWarehouse;
    private HashMap<Resource, Integer> selectedResourcesFromLeaderPowers;

    public ChosenResourcesEvent(String playerID, HashMap<Resource, Integer> allResourcesChosen, HashMap<Resource, Integer> selectedResourcesFromWarehouse, HashMap<Resource, Integer> selectedResourcesFromLeaderPowers) {
        super(playerID, allResourcesChosen);
        for(Resource r: Resource.values()){
            if(allResourcesChosen.getOrDefault(r, 0) < selectedResourcesFromWarehouse.getOrDefault(r, 0)+selectedResourcesFromLeaderPowers.getOrDefault(r, 0))
                throw new IllegalArgumentException("Too few resources in allResourcesChosen");
        }
        this.selectedResourcesFromWarehouse = (HashMap<Resource, Integer>) selectedResourcesFromWarehouse.clone();
        this.selectedResourcesFromLeaderPowers = (HashMap<Resource, Integer>) selectedResourcesFromLeaderPowers.clone();
    }

    public HashMap<Resource, Integer> getSelectedResourcesFromLeaderPowers() {
        return (HashMap<Resource, Integer>) selectedResourcesFromLeaderPowers.clone();
    }

    public HashMap<Resource, Integer> getSelectedResourcesFromWarehouse() {
        return (HashMap<Resource, Integer>) selectedResourcesFromWarehouse.clone();
    }
}
