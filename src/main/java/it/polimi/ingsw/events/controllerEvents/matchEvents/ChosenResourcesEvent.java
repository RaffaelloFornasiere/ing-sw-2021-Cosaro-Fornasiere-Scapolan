package it.polimi.ingsw.events.controllerEvents.matchEvents;

import it.polimi.ingsw.model.Resource;

import java.util.HashMap;

/**
 * Event sent to the server when a player has to select from where to take it's resources to do something
 * The client will receive a BadRequestEvent if this event was mal-posed
 * The client will receive a PlayerActionError if the action represented by this event can't be done
 */
public class ChosenResourcesEvent extends SimpleChosenResourcesEvent{

    private final HashMap<Resource, Integer> selectedResourcesFromWarehouse;
    private final HashMap<Resource, Integer> selectedResourcesFromLeaderPowers;

    /**
     * Constructor for the class
     * @param playerID The ID of the player that generated(directly or indirectly) this event
     * @param allResourcesChosen All the resources chosen by the player
     * @param selectedResourcesFromWarehouse The resources chosen from the warehouse
     * @param selectedResourcesFromLeaderPowers The resources chosen from the powers of leader cards
     */
    public ChosenResourcesEvent(String playerID, HashMap<Resource, Integer> allResourcesChosen, HashMap<Resource, Integer> selectedResourcesFromWarehouse, HashMap<Resource, Integer> selectedResourcesFromLeaderPowers) {
        super(playerID, allResourcesChosen);
        for(Resource r: Resource.values()){
            if(allResourcesChosen.getOrDefault(r, 0) < selectedResourcesFromWarehouse.getOrDefault(r, 0)+selectedResourcesFromLeaderPowers.getOrDefault(r, 0))
                throw new IllegalArgumentException("Too few resources in allResourcesChosen");
        }
        this.selectedResourcesFromWarehouse = new HashMap<>(selectedResourcesFromWarehouse);
        this.selectedResourcesFromLeaderPowers = new HashMap<>(selectedResourcesFromLeaderPowers);
    }

    /**
     * Getter for the resources chosen from the warehouse
     * @return The resources chosen from the warehouse
     */
    public HashMap<Resource, Integer> getSelectedResourcesFromLeaderPowers() {
        return new HashMap<>(selectedResourcesFromLeaderPowers);
    }

    /**
     * Getter for the resources chosen from the powers of leader cards
     * @return The resources chosen from the powers of leader cards
     */
    public HashMap<Resource, Integer> getSelectedResourcesFromWarehouse() {
        return new HashMap<>(selectedResourcesFromWarehouse);
    }
}
