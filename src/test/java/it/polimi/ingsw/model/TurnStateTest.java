package it.polimi.ingsw.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class TurnStateTest {

    @Test
    public void getDescription() {
        assertEquals("is waiting for another player", TurnState.WAITING_FOR_PLAYER.getDescription());
    }

    @Test
    public void getStateCode() {
        assertEquals(1, TurnState.WAITING_FOR_PLAYER.getStateCode());
    }
}