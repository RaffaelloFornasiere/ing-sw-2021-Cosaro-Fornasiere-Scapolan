package it.polimi.ingsw.model.singlePlayer;

import com.google.gson.Gson;
import com.sun.scenario.effect.impl.sw.java.JSWBlend_GREENPeer;
import it.polimi.ingsw.events.ControllerEvents.MatchEvents.DiscardLeaderCardEvent;
import it.polimi.ingsw.exceptions.NotPresentException;
import it.polimi.ingsw.model.CardColor;
import it.polimi.ingsw.model.DashBoard;
import it.polimi.ingsw.model.DevCards.DevCard;
import it.polimi.ingsw.model.DevCards.DevDeck;
import it.polimi.ingsw.model.FaithTrack.FaithTrack;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.ProductionPower;
import it.polimi.ingsw.utilities.Config;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class SoloActionTokenDiscardTest {

    @Test
    public void testGetCardsDiscarded() {
        HashMap<CardColor, Integer> discarded = new HashMap<>();
        discarded.put(CardColor.BLUE, 2);
        discarded.put(CardColor.YELLOW, 3);

        SoloActionTokenDiscard token = new SoloActionTokenDiscard(discarded);

        discarded.remove(CardColor.BLUE);

        HashMap<CardColor, Integer> expected = new HashMap<>();
        expected.put(CardColor.BLUE, 2);
        expected.put(CardColor.YELLOW, 3);

        assertEquals(expected, token.getCardsDiscarded());
    }

    @Test
    public void testDoAction() {
        Player player = new Player("Test", null);
        ArrayList<DevCard> devCards = new ArrayList<>();
        devCards.add(new DevCard(new HashMap<>(), 1, CardColor.BLUE, 1, null));
        devCards.add(new DevCard(new HashMap<>(), 1, CardColor.BLUE, 2, null));
        devCards.add(new DevCard(new HashMap<>(), 2, CardColor.BLUE, 3, null));
        devCards.add(new DevCard(new HashMap<>(), 1, CardColor.GREEN, 4, null));

        SinglePlayerMatchState singlePlayerMatchState = new SinglePlayerMatchState(player,
                devCards, Config.getDefaultConfig().getMarketRows(), Config.getDefaultConfig().getMarketColumns(),
                Config.getDefaultConfig().getMarbles(), Config.getDefaultConfig().getSoloActionTokens());

        HashMap<CardColor, Integer> discarded = new HashMap<>();
        discarded.put(CardColor.BLUE, 1);
        SoloActionTokenDiscard token = new SoloActionTokenDiscard(discarded);

        assertFalse(token.doAction(singlePlayerMatchState));

        ArrayList<DevCard> topCards = new ArrayList<>();
        for(int i=0; i<singlePlayerMatchState.getDevCardGrid().getRowsNumber(); i++){
            for(int j=0; j<singlePlayerMatchState.getDevCardGrid().getColumnsNumber(); j++){
                try {
                    topCards.add(singlePlayerMatchState.getDevCardGrid().topCard(i, j));
                } catch (NotPresentException ignore) {
                }
            }
        }
        DevCard removed = devCards.remove(1);
        assertTrue(topCards.containsAll(devCards));
        assertFalse(topCards.contains(removed));

        assertFalse(token.doAction(singlePlayerMatchState));

        topCards = new ArrayList<>();
        for(int i=0; i<singlePlayerMatchState.getDevCardGrid().getRowsNumber(); i++){
            for(int j=0; j<singlePlayerMatchState.getDevCardGrid().getColumnsNumber(); j++){
                try {
                    topCards.add(singlePlayerMatchState.getDevCardGrid().topCard(i, j));
                } catch (NotPresentException ignore) {
                }
            }
        }
        removed = devCards.remove(0);
        assertTrue(topCards.containsAll(devCards));
        assertFalse(topCards.contains(removed));

        assertTrue(token.doAction(singlePlayerMatchState));

        topCards = new ArrayList<>();
        for(int i=0; i<singlePlayerMatchState.getDevCardGrid().getRowsNumber(); i++){
            for(int j=0; j<singlePlayerMatchState.getDevCardGrid().getColumnsNumber(); j++){
                try {
                    topCards.add(singlePlayerMatchState.getDevCardGrid().topCard(i, j));
                } catch (NotPresentException ignore) {
                }
            }
        }
        removed = devCards.remove(0);
        assertTrue(topCards.containsAll(devCards));
        assertFalse(topCards.contains(removed));
    }

    @Test
    public void testDescription() {
        HashMap<CardColor, Integer> discarded = new HashMap<>();
        discarded.put(CardColor.BLUE, 2);
        discarded.put(CardColor.YELLOW, 3);
        discarded.put(CardColor.GREEN, -3);

        SoloActionTokenDiscard token = new SoloActionTokenDiscard(discarded);

        String description = token.description();

        assertTrue(description.equals("The following cards will be discarded:\n2 BLUE\n3 YELLOW") ||
                description.equals("The following cards will be discarded:\n3 YELLOW\n2 BLUE"));
    }
}