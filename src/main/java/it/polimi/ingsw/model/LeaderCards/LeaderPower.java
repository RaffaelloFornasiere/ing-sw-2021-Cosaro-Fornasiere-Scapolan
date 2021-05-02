package it.polimi.ingsw.model.LeaderCards;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Class containing the data needed to use the ability of a leader card
 */
public abstract class LeaderPower implements Serializable {

    protected static transient ArrayList<java.lang.Class<? extends LeaderPower>> incompatiblePowers;

    public static java.lang.Class<? extends LeaderPower> getIncompatiblePowers() {
        return (java.lang.Class<? extends LeaderPower>) incompatiblePowers.clone();
    }
}