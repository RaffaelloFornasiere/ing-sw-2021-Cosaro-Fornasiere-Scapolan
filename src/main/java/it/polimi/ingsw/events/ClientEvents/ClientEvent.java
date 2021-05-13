package it.polimi.ingsw.events.ClientEvents;

import it.polimi.ingsw.events.Event;

public abstract class ClientEvent extends Event {
    /**
     * constructor of the class
     *
     * @param playerId the player that generated(directly or indirectly) this event
     */
    public ClientEvent(String playerId) {
        super(playerId);
    }
}
