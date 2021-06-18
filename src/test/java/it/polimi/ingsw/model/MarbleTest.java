package it.polimi.ingsw.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class MarbleTest {

    @Test
    public void testToString() {
        assertEquals("Red", Marble.RED.getColor());
    }

    @Test
    public void getColor() {
        assertEquals("RED", Marble.RED.toString());
    }
}