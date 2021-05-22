package it.polimi.ingsw.events.ControllerEvents;

import it.polimi.ingsw.events.Event;

/**
 * Event sent by a lobby leader to start the game
 * Each player in the lobby should expect to receive an InitialChoicesEvent
 */
public class StartMatchEvent extends ControllerEvent {
    /**
     * constructor of the class
     *
     * @param playerId the player that generated(directly or indirectly) this event
     */
    public StartMatchEvent(String playerId) {
        super(playerId);
    }
}
