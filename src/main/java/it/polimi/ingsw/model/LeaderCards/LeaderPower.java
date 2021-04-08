package it.polimi.ingsw.model.LeaderCards;

/**
 * Class containing the data needed to use the ability of a leader card
 * This class family can decorate another LeaderPower object to add another power to the same leader card
 */
public abstract class LeaderPower {

    protected LeaderPower decoratedLeaderPower;

    /**
     * Constructor that initializes the decorated LeaderPower as null
     */
    public LeaderPower(){
        this.decoratedLeaderPower = null;
    }

    /**
     * Getter for the decorated LeaderPower
     * @return the LeaderPower that it's being decorated by this class or null if it is not decorating any other LeaderPower
     */
    public LeaderPower getDecoratedLeaderPower() {
        return decoratedLeaderPower;
    }
}