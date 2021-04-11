package it.polimi.ingsw.model.LeaderCards;

import it.polimi.ingsw.model.Resource;

import java.util.HashMap;

/**
 * Class specifying the quantity of resources of each type required needed to activate the leader card
 */
public class ResourcesRequirement extends Requirement{

    private HashMap<Resource, Integer> resources;

    /**
     * Constructor for the class
     * @param resources the quantity of resources of each type needed to activate the leader cards
     */
    public ResourcesRequirement(HashMap<Resource, Integer> resources){
        this.resources = (HashMap<Resource, Integer>)resources.clone();
    }

    /**
     * Getter for the resources required to activate the leader card
     * @return the quantity required for each resource type
     */
    public HashMap<Resource, Integer> getResources() {
        return (HashMap<Resource, Integer>)resources.clone();
    }
}
