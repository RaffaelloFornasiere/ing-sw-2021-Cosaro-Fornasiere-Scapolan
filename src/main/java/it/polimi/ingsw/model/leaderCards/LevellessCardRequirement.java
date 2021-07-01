package it.polimi.ingsw.model.leaderCards;

import it.polimi.ingsw.exceptions.IllegalOperation;
import it.polimi.ingsw.model.CardColor;
import it.polimi.ingsw.model.devCards.DevCard;
import it.polimi.ingsw.model.Player;

import java.util.ArrayList;
import java.util.Stack;

/**
* Class specifying the amount of development card of a certain color needed to activate a leader card
*/
public class LevellessCardRequirement extends Requirement{

    private final CardColor type;
    private final int quantity;

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

    @Override
    public boolean isEquivalent(Requirement other) {
        if(!this.getClass().isInstance(other)) return false;
        return this.type==((LevellessCardRequirement)other).type;
    }

    @Override
    public Requirement merge(Requirement other) throws IllegalOperation {
        if(!this.isEquivalent(other)) throw new IllegalOperation("The requirements to merge must be equivalent");
        return new LevellessCardRequirement(this.type, this.quantity+((LevellessCardRequirement)other).quantity);
    }

    @Override
    public boolean checkRequirement(Player p) {
        int count = 0;

        ArrayList<Stack<DevCard>> playerDevCards = p.getDashBoard().getCardSlots();

        for(Stack<DevCard> devCardSlot: playerDevCards)
            for(DevCard dc: devCardSlot)
                if(dc.getColor() == type) count++;

        return count>=quantity;
    }
}
