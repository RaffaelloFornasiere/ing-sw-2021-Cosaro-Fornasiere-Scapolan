package it.polimi.ingsw.events.ControllerEvents.MatchEvents;

import it.polimi.ingsw.model.Resource;

import java.util.HashMap;

public class SimpleChosenResourcesEvent extends MatchEvent{
    private HashMap<Resource, Integer> allResourcesChosen;

    public SimpleChosenResourcesEvent(String playerID, HashMap<Resource, Integer> allResourcesChosen) {
        super(playerID);
        this.allResourcesChosen = (HashMap<Resource, Integer>) allResourcesChosen.clone();
    }

    public HashMap<Resource, Integer> getAllResourcesChosen() {
        return (HashMap<Resource, Integer>) allResourcesChosen.clone();
    }
}
