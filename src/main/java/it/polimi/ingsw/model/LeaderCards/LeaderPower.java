package it.polimi.ingsw.model.LeaderCards;

public abstract class LeaderPower {

    protected LeaderPower decoratedLeaderPower;

    public LeaderPower(){
        this.decoratedLeaderPower = null;
    }

    public LeaderPower getDecoratedLeaderPower() {
        return decoratedLeaderPower;
    }
}
