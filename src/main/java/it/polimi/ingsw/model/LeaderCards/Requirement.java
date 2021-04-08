package it.polimi.ingsw.model.LeaderCards;

public abstract class Requirement {

    protected Requirement decoratedRequirement;

    public Requirement(){
        this.decoratedRequirement = null;
    }

    public Requirement getDecoratedRequirement() {
        return decoratedRequirement;
    }
}
