package it.polimi.ingsw.events;

public abstract class Event {
    String eventType;



    int playerId;

    Event(String eventType){this.eventType = eventType;}


    public String getEventType() {
        return eventType;
    }
    public int getPlayerId() {
        return playerId;
    }

}
