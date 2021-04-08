package it.polimi.ingsw.model.LeaderCards;

import it.polimi.ingsw.model.ProductionPower;

/**
 * Class containing the data needed to use the ability of a leader card that give an additional production power
 */
public class ProductionLeaderPower extends LeaderPower{

    private ProductionPower effectPower;

    /**
     * Constructor for the class used when it is going to decorate another LeaderPower
     * @param decoratedLeaderPower the LeaderPower that is going to be decorated
     * @param effectPower the production power given by the card
     */
    public ProductionLeaderPower(LeaderPower decoratedLeaderPower, ProductionPower effectPower){
        this.decoratedLeaderPower = decoratedLeaderPower;
        this.effectPower = effectPower;
    }

    /**
     * Constructor for the class used when it is going to decorate another LeaderPower
     * @param effectPower the production power given by the card
     */
    public ProductionLeaderPower(ProductionPower effectPower){
        super();
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
