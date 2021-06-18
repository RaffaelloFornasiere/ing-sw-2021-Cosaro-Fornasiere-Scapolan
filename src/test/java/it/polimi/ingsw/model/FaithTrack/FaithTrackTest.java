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

    public void testSize() {
        FaithTrack.initFaithTrack(Config.getDefaultConfig().getFaithTrack());
        Assert.assertEquals(25, FaithTrack.size());

    }

    public void testGetArrayOfCells() {
        FaithTrack.initFaithTrack(Config.getDefaultConfig().getFaithTrack());
        Assert.assertEquals(Config.getDefaultConfig().getFaithTrack(), FaithTrack.getArrayOfCells());


    }
}