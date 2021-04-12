package it.polimi.ingsw.model.LeaderCards;

import it.polimi.ingsw.model.CardColor;

/**
* Class specifying the amount of development card of a certain color needed to activate a leader card
*/
public class LevellessCardRequirement extends Requirement{

    private CardColor type;
    private int quantity;

    /**
     * Constructor for the class
     * @param type the color of the cards needed
     * @param quantity the amount of cards of specified color and level needed
     */
    public LevellessCardRequirement(CardColor type, int quantity){
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
}
