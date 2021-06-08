package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.IllegalOperation;
import it.polimi.ingsw.exceptions.NotPresentException;
import it.polimi.ingsw.exceptions.ResourcesLimitsException;
import it.polimi.ingsw.model.LeaderCards.DepositLeaderPower;
import it.polimi.ingsw.model.LeaderCards.LeaderCard;
import it.polimi.ingsw.model.LeaderCards.LeaderPower;
import it.polimi.ingsw.model.LeaderCards.Requirement;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class PlayerTest {

    @Test
    public void getAllPayerResources() {
        ArrayList<Integer> eachDepotCapacity = new ArrayList<>();
        eachDepotCapacity.add(1);
        eachDepotCapacity.add(2);
        eachDepotCapacity.add(3);
        DashBoard dashBoard = new DashBoard(3, eachDepotCapacity, null, null);

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

        LeaderCard leaderCard = new LeaderCard("test", 3, new ArrayList<Requirement>(), leaderPowers);

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
}