package it.polimi.ingsw.events.ClientEvents;

import it.polimi.ingsw.model.Resource;

public class DepotState {
    private Resource resourceType;
    private int maxQuantity;
    private int currentQuantity;

    public DepotState(Resource resourceType, int maxQuantity, int currentQuantity) {
        this.resourceType = resourceType;
        this.maxQuantity = maxQuantity;
        this.currentQuantity = currentQuantity;
    }

    public Resource getResourceType() {
        return resourceType;
    }

    public int getMaxQuantity() {
        return maxQuantity;
    }

    public int getCurrentQuantity() {
        return currentQuantity;
    }
}
