package it.polimi.ingsw.model.leaderCards;

import it.polimi.ingsw.exceptions.IllegalOperation;
import it.polimi.ingsw.exceptions.NotPresentException;
import it.polimi.ingsw.model.CardColor;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.utilities.Pair;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.*;

public class LeaderCardTest {

    @Test
    public void testBasicGetters() {
        ArrayList<Requirement> requirements= new ArrayList<>();
        requirements.add(new LevelCardRequirement(CardColor.BLUE, 1, 2));
        requirements.add(new LevellessCardRequirement(CardColor.YELLOW, 1));

        ArrayList<LeaderPower> powers = new ArrayList<>();
        powers.add(new ExtraResourceLeaderPower(Resource.ROCK));
        powers.add(new DepositLeaderPower(new HashMap<>()));

        LeaderCard lc = new LeaderCard("Test", 3, requirements, powers);

        assertEquals("Test", lc.getCardID());
        assertEquals(3, lc.getVictoryPoints());
        assertEquals(requirements, lc.getActivationRequirement());
        assertEquals(powers, lc.getLeaderPowers());
    }

    @Test
    public void testGetBooleanPowers() {
        ArrayList<Requirement> requirements= new ArrayList<>();
        requirements.add(new LevelCardRequirement(CardColor.BLUE, 1, 2));
        requirements.add(new LevellessCardRequirement(CardColor.YELLOW, 1));

        ArrayList<LeaderPower> powers = new ArrayList<>();
        LeaderPower lp1 = new ExtraResourceLeaderPower(Resource.ROCK);
        powers.add(lp1);
        LeaderPower lp2 = new DepositLeaderPower(new HashMap<>());
        powers.add(lp2);

        LeaderCard lc = new LeaderCard("Test", 3, requirements, powers);

        try {
            lc.selectLeaderPower(lp1);
        } catch (NotPresentException | IllegalOperation notPresentException) {
            fail();
        }

        ArrayList<Pair<LeaderPower, Boolean>> expectedBooleanPowers = new ArrayList<>();
        expectedBooleanPowers.add(new Pair<>(lp1, true));
        expectedBooleanPowers.add(new Pair<>(lp2, false));
        assertEquals(expectedBooleanPowers, lc.getBooleanPowers());
    }

    @Test
    public void testGetSelectedLeaderPowers() {
        ArrayList<Requirement> requirements= new ArrayList<>();
        requirements.add(new LevelCardRequirement(CardColor.BLUE, 1, 2));
        requirements.add(new LevellessCardRequirement(CardColor.YELLOW, 1));

        ArrayList<LeaderPower> powers = new ArrayList<>();
        LeaderPower lp1 = new ExtraResourceLeaderPower(Resource.ROCK);
        powers.add(lp1);
        LeaderPower lp2 = new DepositLeaderPower(new HashMap<>());
        powers.add(lp2);

        LeaderCard lc = new LeaderCard("Test", 3, requirements, powers);

        try {
            lc.selectLeaderPower(lp1);
        } catch (NotPresentException | IllegalOperation notPresentException) {
            fail();
        }

        ArrayList<LeaderPower> selectedLeaderPowers = lc.getSelectedLeaderPowers();
        assertTrue(selectedLeaderPowers.contains(lp1));
        assertFalse(selectedLeaderPowers.contains(lp2));
    }

    @Test
    public void testSelectLeaderPowerNotPresentException() {
        ArrayList<Requirement> requirements= new ArrayList<>();
        requirements.add(new LevelCardRequirement(CardColor.BLUE, 1, 2));
        requirements.add(new LevellessCardRequirement(CardColor.YELLOW, 1));

        ArrayList<LeaderPower> powers = new ArrayList<>();
        LeaderPower lp1 = new ExtraResourceLeaderPower(Resource.ROCK);
        powers.add(lp1);
        LeaderPower lp2 = new DepositLeaderPower(new HashMap<>());

        LeaderCard lc = new LeaderCard("Test", 3, requirements, powers);

        try {
            lc.selectLeaderPower(lp2);
            fail();
        } catch (IllegalOperation notPresentException) {
            fail();
        } catch (NotPresentException ignore) {
        }
    }

