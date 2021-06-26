package it.polimi.ingsw.model.FaithTrack;

import it.polimi.ingsw.model.MatchState;

import java.io.Serializable;

public class Cell extends AbstractCell implements Serializable {
    private final int index;
    private final int victoryPoints;

    /**
     * Constructor
     */
    public Cell(int index, int victoryPoints) {
        this.index = index;
        this.victoryPoints = victoryPoints;
    }


    /**
     * activates the effect according to the type of cell.
     */
    @Override
    public void activateEffect(MatchState matchState) {

    }

    /**
     * Getter of index of the Cell
     *
     * @return index of the Cell
     */
    public int getIndex() {
        return index;
    }


    /**
     * Getter of victoryPoints of one Cell
     *
     * @return victoryPoints of one Cell
     */
    public int getVictoryPoints() {
        return victoryPoints;
    }



}
