package it.polimi.ingsw.events;

public class QueueStopEvent extends Event{
    /**
     * Constructor of the class
     *
     * @param playerId The player that generated(directly or indirectly) this event
     */
    public QueueStopEvent(String playerId) {
        super(playerId);
    }
}
