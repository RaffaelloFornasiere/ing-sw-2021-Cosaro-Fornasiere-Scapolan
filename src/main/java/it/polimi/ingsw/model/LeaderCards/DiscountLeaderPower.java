package it.polimi.ingsw.model.LeaderCards;

import it.polimi.ingsw.model.Resource;

import java.util.HashMap;

/**
 * Class containing the data needed to use the ability of a leader card that give a discount when buying a development card
 */
public class DiscountLeaderPower extends LeaderPower{

    private HashMap<Resource, Integer> discount;

    /**
     * Constructor for the class
     * @param discount the discount for each resource type that is applied when buying a development card
     */
    public DiscountLeaderPower(HashMap<Resource, Integer> discount){
        this.discount = (HashMap<Resource, Integer>)discount.clone();
    }

    /**
     * Getter for the discount applied for each type of resource
     * @return the discount for each resource type
     */
    public HashMap<Resource, Integer> getDiscount() {
        return (HashMap<Resource, Integer>)discount.clone();
    }
}
