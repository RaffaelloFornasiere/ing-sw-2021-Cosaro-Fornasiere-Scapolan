package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.DepotLimitException;
import it.polimi.ingsw.exceptions.DepotResourceException;

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
     * Setter of quantity.
     * @param quantity
     */

    public void setCurrentQuantity( int quantity) {
         currentQuantity= quantity;
    }

    /**
     *
     * @param quantity  to add
     * @param type of resource to add
     * @throws DepotLimitException when the maximum number of resources is exceeded
     * @throws DepotResourceException when the type of Resource doesn't match the type of resource required.
     */
    public void addResources(int quantity, Resource type) throws DepotLimitException, DepotResourceException {
     if (!(type==resourceType)) throw new DepotResourceException();
     if((quantity+ currentQuantity)>maxQuantity) throw new DepotLimitException();
     currentQuantity+=quantity;
    }

    /**
     *
     * @param quantity  to subtract
     * @param type of resource to subtract
     * @throws DepotLimitException when the minimum number of resources is exceeded
     * @throws DepotResourceException when the type of Resource doesn't match the type of resource required.
     */
    public void subResouces(int quantity, Resource type) throws DepotLimitException, DepotResourceException {
        if (!(type==resourceType)) throw new DepotResourceException();
        if((currentQuantity-quantity)<0) throw new DepotLimitException();
        currentQuantity-=quantity;
    }

    /**
     * Swithes the content of two deposits.
     * @param depot is the deposit
     */
    public void switchDepot( Depot depot)throws DepotLimitException{
        if( depot.getCurrentQuantity()>this.maxQuantity || this.currentQuantity>depot.getMaxQuantity()) throw new DepotLimitException();
        Resource tempResourceType = this.resourceType;
        this.resourceType=depot.getResourceType();
        depot.setResourceType(tempResourceType);
        int tempCurrentQuantity=this.currentQuantity;
        this.currentQuantity=depot.getCurrentQuantity();
        depot.setCurrentQuantity(tempCurrentQuantity);
    }

}
