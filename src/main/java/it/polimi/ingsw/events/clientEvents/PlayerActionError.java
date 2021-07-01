package it.polimi.ingsw.events.clientEvents;

import it.polimi.ingsw.events.Event;

/**
 * Event sent when an action the player wants to do cannot be done
 */
public class PlayerActionError extends ClientEvent{
    private final String description;
    private final Event cause;

    /**
     * Constructor for the class
     * @param playerID The ID of the player that generated the event that can't be done
     * @param description Description of why the request can't be done
     * @param cause The event that can't be done
     */
    public PlayerActionError(String playerID, String description, Event cause){
        super(playerID);
        this.description = description;
        this.cause = cause;
    }

    /**
     * Getter for the description that explains why the request can't be done
     * @return The description that explains why the request can't be done
     */
    public String getDescription() {
        return description;
    }

    /**
     * Getter for the event that can't be done
     * @return The event that can't be done
     */
    public Event getCause(){
        return cause;
    }
}
