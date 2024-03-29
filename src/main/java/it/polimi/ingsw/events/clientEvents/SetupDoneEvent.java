package it.polimi.ingsw.events.clientEvents;

/**
 * Event sent to the client once its setup has benn completed
 */
public class SetupDoneEvent extends ClientEvent{
    /**
     * Constructor of the class
     * @param playerId The ID of the player that had their setup finished
     */
    public SetupDoneEvent(String playerId) {
        super(playerId);
    }
}
