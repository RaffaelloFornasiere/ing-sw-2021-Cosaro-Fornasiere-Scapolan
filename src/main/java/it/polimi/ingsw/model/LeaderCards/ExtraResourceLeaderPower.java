package it.polimi.ingsw.model.LeaderCards;

import it.polimi.ingsw.model.Resource;

import java.util.HashMap;

public class ExtraResourceLeaderPower extends LeaderPower{

    private Resource resourceType;

    public ExtraResourceLeaderPower(LeaderPower decoratedLeaderPower, Resource resourceType){
        this.decoratedLeaderPower = decoratedLeaderPower;
        this.resourceType = resourceType;
    }

    public ExtraResourceLeaderPower(Resource resourceType){
        super();
        this.resourceType = resourceType;
    }

    public Resource getResourceType() {
        return resourceType;
    }
}
