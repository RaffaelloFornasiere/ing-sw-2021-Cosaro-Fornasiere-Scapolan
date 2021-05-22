package it.polimi.ingsw.events.ControllerEvents.MatchEvents;

import it.polimi.ingsw.model.Resource;

import java.util.HashMap;

/**
 * Event sent to the server when a player has to select some resources to do something
 * The client will receive a BadRequestEvent if this event was mal-pose
 */
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
