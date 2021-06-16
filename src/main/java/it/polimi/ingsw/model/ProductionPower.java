package it.polimi.ingsw.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Immutable class
 */
public class ProductionPower {
    private final HashMap<Resource, Integer> consumedResources;
    private final HashMap<Resource, Integer> producedResources;

    private final int requiredResourceOfChoice;
    private final int producedResourceOfChoice;
    private final int faithPointsProduced;

    @SuppressWarnings("unchecked")
    public ProductionPower(HashMap<Resource, Integer> consumedResources, HashMap<Resource, Integer> producedResources,
                           int requiredResourceOfChoice, int producedResourceOfChoice, int faithPointsProduced) {
        this.consumedResources = (HashMap<Resource, Integer>) consumedResources.clone();
        this.producedResources = (HashMap<Resource, Integer>) producedResources.clone();
        this.requiredResourceOfChoice = requiredResourceOfChoice;
        this.producedResourceOfChoice = producedResourceOfChoice;
        this.faithPointsProduced = faithPointsProduced;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o.getClass() != getClass())
            return false;
        ProductionPower p = (ProductionPower) o;
        int res = 1;
        res *= (p.consumedResources.equals(consumedResources))?1:0;
        res *= (p.faithPointsProduced == faithPointsProduced)?1:0;
        res *= (p.producedResources.equals(producedResources))?1:0;
        res *= (p.producedResourceOfChoice == producedResourceOfChoice)?1:0;
        res *= (p.requiredResourceOfChoice == requiredResourceOfChoice)?1:0;
        return res == 1;
    }


    /**
     * getter
     *
     * @return the consumed resources
     */
    @SuppressWarnings("unchecked")
    public HashMap<Resource, Integer> getConsumedResources() {
        return (HashMap<Resource, Integer>) consumedResources.clone();
    }

    /**
     * getter
     *
     * @return the produced resources
     */
    @SuppressWarnings("unchecked")
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
     *
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