package it.polimi.ingsw.model.LeaderCards;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Class containing the data needed to use the ability of a leader card
 */
public abstract class LeaderPower implements Serializable {

    protected transient ArrayList<java.lang.Class<? extends LeaderPower>> incompatiblePowers;

    /**
     * Methods that returns all the classes of powers that can not be selected while this is selected
     */
    public ArrayList<java.lang.Class<? extends LeaderPower>> getIncompatiblePowers() {
        return (ArrayList<java.lang.Class<? extends LeaderPower>>) incompatiblePowers.clone();
    }

    /**
     * constructor for the class that initializes the internal variables of the class
     */
    public LeaderPower() {
        incompatiblePowers = new ArrayList<>();
    }
}