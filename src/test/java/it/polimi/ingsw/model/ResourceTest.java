package it.polimi.ingsw.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class ResourceTest {

    @Test
    public void getResourceCode() {
        assertEquals(1, Resource.COIN.getResourceCode());
    }
}