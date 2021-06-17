package it.polimi.ingsw.model.singlePlayer;

import it.polimi.ingsw.model.DashBoard;
import it.polimi.ingsw.model.FaithTrack.FaithTrack;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.utilities.Config;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class SoloActionTokenMoveTest {

    @Test
    public void testGetters() {
        SoloActionTokenMove token = new SoloActionTokenMove(3, true);
        assertEquals(3, token.getSteps());
        assertTrue(token.reshuffle());
    }

    @Test
    public void testDoAction() {
        FaithTrack faithTrack = FaithTrack.initFaithTrack(Config.getDefaultConfig().getFaithTrack());
        DashBoard dashBoard = new DashBoard(0, new ArrayList<>(), null);
        Player player = new Player("Test", dashBoard);

        SinglePlayerMatchState singlePlayerMatchState = new SinglePlayerMatchState(player,
                Config.getDefaultConfig().getDevCards(), Config.getDefaultConfig().getMarketRows(), Config.getDefaultConfig().getMarketColumns(),
                Config.getDefaultConfig().getMarbles(), Config.getDefaultConfig().getSoloActionTokens());

        SoloActionTokenMove token = new SoloActionTokenMove(15, false);

        assertFalse(token.doAction(singlePlayerMatchState));
        assertEquals(15, singlePlayerMatchState.getLorenzoPosition());
        assertTrue(token.doAction(singlePlayerMatchState));
        assertEquals(24, singlePlayerMatchState.getLorenzoPosition());
    }
}