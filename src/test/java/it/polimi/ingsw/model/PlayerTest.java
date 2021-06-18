package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.IllegalOperation;
import it.polimi.ingsw.exceptions.NotPresentException;
import it.polimi.ingsw.exceptions.ResourcesLimitsException;
import it.polimi.ingsw.model.LeaderCards.DepositLeaderPower;
import it.polimi.ingsw.model.LeaderCards.LeaderCard;
import it.polimi.ingsw.model.LeaderCards.LeaderPower;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.*;

public class PlayerTest {

    @Test
    public void testGetters(){
        DashBoard dashBoard = new DashBoard(3, new ArrayList<>(), null);
        Player player = new Player("test", dashBoard);

        assertEquals("test", player.getPlayerId());
        assertEquals(dashBoard, player.getDashBoard());
    }

    @Test
    public void testGetSetLeaderCards(){
        Player player = new Player("test", null);

        LeaderCard lc1 = new LeaderCard("lc1", 0, new ArrayList<>(), new ArrayList<>());
        LeaderCard lc2 = new LeaderCard("lc2", 0, new ArrayList<>(), new ArrayList<>());
        LeaderCard lc3 = new LeaderCard("lc3", 0, new ArrayList<>(), new ArrayList<>());

        ArrayList<LeaderCard> leaderCards = new ArrayList<>();
        leaderCards.add(lc1);
        leaderCards.add(lc3);

        player.setLeaderCards(leaderCards);

        ArrayList<LeaderCard> playerLeaderCards = player.getLeaderCards();

        assertTrue(playerLeaderCards.contains(lc1));
        assertFalse(playerLeaderCards.contains(lc2));
        assertTrue(playerLeaderCards.contains(lc3));

    }

    @Test
    public void testActivateLeaderCardsSuccessfulAndGetActiveLeaderCards(){
        Player player = new Player("test", null);

        LeaderCard lc1 = new LeaderCard("lc1", 0, new ArrayList<>(), new ArrayList<>());
        LeaderCard lc2 = new LeaderCard("lc2", 0, new ArrayList<>(), new ArrayList<>());
        LeaderCard lc3 = new LeaderCard("lc3", 0, new ArrayList<>(), new ArrayList<>());

        ArrayList<LeaderCard> leaderCards = new ArrayList<>();
        leaderCards.add(lc1);
        leaderCards.add(lc3);

        player.setLeaderCards(leaderCards);

        try {
            player.activateLeaderCard(lc1);
        } catch (NotPresentException | IllegalOperation notPresentException) {
            fail();
        }

        ArrayList<LeaderCard> playerLeaderCards = player.getLeaderCards();

        assertTrue(playerLeaderCards.contains(lc1));
        assertFalse(playerLeaderCards.contains(lc2));
        assertTrue(playerLeaderCards.contains(lc3));

        ArrayList<LeaderCard> playerActiveLeaderCards = player.getActiveLeaderCards();

        assertTrue(playerActiveLeaderCards.contains(lc1));
        assertFalse(playerActiveLeaderCards.contains(lc2));
        assertFalse(playerActiveLeaderCards.contains(lc3));

    }

    @Test
    public void testActivateLeaderCardsAlreadyActive(){
        Player player = new Player("test", null);

        LeaderCard lc1 = new LeaderCard("lc1", 0, new ArrayList<>(), new ArrayList<>());
        LeaderCard lc2 = new LeaderCard("lc2", 0, new ArrayList<>(), new ArrayList<>());
        LeaderCard lc3 = new LeaderCard("lc3", 0, new ArrayList<>(), new ArrayList<>());

        ArrayList<LeaderCard> leaderCards = new ArrayList<>();
        leaderCards.add(lc1);
        leaderCards.add(lc3);

        player.setLeaderCards(leaderCards);

        try {
            player.activateLeaderCard(lc1);
        } catch (NotPresentException | IllegalOperation notPresentException) {
            fail();
        }

        try {
            player.activateLeaderCard(lc1);
            fail();
        } catch (NotPresentException notPresentException) {
            fail();
        } catch (IllegalOperation ignored) { }

        ArrayList<LeaderCard> playerLeaderCards = player.getLeaderCards();

        assertTrue(playerLeaderCards.contains(lc1));
        assertFalse(playerLeaderCards.contains(lc2));
        assertTrue(playerLeaderCards.contains(lc3));

        ArrayList<LeaderCard> playerActiveLeaderCards = player.getActiveLeaderCards();

        assertTrue(playerActiveLeaderCards.contains(lc1));
        assertFalse(playerActiveLeaderCards.contains(lc2));
        assertFalse(playerActiveLeaderCards.contains(lc3));
    }

