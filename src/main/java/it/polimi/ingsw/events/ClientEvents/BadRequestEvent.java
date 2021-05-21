package it.polimi.ingsw.events.ClientEvents;

import it.polimi.ingsw.events.Event;

/**
 * Event sent to the client when a previously sent request was malformed
 */
public class BadRequestEvent extends ClientEvent {
    private final String description;
    private final Event cause;

    /**
     * Constructor for the class
     * @param playerID The ID of the player that generated the event that was miss constructed
     * @param description Description of why the request is bad
     * @param cause The event that was miss constructed
     */
    public BadRequestEvent(String playerID, String description, Event cause){
        super(playerID);
        this.description = description;
        this.cause = cause;
    }

    /**
     * Getter for the description that explains why the request is bad
     * @return The description that explains why the request is bad
     */
    public String getDescription() {
        return description;
    }

    /**
     * Getter for the event that was miss constructed
     * @return The event that was miss constructed
     */
    public Event getCause(){
        return cause;
    }
}
