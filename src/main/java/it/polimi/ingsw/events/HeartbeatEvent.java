package it.polimi.ingsw.events;

public class HeartbeatEvent extends Event{
    /**
     * Constructor of the class
     *
     * @param playerId The player that generated(directly or indirectly) this event
     */
    public HeartbeatEvent(String playerId) {
        super(playerId);
    }
}
