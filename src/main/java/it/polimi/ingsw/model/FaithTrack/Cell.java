package it.polimi.ingsw.model.FaithTrack;

public class Cell{
    private int index;
    private int victoryPoints;

    /**
     * Getter of index of the Cell
     * @return index of the Cell
     */
    public int getIndex() {
        return index;
    }

    /**
     * Setter of one cell's index
     * @param index of the cell
     */
    public void setIndex(int index) {
        this.index = index;
    }

    /**
     *Getter of victoryPoints of one Cell
     * @return victoryPoints of one Cell
     */
    public int getVictoryPoints() {
        return victoryPoints;
    }

    /**
     * Setter of victoryPoints on one cell
     * @param victoryPoints for one cell
     */
    public void setVictoryPoints(int victoryPoints) {
        this.victoryPoints = victoryPoints;
    }


    /**
     * must be overrided, activate the effect according to the type of cell.
     */
    public void activateEffect(){}

}
