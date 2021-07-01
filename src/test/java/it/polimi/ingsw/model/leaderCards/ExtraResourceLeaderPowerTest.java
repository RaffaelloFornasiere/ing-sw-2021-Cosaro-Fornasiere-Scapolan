package it.polimi.ingsw.model.leaderCards;

import it.polimi.ingsw.model.Resource;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class ExtraResourceLeaderPowerTest {

    @Test
    public void testEmptyConstructor(){
        ExtraResourceLeaderPower lp = new ExtraResourceLeaderPower();

        ArrayList<java.lang.Class<? extends LeaderPower>> expectedIncompatiblePowers = new ArrayList<>();
        expectedIncompatiblePowers.add(DiscountLeaderPower.class);
        expectedIncompatiblePowers.add(ProductionLeaderPower.class);

        assertTrue(lp.getIncompatiblePowers().containsAll(expectedIncompatiblePowers));
        assertTrue(expectedIncompatiblePowers.containsAll(lp.getIncompatiblePowers()));
        assertNull(lp.getResourceType());
    }

    @Test
    public void testGetResourceType() {
        ExtraResourceLeaderPower lp = new ExtraResourceLeaderPower(Resource.COIN);

        assertEquals(Resource.COIN, lp.getResourceType());
    }
}