package it.polimi.ingsw.events.ControllerEvents.MatchEvents;

import it.polimi.ingsw.model.Resource;

import java.util.HashMap;

/**
 * Event sent to the server when a player has to select some resources to do something
 * The client will receive a BadRequestEvent if this event was mal-pose
 * The client will receive a PlayerActionError if the action represented by this event can't be done
 */
public class SimpleChosenResourcesEvent extends MatchEvent{
    private final HashMap<Resource, Integer> allResourcesChosen;

    /**
     * Constructor for the class
     * @param playerID The ID of the player that generated(directly or indirectly) this event
     * @param allResourcesChosen All the resources chosen by the player
     */
    public SimpleChosenResourcesEvent(String playerID, HashMap<Resource, Integer> allResourcesChosen) {
        super(playerID);
        this.allResourcesChosen = new HashMap<>(allResourcesChosen);
    }

    /**
     * Getter for all the resources chosen by the player
     * @return All the resources chosen by the player
     */
    public HashMap<Resource, Integer> getAllResourcesChosen() {
        return new HashMap<>(allResourcesChosen);
    }
}
