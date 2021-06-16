package it.polimi.ingsw.controller.modelChangeHandlers;

import it.polimi.ingsw.client.Sender;
import it.polimi.ingsw.events.ClientEvents.MatchStateEvent;
import it.polimi.ingsw.model.MatchState;
import it.polimi.ingsw.Server.ClientHandlerSender;
import it.polimi.ingsw.model.TurnState;

import java.util.HashMap;

public class MatchStateHandler extends MatchObserver{

    private TurnState oldTurnState = null;

    public MatchStateHandler(HashMap<String, Sender> networkData) {
        super(networkData);
    }

    @Override
    public void update(Object o) {
        MatchState matchState = (MatchState) o;
        TurnState newTurnState = matchState.getTurnState();
        if(oldTurnState==null || oldTurnState!=newTurnState){
            oldTurnState = newTurnState;
            sendToAll(new MatchStateEvent(matchState.getPlayers().get(matchState.getCurrentPlayerIndex()).getPlayerId(), matchState.isLastRound(), newTurnState));
        }
    }
}
