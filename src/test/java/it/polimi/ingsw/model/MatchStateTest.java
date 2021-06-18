package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.NotPresentException;
import it.polimi.ingsw.utilities.Config;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.*;

public class MatchStateTest {

    @Test
    public void testGetPlayers(){
        Player p1 = new Player("p1", null);
        Player p2 = new Player("p2", null);
        ArrayList<Player> players = new ArrayList<>();
        players.add(p1);
        players.add(p2);

        Config config = Config.getDefaultConfig();

        MatchState matchState =  new MatchState(players, config.getDevCards(),
                config.getMarketRows(), config.getMarketColumns(), config.getMarbles());

        assertEquals(players, matchState.getPlayers());
    }

    @Test
    public void testGetPlayerFromIDSuccessful() {
        Player p1 = new Player("p1", null);
        Player p2 = new Player("p2", null);
        ArrayList<Player> players = new ArrayList<>();
        players.add(p1);
        players.add(p2);

        Config config = Config.getDefaultConfig();

        MatchState matchState =  new MatchState(players, config.getDevCards(),
                config.getMarketRows(), config.getMarketColumns(), config.getMarbles());

        try {
            assertEquals(p1, matchState.getPlayerFromID("p1"));
        } catch (NotPresentException notPresentException) {
            fail();
        }
    }

    @Test
    public void testGetPlayerFromIDException() {
        Player p1 = new Player("p1", null);
        Player p2 = new Player("p2", null);
        ArrayList<Player> players = new ArrayList<>();
        players.add(p1);
        players.add(p2);

        Config config = Config.getDefaultConfig();

        MatchState matchState =  new MatchState(players, config.getDevCards(),
                config.getMarketRows(), config.getMarketColumns(), config.getMarbles());

        try {
            assertEquals(p1, matchState.getPlayerFromID("p3"));
            fail();
        } catch (NotPresentException ignored) { }
    }

    @Test
    public void testGetPlayerPositionSuccessful() {
        Player p1 = new Player("p1", null);
        Player p2 = new Player("p2", null);
        ArrayList<Player> players = new ArrayList<>();
        players.add(p1);
        players.add(p2);

        Config config = Config.getDefaultConfig();

        MatchState matchState =  new MatchState(players, config.getDevCards(),
                config.getMarketRows(), config.getMarketColumns(), config.getMarbles());

        try {
            assertEquals(1, matchState.getPlayerPosition(p2));
        } catch (NotPresentException notPresentException) {
            fail();
        }
    }

    @Test
    public void testGetPlayerPositionException() {
        Player p1 = new Player("p1", null);
        Player p2 = new Player("p2", null);
        Player p3 = new Player("p3", null);
        ArrayList<Player> players = new ArrayList<>();
        players.add(p1);
        players.add(p2);

        Config config = Config.getDefaultConfig();

        MatchState matchState =  new MatchState(players, config.getDevCards(),
                config.getMarketRows(), config.getMarketColumns(), config.getMarbles());

        try {
            matchState.getPlayerPosition(p3);
            fail();
        } catch (NotPresentException ignored) { }
    }

    @Test
    public void testGetMarket() {
        MatchState matchState = createMatchState();
        Market market = matchState.getMarket();

        Config config = Config.getDefaultConfig();
        assertEquals(config.getMarketRows(), market.getRows());
        assertEquals(config.getMarketColumns(), market.getCols());

        HashMap<Marble, Integer> marketMarbles = new HashMap<>();
        for(Marble[] marbles: market.getMarketStatus()){
            for(Marble m: marbles){
                marketMarbles.put(m, marketMarbles.getOrDefault(m, 0) +1);
            }
        }
        Marble left = market.getMarbleLeft();
        marketMarbles.put(left, marketMarbles.getOrDefault(left, 0) + 1);

        assertEquals(config.getMarbles(), marketMarbles);
    }

    @Test
    public void testBeginMatch() {
        MatchState matchState = createMatchState();
        matchState.beginMatch();
        assertEquals(0, matchState.getCurrentPlayerIndex());
        assertEquals(TurnState.START, matchState.getTurnState());
        assertFalse(matchState.leaderActionExecuted);
    }

    @Test
    public void testNextTurn() {
        MatchState matchState = createMatchState();
        matchState.nextTurn();
        assertEquals(TurnState.START, matchState.getTurnState());
        assertEquals(1, matchState.getCurrentPlayerIndex());
        assertFalse(matchState.leaderActionExecuted);
    }

    @Test
    public void testGetSetTurnState() {
        MatchState matchState = createMatchState();
        matchState.setTurnState(TurnState.AFTER_MAIN_ACTION);
        assertEquals(TurnState.AFTER_MAIN_ACTION, matchState.getTurnState());
    }

    @Test
    public void testSetWaitingForSomething() {
        MatchState matchState = createMatchState();
        matchState.setTurnState(TurnState.AFTER_MAIN_ACTION);
        matchState.setWaitingForSomething();
        assertEquals(TurnState.WAITING_FOR_SOMETHING, matchState.getTurnState());
    }

    @Test
    public void somethingArrived() {
        MatchState matchState = createMatchState();
        matchState.setTurnState(TurnState.AFTER_MAIN_ACTION);
        matchState.setWaitingForSomething();
        matchState.somethingArrived();
        assertEquals(TurnState.AFTER_MAIN_ACTION, matchState.getTurnState());
    }

    @Test
    public void testSetLastRoundAndIsLastRound() {
        MatchState matchState = createMatchState();
        matchState.setLastRound();
        assertTrue(matchState.isLastRound());
    }

    private MatchState createMatchState(){
        Player p1 = new Player("p1", null);
        Player p2 = new Player("p2", null);
        ArrayList<Player> players = new ArrayList<>();
        players.add(p1);
        players.add(p2);

        Config config = Config.getDefaultConfig();

        return new MatchState(players, config.getDevCards(),
                config.getMarketRows(), config.getMarketColumns(), config.getMarbles());
    }
}