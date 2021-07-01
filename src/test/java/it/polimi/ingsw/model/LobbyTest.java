package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.IllegalOperation;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class LobbyTest {

    @Test
    public void testConstructorException(){
        try{
            Lobby lobby = new Lobby(-9);
            fail();
        }
        catch (IllegalArgumentException ignore){}
    }

    @Test
    public void testGetSetCanAcceptPlayers() {
        Lobby lobby = new Lobby(4);
        assertTrue(lobby.canAcceptPlayers());
        lobby.setCanAcceptPlayers(false);
        assertFalse(lobby.canAcceptPlayers());
    }

    @Test
    public void testSetLeaderID() {
        Lobby lobby = new Lobby(4);
        lobby.setLeaderID("test");
        assertEquals("test", lobby.getLeaderID());
    }

    @Test
    public void testIsFullSuccessful() {
        Lobby lobby = new Lobby(4);
        lobby.setLeaderID("1");
        try {
            lobby.addPlayerID("2");
            lobby.addPlayerID("3");
            lobby.addPlayerID("4");
        } catch (IllegalOperation illegalOperation) {
            fail();
        }
        assertTrue(lobby.isFull());
    }

    @Test
    public void testIsFullUnsuccessful() {
        Lobby lobby = new Lobby(4);
        lobby.setLeaderID("1");
        try {
            lobby.addPlayerID("2");
        } catch (IllegalOperation illegalOperation) {
            fail();
        }
        assertFalse(lobby.isFull());
    }

    @Test
    public void testAddPlayerIDNoLeader() {
        Lobby lobby = new Lobby(4);
        try {
            lobby.addPlayerID("2");
        } catch (IllegalOperation illegalOperation) {
            fail();
        }
        assertEquals("2", lobby.getLeaderID());
        assertEquals(0, lobby.getOtherPLayersID().size());
    }

    @Test
    public void testAddPlayerIDYesLeader() {
        Lobby lobby = new Lobby(4);
        lobby.setLeaderID("1");
        try {
            lobby.addPlayerID("2");
        } catch (IllegalOperation illegalOperation) {
            fail();
        }
        assertTrue(lobby.getOtherPLayersID().contains("2"));
        assertEquals(1, lobby.getOtherPLayersID().size());
    }

    @Test
    public void testAddPlayerIDExceptionFull() {
        Lobby lobby = new Lobby(4);
        lobby.setLeaderID("1");
        try {
            lobby.addPlayerID("2");
            lobby.addPlayerID("3");
            lobby.addPlayerID("4");
        } catch (IllegalOperation illegalOperation) {
            fail();
        }
        try {
            lobby.addPlayerID("5");
            fail();
        } catch (IllegalOperation ignored) { }
    }

    @Test
    public void testAddPlayerIDExceptionCantAcceptPlayers() {
        Lobby lobby = new Lobby(4);
        lobby.setLeaderID("1");
        try {
            lobby.addPlayerID("2");
        } catch (IllegalOperation illegalOperation) {
            fail();
        }
        lobby.setCanAcceptPlayers(false);
        try {
            lobby.addPlayerID("3");
            fail();
        } catch (IllegalOperation ignored) { }
    }

    @Test
    public void testRemovePlayerNotPresent() {
        Lobby lobby = new Lobby(4);
        lobby.setLeaderID("1");
        try {
            lobby.addPlayerID("2");
            lobby.addPlayerID("3");
            lobby.addPlayerID("4");
        } catch (IllegalOperation illegalOperation) {
            fail();
        }
        assertEquals(4, lobby.removePlayer("5"));
        assertEquals("1", lobby.getLeaderID());
        ArrayList<String> otherPlayers = lobby.getOtherPLayersID();
        assertTrue(otherPlayers.contains("2"));
        assertTrue(otherPlayers.contains("3"));
        assertTrue(otherPlayers.contains("4"));
    }

    @Test
    public void testRemovePlayer() {
        Lobby lobby = new Lobby(4);
        lobby.setLeaderID("1");
        try {
            lobby.addPlayerID("2");
            lobby.addPlayerID("3");
            lobby.addPlayerID("4");
        } catch (IllegalOperation illegalOperation) {
            fail();
        }
        assertEquals(3, lobby.removePlayer("2"));
        assertEquals("1", lobby.getLeaderID());
        ArrayList<String> otherPlayers = lobby.getOtherPLayersID();
        assertFalse(otherPlayers.contains("2"));
        assertTrue(otherPlayers.contains("3"));
        assertTrue(otherPlayers.contains("4"));
    }

    @Test
    public void testRemovePlayerLeaderWithOtherPlayers() {
        Lobby lobby = new Lobby(4);
        lobby.setLeaderID("1");
        try {
            lobby.addPlayerID("2");
            lobby.addPlayerID("3");
            lobby.addPlayerID("4");
        } catch (IllegalOperation illegalOperation) {
            fail();
        }
        assertEquals(3, lobby.removePlayer("1"));
        assertNotNull(lobby.getLeaderID());
        ArrayList<String> players = lobby.getOtherPLayersID();
        players.add(lobby.getLeaderID());
        assertTrue(players.contains("2"));
        assertTrue(players.contains("3"));
        assertTrue(players.contains("4"));
    }

    @Test
    public void testRemovePlayerLeaderWithoutOtherPlayers() {
        Lobby lobby = new Lobby(4);
        lobby.setLeaderID("1");

        assertEquals(0, lobby.removePlayer("1"));
        assertNull(lobby.getLeaderID());
        assertEquals(0, lobby.getOtherPLayersID().size());
    }
}