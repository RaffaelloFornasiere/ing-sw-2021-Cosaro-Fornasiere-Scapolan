package it.polimi.ingsw.controller.modelChangeHandlers;

import it.polimi.ingsw.messageSenders.Sender;
import it.polimi.ingsw.events.ClientEvents.LorenzoPositionEvent;
import it.polimi.ingsw.model.singlePlayer.SinglePlayerMatchState;

import java.util.HashMap;

/**
 * Observer for the SinglePlayerMatchState
 */
public class LorenzoPositionHandler extends MatchObserver{

    private int lastPosition = 0;

    public LorenzoPositionHandler(HashMap<String, Sender> networkData) {
        super(networkData);
    }

    /**
     * Sends to all the player the position of Lorenzo il Magnifico in the faith track when it changes
     * @param o The SinglePlayerMatchState that changed
     */
    @Override
    public void update(Object o) {
        SinglePlayerMatchState singlePlayerMatchState = (SinglePlayerMatchState) o;
        int newPosition = singlePlayerMatchState.getLorenzoPosition();
        if(lastPosition!=newPosition){
            lastPosition = newPosition;
            sendToAll(new LorenzoPositionEvent(singlePlayerMatchState.getPlayer().getPlayerId(), newPosition));
        }
    }
}
