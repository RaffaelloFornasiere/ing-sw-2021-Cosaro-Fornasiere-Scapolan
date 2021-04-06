package it.polimi.ingsw.model.LeaderCards;

public class ProductionLeaderPower extends LeaderPower{

    private ProductionPower effectPower;

    public ProductionLeaderPower(LeaderPower decoratedLeaderPower, ProductionPower effectPower){
        this.decoratedLeaderPower = decoratedLeaderPower;
        this.effectPower = effectPower;
    }

    public ProductionLeaderPower(ProductionPower effectPower){
        super();
        this.effectPower = effectPower;
    }

    public ProductionPower getEffectPower() {
        return effectPower;
    }

    //STUB class
    private class ProductionPower {
    }
}
