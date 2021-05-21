package it.polimi.ingsw.controller.modelChangeHandlers;

import it.polimi.ingsw.events.ClientEvents.MatchStateEvent;
import it.polimi.ingsw.model.MatchState;
import it.polimi.ingsw.virtualview.ClientHandlerSender;

import java.util.HashMap;

public class MatchStateHandler extends MatchObserver{

    public MatchStateHandler(HashMap<String, ClientHandlerSender> networkData) {
        super(networkData);
    }

    @Override
    public void update(Object o) {
        MatchState matchState = (MatchState) o;
        sendToAll(new MatchStateEvent(matchState.getPlayers().get(matchState.getCurrentPlayerIndex()).getPlayerId(), matchState.isLastRound(), matchState.getTurnState()));
    }
}
