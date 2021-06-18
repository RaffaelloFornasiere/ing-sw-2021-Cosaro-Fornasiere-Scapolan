package it.polimi.ingsw.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class CardColorTest {

    @Test
    public void testToString() {
        assertEquals("YELLOW", CardColor.YELLOW.toString());
    }

    @Test
    public void getCode() {
        assertEquals(3, CardColor.YELLOW.getCode());
    }

    @Test
    public void getColor() {
        assertEquals("Yellow", CardColor.YELLOW.getColor());
    }
}