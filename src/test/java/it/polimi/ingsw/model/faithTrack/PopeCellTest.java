package it.polimi.ingsw.model.faithTrack;

import junit.framework.TestCase;
import org.junit.Assert;

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
        assertEquals("it.polimi.ingsw.model.faithTrack.PopeCell",  effectCell.getClass().getName());
    }

    public void testGetCard() {
        PopeCell effectCell= new PopeCell(3, 4, new PopeFavorCard(4),5);
        Assert.assertEquals(4, effectCell.getCard().getVictoryPoints());
    }

    public void testTestGetVaticanReportSection() {
        PopeCell effectCell= new PopeCell(3, 4, new PopeFavorCard(4),5);
        Assert.assertEquals(5, effectCell.getVaticanReportSection());

    }

    public void testTestGetCard() {
        PopeCell effectCell= new PopeCell(new Cell(2,3), new PopeFavorCard(4),5);
        Assert.assertEquals(5, effectCell.getVaticanReportSection());
        Assert.assertEquals(3, effectCell.getVictoryPoints());

    }

    public void testActivateEffect() {
    }
}