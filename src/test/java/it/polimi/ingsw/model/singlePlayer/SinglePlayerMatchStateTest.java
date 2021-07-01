package it.polimi.ingsw.model.singlePlayer;

import it.polimi.ingsw.exceptions.IllegalOperation;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.faithTrack.FaithTrack;
import it.polimi.ingsw.utilities.Config;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class SinglePlayerMatchStateTest {

    @Test
    public void testGetters() {
        Player player = new Player("Test", null);
        SinglePlayerMatchState singlePlayerMatchState = new SinglePlayerMatchState(player,
                Config.getDefaultConfig().getDevCards(), Config.getDefaultConfig().getMarketRows(), Config.getDefaultConfig().getMarketColumns(),
                Config.getDefaultConfig().getMarbles(), Config.getDefaultConfig().getSoloActionTokens());

        assertEquals(player, singlePlayerMatchState.getPlayer());
        assertEquals(0, singlePlayerMatchState.getLorenzoPosition());
    }

    @Test
    public void testPopSoloActionTokens() {
        ArrayList<SoloActionToken> defaultTokens = Config.getDefaultConfig().getSoloActionTokens();
        Player player = new Player("Test", null);
        SinglePlayerMatchState singlePlayerMatchState = new SinglePlayerMatchState(player,
                Config.getDefaultConfig().getDevCards(), Config.getDefaultConfig().getMarketRows(), Config.getDefaultConfig().getMarketColumns(),
                Config.getDefaultConfig().getMarbles(), defaultTokens);

        ArrayList<SoloActionToken> tokens = new ArrayList<>();
        for(int i=0; i<defaultTokens.size(); i++){
            try {
                tokens.add(singlePlayerMatchState.popSoloActionTokens());
            } catch (IllegalOperation ignore) {
            }
        }
        assertTrue(tokens.containsAll(defaultTokens));
        assertTrue(defaultTokens.containsAll(tokens));
    }

    @Test
    public void testTopSoloActionTokens() {
        ArrayList<SoloActionToken> defaultTokens = Config.getDefaultConfig().getSoloActionTokens();
        Player player = new Player("Test", null);
        SinglePlayerMatchState singlePlayerMatchState = new SinglePlayerMatchState(player,
                Config.getDefaultConfig().getDevCards(), Config.getDefaultConfig().getMarketRows(), Config.getDefaultConfig().getMarketColumns(),
                Config.getDefaultConfig().getMarbles(), defaultTokens);

        for(int i=0; i<defaultTokens.size(); i++){
            try {
                assertTrue(defaultTokens.contains(singlePlayerMatchState.topSoloActionTokens()));
                singlePlayerMatchState.popSoloActionTokens();
            } catch (IllegalOperation ignore) {
            }
        }
    }

    @Test
    public void testShuffleToken() {
        ArrayList<SoloActionToken> defaultTokens = Config.getDefaultConfig().getSoloActionTokens();
        Player player = new Player("Test", null);
        SinglePlayerMatchState singlePlayerMatchState = new SinglePlayerMatchState(player,
                Config.getDefaultConfig().getDevCards(), Config.getDefaultConfig().getMarketRows(), Config.getDefaultConfig().getMarketColumns(),
                Config.getDefaultConfig().getMarbles(), defaultTokens);

        singlePlayerMatchState.shuffleToken();

        ArrayList<SoloActionToken> tokens = new ArrayList<>();
        for(int i=0; i<defaultTokens.size(); i++){
            try {
                tokens.add(singlePlayerMatchState.popSoloActionTokens());
            } catch (IllegalOperation ignore) {
            }
        }
        assertTrue(tokens.containsAll(defaultTokens));
        assertTrue(defaultTokens.containsAll(tokens));
    }

    @Test
    public void testIncrementLorenzoPosition() {
        FaithTrack.initFaithTrack(Config.getDefaultConfig().getFaithTrack());
        Player player = new Player("Test", null);
        SinglePlayerMatchState singlePlayerMatchState = new SinglePlayerMatchState(player,
                Config.getDefaultConfig().getDevCards(), Config.getDefaultConfig().getMarketRows(), Config.getDefaultConfig().getMarketColumns(),
                Config.getDefaultConfig().getMarbles(), Config.getDefaultConfig().getSoloActionTokens());

        singlePlayerMatchState.incrementLorenzoPosition(4);
        assertEquals(4, singlePlayerMatchState.getLorenzoPosition());
    }
}