package it.polimi.ingsw.controller.modelChangeHandlers;

import it.polimi.ingsw.events.clientEvents.LobbyStateEvent;
import it.polimi.ingsw.model.Lobby;
import it.polimi.ingsw.utilities.Observer;
import it.polimi.ingsw.server.RequestsElaborator;

import java.util.HashMap;

/**
 * Observer for the Lobby
 */
public class LobbyHandler implements Observer {

    private final HashMap<String, RequestsElaborator> networkData;

    /**
     * Constructor for the class
     * @param networkData The network data of the players in the Lobby
     */
    public LobbyHandler(HashMap<String, RequestsElaborator> networkData){
        this.networkData = networkData;
    }

    /**
     * Sends to the players in the Lobby the state of it every time a player joins/leaves
     * @param o The Lobby that changed
     */
    @Override
    public void update(Object o) {
        Lobby lobby = (Lobby) o;
        LobbyStateEvent lobbyStateEvent = new LobbyStateEvent(lobby.getLeaderID(), lobby.getOtherPLayersID());

        RequestsElaborator lobbyLeaderRequestElaborator =  networkData.get(lobby.getLeaderID());
        if(lobbyLeaderRequestElaborator!=null){
            lobbyLeaderRequestElaborator.getClientHandlerSender().sendObject(lobbyStateEvent);
        }
        for(String playerID: lobby.getOtherPLayersID())
            networkData.get(playerID).getClientHandlerSender().sendObject(lobbyStateEvent);
    }

}
