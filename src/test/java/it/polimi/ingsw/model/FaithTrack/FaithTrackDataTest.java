package it.polimi.ingsw.model.FaithTrack;

import it.polimi.ingsw.exceptions.OutOfBoundException;
import junit.framework.TestCase;
import org.junit.Test;

import java.util.ArrayList;

public class FaithTrackDataTest extends TestCase {
    /**
     * This method tests the correctness of adding PopeFavorCard to the FaithTrackData
     */

    @Test
    public void testAddPopeFavorCard() {
        ArrayList<CellWithEffect> effects = new ArrayList<>();
        ArrayList<Integer> points = new ArrayList<>(3);
        points.add(0, 1);
        points.add(1, 2);
        points.add(2, 3);
        assertEquals(3, points.size());
        FaithTrack faithTrack = FaithTrack.initFaithTrack(3, effects, points);
        FaithTrackData ftd = new FaithTrackData(faithTrack);
        PopeFavorCard card = new PopeFavorCard(3);
        ftd.addPopeFavorCard(0, card);
        assertEquals(3, ftd.getAcquiredPopeFavorCards().get(0).getVictoryPoints());
        assertEquals(0, ftd.getPosition());
    }
    /**
     * This method tests the correctness of computing the total amount of points from the PopeFavorCards
     */
    @Test
    public void testGetFavorPopeCardPoints() {
        ArrayList<CellWithEffect> effects = new ArrayList<>();
        ArrayList<Integer> points = new ArrayList<>(3);
        points.add(0, 1);
        points.add(1, 2);
        points.add(2, 3);
        assertEquals(3, points.size());
        FaithTrack faithTrack = FaithTrack.initFaithTrack(3, effects, points);
        FaithTrackData ftd = new FaithTrackData(faithTrack);
        PopeFavorCard card1 = new PopeFavorCard(3);
        PopeFavorCard card2 = new PopeFavorCard(1);
        PopeFavorCard card3 = new PopeFavorCard(2);
        ftd.addPopeFavorCard(1,card1);
        ftd.addPopeFavorCard(2, card2);
        ftd.addPopeFavorCard(3,card3);

        assertEquals(6, ftd.getFavorPopeCardPoints());
    }
    /**
     * This method tests the correctness of incrementing  the position in the FaithTrackData
     */
    @Test
    public void testIncrementPositionSuccessful() {
        ArrayList<CellWithEffect>effects = new ArrayList<>();
        ArrayList<Integer> points = new ArrayList<>(3);
        points.add(0, 1);
        points.add(1, 2);
        points.add(2, 3);
        assertEquals(3, points.size());
        FaithTrack faithTrack = FaithTrack.initFaithTrack(3, effects, points);
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
        ArrayList<CellWithEffect> effects = new ArrayList<>();
        ArrayList<Integer> points = new ArrayList<>(3);
        points.add(0, 1);
        points.add(1, 2);
        assertEquals(2, points.size());
        FaithTrack faithTrack = FaithTrack.initFaithTrack(2, effects, points);
        FaithTrackData ftd = new FaithTrackData(faithTrack);
        try {
            ftd.incrementPosition(6); fail();
        } catch (OutOfBoundException e) {
        }

    }
}