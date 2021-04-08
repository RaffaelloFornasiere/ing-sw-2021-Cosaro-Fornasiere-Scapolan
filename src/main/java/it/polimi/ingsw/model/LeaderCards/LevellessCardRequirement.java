package it.polimi.ingsw.model.LeaderCards;

/**
* Class specifying the amount of development card of a certain color to activate a leader card
*/
public class LevellessCardRequirement extends Requirement{

    private CardColor type;
    private int quantity;

    /**
     * Constructor for the class used when it is going to decorate another Requirement
     * @param decoratedRequirement the Requirement that is going to be decorated
     * @param type the tcolor of the cards needed
     * @param quantity the amount of cards of specified color and level needed
     */
    public LevellessCardRequirement(Requirement decoratedRequirement, CardColor type, int quantity){
        this.decoratedRequirement = decoratedRequirement;
        this.type = type;
        this.quantity = quantity;
    }

    /**
     * Constructor for the class used when it is not going to decorate another Requirement
     * @param type the color of the cards needed
     * @param quantity the amount of cards of specified color and level needed
     */
    public LevellessCardRequirement(CardColor type, int quantity){
        super();
        this.type = type;
        this.quantity = quantity;
    }

    /**
     * Getter for the color of the cards required to activate the leader card
     * @return the color of the cards required
     */
    public CardColor getType() {
        return type;
    }

    /**
     * Getter for the quantity of cards required to activate the leader card
     * @return the quantity of cards required
     */
    public int getQuantity() {
        return quantity;
    }

    //STUB class
    private class CardColor {
    }
}
