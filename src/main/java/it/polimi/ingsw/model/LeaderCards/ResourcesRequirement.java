package it.polimi.ingsw.model.LeaderCards;

import it.polimi.ingsw.model.Resource;

import java.util.HashMap;

/**
 * Class specifying the resource requirement for a leader card
 */
public class ResourcesRequirement extends Requirement{

    private HashMap<Resource, Integer> resources;

    /**
     * Constructor for the class used when it is going to decorate another Requirement
     * @param decoratedRequirement the Requirement that is going to be decorated
     * @param resources the resources needed to activate the leader cards
     */
    public ResourcesRequirement(Requirement decoratedRequirement, HashMap<Resource, Integer> resources){
        this.decoratedRequirement = decoratedRequirement;
        this.resources = resources;
    }

    /**
     * Constructor for the class used when it is not going to decorate another Requirement
     * @param resources the quantity for each resource type needed to activate the leader card
     */
    public ResourcesRequirement(HashMap<Resource, Integer> resources){
        super();
        this.resources = resources;
    }

    /**
     * Getter for the resources required to activate the leader card
     * @return the quantity required for each resource type
     */
    public HashMap<Resource, Integer> getResources() {
        return resources;
    }
}
