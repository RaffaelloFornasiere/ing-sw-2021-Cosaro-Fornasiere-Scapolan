package it.polimi.ingsw.model.LeaderCards;

import it.polimi.ingsw.exceptions.IllegalOperation;
import it.polimi.ingsw.exceptions.NotPresentException;
import it.polimi.ingsw.exceptions.ResourcesLimitsException;
import it.polimi.ingsw.model.CardColor;
import it.polimi.ingsw.model.DashBoard;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Resource;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.*;

public class ResourcesRequirementTest {

    @Test
    public void testIsEquivalentTrue() {
        Requirement rr1 = new ResourcesRequirement(new HashMap<>());
        Requirement rr2 = new ResourcesRequirement(new HashMap<>());
        assertTrue(rr1.isEquivalent(rr2));
    }

    @Test
    public void testIsEquivalentFalse() {
        Requirement rr1 = new ResourcesRequirement(new HashMap<>());
        Requirement rr2 = new LevelCardRequirement(CardColor.BLUE, 2, 1);
        assertFalse(rr1.isEquivalent(rr2));
    }

    @Test
    public void testIsEquivalentNull() {
        Requirement rr1 = new ResourcesRequirement(new HashMap<>());
        assertFalse(rr1.isEquivalent(null));
    }

    @Test
    public void testMergeSuccessful() {
        HashMap<Resource, Integer> r1 = new HashMap<>();
        r1.put(Resource.COIN, 3);
        r1.put(Resource.SHIELD, 2);
        Requirement rr1 = new ResourcesRequirement(r1);

        HashMap<Resource, Integer> r2 = new HashMap<>();
        r2.put(Resource.SERVANT, 1);
        r2.put(Resource.SHIELD, 4);
        Requirement rr2 = new ResourcesRequirement(r2);

        HashMap<Resource, Integer> re = new HashMap<>();
        re.put(Resource.COIN, 3);
        re.put(Resource.SHIELD, 6);
        re.put(Resource.SERVANT, 1);

        try {
            Requirement rrm = rr1.merge(rr2);
            if(rrm.getClass()!=rr1.getClass())
                fail();
            assertEquals(re, ((ResourcesRequirement)rrm).getResources());
        } catch (IllegalOperation illegalOperation) {
            fail();
        }
    }

    @Test
    public void testMergeException() {
        HashMap<Resource, Integer> r1 = new HashMap<>();
        r1.put(Resource.COIN, 3);
        r1.put(Resource.SHIELD, 2);
        Requirement rr1 = new ResourcesRequirement(r1);

        Requirement rr2 = new LevelCardRequirement(CardColor.BLUE, 2, 1);


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
        DashBoard dashBoard = new DashBoard(3, eachDepotCapacity, null, null);

        try {
            dashBoard.getWarehouse().get(1).addResources(1, Resource.SERVANT);
        } catch (Exception e) {
            fail();
        }

        try {
            dashBoard.getWarehouse().get(2).addResources(3, Resource.SHIELD);
        } catch (Exception e) {
            fail();
        }

        dashBoard.addResourcesToStrongBox(Resource.SERVANT, 4);

        ArrayList<LeaderPower> leaderPowers = new ArrayList<>();

        HashMap<Resource, Integer> maxResources = new HashMap<>();
        maxResources.put(Resource.SHIELD, 3);
        maxResources.put(Resource.COIN, 2);

        leaderPowers.add(new DepositLeaderPower(maxResources));

        HashMap<Resource, Integer> addedResources = new HashMap<>();
        addedResources.put(Resource.COIN, 1);
        try {
            ((DepositLeaderPower)leaderPowers.get(0)).addResources(addedResources);
        } catch (ResourcesLimitsException e) {
            fail();
        }

        LeaderCard leaderCard = new LeaderCard("Test", 3, new ArrayList<>(), leaderPowers);

        Player player = new Player("test", dashBoard);

        ArrayList<LeaderCard> leaderCards = new ArrayList<>();
        leaderCards.add(leaderCard);
        player.setLeaderCards(leaderCards);

        try {
            player.activateLeaderCard(leaderCard);
        } catch (NotPresentException | IllegalOperation e) {
            fail();
        }

        HashMap<Resource, Integer> r1 = new HashMap<>();
        r1.put(Resource.COIN, 1);
        r1.put(Resource.SHIELD, 2);
        Requirement rr1 = new ResourcesRequirement(r1);

        assertTrue(rr1.checkRequirement(player));
    }

    @Test
    public void testCheckRequirementFalse(){
        ArrayList<Integer> eachDepotCapacity = new ArrayList<>();
        eachDepotCapacity.add(1);
        eachDepotCapacity.add(2);
        eachDepotCapacity.add(3);
        DashBoard dashBoard = new DashBoard(3, eachDepotCapacity, null, null);

        try {
            dashBoard.getWarehouse().get(1).addResources(1, Resource.SERVANT);
        } catch (Exception e) {
            fail();
        }

        try {
            dashBoard.getWarehouse().get(2).addResources(3, Resource.SHIELD);
        } catch (Exception e) {
            fail();
        }

        dashBoard.addResourcesToStrongBox(Resource.SERVANT, 4);

        ArrayList<LeaderPower> leaderPowers = new ArrayList<>();

        HashMap<Resource, Integer> maxResources = new HashMap<>();
        maxResources.put(Resource.SHIELD, 3);
        maxResources.put(Resource.COIN, 2);

        leaderPowers.add(new DepositLeaderPower(maxResources));

        HashMap<Resource, Integer> addedResources = new HashMap<>();
        addedResources.put(Resource.COIN, 1);
        try {
            ((DepositLeaderPower)leaderPowers.get(0)).addResources(addedResources);
        } catch (ResourcesLimitsException e) {
            fail();
        }

        LeaderCard leaderCard = new LeaderCard("Test", 3, new ArrayList<>(), leaderPowers);

        Player player = new Player("test", dashBoard);

        ArrayList<LeaderCard> leaderCards = new ArrayList<>();
        leaderCards.add(leaderCard);
        player.setLeaderCards(leaderCards);

        try {
            player.activateLeaderCard(leaderCard);
        } catch (NotPresentException | IllegalOperation e) {
            fail();
        }

        HashMap<Resource, Integer> r1 = new HashMap<>();
        r1.put(Resource.COIN, 5);
        r1.put(Resource.SHIELD, 2);
        Requirement rr1 = new ResourcesRequirement(r1);

        assertFalse(rr1.checkRequirement(player));
    }
}