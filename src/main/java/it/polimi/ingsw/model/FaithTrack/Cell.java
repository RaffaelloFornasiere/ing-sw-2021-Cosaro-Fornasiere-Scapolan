package it.polimi.ingsw.model.FaithTrack;

public class Cell{
    private final int index;
    private final int victoryPoints;


    /**
     * @Constructor
     * @param index
     * @param vpoints
     */
    public Cell(int index, int vpoints){
        this.index= index;
        this.victoryPoints= vpoints;
    }

    /**
     * Getter of index of the Cell
     * @return index of the Cell
     */
    public int getIndex() {
        return index;
    }



    /**
     *Getter of victoryPoints of one Cell
     * @return victoryPoints of one Cell
     */
    public int getVictoryPoints() {
        return victoryPoints;
    }




    /**
     * must be overrided, activate the effect according to the type of cell.
     */
    public void activateEffect(){}

}
