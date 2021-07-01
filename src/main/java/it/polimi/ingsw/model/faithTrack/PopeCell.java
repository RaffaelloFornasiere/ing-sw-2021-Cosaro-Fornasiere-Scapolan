package it.polimi.ingsw.model.faithTrack;

import it.polimi.ingsw.controller.EffectOfPopeCell;
import it.polimi.ingsw.model.MatchState;

public class PopeCell extends CellWithEffect {
    private final int vaticanReportSection;
    private final PopeFavorCard card;

    /**
     * constructor
     *
     * @param card                 favorPopeCard associated to this pope cell
     * @param vaticanReportSection the array of other cells which constitutes the vatican report section
     */
    public PopeCell(AbstractCell cell, PopeFavorCard card, int vaticanReportSection) {
        this.cell = cell;
        this.card = card;
        this.vaticanReportSection = vaticanReportSection;
        this.effect = new EffectOfPopeCell();
    }

    /**
     * Constructor
     *
     * @param index                The index of cell
     * @param victoryPoints        The victory points of cell
     * @param card                 The popeFavorCard associated with this popeCell
     * @param vaticanReportSection The number of cells contained in the Vatican Report Section , starting from the current PopeCell back on.
     */
    public PopeCell(int index, int victoryPoints, PopeFavorCard card, int vaticanReportSection) {
        this.cell = new Cell(index, victoryPoints);
        this.card = card;
        this.vaticanReportSection = vaticanReportSection;
        this.effect = new EffectOfPopeCell();
    }


    /**
     * Getter of the length of the vaticanReportSection. For instance, if it is 4,
     * it means that from the popeCell (included), back to four cell before, it is
     * all VaticanReportSection.
     *
     * @return The array of cells belonging to the vatican report section.
     */
    public int getVaticanReportSection() {
        return vaticanReportSection;
    }

    /**
     * getter of the favor pope card
     *
     * @return the favor pope card
     */
    public PopeFavorCard getCard() {
        return new PopeFavorCard(card.getVictoryPoints()
        );
    }

    /**
     * Method to activate the effect of the popeCell
     *
     * @param matchState The state of the match from which the method activateEffect() gets some information about the state of other players.
     */
    @Override
    public void activateEffect(MatchState matchState) {
        super.activateEffect(matchState);
    }
}
