package it.polimi.ingsw.model.LeaderCards;

import it.polimi.ingsw.model.CardColor;

/**
 * Class specifying the amount of development card of a certain color and level to activate a leader card
 */
public class LevelCardRequirement extends Requirement{

    private CardColor type;
    private int level;
    private int quantity;

    /**
     * Constructor for the class used when it is going to decorate another Requirement
     * @param decoratedRequirement the Requirement that is going to be decorated
     * @param type the tcolor of the cards needed
     * @param level the level of the cards needed
     * @param quantity the amount of cards of specified color and level needed
     */
    public LevelCardRequirement(Requirement decoratedRequirement, CardColor type, int level, int quantity){
        this.decoratedRequirement = decoratedRequirement;
        this.type = type;
        this.level = level;
        this.quantity = quantity;
    }

    /**
     * Constructor for the class used when it is not going to decorate another Requirement
     * @param type the color of the cards needed
     * @param level the level of the cards needed
     * @param quantity the amount of cards of specified color and level needed
     */
    public LevelCardRequirement(CardColor type, int level, int quantity){
        super();
        this.type = type;
        this.level = level;
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
     * Getter for the level of the cards required to activate the leader card
     * @return the level of the cards required
     */
    public int getLevel(){
        return level;
    }

    /**
     * Getter for the quantity of cards required to activate the leader card
     * @return the quantity of cards required
     */
    public int getQuantity() {
        return quantity;
    }
}
