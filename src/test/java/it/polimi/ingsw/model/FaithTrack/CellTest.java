package it.polimi.ingsw.model.FaithTrack;

import junit.framework.TestCase;

public class CellTest extends TestCase {


    public void testGetIndex() {
        Cell cell= new Cell(3, 2);
        assertEquals(3,cell.getIndex());
    }

    public void testGetVictoryPoints() {
        Cell cell= new Cell(3, 2);
        assertEquals(2,cell.getVictoryPoints());
    }
}