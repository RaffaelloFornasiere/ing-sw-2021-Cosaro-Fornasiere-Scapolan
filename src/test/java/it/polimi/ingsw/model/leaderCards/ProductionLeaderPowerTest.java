package it.polimi.ingsw.model.leaderCards;

import it.polimi.ingsw.model.ProductionPower;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.*;

public class ProductionLeaderPowerTest {

    @Test
    public void testGetEffectPower() {
        ProductionPower productionPower = new ProductionPower(new HashMap<>(), new HashMap<>(), 2, 1, 0);
        ProductionLeaderPower productionLeaderPower = new ProductionLeaderPower(productionPower);
        assertEquals(productionPower, productionLeaderPower.getEffectPower());
    }
}