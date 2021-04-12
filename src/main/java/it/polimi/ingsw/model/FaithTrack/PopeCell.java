package it.polimi.ingsw.model.FaithTrack;

import it.polimi.ingsw.controller.EffectOfCell;

import java.util.ArrayList;

public class PopeCell extends CellWithEffect {
    private final ArrayList<Cell> vaticanReportSection;
    private final PopeFavorCard card;

    /**
     * constructor
     * @param index index of cell
     * @param vPoints victory points of cell
     * @param card favorpopecard associted to this pope cell
     * @param vaticanReportSection the array of other cells which constitutes thevatican report section
     * @param effect effect of the cell.
     */
    public PopeCell(int index, int vPoints, PopeFavorCard card,  ArrayList<Cell> vaticanReportSection, EffectOfCell effect) {
        super(index, vPoints, effect);
        this.card= card;
        this.vaticanReportSection=vaticanReportSection;
    }



    /**
     * Getter of the vatican report section cells
     * @return array of cells belonging to the vatican report section
     */
    public ArrayList<Cell> getVaticanReportSection() {
        return vaticanReportSection;
    }



    /**
     * getter of the favor pope card
     * @return the favor pope card
     */
    public PopeFavorCard getCard() {
        return card;
    }

    @Override
    public void activateEffect() {
        super.activateEffect();
    }
}
