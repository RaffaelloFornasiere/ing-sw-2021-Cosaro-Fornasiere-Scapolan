package it.polimi.ingsw.model.faithTrack;

import junit.framework.TestCase;
import org.junit.Assert;

public class PopeFavorCardTest extends TestCase {

    public void testGetVictoryPoints() {
        PopeFavorCard card= new PopeFavorCard(5);
        Assert.assertEquals(5,card.getVictoryPoints());
    }
}