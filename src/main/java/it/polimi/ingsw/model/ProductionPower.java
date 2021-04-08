package it.polimi.ingsw.model;

import javax.swing.text.Keymap;
import java.util.HashMap;
import java.util.Map;

public class ProductionPower {
    private HashMap<Resource, Integer> consumedResources;
    private HashMap<Resource, Integer> producedResources;

    public ProductionPower(HashMap<Resource, Integer> consumedResources, HashMap<Resource, Integer> producedResources) {
        this.consumedResources = consumedResources;
        this.producedResources = producedResources;
    }

    /**
     * takes some resources as input, test if there are enough of them
     * and in case produce other resoruces. the re
     *
     * @param resources takes as input some resources that
     * @return produced resources
     */
    public HashMap<Resource, Integer> produce(HashMap<Resource, Integer> resources) {
        if (canProduce(resources)) {
            consumedResources.forEach((key, value) -> resources.put(key,
                    resources.get(key) - value));
            return producedResources;
        }

        // throw some exception;
        return null;
    }

    /**
     * getter
     *
     * @return the consumed resources
     */
    public HashMap<Resource, Integer> getConsumedResources() {
        return consumedResources;
    }

    /**
     * getter
     *
     * @return the produced resources
     */
    public HashMap<Resource, Integer> getProducedResources() {
        return producedResources;
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