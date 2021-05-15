package it.polimi.ingsw.controller.modelChangeHandlers;

import it.polimi.ingsw.events.ClientEvents.NewTurnEvent;
import it.polimi.ingsw.model.MatchState;
import it.polimi.ingsw.virtualview.RequestsElaborator;

import java.util.HashMap;

public class MatchStateHandler extends MatchObserver{

    public MatchStateHandler(HashMap<String, RequestsElaborator> networkData) {
        super(networkData);
    }

    @Override
    public void update(Object o) {
        MatchState matchState = (MatchState) o;
        sendToAll(new NewTurnEvent(matchState.getPlayers().get(matchState.getCurrentPlayerIndex()).getPlayerId(), matchState.isLastRound()));
    }
}
