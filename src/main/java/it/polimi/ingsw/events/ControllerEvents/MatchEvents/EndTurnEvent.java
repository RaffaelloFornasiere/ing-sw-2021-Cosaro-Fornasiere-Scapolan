package it.polimi.ingsw.events.ControllerEvents.MatchEvents;

public class EndTurnEvent extends MatchEvent{

    /**
     * constructor for the class
     *
     * @param playerID the player that generated(directly or indirectly) this event
     */
    public EndTurnEvent(String playerID) {
        super(playerID);
    }
}
