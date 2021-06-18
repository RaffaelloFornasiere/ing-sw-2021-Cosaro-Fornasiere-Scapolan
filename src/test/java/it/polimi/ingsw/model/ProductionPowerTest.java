package it.polimi.ingsw.model;

import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.*;

public class ProductionPowerTest {

    @Test
    public void testEqualsSameObject() {
        HashMap<Resource, Integer> consumed = new HashMap<>();
        consumed.put(Resource.SHIELD, 2);

        HashMap<Resource, Integer> produced = new HashMap<>();
        produced.put(Resource.COIN, 2);
        produced.put(Resource.ROCK, 1);

        ProductionPower productionPower1 = new ProductionPower(consumed, produced, 2 ,1, 4);

        assertEquals(productionPower1, productionPower1);
    }

    @Test
    public void testEqualsDifferentType() {
        HashMap<Resource, Integer> consumed = new HashMap<>();
        consumed.put(Resource.SHIELD, 2);

        HashMap<Resource, Integer> produced = new HashMap<>();
        produced.put(Resource.COIN, 2);
        produced.put(Resource.ROCK, 1);

        ProductionPower productionPower1 = new ProductionPower(consumed, produced, 2 ,1, 4);

        Object object = new Object();

        assertNotEquals(productionPower1, object);
    }

    @Test
    public void testGetters() {
        HashMap<Resource, Integer> consumed = new HashMap<>();
        consumed.put(Resource.SHIELD, 2);

        HashMap<Resource, Integer> produced = new HashMap<>();
        produced.put(Resource.COIN, 2);
        produced.put(Resource.ROCK, 1);

        ProductionPower productionPower = new ProductionPower(consumed, produced, 2 ,1, 4);

        assertEquals(consumed, productionPower.getConsumedResources());
        assertEquals(produced, productionPower.getProducedResources());
        assertEquals(2, productionPower.getRequiredResourceOfChoice());
        assertEquals(1, productionPower.getProducedResourceOfChoice());
        assertEquals(4, productionPower.getFaithPointsProduced());
    }
}