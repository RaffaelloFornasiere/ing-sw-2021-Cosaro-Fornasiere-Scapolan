package it.polimi.ingsw.controller;

import it.polimi.ingsw.events.BadRequestEvent;
import it.polimi.ingsw.events.NewPlayerEvent;
import it.polimi.ingsw.events.NewPlayerEventWithNetworkData;
import it.polimi.ingsw.exceptions.IllegalOperation;
import it.polimi.ingsw.model.Lobby;
import it.polimi.ingsw.utilities.PropertyChangeSubject;
import it.polimi.ingsw.virtualview.RequestsElaborator;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.HashMap;

public class PreGameController {
    private ArrayList<Lobby> fillingLobbies;
    private HashMap<String, RequestsElaborator> networkData;

    public PreGameController(PropertyChangeSubject subject){
        subject.addPropertyChangeListener(NewPlayerEventWithNetworkData.class.getSimpleName(), this::NewPlayerEventHandler);
        this.fillingLobbies = new ArrayList<>();
        this.networkData = new HashMap<>();
    }

    public void NewPlayerEventHandler(PropertyChangeEvent evt){
        NewPlayerEventWithNetworkData event = (NewPlayerEventWithNetworkData) evt.getNewValue();

        if(networkData.containsKey(event.getPlayerId())){
            event.getRequestsElaborator().getClientHandlerSender().sendEvent(new BadRequestEvent(event.getPlayerId(),
                    "Username already taken", new NewPlayerEvent(event.getPlayerId(), event.getLobbyLeaderID())));
            return;
        }

        if(event.getPlayerId().equals(event.getLobbyLeaderID())){
            fillingLobbies.add(new Lobby(event.getPlayerId()));
            networkData.put(event.getPlayerId(), event.getRequestsElaborator());
            return;
        }

        int lobbyIndex = searchLobby(event.getLobbyLeaderID());
        if(lobbyIndex == -1)
            event.getRequestsElaborator().getClientHandlerSender().sendEvent(new BadRequestEvent(event.getPlayerId(),
                    "No lobby with the given Leader", new NewPlayerEvent(event.getPlayerId(), event.getLobbyLeaderID())));
        else{
            try {
                fillingLobbies.get(lobbyIndex).addPlayerID(event.getPlayerId());
                networkData.put(event.getPlayerId(), event.getRequestsElaborator());
            } catch (IllegalOperation illegalOperation) {
                event.getRequestsElaborator().getClientHandlerSender().sendEvent(new BadRequestEvent(event.getPlayerId(),
                        "The lobby is full", new NewPlayerEvent(event.getPlayerId(), event.getLobbyLeaderID())));
            }
        }
    }

    private int searchLobby(String lobbyLeaderID) {
        for(int i = 0; i<fillingLobbies.size(); i++)
            if(fillingLobbies.get(i).getLeaderID().equals(lobbyLeaderID))
                return i;

        return -1;
    }
}
