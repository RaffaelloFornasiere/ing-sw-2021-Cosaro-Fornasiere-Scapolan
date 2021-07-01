package it.polimi.ingsw.events.clientEvents;

import it.polimi.ingsw.model.Resource;

import java.util.HashMap;

/**
 * Event sent to the client when it has to choose from where to get the resources needed for something, for example
 * when the players has to chose from where to get the resources to use to buy a development card
 */
public class ChoseResourcesEvent extends SimpleChoseResourcesEvent {
    private final HashMap<Resource, Integer> requiredResources;

    /**
     * Constructor for the class
     *
     * @param playerId                  The ID of the player that will have to make the choice
     * @param requiredResources         The required resources of each type
     * @param requiredResourcesOfChoice The required resources of any type
     */
    public ChoseResourcesEvent(String playerId, HashMap<Resource, Integer> requiredResources, int requiredResourcesOfChoice) {
        super(playerId, requiredResourcesOfChoice);
        this.requiredResources = new HashMap<>(requiredResources);
    }

    /**
     * Getter for the required resources of each type
     *
     * @return The required resources of each type
     */
    public HashMap<Resource, Integer> getRequiredResources() {
        return new HashMap<>(requiredResources);
    }
}
