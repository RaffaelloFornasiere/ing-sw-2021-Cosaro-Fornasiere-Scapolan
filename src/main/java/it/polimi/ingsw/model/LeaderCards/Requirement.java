package it.polimi.ingsw.model.LeaderCards;

/**
 * Class containing the requirements for activating a leader card
 * This class family can decorate another Requirement object to add another requirement for the same card
 */
public abstract class Requirement {

    protected Requirement decoratedRequirement;

    /**
     * Constructor that initializes the decorated Requirement object as null
     */
    public Requirement(){
        this.decoratedRequirement = null;
    }

    /**
     * Getter for the decorated Requirement object
     * @return the Requirement that it's being decorated by this object or null if it is not decorating any other Requirement
     */
    public Requirement getDecoratedRequirement() {
        return decoratedRequirement;
    }
}
