package it.polimi.ingsw.model.LeaderCards;

import it.polimi.ingsw.exceptions.DepotLimitException;
import it.polimi.ingsw.model.Resource;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.*;

public class DepositLeaderPowerTest {

    @Test(expected = DepotLimitException.class)
    public void testAddResources() {
        HashMap<Resource, Integer> maxResources = new HashMap<>();
        maxResources.put(Resource.SERVANT, 3);
        maxResources.put(Resource.COIN, 7);

        DepositLeaderPower dlp = new DepositLeaderPower(maxResources);

        //test for the expected behaviour
        HashMap<Resource, Integer> addedResources = new HashMap<>();
        addedResources.put(Resource.COIN, 4);
        try {
            dlp.addResources(addedResources);
        } catch (DepotLimitException e) {
            fail();
        }
        HashMap<Resource, Integer> expectedResources = new HashMap<>();
        maxResources.put(Resource.SERVANT, 0);
        maxResources.put(Resource.COIN, 4);
        assertEquals(expectedResources, dlp.getCurrentResources());

        addedResources.put(Resource.COIN, 8);
        try {
            dlp.addResources(addedResources);
            fail();
        } catch (Exception e) {
            //nothing
        }

        HashMap<Resource, Integer> addedResources2 = new HashMap<>();
        addedResources2.put(Resource.ROCK, 2);
        try {
            dlp.addResources(addedResources2);
            fail();
        } catch (Exception e) {
            //nothing
        }
    }

    @Test
    public void testRemoveResources() {
    }
}