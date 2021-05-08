package it.polimi.ingsw.controller;

import it.polimi.ingsw.events.NewPlayerEvent;
import it.polimi.ingsw.exceptions.IllegalOperation;
import it.polimi.ingsw.model.Lobby;
import it.polimi.ingsw.utilities.PropertyChangeSubject;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;

public class PreGameController {
    private Lobby fillingLobby;
    private ArrayList<Lobby> filledLobbies;

    public PreGameController(PropertyChangeSubject subject){
        subject.addPropertyChangeListener(NewPlayerEvent.class.getName(), this::NewPlayerEventHandler);
        this.fillingLobby = null;
        this.filledLobbies = new ArrayList<>();
    }

    public void NewPlayerEventHandler(PropertyChangeEvent evt){
        NewPlayerEvent event = (NewPlayerEvent) evt.getNewValue();

        if(fillingLobby == null){
            fillingLobby = new Lobby(event.getPlayerId());
        }

        try {
            fillingLobby.addPlayerID(event.getPlayerId());
        } catch (IllegalOperation illegalOperation) {
            filledLobbies.add(fillingLobby);
            fillingLobby = new Lobby(event.getPlayerId());
        }
    }
}
