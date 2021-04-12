package it.polimi.ingsw.model.LeaderCards;

import it.polimi.ingsw.model.Resource;

import java.util.HashMap;

/**
 * Class containing the data needed to use the ability of a leader card that give a resource of a certain type for each white marble got from the market
 */
public class ExtraResourceLeaderPower extends LeaderPower{

    private Resource resourceType;

    /**
     * Constructor for the class
     * @param resourceType the type of the resource given instead of nothing when getting a withe marble from the market
     */
    public ExtraResourceLeaderPower(Resource resourceType){
        this.resourceType = resourceType;
    }

    /**
     * Getter for the type of resource gotten instead of nothing when getting a withe marble from the market
     * @return the resource type
     */
    public Resource getResourceType() {
        return resourceType;
    }
}
