package it.polimi.ingsw.events;

public abstract class Event {
    String eventType;

    Event(String eventType){this.eventType = eventType;}


    public String getEventType() {
        return eventType;
    }

}
