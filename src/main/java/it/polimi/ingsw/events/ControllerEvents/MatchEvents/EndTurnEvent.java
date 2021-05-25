package it.polimi.ingsw.events.ControllerEvents.MatchEvents;

/**
 * Event sent to the server when a player wants to end its turn
 * The client will receive a BadRequestEvent if this event was mal-posed
 */
public class EndTurnEvent extends MatchEvent{

    /**
     * constructor for the class
     * @param playerID the player that generated(directly or indirectly) this event
     */
    public EndTurnEvent(String playerID) {
        super(playerID);
    }
}
