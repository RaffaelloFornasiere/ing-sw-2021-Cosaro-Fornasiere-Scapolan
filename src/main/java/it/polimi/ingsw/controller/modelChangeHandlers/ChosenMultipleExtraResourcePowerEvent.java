package it.polimi.ingsw.controller.modelChangeHandlers;

import it.polimi.ingsw.events.ControllerEvents.MatchEvents.MatchEvent;
import it.polimi.ingsw.model.Resource;

import java.util.HashMap;

public class ChosenMultipleExtraResourcePowerEvent extends MatchEvent {
    private HashMap<Resource, Integer> chosenResources;

    public ChosenMultipleExtraResourcePowerEvent(String playerID, HashMap<Resource, Integer> chosenResources) {
        super(playerID);
        this.chosenResources = (HashMap<Resource, Integer>) chosenResources.clone();
    }

    public HashMap<Resource, Integer> getChosenResources() {
        return (HashMap<Resource, Integer>) chosenResources.clone();
    }
}
