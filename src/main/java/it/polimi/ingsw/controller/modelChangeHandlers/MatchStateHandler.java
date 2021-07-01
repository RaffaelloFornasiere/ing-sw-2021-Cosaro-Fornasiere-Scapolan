package it.polimi.ingsw.controller.modelChangeHandlers;

import it.polimi.ingsw.client.Sender;
import it.polimi.ingsw.events.ClientEvents.MatchStateEvent;
import it.polimi.ingsw.model.MatchState;
import it.polimi.ingsw.model.TurnState;

import java.util.HashMap;

/**
 * Observer for the MatchState
 */
public class MatchStateHandler extends MatchObserver{

    private TurnState oldTurnState = null;
    private int oldPlayerIndex = 0;

    public MatchStateHandler(HashMap<String, Sender> networkData) {
        super(networkData);
    }

    /**
     * sends to all the player the new match state every time the turn state changes
     * @param o The MatchState that changed
     */
    @Override
    public void update(Object o) {
        MatchState matchState = (MatchState) o;
        TurnState newTurnState = matchState.getTurnState();
        int newPlayerIndex = matchState.getCurrentPlayerIndex();
        if(oldTurnState==null || oldTurnState!=newTurnState || oldPlayerIndex!=newPlayerIndex){
            oldTurnState = newTurnState;
            oldPlayerIndex = newPlayerIndex;
            sendToAll(new MatchStateEvent(matchState.getPlayers().get(matchState.getCurrentPlayerIndex()).getPlayerId(), matchState.isLastRound(), newTurnState));
        }
    }
}
