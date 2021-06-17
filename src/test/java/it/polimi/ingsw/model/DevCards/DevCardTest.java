package it.polimi.ingsw.model.DevCards;

import com.google.gson.Gson;
import it.polimi.ingsw.model.CardColor;
import it.polimi.ingsw.model.ProductionPower;
import it.polimi.ingsw.model.Resource;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

import static org.junit.Assert.*;

public class DevCardTest {

    HashMap<Resource, Integer> cost = new HashMap<>() {{
        put(Resource.COIN, 3);
        put(Resource.ROCK, 2);
    }};
    int level = 2;
    CardColor color = CardColor.BLUE;
    int victoryPoints = 5;
    ProductionPower productionPower = new ProductionPower(new HashMap<>() {{
        put(Resource.SERVANT, 1);
    }}, new HashMap<>() {{
        put(Resource.COIN, 3);
    }},
            0, 0, 3);
//    HashMap<Resource, Integer> consumedResources, HashMap<Resource, Integer> producedResources,
//    int requiredResourceOfChoice, int producedResourceOfChoice, int faithPointsProduced
    //DevCard devCard = new DevCard(cost, level, color, victoryPoints, productionPower);


    @Test
    public void testGetCardID() {
        DevCard devCard = new DevCard(cost, level, color, victoryPoints, productionPower);
        String s = devCard.getCardID();
    }

    @Test
    public void testGetCost() {
        DevCard devCard = new DevCard(cost, level, color, victoryPoints, productionPower);
        assertEquals(devCard.getCost(), cost);
    }

    @Test
    public void testGetLevel() {
        DevCard devCard = new DevCard(cost, level, color, victoryPoints, productionPower);
        assertEquals(devCard.getLevel(), level);
    }

    @Test
    public void testGetColor() {
        DevCard devCard = new DevCard(cost, level, color, victoryPoints, productionPower);
        assertEquals(devCard.getColor(), color);
    }

    @Test
    public void testGetVictoryPoints() {
        DevCard devCard = new DevCard(cost, level, color, victoryPoints, productionPower);
        assertEquals(devCard.getVictoryPoints(), victoryPoints);
    }

    @Test
    public void testGetProductionPower() {
        DevCard devCard = new DevCard(cost, level, color, victoryPoints, productionPower);
        assertEquals(devCard.getProductionPower(), productionPower);
    }

    @Test
    public void testCheckCost() {
        DevCard devCard = new DevCard(cost, level, color, victoryPoints, productionPower);
        assertTrue(devCard.checkCost(new HashMap<>() {{
            put(Resource.COIN, 10);
            put(Resource.ROCK, 20);
        }}));
        assertFalse(devCard.checkCost(new HashMap<>() {{
            put(Resource.COIN, 1);
            put(Resource.ROCK, 3);
        }}));
    }

    @Test
    public void testEquals() {
        Gson gson = new Gson();
        DevCard devCard1 = null;
        DevCard devCard2 = null;

        try {
            devCard1 = gson.fromJson(Files.readString(Paths.get("src\\main\\resources\\DevCard3.json")), DevCard.class);
            devCard2 = gson.fromJson(Files.readString(Paths.get("src\\main\\resources\\DevCard3.json")), DevCard.class);

        } catch (IOException e) {
            e.printStackTrace();
        }
        Assert.assertEquals(devCard1, devCard2);
    }
}