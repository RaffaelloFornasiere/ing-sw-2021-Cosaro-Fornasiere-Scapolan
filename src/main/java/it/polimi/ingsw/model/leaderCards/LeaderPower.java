package it.polimi.ingsw.model.leaderCards;

import it.polimi.ingsw.utilities.Observable;

import java.util.ArrayList;

;

/**
 * Class containing the data needed to use the ability of a leader card
 */
public abstract class LeaderPower extends Observable {

    protected transient final ArrayList<java.lang.Class<? extends LeaderPower>> incompatiblePowers = new ArrayList<>();

    /**
     * Methods that returns all the classes of powers that can not be selected while this is selected
     */
    public ArrayList<java.lang.Class<? extends LeaderPower>> getIncompatiblePowers() {
        return new ArrayList<>(incompatiblePowers);
    }
}