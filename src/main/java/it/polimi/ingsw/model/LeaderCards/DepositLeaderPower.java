package it.polimi.ingsw.model.LeaderCards;

import it.polimi.ingsw.model.Resource;

import java.util.HashMap;

public class DepositLeaderPower extends LeaderPower{

    private HashMap<Resource, Integer> maxResources;
    private HashMap<Resource, Integer> currentResources;

    public DepositLeaderPower(LeaderPower decoratedLeaderPower, HashMap<Resource, Integer> maxResources, HashMap<Resource, Integer> currentResources) {
        this.decoratedLeaderPower = decoratedLeaderPower;
        this.maxResources = maxResources;
        this.currentResources = currentResources;
    }

    public DepositLeaderPower(HashMap<Resource, Integer> maxResources, HashMap<Resource, Integer> currentResources) {
        super();
        this.maxResources = maxResources;
        this.currentResources = currentResources;
    }

    public HashMap<Resource, Integer> getMaxResources() {
        return maxResources;
    }

    public HashMap<Resource, Integer> getCurrentResources() {
        return currentResources;
    }
}
