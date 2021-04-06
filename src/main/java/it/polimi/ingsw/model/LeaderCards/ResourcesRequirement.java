package it.polimi.ingsw.model.LeaderCards;

import it.polimi.ingsw.model.Resource;

import java.util.HashMap;

public class ResourcesRequirement extends Requirement{

    private HashMap<Resource, Integer> resources;

    public ResourcesRequirement(Requirement decoratedRequirement, HashMap<Resource, Integer> resources){
        this.decoratedRequirement = decoratedRequirement;
        this.resources = resources;
    }

    public ResourcesRequirement(HashMap<Resource, Integer> resources){
        super();
        this.resources = resources;
    }

    public HashMap<Resource, Integer> getResources() {
        return resources;
    }
}
