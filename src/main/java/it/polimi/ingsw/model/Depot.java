package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.DepotLimitException;
import it.polimi.ingsw.exceptions.DepotResourceException;

public class Depot{
    private Resource resourceType;
    private int maxQuantity;
    private int currentQuantity;

    public Depot(int maxQuantity){
     this.maxQuantity= maxQuantity;
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
    public void addResouces(int quantity, Resource type) throws DepotLimitException, DepotResourceException {
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
     * Swith the content of two deposits.
     * @param depot
     */
    public void switchDepot( Depot depot)throws DepotLimitException{
        if( depot.getCurrentQuantity()>this.maxQuantity || this.currentQuantity>depot.getMaxQuantity()) throw new DepotLimitException();
        this.resourceType=depot.getResourceType();
        depot.setResourceType(this.resourceType);
        this.currentQuantity=depot.getCurrentQuantity();
        depot.setCurrentQuantity(this.currentQuantity);
    }

}
