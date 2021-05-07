package it.polimi.ingsw.events;

public abstract class Event {
    String playerId;

    public Event() {
    }

    public String getPlayerId() {
        return playerId;
    }

    public String getEventName() {
        return this.getClass().getSimpleName();
    }

}
