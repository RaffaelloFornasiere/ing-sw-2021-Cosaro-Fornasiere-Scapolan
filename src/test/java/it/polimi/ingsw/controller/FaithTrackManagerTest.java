package it.polimi.ingsw.controller;


import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.devCards.DevCard;
import it.polimi.ingsw.model.faithTrack.FaithTrack;
import it.polimi.ingsw.utilities.Config;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.HashMap;

public class FaithTrackManagerTest extends TestCase {

        public void testIncrementFaithTrackPosition() {
                //creates the array of depot capacities: first level contains max 5 resources, second level max 3, fourth level max 1
                ArrayList<Integer> dCapacity = new ArrayList<>(3);
                dCapacity.add(1);
                dCapacity.add(3);
                dCapacity.add(5);
                //creates faithTrack: it is 4 cells long, with respective victory points: 1,2,3,4
                FaithTrack.initFaithTrack(Config.getDefaultConfig().getFaithTrack());
                //creates production power
                ProductionPower p = new ProductionPower(new HashMap<>(), new HashMap<>(), 0, 0, 0);
                //create dashboard
                DashBoard d1 = new DashBoard(dCapacity.size(), dCapacity, p);
                DashBoard d2 = new DashBoard(dCapacity.size(), dCapacity, p);
                DashBoard d3 = new DashBoard(dCapacity.size(), dCapacity, p);

                Player player1= new Player("Lisa", d1);
                Player player2= new Player("Leonardo", d2);
                Player player3= new Player("Raffaello", d3);

                ArrayList<Player> players= new ArrayList<>();
                players.add(player1);
                players.add(player2);
                players.add(player3);

                //Create DevCard
                //Create Resources
                HashMap<Resource, Integer> resources= new HashMap<>();
                resources.put(Resource.SHIELD, 3);
                DevCard devCard= new DevCard(resources, 1, CardColor.GREEN,4, new ProductionPower(new HashMap<>(), new HashMap<>(), 0, 0, 0));
                ArrayList<DevCard> aDevCard= new ArrayList<>();
                aDevCard.add(devCard);
                assertEquals(1, aDevCard.size());
                //create MatchState
                MatchState matchState= new MatchState(players, aDevCard, 0, 0, new HashMap<>() {{
                        put(Marble.RED, 1);
                }});
                //player1 has incremented its position of one step, now ITS POSITION SHOULD BE 1.
                new FaithTrackManager(matchState).incrementFaithTrackPosition(player1, 1);
                assertEquals(1, player1.getDashBoard().getFaithTrackData().getPosition());
                //increment another 8 steps, now its position should be 9. The popeCell at position 8 should be past,
                // so the VaticanReportSection should have happened.
                //since other players are still at position 0, no one is in the vaticanReportSection, so no one gets
                //a favor Card except for the player 1.
                new FaithTrackManager(matchState).incrementFaithTrackPosition(player1, 8);
                assertEquals(9, player1.getDashBoard().getFaithTrackData().getPosition());
                assertEquals(0, player2.getDashBoard().getFaithTrackData().getPosition());
                assertEquals(0, player3.getDashBoard().getFaithTrackData().getPosition());
                assertEquals(1, player1.getDashBoard().getFaithTrackData().getAcquiredPopeFavorCards().size());
                assertEquals(2, player1.getDashBoard().getFaithTrackData().getAcquiredPopeFavorCards().get(8).getVictoryPoints());
                assertEquals(0, player2.getDashBoard().getFaithTrackData().getAcquiredPopeFavorCards().size());
                assertEquals(0, player3.getDashBoard().getFaithTrackData().getAcquiredPopeFavorCards().size());
                //increment position further 20 steps. Should go past the end of faithTrack
                new FaithTrackManager(matchState).incrementFaithTrackPosition(player1, 20);
                assertEquals(24, player1.getDashBoard().getFaithTrackData().getPosition());
                assertEquals(3, player1.getDashBoard().getFaithTrackData().getAcquiredPopeFavorCards().size());
                assertEquals(2, player1.getDashBoard().getFaithTrackData().getAcquiredPopeFavorCards().get(8).getVictoryPoints());
                assertEquals(true, matchState.isLastRound());

        }


}