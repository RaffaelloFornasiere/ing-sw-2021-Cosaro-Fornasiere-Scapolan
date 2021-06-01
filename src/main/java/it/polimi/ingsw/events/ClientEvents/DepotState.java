package it.polimi.ingsw.events.ClientEvents;

import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.ui.cli.DepotResultMessage;

/**
 * Class used for representing the state of a depot
 */
public class DepotState {
    private final Resource resourceType;
    private final int maxQuantity;
    private  int currentQuantity;

    /**
     * Constructor for the class
     *
     * @param resourceType    The type of resource stored in the depot
     * @param maxQuantity     The maximum quantity of resources that the depot can store
     * @param currentQuantity The quantity of resources actually stored
     * @throws IllegalArgumentException if the current quantity is higher than the max quantity
     */
    public DepotState(Resource resourceType, int maxQuantity, int currentQuantity) throws IllegalArgumentException {
        if (currentQuantity > maxQuantity)
            throw new IllegalArgumentException("current quantity must be less than max quantity");
        this.resourceType = resourceType;
        this.maxQuantity = maxQuantity;
        this.currentQuantity = currentQuantity;
    }

    /**
     * getter for the type of resource stored in the depot
     *
     * @return The type of resource stored in the depot
     */
    public Resource getResourceType() {
        return resourceType;
    }

    /**
     * getter for the maximum quantity of resources that the depot can store
     *
     * @return The maximum quantity of resources that the depot can store
     */
    public int getMaxQuantity() {
        return maxQuantity;
    }

    /**
     * getter for the quantity of resources actually stored
     *
     * @return The quantity of resources actually stored
     */
    public int getCurrentQuantity() {
        return currentQuantity;
    }

    public DepotResultMessage tryAddResource(Resource r) {
        if (r != resourceType) return DepotResultMessage.INVALID_RES_DEPOT;
        if (currentQuantity + 1 > maxQuantity) return DepotResultMessage.REACH_MAX_CAP_DEPOT;{
            this.currentQuantity++;
        return DepotResultMessage.SUCCESSFUL_DEPOT;}
    }
}
