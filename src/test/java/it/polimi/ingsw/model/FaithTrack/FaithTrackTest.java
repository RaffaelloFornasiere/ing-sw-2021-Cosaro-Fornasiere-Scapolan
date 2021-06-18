package it.polimi.ingsw.model.FaithTrack;

import it.polimi.ingsw.utilities.Config;
import junit.framework.TestCase;
import org.junit.Assert;

import java.util.ArrayList;

public class FaithTrackTest extends TestCase {
    /**
     * This method tests the correctness of initializing the singleton FaithTrack
     */
    public void testInitFaithTrackSuccessful() {
        try {
            FaithTrack.initFaithTrack(Config.getDefaultConfig().getFaithTrack());
            assertEquals(25, FaithTrack.size());
            assertEquals(2, FaithTrack.getArrayOfCells().get(8).getVictoryPoints());
            assertEquals(4, FaithTrack.getArrayOfCells().get(10).getVictoryPoints());
            assertEquals(9, FaithTrack.getArrayOfCells().get(16).getVictoryPoints());
            assertEquals(12, FaithTrack.getArrayOfCells().get(20).getVictoryPoints());
            FaithTrack.resetForTest();
        } catch (IllegalArgumentException e) {
            fail();
        }


    }

    /**
     * This method tests the correctness of initializing the singleton FaithTrack, in case a wrong parameter is given
     * ( the length of the array of points for the cells must be equal to the length of the faith track)
     */

    public void testInitFaithTrackUnsuccessful() {

        try {
            ArrayList<Integer> a = new ArrayList<>(4);
            a.add(1);
            a.add(2);
            a.add(3);
            a.add(4);
            assertEquals(4, a.size());
            FaithTrack.initFaithTrack(3, new ArrayList<>(), a);
            fail();
        } catch (IllegalArgumentException e) {
            FaithTrack.resetForTest();
        }
    }

    public void testInitFaithTrackMoreUnsuccessful() {
        try {
            ArrayList<Integer> a = new ArrayList<>(4);
            a.add(1);
            a.add(2);
            a.add(3);
            a.add(4);
            assertEquals(4, a.size());
            // the array of cells of the faithTrack is 4 cells long.
            // Here the PopeCell should be put at cell 5, which doesn't exist
            PopeCell popeCell = new PopeCell(5, 2, new PopeFavorCard(3), 3);
            ArrayList<CellWithEffect> array = new ArrayList<>();
            array.add(popeCell);
            FaithTrack.initFaithTrack(4, array, a);
            fail();
        } catch (IllegalArgumentException e) {
            FaithTrack.resetForTest();

        }
    }

    public void testSize() {
        FaithTrack.initFaithTrack(Config.getDefaultConfig().getFaithTrack());
        Assert.assertEquals(25, FaithTrack.size());
        FaithTrack.resetForTest();

    }

    public void testGetArrayOfCells() {
        FaithTrack.initFaithTrack(Config.getDefaultConfig().getFaithTrack());
        Assert.assertEquals(Config.getDefaultConfig().getFaithTrack(), FaithTrack.getArrayOfCells());
        FaithTrack.resetForTest();


    }
}