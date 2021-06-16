package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.DepotResourceException;
import it.polimi.ingsw.exceptions.ResourcesLimitsException;

public class Depot{
    private Resource resourceType;
    private final int maxQuantity;
    private int currentQuantity;

    /**
     * Constructor, takes as input the max quantity of resources for the depot.
     * Initializes to zero the current quantity
     * Initializes to ROCK the type of resource, which can be later on modified.
     * @param maxQuantity The maximum quantity of resources the depot can store
     */
    public Depot(int maxQuantity){
     this.maxQuantity= maxQuantity;
     currentQuantity=0;
     this.resourceType=Resource.ROCK;
    }

    /**
     * Getter for the type of resource is in this depot
     * @return The type of resource is in this depot
     */
    public Resource getResourceType() {
        return resourceType;
    }

    /**
     * Getter for the max quantity this depot can hold
     * @return The max quantity this depot can hold
     */
    public int getMaxQuantity() {
        return maxQuantity;
    }


    /**
     * Getter for the current quantity that is in the depot
     * @return The current quantity that is in the depot
     */
    public int getCurrentQuantity() {
        return currentQuantity;
    }

    /**
     * Methods that add resources to the depot
     * @param type The type of resource to add
     * @param quantity The quantity of resources to add
     * @throws ResourcesLimitsException When the maximum number of resources is exceeded
     * @throws DepotResourceException When the type of Resource doesn't match the type of resource required.
     */
    protected void addResources(Resource type, int quantity) throws ResourcesLimitsException, DepotResourceException {
        if(currentQuantity==0) resourceType = type;
        if (!(type==resourceType)) throw new DepotResourceException();
        if((quantity+ currentQuantity)>maxQuantity) throw new ResourcesLimitsException();
        currentQuantity+=quantity;
    }

    /**
     *Methods that subtract resources to the depot
     * @param type The type of resource to subtract
     * @param quantity The quantity of resources to subtract
     * @throws ResourcesLimitsException When the minimum number of resources is exceeded
     * @throws DepotResourceException When the type of Resource doesn't match the type of resource required.
     */
    protected void subResources(Resource type, int quantity) throws ResourcesLimitsException, DepotResourceException {
        if (!(type==resourceType)) throw new DepotResourceException();
        if((currentQuantity-quantity)<0) throw new ResourcesLimitsException();
        currentQuantity-=quantity;
    }

    /**
     * Switches the content of two deposits.
     * @param depot The deposit to switch with
     * @throws ResourcesLimitsException If any of the depot cannot contain the resources of the other
     */
    protected void switchDepot( Depot depot)throws ResourcesLimitsException {
        if( depot.getCurrentQuantity()>this.maxQuantity || this.currentQuantity>depot.getMaxQuantity()) throw new ResourcesLimitsException();
        Resource tempResourceType = this.resourceType;
        this.resourceType=depot.getResourceType();
        depot.resourceType=tempResourceType;
        int tempCurrentQuantity=this.currentQuantity;
        this.currentQuantity=depot.getCurrentQuantity();
        depot.currentQuantity=tempCurrentQuantity;
    }

}
