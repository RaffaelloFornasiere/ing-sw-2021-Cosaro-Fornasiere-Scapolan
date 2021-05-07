package it.polimi.ingsw.events;

public abstract class Event {
    int playerId;

    public Event(){}
    public int getPlayerId() {
        return playerId;
    }

    public String getEventName()
    {
        return this.getClass().getName();
    }

}
