package it.polimi.ingsw.controller;


import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.DevCards.DevCard;
import it.polimi.ingsw.model.FaithTrack.CellWithEffect;
import it.polimi.ingsw.model.FaithTrack.FaithTrack;
import it.polimi.ingsw.model.FaithTrack.PopeCell;
import it.polimi.ingsw.model.FaithTrack.PopeFavorCard;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.HashMap;

public class FaithTrackManagerTest extends TestCase {

        public void testIncrementFaithTrackPosition() {
                //creates the array of depot capacities: first level contains max 5 resources, second level max 3, fourth level max 1
                ArrayList<Integer> dcapacity = new ArrayList<>(3);
                dcapacity.add(1);
                dcapacity.add(3);
                dcapacity.add(5);
                //creates faithtrack: it is 4 cells long, with respective victory points: 1,2,3,4
                FaithTrack ft;
                ArrayList<Integer> a = new ArrayList<Integer>(4);
                a.add(1);
                a.add(2);
                a.add(3);
                a.add(4);
                //create Hashmap of CellWithEffect : put a PopeCell in third cell(position 2) index and victory Points coincides.
                ArrayList<CellWithEffect> effects= new ArrayList<>();
                effects.add(new PopeCell(2,3, new PopeFavorCard(3),2));
                ft = FaithTrack.initFaithTrack(4, effects, a);
                //creates production power
                ProductionPower p = new ProductionPower(new HashMap<>(), new HashMap<Resource, Integer>(), 0, 0, 0);
                //create dashboard
                DashBoard d1 = new DashBoard(dcapacity.size(), dcapacity, p, ft);
                DashBoard d2 = new DashBoard(dcapacity.size(), dcapacity, p, ft);
                DashBoard d3 = new DashBoard(dcapacity.size(), dcapacity, p, ft);

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
                DevCard devCard= new DevCard(resources, 1, CardColor.GREEN,4, new ProductionPower());
                ArrayList<DevCard> aDevCard= new ArrayList<>();
                aDevCard.add(devCard);
                assertEquals(1, aDevCard.size());
                //create MatchState
                MatchState matchState= new MatchState(players, aDevCard, 0, 0, new HashMap<Marble, Integer>(){{put(Marble.RED, 1);}});
                //player1 has incremented its position of one step, now ITS POSITION SHOULD BE 1.
                new FaithTrackManager(matchState).incrementFaithTrackPosition(player1, 1);
                assertEquals(1, (int) player1.getDashBoard().getFaithTrackData().getPosition());
                //increment another 2 steps, now its position should be 3. The popeCell at position 2 should be past,
                // so the VaticanReportSection should have happened.
                //since other players are still at position 0, no one is in the vaticanReportSection, so no one gets
                //a favor Card except for the plyer 1.
                new FaithTrackManager(matchState).incrementFaithTrackPosition(player1, 2);
                assertEquals(3, (int) player1.getDashBoard().getFaithTrackData().getPosition());
                assertEquals(0, (int) player2.getDashBoard().getFaithTrackData().getPosition());
                assertEquals(0, (int) player3.getDashBoard().getFaithTrackData().getPosition());
                assertEquals(1, player1.getDashBoard().getFaithTrackData().getAcquiredPopeFavorCards().size());
                assertEquals(3, player1.getDashBoard().getFaithTrackData().getAcquiredPopeFavorCards().get(2).getVictoryPoints());
                assertEquals(0, player2.getDashBoard().getFaithTrackData().getAcquiredPopeFavorCards().size());
                assertEquals(0, player3.getDashBoard().getFaithTrackData().getAcquiredPopeFavorCards().size());
        }


}