package it.polimi.ingsw.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Immutable class
 */
public class ProductionPower {
    private HashMap<Resource, Integer> consumedResources;
    private HashMap<Resource, Integer> producedResources;

    private int requiredResourceOfChoice;
    private int producedResourceOfChoice;
    private int faithPointsProduced;

    public ProductionPower(){}

    public ProductionPower(HashMap<Resource, Integer> consumedResources, HashMap<Resource, Integer> producedResources,
                           int requiredResourceOfChoice, int producedResourceOfChoice, int faithPointsProduced) {
        this.consumedResources = (HashMap<Resource, Integer>) consumedResources.clone();
        this.producedResources = (HashMap<Resource, Integer>) producedResources.clone();
        this.requiredResourceOfChoice = requiredResourceOfChoice;
        this.producedResourceOfChoice = producedResourceOfChoice;
        this.faithPointsProduced = faithPointsProduced;
    }

    /**
     * getter
     *
     * @return the consumed resources
     */
    public HashMap<Resource, Integer> getConsumedResources() {
        return (HashMap<Resource, Integer>) consumedResources.clone();
    }

    /**
     * getter
     *
     * @return the produced resources
     */
    public HashMap<Resource, Integer> getProducedResources() {
        return (HashMap<Resource, Integer>) producedResources.clone();
    }

    public int getRequiredResourceOfChoice() {
        return requiredResourceOfChoice;
    }

    public int getProducedResourceOfChoice() {
        return producedResourceOfChoice;
    }

    /**
     * getter of produced FaithPoints
     * @return faith points
     */
    public int getFaithPointsProduced() {
        return faithPointsProduced;
    }

    /**
     * tells if the given resource satisfyies the consumed ones
     *
     * @param resources given resoruces
     * @return true if are enough, false if not
     */
    public boolean canProduce(HashMap<Resource, Integer> resources) {
        for (Map.Entry<Resource, Integer> i : consumedResources.entrySet()) {
            if (resources.containsKey(i.getKey()))
                return false;
            else if (resources.get(i.getKey()) < i.getValue())
                return false;
        }
        return true;
    }

}