package it.polimi.ingsw.model.LeaderCards;

import it.polimi.ingsw.model.Resource;

import java.util.HashMap;

/**
 * Class containing the data needed to use the ability of a leader card that give additional resource deposit space
 */
public class DepositLeaderPower extends LeaderPower{

    private HashMap<Resource, Integer> maxResources;
    private HashMap<Resource, Integer> currentResources;

    /**
     * Constructor for the class used when it is going to decorate another LeaderPower
     * @param decoratedLeaderPower the LeaderPower that is going to be decorated
     * @param maxResources the amount of extra resources that can be stored
     */
    public DepositLeaderPower(LeaderPower decoratedLeaderPower, HashMap<Resource, Integer> maxResources) {
        this.decoratedLeaderPower = decoratedLeaderPower;
        this.maxResources = maxResources;

        this.currentResources = new HashMap<>();
        for (Resource r: maxResources.keySet()) {
            this.currentResources.put(r, 0);
        }
    }
    /**
     * Constructor for the class used when it is not going to decorate another LeaderPower
     * @param maxResources the amount of extra resources that can be stored
     */
    public DepositLeaderPower(HashMap<Resource, Integer> maxResources) {
        super();
        this.maxResources = maxResources;

        this.currentResources = new HashMap<>();
        for (Resource r: maxResources.keySet()) {
            this.currentResources.put(r, 0);
        }
    }

    /**
     * Getter for the extra storage space for the resources
     * @return the extra space for each resource
     */
    public HashMap<Resource, Integer> getMaxResources() {
        return maxResources;
    }

    /**
     * Getter for the current resources stored thanks to this power
     * @return resources currently stored
     */
    public HashMap<Resource, Integer> getCurrentResources() {
        return currentResources;
    }
}
