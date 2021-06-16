package it.polimi.ingsw.model;

import java.util.HashMap;
import java.util.Map;

public class ProductionPower {
    private final HashMap<Resource, Integer> consumedResources;
    private final HashMap<Resource, Integer> producedResources;

    private final int requiredResourceOfChoice;
    private final int producedResourceOfChoice;
    private final int faithPointsProduced;

    /**
     * Constructor for the class
     * @param consumedResources The quantity of each type of resource consumed by the production
     * @param producedResources The quantity of each type of resource produced
     * @param requiredResourceOfChoice The quantity of resources of choice consumed by the production
     * @param producedResourceOfChoice The quantity of resources of choice produced
     * @param faithPointsProduced The quantity of faith points produced
     */
    public ProductionPower(HashMap<Resource, Integer> consumedResources, HashMap<Resource, Integer> producedResources,
                           int requiredResourceOfChoice, int producedResourceOfChoice, int faithPointsProduced) {
        this.consumedResources = new HashMap<>(consumedResources);
        this.producedResources = new HashMap<>(producedResources);
        this.requiredResourceOfChoice = requiredResourceOfChoice;
        this.producedResourceOfChoice = producedResourceOfChoice;
        this.faithPointsProduced = faithPointsProduced;
    }

    /**
     * Getter for the quantity of each type of resource consumed by the production
     * @return the quantity of each type of resource consumed by the production
     */
    public HashMap<Resource, Integer> getConsumedResources() {
        return new HashMap<>(consumedResources);
    }

    /**
     * Getter for the quantity of each type of resource produced
     * @return the quantity of each type of resource produced
     */
    public HashMap<Resource, Integer> getProducedResources() {
        return new HashMap<>(producedResources);
    }

    /**
     * Getter for the quantity of resources of choice consumed by the production
     * @return the quantity of resources of choice consumed by the production
     */
    public int getRequiredResourceOfChoice() {
        return requiredResourceOfChoice;
    }

    /**
     * Getter for the quantity of resources of choice produced
     * @return the quantity of resources of choice produced
     */
    public int getProducedResourceOfChoice() {
        return producedResourceOfChoice;
    }

    /**
     * Getter for the quantity of faith points produced
     * @return the quantity of faith points produced
     */
    public int getFaithPointsProduced() {
        return faithPointsProduced;
    }

}