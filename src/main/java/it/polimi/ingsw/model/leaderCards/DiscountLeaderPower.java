package it.polimi.ingsw.model.leaderCards;

import it.polimi.ingsw.model.Resource;

import java.util.HashMap;

/**
 * Class containing the data needed to use the ability of a leader card that give a discount when buying a development card
 */
public class DiscountLeaderPower extends LeaderPower{

    private final HashMap<Resource, Integer> discount;

    /**
     * Constructor that initializes the power without any discount
     */
    public DiscountLeaderPower(){
        incompatiblePowers.add(ProductionLeaderPower.class);
        incompatiblePowers.add(ExtraResourceLeaderPower.class);
        discount = new HashMap<>();
    }

    /**
     * Constructor for the class
     * @param discount the discount for each resource type that is applied when buying a development card
     */
    public DiscountLeaderPower(HashMap<Resource, Integer> discount){
        this();
        this.discount.putAll(discount);
    }

    /**
     * Getter for the discount applied for each type of resource
     * @return the discount for each resource type
     */
    public HashMap<Resource, Integer> getDiscount() {
        return new HashMap<>(discount);
    }
}
