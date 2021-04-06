package it.polimi.ingsw.model.LeaderCards;

import it.polimi.ingsw.model.Resource;

import java.util.HashMap;

public class DiscountLeaderPower extends LeaderPower{

    private HashMap<Resource, Integer> discount;

    public DiscountLeaderPower(LeaderPower decoratedLeaderPower, HashMap<Resource, Integer> discount){
        this.decoratedLeaderPower = decoratedLeaderPower;
        this.discount = discount;
    }

    public HashMap<Resource, Integer> getDiscount() {
        return discount;
    }
}
