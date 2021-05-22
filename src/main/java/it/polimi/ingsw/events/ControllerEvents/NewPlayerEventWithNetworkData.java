package it.polimi.ingsw.events.ControllerEvents;

import it.polimi.ingsw.virtualview.RequestsElaborator;

/**
 * Event decorator for NewPlayerEvent that adds network data for the player
 */
public class NewPlayerEventWithNetworkData extends NewPlayerEvent {
    private RequestsElaborator requestsElaborator;

    public NewPlayerEventWithNetworkData(NewPlayerEvent npe, RequestsElaborator requestsElaborator){
        super(npe.getPlayerId(), npe.getLobbyLeaderID());
        this.requestsElaborator = requestsElaborator;
    }


    public RequestsElaborator getRequestsElaborator() {
        return requestsElaborator;
    }
}
