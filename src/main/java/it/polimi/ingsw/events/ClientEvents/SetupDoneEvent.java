package it.polimi.ingsw.events.ClientEvents;

public class SetupDoneEvent extends ClientEvent{
    /**
     * constructor of the class
     *
     * @param playerId the player that generated(directly or indirectly) this event
     */
    public SetupDoneEvent(String playerId) {
        super(playerId);
    }
}
