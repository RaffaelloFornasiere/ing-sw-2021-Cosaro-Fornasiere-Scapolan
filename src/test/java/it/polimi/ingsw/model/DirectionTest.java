package it.polimi.ingsw.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DirectionTest {

    @Test
    public void testGetDirection() {
        assertEquals("Row", Direction.ROW.getDirection());
    }

    @Test
    public void testToString() {
        assertEquals("ROW", Direction.ROW.toString());
    }
}