package it.polimi.ingsw.model.LeaderCards;

import it.polimi.ingsw.exceptions.IllegalOperation;
import it.polimi.ingsw.model.CardColor;
import it.polimi.ingsw.model.DevCards.DevCard;
import it.polimi.ingsw.model.Player;

import java.util.ArrayList;
import java.util.Stack;

/**
 * Class specifying the amount of development card of a certain color and level to activate a leader card
 */
public class LevelCardRequirement extends Requirement{

    private CardColor type;
    private int level;
    private int quantity;

    /**
     * Constructor for the class used when it is going to decorate another Requirement
     * @param type the color of the cards needed
     * @param level the level of the cards needed
     * @param quantity the amount of cards of specified color and level needed
     */
    public LevelCardRequirement(CardColor type, int level, int quantity){
        if(level <= 0) throw new IllegalArgumentException("The card level cannot be negative");
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

    @Override
    public boolean isEquivalent(Requirement other) {
        if(!this.getClass().isInstance(other)) return false;
        if(this.type!=((LevelCardRequirement)other).type) return false;
        return this.level == ((LevelCardRequirement) other).level;
    }

    @Override
    public Requirement merge(Requirement other) throws IllegalOperation {
        if(!this.isEquivalent(other)) throw new IllegalOperation("The requirements to merge must be equivalent");
        return new LevelCardRequirement(this.type, this.level, this.quantity+((LevelCardRequirement)other).quantity);
    }

    @Override
    public boolean checkRequirement(Player p) {
        int count = 0;

        ArrayList<Stack<DevCard>> playerDevCards = p.getDashBoard().getCardSlots();

        for(Stack<DevCard> devCardSlot: playerDevCards)
            for(DevCard dc: devCardSlot)
                if(dc.getColor() == type && dc.getLevel() == level) count++;

        return count>=quantity;
    }
}
