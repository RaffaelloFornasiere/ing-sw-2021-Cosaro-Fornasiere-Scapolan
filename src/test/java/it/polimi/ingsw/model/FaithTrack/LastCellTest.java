package it.polimi.ingsw.model.FaithTrack;

import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;

public class LastCellTest extends TestCase {
    @Test
    public void testGetIndex(){
        LastCell cell= new LastCell( new Cell(2,4));
        Assert.assertEquals(4, cell.getVictoryPoints());
    }
}