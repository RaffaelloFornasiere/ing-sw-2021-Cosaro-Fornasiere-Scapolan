package it.polimi.ingsw.events.ServerEvents;

import it.polimi.ingsw.events.Event;

public class ServerEvent extends Event {
    /**
     * constructor of the class
     *
     * @param playerId the player that generated(directly or indirectly) this event
     */
    public ServerEvent(String playerId) {
        super(playerId);
    }
}
