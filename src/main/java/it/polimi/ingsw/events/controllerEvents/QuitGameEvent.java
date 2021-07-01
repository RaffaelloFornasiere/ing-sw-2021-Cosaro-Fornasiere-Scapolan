package it.polimi.ingsw.events.controllerEvents;

/**
 * Event sent to the server when the player quits the game
 */
public class QuitGameEvent extends ControllerEvent {

    /**
     * Constructor of the class
     * @param playerId The player that generated(directly or indirectly) this event
     */
    public QuitGameEvent(String playerId) {
        super(playerId);
    }
}
