package it.polimi.ingsw.events;

public abstract class Event {
    protected String playerId;

    /**
     * constructor of the class
     * @param playerId the player that generated(directly or indirectly) this event
     */
    public Event(String playerId) {
        this.playerId = playerId;
    }

    /**
     * the getter for the player that generated(directly or indirectly) this event
     * @return the player that generated(directly or indirectly) this event
     */
    public String getPlayerId() {
        return playerId;
    }

    /**
     * gets the event name
     * @return the event name
     */
    public String getEventName() {
        return this.getClass().getSimpleName();
    }

}
