package it.polimi.ingsw.model.FaithTrack;

import it.polimi.ingsw.controller.EffectOfPopeCell;
import it.polimi.ingsw.model.MatchState;

public class PopeCell extends CellWithEffect {
    private final int vaticanReportSection;
    private final PopeFavorCard card;

    /**
     * constructor
     * @param card favorpPopeCard associated to this pope cell
     * @param vaticanReportSection the array of other cells which constitutes the vatican report section
     */
    public PopeCell(AbstractCell cell, PopeFavorCard card, int vaticanReportSection) {
        this.cell= cell;
        this.card= card;
        this.vaticanReportSection=vaticanReportSection;
        this.effect= new EffectOfPopeCell();
    }

    public PopeCell( int index, int victoryPoints, PopeFavorCard card,  int vaticanReportSection) {
        this.cell= new Cell(index, victoryPoints);
        this.card= card;
        this.vaticanReportSection=vaticanReportSection;
        this.effect= new EffectOfPopeCell();
    }


    /**
     * Getter of the length of the vaticanReportSection. For instance, if it is 4,
     * it means that from the popeCell (included), back to four cell before, it is
     * all VaticanReportSection.
     * @return array of cells belonging to the vatican report section
     */
    public int getVaticanReportSection() {
        return  vaticanReportSection;
    }

    /**
     * getter of the favor pope card
     * @return the favor pope card
     */
    public PopeFavorCard getCard() {
        return new PopeFavorCard( card.getVictoryPoints()
        );
    }

    @Override
    public void activateEffect(MatchState matchState) {
        super.activateEffect(matchState);
    }
}
