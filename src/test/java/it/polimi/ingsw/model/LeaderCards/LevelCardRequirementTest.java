package it.polimi.ingsw.model.LeaderCards;

import it.polimi.ingsw.exceptions.IllegalOperation;
import it.polimi.ingsw.exceptions.IndexSlotException;
import it.polimi.ingsw.exceptions.LevelCardException;
import it.polimi.ingsw.model.CardColor;
import it.polimi.ingsw.model.DashBoard;
import it.polimi.ingsw.model.DevCards.DevCard;
import it.polimi.ingsw.model.Player;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.*;

public class LevelCardRequirementTest {
    @Test
    public void testIsEquivalentTrue() {
        Requirement r1 = new LevelCardRequirement(CardColor.BLUE, 2, 4);
        Requirement r2 = new LevelCardRequirement(CardColor.BLUE, 2, 3);
        assertTrue(r1.isEquivalent(r2));
    }

    @Test
    public void testIsEquivalentFalseWrongType() {
        Requirement r1 = new LevelCardRequirement(CardColor.BLUE, 2, 1);
        Requirement r2 = new ResourcesRequirement(new HashMap<>());
        assertFalse(r1.isEquivalent(r2));
    }

    @Test
    public void testIsEquivalentFalseWrongLevel() {
        Requirement r1 = new LevelCardRequirement(CardColor.BLUE, 2, 1);
        Requirement r2 = new LevelCardRequirement(CardColor.BLUE, 3, 3);
        assertFalse(r1.isEquivalent(r2));
    }

    @Test
    public void testIsEquivalentFalseWrongColor() {
        Requirement r1 = new LevelCardRequirement(CardColor.BLUE, 2, 3);
        Requirement r2 = new LevelCardRequirement(CardColor.GREEN, 2, 1);
        assertFalse(r1.isEquivalent(r2));
    }

    @Test
    public void testMergeSuccessful() {
        Requirement r1 = new LevelCardRequirement(CardColor.BLUE, 2, 4);
        Requirement r2 = new LevelCardRequirement(CardColor.BLUE, 2, 3);

        try {
            Requirement rm_noType = r1.merge(r2);
            if(rm_noType.getClass()!=r1.getClass())
                fail();
            LevelCardRequirement rm = (LevelCardRequirement) rm_noType;
            assertEquals(CardColor.BLUE, rm.getType());
            assertEquals(2, rm.getLevel());
            assertEquals(7, rm.getQuantity());
        } catch (IllegalOperation illegalOperation) {
            fail();
        }
    }

    @Test
    public void testMergeException() {
        Requirement rr1 = new LevelCardRequirement(CardColor.BLUE, 2, 1);
        Requirement rr2 = new ResourcesRequirement(new HashMap<>());

        try {
            rr1.merge(rr2);
            fail();
        } catch (IllegalOperation ignore) {
        }
    }

    @Test
    public void testCheckRequirementTrue(){
        ArrayList<Integer> eachDepotCapacity = new ArrayList<>();
        eachDepotCapacity.add(1);
        eachDepotCapacity.add(2);
        eachDepotCapacity.add(3);
        DashBoard dashBoard = new DashBoard(3, eachDepotCapacity, null);
        Player player = new Player("test", dashBoard);

        try {
            player.getDashBoard().addCard(1, new DevCard(new HashMap<>(), 1, CardColor.BLUE, 3, null));
        } catch (IndexSlotException | LevelCardException e) {
            fail();
        }

        Requirement r1 = new LevelCardRequirement(CardColor.BLUE, 1, 1);

        assertTrue(r1.checkRequirement(player));
    }

    @Test
    public void testCheckRequirementFalse(){
        ArrayList<Integer> eachDepotCapacity = new ArrayList<>();
        eachDepotCapacity.add(1);
        eachDepotCapacity.add(2);
        eachDepotCapacity.add(3);
        DashBoard dashBoard = new DashBoard(3, eachDepotCapacity, null);
        Player player = new Player("test", dashBoard);

        try {
            player.getDashBoard().addCard(1, new DevCard(new HashMap<>(), 1, CardColor.BLUE, 3, null));
        } catch (IndexSlotException | LevelCardException e) {
            fail();
        }

        Requirement r1 = new LevelCardRequirement(CardColor.BLUE, 2, 1);

        assertFalse(r1.checkRequirement(player));
    }
}