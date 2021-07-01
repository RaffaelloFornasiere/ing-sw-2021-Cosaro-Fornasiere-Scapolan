package it.polimi.ingsw.events.clientEvents;

public class ServerDisconnectionEvent extends ClientEvent {
    /**
     * Constructor of the class
     * @param playerId The ID of the player that the event refers too
     */
    public ServerDisconnectionEvent(String playerId) {
        super(playerId);
    }
}
