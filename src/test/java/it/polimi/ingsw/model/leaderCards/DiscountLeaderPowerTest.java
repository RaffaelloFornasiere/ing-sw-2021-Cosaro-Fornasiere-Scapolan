package it.polimi.ingsw.model.leaderCards;

import it.polimi.ingsw.model.Resource;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.*;

public class DiscountLeaderPowerTest {

    @Test
    public void testEmptyConstructor(){
        DiscountLeaderPower lp = new DiscountLeaderPower();

        ArrayList<Class<? extends LeaderPower>> expectedIncompatiblePowers = new ArrayList<>();
        expectedIncompatiblePowers.add(ExtraResourceLeaderPower.class);
        expectedIncompatiblePowers.add(ProductionLeaderPower.class);

        assertTrue(lp.getIncompatiblePowers().containsAll(expectedIncompatiblePowers));
        assertTrue(expectedIncompatiblePowers.containsAll(lp.getIncompatiblePowers()));
        assertTrue(lp.getDiscount().isEmpty());
    }

    @Test
    public void testGetDiscount() {
        HashMap<Resource, Integer> discount = new HashMap<>();
        discount.put(Resource.ROCK, 2);

        DiscountLeaderPower discountLeaderPower = new DiscountLeaderPower(discount);

        HashMap<Resource, Integer> effectiveDiscount = discountLeaderPower.getDiscount();
        HashMap<Resource, Integer> expectedDiscount = new HashMap<>();
        expectedDiscount.put(Resource.ROCK, 2);

        assertEquals(effectiveDiscount, expectedDiscount);
    }
}