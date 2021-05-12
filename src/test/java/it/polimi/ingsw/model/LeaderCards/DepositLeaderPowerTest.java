package it.polimi.ingsw.model.LeaderCards;

import it.polimi.ingsw.exceptions.ResourcesLimitsException;
import it.polimi.ingsw.model.Resource;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class DepositLeaderPowerTest {

    @Test
    public void testMaxResourcesImmutabilityFromConstructor(){
        HashMap<Resource, Integer> maxResources = new HashMap<>();
        maxResources.put(Resource.SERVANT, 3);
        maxResources.put(Resource.COIN, 7);

        DepositLeaderPower dlp = new DepositLeaderPower(maxResources);

        maxResources.put(Resource.COIN, 5);

        HashMap<Resource, Integer> expectedResources = new HashMap<>();
        expectedResources.put(Resource.SERVANT, 3);
        expectedResources.put(Resource.COIN, 7);
        assertEquals(expectedResources, dlp.getMaxResources());
    }

    @Test
    public void testGetMaxResources(){
        HashMap<Resource, Integer> maxResources = new HashMap<>();
        maxResources.put(Resource.SERVANT, 3);
        maxResources.put(Resource.COIN, 7);

        DepositLeaderPower dlp = new DepositLeaderPower(maxResources);

        HashMap<Resource, Integer> expectedResources = new HashMap<>();
        expectedResources.put(Resource.SERVANT, 3);
        expectedResources.put(Resource.COIN, 7);
        assertEquals(expectedResources, dlp.getMaxResources());
    }

    @Test
    public void testGetMaxResourcesImmutability(){
        HashMap<Resource, Integer> maxResources = new HashMap<>();
        maxResources.put(Resource.SERVANT, 3);
        maxResources.put(Resource.COIN, 7);

        DepositLeaderPower dlp = new DepositLeaderPower(maxResources);

        dlp.getMaxResources().put(Resource.COIN, 5);

        HashMap<Resource, Integer> expectedResources = new HashMap<>();
        expectedResources.put(Resource.SERVANT, 3);
        expectedResources.put(Resource.COIN, 7);
        assertEquals(expectedResources, dlp.getMaxResources());
    }

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
        } catch (ResourcesLimitsException e) {
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
        } catch (ResourcesLimitsException e) {
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
        } catch (ResourcesLimitsException e) {
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
        } catch (ResourcesLimitsException e) {
            fail();
        }

        HashMap<Resource, Integer> removedResources = new HashMap<>();
        removedResources.put(Resource.ROCK, 2);
        try {
            dlp.removeResources(removedResources);
        } catch (ResourcesLimitsException e) {
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
        } catch (ResourcesLimitsException e) {
            fail();
        }

        HashMap<Resource, Integer> removedResources = new HashMap<>();
        removedResources.put(Resource.COIN, 2);
        try {
            dlp.removeResources(removedResources);
            fail();
        } catch (ResourcesLimitsException e) {
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
        } catch (ResourcesLimitsException e) {
            fail();
        }

        HashMap<Resource, Integer> removedResources = new HashMap<>();
        removedResources.put(Resource.ROCK, 3);
        try {
            dlp.removeResources(removedResources);
            fail();
        } catch (ResourcesLimitsException e) {
            //nothing
        }
    }
}