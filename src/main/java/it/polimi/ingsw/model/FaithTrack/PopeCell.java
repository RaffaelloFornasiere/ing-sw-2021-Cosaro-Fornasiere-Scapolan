package it.polimi.ingsw.model.FaithTrack;

import java.util.ArrayList;

public class PopeCell extends CellWithEffect {
    private ArrayList<Cell> vaticanReportSection;
    private PopeFavorCard card;

    /**
     * @param index
     * @param vPoints
     * @Constructor
     */
    public PopeCell(int index, int vPoints, PopeFavorCard card) {
        super(index, vPoints);
        this.card= card;
    }

    @Override
    public void activateEffect() {
        super.activateEffect();
    }
}
