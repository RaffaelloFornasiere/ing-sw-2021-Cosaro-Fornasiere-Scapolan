package it.polimi.ingsw.events.ControllerEvents;

/**
 * Event sent by a lobby leader to start the game
 * Each player in the lobby should expect to receive an InitialChoicesEvent
 */
public class StartMatchEvent extends ControllerEvent {
    /**
     * Constructor of the class
     * @param playerId The player that generated(directly or indirectly) this event
     */
    public StartMatchEvent(String playerId) {
        super(playerId);
    }
}
