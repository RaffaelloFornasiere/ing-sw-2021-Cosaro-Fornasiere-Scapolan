package it.polimi.ingsw.events.ClientEvents;

import it.polimi.ingsw.events.Event;

/**
 * Events that will be handled by the clients
 */
public abstract class ClientEvent extends Event {
    /**
     * Constructor of the class
     * @param playerId The ID of the player that the event refers too
     */
    public ClientEvent(String playerId) {
        super(playerId);
    }
}
