package it.polimi.ingsw.events.ClientEvents;

import it.polimi.ingsw.model.Resource;

import java.util.ArrayList;

/**
 * Event sent to the client when it has to choose a certain number of resources between different types, like when the player
 * buys from the market some white marbles and they have multiple leader power selected that converts them into different kinds
 * resources
 */
public class ChoseMultipleExtraResourcePowerEvent extends ClientEvent{
    private final ArrayList<Resource> resourceTypes;
    private final int numberOfResources;

    /**
     * Constructor for the class
     * @param playerId The ID of the player that will receive this event
     * @param resourceTypes The type of resources they can chose from
     * @param numberOfResources The total number of resources that they will need to chose
     */
    @SuppressWarnings("unchecked")
    public ChoseMultipleExtraResourcePowerEvent(String playerId, ArrayList<Resource> resourceTypes, int numberOfResources){
        super(playerId);
        this.resourceTypes = (ArrayList<Resource>) resourceTypes.clone();
        this.numberOfResources = numberOfResources;
    }

    /**
     * Getter for the type of resources they can chose from
     * @return The type of resources they can chose from
     */
    @SuppressWarnings("unchecked")
    public ArrayList<Resource> getResourceTypes() {
        return (ArrayList<Resource>) resourceTypes.clone();
    }

    /**
     * Getter for total number of resources that they will need to chose
     * @return The total number of resources that they will need to chose
     */
    public int getNumberOfResources() {
        return numberOfResources;
    }
}
