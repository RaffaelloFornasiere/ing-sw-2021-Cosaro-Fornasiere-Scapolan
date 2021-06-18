package it.polimi.ingsw.model.FaithTrack;

import it.polimi.ingsw.exceptions.OutOfBoundException;
import it.polimi.ingsw.utilities.Config;
import org.junit.Assert;
import org.junit.Test;

public class FaithTrackDataTest {
    /**
     * This method tests the correctness of adding PopeFavorCard to the FaithTrackData
     */

    @Test
    public void testAddPopeFavorCard() {
        FaithTrack.initFaithTrack(Config.getDefaultConfig().getFaithTrack());
        FaithTrackData ftd = new FaithTrackData();
        PopeFavorCard card = new PopeFavorCard(3);
        try {
            ftd.addPopeFavorCard(8, card);
            Assert.assertEquals(3, ftd.getFavorPopeCardPoints());
            Assert.assertEquals(0, ftd.getPosition());
        } catch (IllegalArgumentException e) {
            Assert.fail();
        }
    }

    @Test
    public void testAddPopeFavorCardUnsuccessful() {
        FaithTrack.initFaithTrack(Config.getDefaultConfig().getFaithTrack());
        FaithTrackData ftd = new FaithTrackData();
        PopeFavorCard card = new PopeFavorCard(3);
        try {
            ftd.addPopeFavorCard(2, card);
            Assert.fail();

        } catch (IllegalArgumentException e) {

        }
    }

    /**
     * This method tests the correctness of computing the total amount of points from the PopeFavorCards
     */
    @Test
    public void testGetFavorPopeCardPoints() {
        FaithTrack.initFaithTrack(Config.getDefaultConfig().getFaithTrack());
        FaithTrackData ftd = new FaithTrackData();
        PopeFavorCard card1 = new PopeFavorCard(3);
        PopeFavorCard card2 = new PopeFavorCard(1);
        PopeFavorCard card3 = new PopeFavorCard(2);
        try {
            ftd.addPopeFavorCard(8, card1);
            ftd.addPopeFavorCard(16, card2);
            ftd.addPopeFavorCard(24, card3);
            Assert.assertEquals(6, ftd.getFavorPopeCardPoints());

        }catch (IllegalArgumentException e){Assert.fail();}

    }

    /**
     * This method tests the correctness of incrementing  the position in the FaithTrackData
     */
    @Test
    public void testIncrementPositionSuccessful() {
        FaithTrack.initFaithTrack(Config.getDefaultConfig().getFaithTrack());
        FaithTrackData ftd = new FaithTrackData();
        try {
            ftd.incrementPosition(24);
            Assert.assertEquals(24, ftd.getPosition());


        } catch (OutOfBoundException e) {
            Assert.fail();
        }
    }

    /**
     * This method tests the correctness of incrementing  the position in the FaithTrackData, in case a wrong parameter is passed.
     */
    @Test
    public void testIncrementPositionUnsuccessful() {
        FaithTrack.initFaithTrack(Config.getDefaultConfig().getFaithTrack());
        FaithTrackData ftd = new FaithTrackData();
        try {
            ftd.incrementPosition(24);
            Assert.assertEquals(24, ftd.getPosition());

        } catch (OutOfBoundException e) {
            Assert.fail();
        }
    }

    @Test
    public void testGetPosition() {
        FaithTrack.initFaithTrack(Config.getDefaultConfig().getFaithTrack());
        FaithTrackData ftd = new FaithTrackData();
        try {
            ftd.setPosition(24);
            Assert.assertEquals(24, ftd.getPosition());
        } catch (OutOfBoundException e) {
            Assert.fail();
        }

    }

    @Test
    public void testSetPosition() {
        FaithTrack.initFaithTrack(Config.getDefaultConfig().getFaithTrack());
        FaithTrackData ftd = new FaithTrackData();
        try {
            ftd.setPosition(24);
            Assert.assertEquals(24, ftd.getPosition());
        } catch (OutOfBoundException e) {
            Assert.fail();
        }
    }

    @Test
    public void testSetPositionUnsuccessful() {
        FaithTrack.initFaithTrack(Config.getDefaultConfig().getFaithTrack());
        FaithTrackData ftd = new FaithTrackData();
        try {
            ftd.setPosition(25);
            Assert.fail();
        } catch (OutOfBoundException e) {
        }
    }


    @Test
    public void testGetAcquiredPopeFavorCards() {
        FaithTrack.initFaithTrack(Config.getDefaultConfig().getFaithTrack());
        FaithTrackData ftd = new FaithTrackData();
        PopeFavorCard card1 = new PopeFavorCard(3);
        PopeFavorCard card2 = new PopeFavorCard(1);
        PopeFavorCard card3 = new PopeFavorCard(2);
        try {
            ftd.addPopeFavorCard(8, card1);
            ftd.addPopeFavorCard(16, card2);
            ftd.addPopeFavorCard(24, card3);
            Assert.assertEquals(card2, ftd.getAcquiredPopeFavorCards().get(16));
        }catch(IllegalArgumentException e){ Assert.fail();}

    }

}