    @Test
    public void testActivateLeaderCardsNotPresent(){
        Player player = new Player("test", null);

        LeaderCard lc1 = new LeaderCard("lc1", 0, new ArrayList<>(), new ArrayList<>());
        LeaderCard lc2 = new LeaderCard("lc2", 0, new ArrayList<>(), new ArrayList<>());
        LeaderCard lc3 = new LeaderCard("lc3", 0, new ArrayList<>(), new ArrayList<>());

        ArrayList<LeaderCard> leaderCards = new ArrayList<>();
        leaderCards.add(lc1);
        leaderCards.add(lc3);

        player.setLeaderCards(leaderCards);

        try {
            player.activateLeaderCard(lc1);
        } catch (NotPresentException | IllegalOperation notPresentException) {
            fail();
        }

        try {
            player.activateLeaderCard(lc2);
            fail();
        } catch (NotPresentException ignore) {
        } catch (IllegalOperation e) {
            fail();
        }

        ArrayList<LeaderCard> playerLeaderCards = player.getLeaderCards();

        assertTrue(playerLeaderCards.contains(lc1));
        assertFalse(playerLeaderCards.contains(lc2));
        assertTrue(playerLeaderCards.contains(lc3));

        ArrayList<LeaderCard> playerActiveLeaderCards = player.getActiveLeaderCards();

        assertTrue(playerActiveLeaderCards.contains(lc1));
        assertFalse(playerActiveLeaderCards.contains(lc2));
        assertFalse(playerActiveLeaderCards.contains(lc3));
    }

    @Test
    public void testActivateLeaderCardsNull(){
        Player player = new Player("test", null);

        LeaderCard lc1 = new LeaderCard("lc1", 0, new ArrayList<>(), new ArrayList<>());
        LeaderCard lc2 = new LeaderCard("lc2", 0, new ArrayList<>(), new ArrayList<>());
        LeaderCard lc3 = new LeaderCard("lc3", 0, new ArrayList<>(), new ArrayList<>());

        ArrayList<LeaderCard> leaderCards = new ArrayList<>();
        leaderCards.add(lc1);
        leaderCards.add(lc3);

        player.setLeaderCards(leaderCards);

        try {
            player.activateLeaderCard(lc1);
        } catch (NotPresentException | IllegalOperation notPresentException) {
            fail();
        }

        try {
            player.activateLeaderCard(null);
            fail();
        } catch (NotPresentException ignore) {
        } catch (IllegalOperation e) {
            fail();
        }

        ArrayList<LeaderCard> playerLeaderCards = player.getLeaderCards();

        assertTrue(playerLeaderCards.contains(lc1));
        assertFalse(playerLeaderCards.contains(lc2));
        assertTrue(playerLeaderCards.contains(lc3));

        ArrayList<LeaderCard> playerActiveLeaderCards = player.getActiveLeaderCards();

        assertTrue(playerActiveLeaderCards.contains(lc1));
        assertFalse(playerActiveLeaderCards.contains(lc2));
        assertFalse(playerActiveLeaderCards.contains(lc3));
    }

    @Test
    public void testGetLeaderCardFromIDSuccessful(){
        Player player = new Player("test", null);

        LeaderCard lc1 = new LeaderCard("lc1", 0, new ArrayList<>(), new ArrayList<>());
        LeaderCard lc3 = new LeaderCard("lc3", 0, new ArrayList<>(), new ArrayList<>());

        ArrayList<LeaderCard> leaderCards = new ArrayList<>();
        leaderCards.add(lc1);
        leaderCards.add(lc3);

        player.setLeaderCards(leaderCards);

        try {
            assertEquals(lc1, player.getLeaderCardFromID("lc1"));
        } catch (NotPresentException notPresentException) {
            fail();
        }
    }

    @Test
    public void testGetLeaderCardFromIDNtPresent(){
        Player player = new Player("test", null);

        LeaderCard lc1 = new LeaderCard("lc1", 0, new ArrayList<>(), new ArrayList<>());
        LeaderCard lc3 = new LeaderCard("lc3", 0, new ArrayList<>(), new ArrayList<>());

        ArrayList<LeaderCard> leaderCards = new ArrayList<>();
        leaderCards.add(lc1);
        leaderCards.add(lc3);

        player.setLeaderCards(leaderCards);

        try {
            assertEquals(lc1, player.getLeaderCardFromID("lc2"));
            fail();
        } catch (NotPresentException ignored) { }
    }

    @Test
    public void testRemoveLeaderCardSuccessful(){
        Player player = new Player("test", null);

        LeaderCard lc1 = new LeaderCard("lc1", 0, new ArrayList<>(), new ArrayList<>());
        LeaderCard lc2 = new LeaderCard("lc2", 0, new ArrayList<>(), new ArrayList<>());
        LeaderCard lc3 = new LeaderCard("lc3", 0, new ArrayList<>(), new ArrayList<>());

        ArrayList<LeaderCard> leaderCards = new ArrayList<>();
        leaderCards.add(lc1);
        leaderCards.add(lc3);

        player.setLeaderCards(leaderCards);

        try {
            player.removeLeaderCard(lc1);
        } catch (NotPresentException notPresentException) {
            fail();
        }

        ArrayList<LeaderCard> playerLeaderCards = player.getLeaderCards();

        assertFalse(playerLeaderCards.contains(lc1));
        assertFalse(playerLeaderCards.contains(lc2));
        assertTrue(playerLeaderCards.contains(lc3));
    }

