package it.polimi.ingsw.model.FaithTrack;

import it.polimi.ingsw.controller.EffectOfCell;
import it.polimi.ingsw.exceptions.OutOfBoundException;
import junit.framework.TestCase;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

public class FaithTrackDataTest extends TestCase {
    /**
     * This method tests the correctness of adding PopeFavorCard to the FaithTrackData
     */

    @Test
    public void testAddPopeFavorCard() {
        HashMap<Integer, EffectOfCell> effects = new HashMap<>();
        ArrayList<Integer> points = new ArrayList<>(3);
        points.add(0, 1);
        points.add(1, 2);
        points.add(2, 3);
        FaithTrack faithTrack = new FaithTrack(3, effects, points);
        FaithTrackData ftd = new FaithTrackData(faithTrack);
        PopeFavorCard card = new PopeFavorCard(3);
        ftd.addPopeFavorCard(card);
        ArrayList<PopeFavorCard> expectedListOfPopeFarorCard = new ArrayList<>();
        expectedListOfPopeFarorCard.add(card);
        assertEquals(expectedListOfPopeFarorCard, ftd.getAcquiredPopeFavorCards());
    }
    /**
     * This method tests the correctness of computing the total amount of points from the PopeFavorCards
     */
    @Test
    public void testGetFavorPopeCardPoints() {
        HashMap<Integer, EffectOfCell> effects = new HashMap<>();
        ArrayList<Integer> points = new ArrayList<>(3);
        points.add(0, 1);
        points.add(1, 2);
        points.add(2, 3);
        FaithTrack faithTrack = new FaithTrack(3, effects, points);
        FaithTrackData ftd = new FaithTrackData(faithTrack);
        PopeFavorCard card1 = new PopeFavorCard(3);
        PopeFavorCard card2 = new PopeFavorCard(1);
        PopeFavorCard card3 = new PopeFavorCard(2);
        ftd.addPopeFavorCard(card1);
        ftd.addPopeFavorCard(card2);
        ftd.addPopeFavorCard(card3);

        assertEquals(6, ftd.getFavorPopeCardPoints());
    }
    /**
     * This method tests the correctness of incrementing  the position in the FaithTrackData
     */
    @Test
    public void testIncrementPositionSuccessful() {
        HashMap<Integer, EffectOfCell> effects = new HashMap<>();
        ArrayList<Integer> points = new ArrayList<>(3);
        points.add(0, 1);
        points.add(1, 2);
        points.add(2, 3);
        FaithTrack faithTrack = new FaithTrack(3, effects, points);
        FaithTrackData ftd = new FaithTrackData(faithTrack);
        try {
            ftd.incrementPosition(2);
        } catch (OutOfBoundException e) {
            fail();
        }
        assertEquals(2, ftd.getPosition());
    }
    /**
     * This method tests the correctness of incrementing  the position in the FaithTrackData, in case a wrong parameter is passed.
     */
    @Test
    public void testIncrementPositionUnsuccessful() {
        HashMap<Integer, EffectOfCell> effects = new HashMap<>();
        ArrayList<Integer> points = new ArrayList<>(3);
        points.add(0, 1);
        points.add(1, 2);
        points.add(2, 3);
        FaithTrack faithTrack = new FaithTrack(3, effects, points);
        FaithTrackData ftd = new FaithTrackData(faithTrack);
        try {
            ftd.incrementPosition(6); fail();
        } catch (OutOfBoundException e) {
        }

    }
}