package it.polimi.ingsw.model.LeaderCards;

public class LevelCardRequirement extends Requirement{

    private CardColor type;
    private int level;
    private int quantity;

    public LevelCardRequirement(Requirement decoratedRequirement, CardColor type, int level, int quantity){
        this.decoratedRequirement = decoratedRequirement;
        this.type = type;
        this.level = level;
        this.quantity = quantity;
    }

    public LevelCardRequirement(CardColor type, int level, int quantity){
        super();
        this.type = type;
        this.level = level;
        this.quantity = quantity;
    }

    public CardColor getType() {
        return type;
    }

    public int getLevel(){
        return level;
    }

    public int getQuantity() {
        return quantity;
    }

    //STUB class
    private class CardColor {
    }
}
