package it.polimi.ingsw.model.FaithTrack;

import junit.framework.TestCase;

public class PopeCellTest extends TestCase {


    public void testGetVaticanReportSection() {

        AbstractCell cell= new Cell(3,4);
        assertEquals(3, cell.getIndex());
        assertEquals(4, cell.getVictoryPoints());

        PopeCell effectCell= new PopeCell(3, 4, new PopeFavorCard(4),5);
        assertEquals(3, effectCell.getIndex());
        assertEquals(4, effectCell.getVictoryPoints());
        assertEquals(4, effectCell.getCard().getVictoryPoints());
        assertEquals(5, effectCell.getVaticanReportSection());

    }

    public void testGetCard() {
    }
}