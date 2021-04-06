package it.polimi.ingsw.model.LeaderCards;

public class LevellessCardRequirement extends Requirement{

    private CardColor type;
    private int quantity;

    public LevellessCardRequirement(Requirement decoratedRequirement, CardColor type, int quantity){
        this.decoratedRequirement = decoratedRequirement;
        this.type = type;
        this.quantity = quantity;
    }

    public LevellessCardRequirement(CardColor type, int quantity){
        super();
        this.type = type;
        this.quantity = quantity;
    }

    public CardColor getType() {
        return type;
    }

    public int getQuantity() {
        return quantity;
    }

    //STUB class
    private class CardColor {
    }
}
