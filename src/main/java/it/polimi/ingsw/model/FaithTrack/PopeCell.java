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
    public PopeCell(int index, int vPoints, PopeFavorCard card,  ArrayList<Cell> vaticanReportSection, EffectOfCell effect) {
        super(index, vPoints, effect);
        this.card= card;
        this.vaticanReportSection=vaticanReportSection;
    }


    public ArrayList<Cell> getVaticanReportSection() {
        return vaticanReportSection;
    }


    public PopeFavorCard getCard() {
        return card;
    }

    @Override
    public void activateEffect() {
        super.activateEffect();
    }
}