    @Test
    public void testRemoveLeaderCardNotPresent(){
        Player player = new Player("test", null);

        LeaderCard lc1 = new LeaderCard("lc1", 0, new ArrayList<>(), new ArrayList<>());
        LeaderCard lc2 = new LeaderCard("lc2", 0, new ArrayList<>(), new ArrayList<>());
        LeaderCard lc3 = new LeaderCard("lc3", 0, new ArrayList<>(), new ArrayList<>());

        ArrayList<LeaderCard> leaderCards = new ArrayList<>();
        leaderCards.add(lc1);
        leaderCards.add(lc3);

        player.setLeaderCards(leaderCards);

        try {
            player.removeLeaderCard(lc2);
            fail();
        } catch (NotPresentException ignored) { }

        ArrayList<LeaderCard> playerLeaderCards = player.getLeaderCards();

        assertTrue(playerLeaderCards.contains(lc1));
        assertFalse(playerLeaderCards.contains(lc2));
        assertTrue(playerLeaderCards.contains(lc3));
    }

    @Test
    public void testGetAllPayerResources() {
        ArrayList<Integer> eachDepotCapacity = new ArrayList<>();
        eachDepotCapacity.add(1);
        eachDepotCapacity.add(2);
        eachDepotCapacity.add(3);
        DashBoard dashBoard = new DashBoard(3, eachDepotCapacity, null);

        try {
            dashBoard.getWarehouse().get(1).addResources(Resource.SERVANT, 1);
        } catch (Exception e) {
            fail();
        }

        try {
            dashBoard.getWarehouse().get(2).addResources(Resource.SHIELD, 3);
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

        LeaderCard leaderCard = new LeaderCard("test", 3, new ArrayList<>(), leaderPowers);

        Player player = new Player("test", dashBoard);

        ArrayList<LeaderCard> leaderCards = new ArrayList<>();
        leaderCards.add(leaderCard);
        player.setLeaderCards(leaderCards);

        try {
            player.activateLeaderCard(leaderCard);
        } catch (NotPresentException | IllegalOperation e) {
            fail();
        }

        HashMap<Resource, Integer> expected = new HashMap<>();
        expected.put(Resource.SERVANT, 5);
        expected.put(Resource.SHIELD, 3);
        expected.put(Resource.COIN, 1);

        for(Resource r: Resource.values())
            if(!expected.containsKey(r))
                expected.put(r,0);

        assertEquals(expected, player.getAllPayerResources());
    }

    @Test
    public void testGetLeaderCardResources() {
        ArrayList<Integer> eachDepotCapacity = new ArrayList<>();
        eachDepotCapacity.add(1);
        eachDepotCapacity.add(2);
        eachDepotCapacity.add(3);
        DashBoard dashBoard = new DashBoard(3, eachDepotCapacity, null);

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

        LeaderCard leaderCard = new LeaderCard("test", 3, new ArrayList<>(), leaderPowers);

        ArrayList<LeaderPower> leaderPowers2 = new ArrayList<>();

        HashMap<Resource, Integer> maxResources2 = new HashMap<>();
        maxResources2.put(Resource.SERVANT, 3);
        maxResources2.put(Resource.COIN, 1);

        leaderPowers2.add(new DepositLeaderPower(maxResources2));

        HashMap<Resource, Integer> addedResources2 = new HashMap<>();
        addedResources2.put(Resource.COIN, 1);
        addedResources2.put(Resource.SERVANT, 1);
        try {
            ((DepositLeaderPower)leaderPowers2.get(0)).addResources(addedResources2);
        } catch (ResourcesLimitsException e) {
            fail();
        }

        LeaderCard leaderCard2 = new LeaderCard("test", 3, new ArrayList<>(), leaderPowers2);

        Player player = new Player("test", dashBoard);

        ArrayList<LeaderCard> leaderCards = new ArrayList<>();
        leaderCards.add(leaderCard);
        leaderCards.add(leaderCard2);
        player.setLeaderCards(leaderCards);

        try {
            player.activateLeaderCard(leaderCard);
            player.activateLeaderCard(leaderCard2);
        } catch (NotPresentException | IllegalOperation e) {
            fail();
        }

        HashMap<Resource, Integer> expected = new HashMap<>();
        expected.put(Resource.SERVANT, 1);
        expected.put(Resource.COIN, 2);

        for(Resource r: Resource.values())
            if(!expected.containsKey(r))
                expected.put(r,0);

        assertEquals(expected, player.getAllPayerResources());
    }
}