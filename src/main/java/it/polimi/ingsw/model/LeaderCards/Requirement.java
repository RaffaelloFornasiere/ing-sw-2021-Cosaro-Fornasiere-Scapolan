package it.polimi.ingsw.model.LeaderCards;

import it.polimi.ingsw.exceptions.IllegalOperation;
import it.polimi.ingsw.model.Player;

import java.io.Serializable;

/**
 * Class containing the requirements for activating a leader card
 */
public abstract class Requirement implements Serializable {

    /**
     * methods that checks if the instance passed represents the same kind of requirement as this or, in other
     * words, if they could be merged
     * @param other the other requirement with which check te equivalence
     * @return whether the two requirements are equivalent
     */
    public abstract boolean isEquivalent(Requirement other);

    /**
     * Method that merges two equivalent requirements
     * @param other the requirement with which to merge
     * @return a new requirement representing the two merged
     * @throws IllegalOperation if the two requirements are not equivalent
     */
    public abstract Requirement merge(Requirement other) throws IllegalOperation;

    /**
     * method that checks if the player passed as argument fulfils the requirement to activate this card
     * @param p the player to check
     * @return whether the player fulfils the requirement
     */
    public abstract boolean checkRequirement(Player p);

}
