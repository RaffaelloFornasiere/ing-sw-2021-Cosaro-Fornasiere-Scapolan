package it.polimi.ingsw.model;

public class Depot{
    private Resource resourceType;
    private int maxResource;
    private int currentNumberResource;

    /**
     * Getter
     * @return which type of Resouce is in this depot
     */
    public Resource getResourceType() {
        return resourceType;
    }

    /**
     * Setter
     * @param resourceType the type of resource that is in this depot
     */
    public void setResourceType(Resource resourceType) {
        this.resourceType = resourceType;
    }

    /**
     * Getter
     * @return the max quantity this depot can hold
     */
    public int getMaxResource() {
        return maxResource;
    }

    /**
     * Setter
     * @param maxResource the max quantity this depot can hold
     */
    public void setMaxResource(int maxResource) {
        this.maxResource = maxResource;
    }

    /**
     * Getter
     * @return the current quantity that is in the depot
     */
    public int getCurrentNumberResource() {
        return currentNumberResource;
    }

    /**
     * Setter
     * @param currentNumberResource the current quantity
     */
    public void setCurrentNumberResource(int currentNumberResource) {
        this.currentNumberResource = currentNumberResource;
    }

}
