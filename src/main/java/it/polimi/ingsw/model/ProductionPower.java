package it.polimi.ingsw.model;

import java.util.HashMap;

public class ProductionPower {
    private HashMap<Resource, Integer> consumedResources;
    private HashMap<Resource, Integer> producedResources;

    public ProductionPower(HashMap<Resource, Integer> consumedResources, HashMap<Resource, Integer> producedResources){
     this.consumedResources=consumedResources;
     this.producedResources=producedResources;
    }


    public HashMap<Resource, Integer> produce(HashMap<Resource, Integer> input)
    {

        return null;
    }

    public HashMap<Resource, Integer> getConsumedResources() {
        return consumedResources;
    }
    public HashMap<Resource, Integer> getProducedResources() {
        return producedResources;
    }

}