    @Test
    public void testSelectLeaderPowerIllegalOperation() {
        ArrayList<Requirement> requirements= new ArrayList<>();
        requirements.add(new LevelCardRequirement(CardColor.BLUE, 1, 2));
        requirements.add(new LevellessCardRequirement(CardColor.YELLOW, 1));

        ArrayList<LeaderPower> powers = new ArrayList<>();
        LeaderPower lp1 = new ExtraResourceLeaderPower(Resource.ROCK);
        powers.add(lp1);

        LeaderCard lc = new LeaderCard("Test", 3, requirements, powers);

        try {
            lc.selectLeaderPower(lp1);
        } catch (IllegalOperation | NotPresentException notPresentException) {
            fail();
        }

        try {
            lc.selectLeaderPower(lp1);
            fail();
        } catch (IllegalOperation ignore) {
        } catch (NotPresentException e) {
            fail();
        }
    }

    @Test
    public void testDeselectLeaderPowerSuccess() {
        ArrayList<Requirement> requirements= new ArrayList<>();
        requirements.add(new LevelCardRequirement(CardColor.BLUE, 1, 2));
        requirements.add(new LevellessCardRequirement(CardColor.YELLOW, 1));

        ArrayList<LeaderPower> powers = new ArrayList<>();
        LeaderPower lp1 = new ExtraResourceLeaderPower(Resource.ROCK);
        powers.add(lp1);
        LeaderPower lp2 = new DepositLeaderPower(new HashMap<>());
        powers.add(lp2);

        LeaderCard lc = new LeaderCard("Test", 3, requirements, powers);

        try {
            lc.selectLeaderPower(lp1);
        } catch (NotPresentException | IllegalOperation notPresentException) {
            fail();
        }

        try {
            lc.deselectLeaderPower(lp1);
        } catch (NotPresentException | IllegalOperation notPresentException) {
            fail();
        }

        ArrayList<LeaderPower> selectedLeaderPowers = lc.getSelectedLeaderPowers();
        assertFalse(selectedLeaderPowers.contains(lp1));
        assertFalse(selectedLeaderPowers.contains(lp2));
    }

    @Test
    public void testDeselectLeaderPowerNotPresentException() {
        ArrayList<Requirement> requirements= new ArrayList<>();
        requirements.add(new LevelCardRequirement(CardColor.BLUE, 1, 2));
        requirements.add(new LevellessCardRequirement(CardColor.YELLOW, 1));

        ArrayList<LeaderPower> powers = new ArrayList<>();
        LeaderPower lp1 = new ExtraResourceLeaderPower(Resource.ROCK);
        powers.add(lp1);
        LeaderPower lp2 = new DepositLeaderPower(new HashMap<>());

        LeaderCard lc = new LeaderCard("Test", 3, requirements, powers);

        try {
            lc.deselectLeaderPower(lp2);
            fail();
        } catch (IllegalOperation notPresentException) {
            fail();
        } catch (NotPresentException ignore) {
        }
    }

    @Test
    public void testDeselectLeaderPowerIllegalOperation() {
        ArrayList<Requirement> requirements= new ArrayList<>();
        requirements.add(new LevelCardRequirement(CardColor.BLUE, 1, 2));
        requirements.add(new LevellessCardRequirement(CardColor.YELLOW, 1));

        ArrayList<LeaderPower> powers = new ArrayList<>();
        LeaderPower lp1 = new ExtraResourceLeaderPower(Resource.ROCK);
        powers.add(lp1);

        LeaderCard lc = new LeaderCard("Test", 3, requirements, powers);

        try {
            lc.deselectLeaderPower(lp1);
            fail();
        } catch (IllegalOperation ignore) {
        } catch (NotPresentException e) {
            fail();
        }
    }
}