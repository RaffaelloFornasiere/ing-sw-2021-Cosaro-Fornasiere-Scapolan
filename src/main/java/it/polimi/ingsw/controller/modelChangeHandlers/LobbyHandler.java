package it.polimi.ingsw.controller.modelChangeHandlers;

import it.polimi.ingsw.events.ClientEvents.LobbyStateEvent;
import it.polimi.ingsw.model.Lobby;
import it.polimi.ingsw.utilities.Observer;
import it.polimi.ingsw.Server.RequestsElaborator;

import java.util.HashMap;

public class LobbyHandler implements Observer {

    private HashMap<String, RequestsElaborator> networkData;

    public LobbyHandler(HashMap<String, RequestsElaborator> networkData){
        this.networkData = networkData;
    }

    @Override
    public void update(Object o) {
        Lobby lobby = (Lobby) o;
        LobbyStateEvent lobbyStateEvent = new LobbyStateEvent(lobby.getLeaderID(), lobby.getOtherPLayersID());

        networkData.get(lobby.getLeaderID()).getClientHandlerSender().sendEvent(lobbyStateEvent);
        for(String playerID: lobby.getOtherPLayersID())
            networkData.get(playerID).getClientHandlerSender().sendEvent(lobbyStateEvent);
    }
}
