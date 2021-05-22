package it.polimi.ingsw.events.ControllerEvents;

import it.polimi.ingsw.virtualview.RequestsElaborator;

/**
 * Event decorator for NewPlayerEvent that adds network data for the player
 */
public class NewPlayerEventWithNetworkData extends NewPlayerEvent {
    private final RequestsElaborator requestsElaborator;

    /**
     * Constructor for the class
     * @param npe The new player event to decorate
     * @param requestsElaborator The network information related to the new player
     */
    public NewPlayerEventWithNetworkData(NewPlayerEvent npe, RequestsElaborator requestsElaborator){
        super(npe.getPlayerId(), npe.getLobbyLeaderID());
        this.requestsElaborator = requestsElaborator;
    }


    /**
     * Getter for the network information related to the new player
     * @return The network information related to the new player
     */
    public RequestsElaborator getRequestsElaborator() {
        return requestsElaborator;
    }
}
