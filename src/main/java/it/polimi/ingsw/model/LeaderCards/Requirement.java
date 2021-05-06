package it.polimi.ingsw.model.LeaderCards;

import it.polimi.ingsw.exceptions.IllegalOperation;
import it.polimi.ingsw.model.Player;

import java.io.Serializable;

/**
 * Class containing the requirements for activating a leader card
 */
public abstract class Requirement implements Serializable {

    public abstract boolean isEquivalent(Requirement other);
    public abstract Requirement merge(Requirement other) throws IllegalOperation;
    public abstract boolean checkRequirement(Player p);

}
