package it.polimi.ingsw.model.LeaderCards;

import it.polimi.ingsw.model.Resource;

import java.util.HashMap;

/**
 * Class containing the data needed to use the ability of a leader card that give a discount when buying a development card
 */
public class DiscountLeaderPower extends LeaderPower{

    private HashMap<Resource, Integer> discount;

    /**
     * Constructor for the class used when it is going to decorate another LeaderPower
     * @param decoratedLeaderPower the LeaderPower that is going to be decorated
     * @param discount the discount for each resource type that is applied when buying a development card
     */
    public DiscountLeaderPower(LeaderPower decoratedLeaderPower, HashMap<Resource, Integer> discount){
        this.decoratedLeaderPower = decoratedLeaderPower;
        this.discount = discount;
    }

    /**
     * Constructor for the class used when it is going to decorate another LeaderPower
     * @param discount the discount for each resource type that is applied when buying a development card
     */
    public DiscountLeaderPower(HashMap<Resource, Integer> discount){
        super();
        this.discount = discount;
    }

    /**
     * Getter for the discount applied for each type of resource
     * @return the discount for each resource type
     */
    public HashMap<Resource, Integer> getDiscount() {
        return discount;
    }
}
