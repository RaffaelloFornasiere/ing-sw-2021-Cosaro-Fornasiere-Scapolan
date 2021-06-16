package it.polimi.ingsw.model.DevCards;

import it.polimi.ingsw.model.CardColor;
import it.polimi.ingsw.model.ProductionPower;
import it.polimi.ingsw.model.Resource;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.*;

public class DevCardTest {

    HashMap<Resource, Integer> cost = new HashMap<>(){{
        put(Resource.COIN, 3);
        put(Resource.ROCK, 2);
    }};
    int level = 2;
    CardColor color = CardColor.BLUE;
    int victoryPoints = 5;
    ProductionPower productionPower = new ProductionPower(new HashMap<>(){{put(Resource.SERVANT, 1);}}, new HashMap<>(){{put(Resource.COIN, 3);}},
            0, 0, 3);
//    HashMap<Resource, Integer> consumedResources, HashMap<Resource, Integer> producedResources,
//    int requiredResourceOfChoice, int producedResourceOfChoice, int faithPointsProduced
    //DevCard devCard = new DevCard(cost, level, color, victoryPoints, productionPower);
    @Test
    public void getCardID() {
        DevCard devCard = new DevCard(cost, level, color, victoryPoints, productionPower);
        String s = devCard.getCardID();
    }

    @Test
    public void getCost() {
        DevCard devCard = new DevCard(cost, level, color, victoryPoints, productionPower);
        assertEquals(devCard.getCost(), cost);
    }

    @Test
    public void getLevel() {
        DevCard devCard = new DevCard(cost, level, color, victoryPoints, productionPower);
        assertEquals(devCard.getLevel(), level);
    }

    @Test
    public void getColor() {
        DevCard devCard = new DevCard(cost, level, color, victoryPoints, productionPower);
        assertEquals(devCard.getColor(), color);
    }

    @Test
    public void getVictoryPoints() {
        DevCard devCard = new DevCard(cost, level, color, victoryPoints, productionPower);
        assertEquals(devCard.getVictoryPoints(), victoryPoints);
    }

    @Test
    public void getProductionPower() {
        DevCard devCard = new DevCard(cost, level, color, victoryPoints, productionPower);
        assertEquals(devCard.getProductionPower(), productionPower);
    }

    @Test
    public void checkCost() {
        DevCard devCard = new DevCard(cost, level, color, victoryPoints, productionPower);
        assertTrue(devCard.checkCost(new HashMap<>(){{
            put(Resource.COIN, 10);
            put(Resource.ROCK, 20);
        }}));
        assertFalse(devCard.checkCost(new HashMap<>(){{
            put(Resource.COIN, 1);
            put(Resource.ROCK, 3);
        }}));
    }
}