package it.polimi.ingsw.events.ClientEvents;

/**
 * Event sent to the client when Lorenzo il Magnifico won the single player game
 */
public class SinglePlayerLostEvent extends ClientEvent{
    /**
     * Constructor of the class
     * @param playerId The ID of the player that the event refers too
     */
    public SinglePlayerLostEvent(String playerId) {
        super(playerId);
    }
}
