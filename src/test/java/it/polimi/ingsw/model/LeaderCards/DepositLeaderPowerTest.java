package it.polimi.ingsw.model.LeaderCards;

import it.polimi.ingsw.exceptions.DepotLimitException;
import it.polimi.ingsw.exceptions.EmptyStrongboxException;
import it.polimi.ingsw.model.Resource;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.*;

public class DepositLeaderPowerTest {

    @Test
    public void testAddResourcesSuccessful() {
        HashMap<Resource, Integer> maxResources = new HashMap<>();
        maxResources.put(Resource.SERVANT, 3);
        maxResources.put(Resource.COIN, 7);

        DepositLeaderPower dlp = new DepositLeaderPower(maxResources);

        HashMap<Resource, Integer> addedResources = new HashMap<>();
        addedResources.put(Resource.COIN, 4);
        try {
            dlp.addResources(addedResources);
        } catch (DepotLimitException e) {
            fail();
        }
        HashMap<Resource, Integer> expectedResources = new HashMap<>();
        expectedResources.put(Resource.SERVANT, 0);
        expectedResources.put(Resource.COIN, 4);
        assertEquals(expectedResources, dlp.getCurrentResources());
    }

    @Test
    public void testAddResourcesResourceExcess() {
        HashMap<Resource, Integer> maxResources = new HashMap<>();
        maxResources.put(Resource.SERVANT, 3);
        maxResources.put(Resource.COIN, 7);

        DepositLeaderPower dlp = new DepositLeaderPower(maxResources);

        HashMap<Resource, Integer> addedResources = new HashMap<>();
        addedResources.put(Resource.COIN, 8);
        try {
            dlp.addResources(addedResources);
            fail();
        } catch (DepotLimitException e) {
            //nothing
        }
    }

    @Test
    public void testAddResourcesResourceNotPresent() {
        HashMap<Resource, Integer> maxResources = new HashMap<>();
        maxResources.put(Resource.SERVANT, 3);
        maxResources.put(Resource.COIN, 7);

        DepositLeaderPower dlp = new DepositLeaderPower(maxResources);

        HashMap<Resource, Integer> addedResources = new HashMap<>();
        addedResources.put(Resource.ROCK, 2);
        try {
            dlp.addResources(addedResources);
            fail();
        } catch (DepotLimitException e) {
            //nothing
        }
    }

    @Test
    public void testRemoveResourcesSuccessful() {
        HashMap<Resource, Integer> maxResources = new HashMap<>();
        maxResources.put(Resource.SHIELD, 3);
        maxResources.put(Resource.ROCK, 5);

        DepositLeaderPower dlp = new DepositLeaderPower(maxResources);

        HashMap<Resource, Integer> addedResources = new HashMap<>();
        addedResources.put(Resource.ROCK, 4);
        try {
            dlp.addResources(addedResources);
        } catch (DepotLimitException e) {
            fail();
        }

        HashMap<Resource, Integer> removedResources = new HashMap<>();
        removedResources.put(Resource.ROCK, 2);
        try {
            dlp.removeResources(removedResources);
        } catch (EmptyStrongboxException e) {
            fail();
        }
    }

    @Test
    public void testRemoveResourcesNotPresent() {
        HashMap<Resource, Integer> maxResources = new HashMap<>();
        maxResources.put(Resource.SHIELD, 3);
        maxResources.put(Resource.ROCK, 5);

        DepositLeaderPower dlp = new DepositLeaderPower(maxResources);

        HashMap<Resource, Integer> addedResources = new HashMap<>();
        addedResources.put(Resource.ROCK, 4);
        try {
            dlp.addResources(addedResources);
        } catch (DepotLimitException e) {
            fail();
        }

        HashMap<Resource, Integer> removedResources = new HashMap<>();
        removedResources.put(Resource.COIN, 2);
        try {
            dlp.removeResources(removedResources);
            fail();
        } catch (EmptyStrongboxException e) {
            //nothing
        }
    }

    @Test
    public void testRemoveResourcesNotEnough() {
        HashMap<Resource, Integer> maxResources = new HashMap<>();
        maxResources.put(Resource.SHIELD, 3);
        maxResources.put(Resource.ROCK, 5);

        DepositLeaderPower dlp = new DepositLeaderPower(maxResources);

        HashMap<Resource, Integer> addedResources = new HashMap<>();
        addedResources.put(Resource.ROCK, 2);
        try {
            dlp.addResources(addedResources);
        } catch (DepotLimitException e) {
            fail();
        }

        HashMap<Resource, Integer> removedResources = new HashMap<>();
        removedResources.put(Resource.ROCK, 3);
        try {
            dlp.removeResources(removedResources);
            fail();
        } catch (EmptyStrongboxException e) {
            //nothing
        }
    }
}