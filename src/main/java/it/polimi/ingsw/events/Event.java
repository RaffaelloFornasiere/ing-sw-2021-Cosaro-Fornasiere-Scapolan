package it.polimi.ingsw.events;

public abstract class Event {
    protected String playerId;

    /**
     * Constructor of the class
     * @param playerId The player that generated(directly or indirectly) this event
     */
    public Event(String playerId) {
        this.playerId = playerId;
    }

    /**
     * The getter for the player that generated(directly or indirectly) this event
     * @return The player that generated(directly or indirectly) this event
     */
    public String getPlayerId() {
        return playerId;
    }

    /**
     * The getter for the event name
     * @return The event name
     */
    public String getEventName() {
        return this.getClass().getSimpleName();
    }

}
