package it.polimi.ingsw.model.LeaderCards;

import it.polimi.ingsw.model.ProductionPower;

import java.util.ArrayList;

/**
 * Class containing the data needed to use the ability of a leader card that give an additional production power
 */
public class ProductionLeaderPower extends LeaderPower{

    private ProductionPower effectPower;


    public ProductionLeaderPower(){
        super();
        incompatiblePowers.add(DiscountLeaderPower.class);
        incompatiblePowers.add(ExtraResourceLeaderPower.class);
    }

    /**
     * Constructor for the class
     * @param effectPower the production power given by the card
     */
    public ProductionLeaderPower(ProductionPower effectPower){
        this();
        this.effectPower = effectPower;
    }

    /**
     * Getter for the production power given
     * @return the production power
     */
    public ProductionPower getEffectPower() {
        return effectPower;
    }


}
