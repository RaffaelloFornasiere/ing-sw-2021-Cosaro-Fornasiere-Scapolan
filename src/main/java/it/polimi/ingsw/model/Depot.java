package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.DepotResourceException;
import it.polimi.ingsw.exceptions.ResourcesLimitsException;

public class Depot{
    private Resource resourceType;
    private int maxQuantity;
    private int currentQuantity;

    /**
     * constructor, takes as input the max quantity of resources for the depot.
     * initializes to zero the current quantity
     * initializes to ROCK the type of resource, which can be later on modified.
     * @param maxQuantity
     */
    public Depot(int maxQuantity){
     this.maxQuantity= maxQuantity;
     currentQuantity=0;
     this.resourceType=Resource.ROCK;
    }

    /**
     * Getter
     * @return which type of Resouce is in this depot
     */
    public Resource getResourceType() {
        return resourceType;
    }

    /**
     * Getter
     * @return the max quantity this depot can hold
     */
    public int getMaxQuantity() {
        return maxQuantity;
    }


    /**
     * Getter
     * @return the current quantity that is in the depot
     */
    public int getCurrentQuantity() {
        return currentQuantity;
    }

    /**
     *
     * @param type of resource to add
     * @param quantity  to add
     * @throws ResourcesLimitsException when the maximum number of resources is exceeded
     * @throws DepotResourceException when the type of Resource doesn't match the type of resource required.
     */
    protected void addResources(Resource type, int quantity) throws ResourcesLimitsException, DepotResourceException {
        if(currentQuantity==0) resourceType = type;
        if (!(type==resourceType)) throw new DepotResourceException();
        if((quantity+ currentQuantity)>maxQuantity) throw new ResourcesLimitsException();
        currentQuantity+=quantity;
    }

    /**
     *
     * @param type of resource to subtract
     * @param quantity  to subtract
     * @throws ResourcesLimitsException when the minimum number of resources is exceeded
     * @throws DepotResourceException when the type of Resource doesn't match the type of resource required.
     */
    protected void subResources(Resource type, int quantity) throws ResourcesLimitsException, DepotResourceException {
        if (!(type==resourceType)) throw new DepotResourceException();
        if((currentQuantity-quantity)<0) throw new ResourcesLimitsException();
        currentQuantity-=quantity;
    }

    /**
     * Swithes the content of two deposits.
     * @param depot is the deposit
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
