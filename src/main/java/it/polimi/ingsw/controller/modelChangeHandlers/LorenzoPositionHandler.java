package it.polimi.ingsw.controller.modelChangeHandlers;

import it.polimi.ingsw.client.Sender;
import it.polimi.ingsw.events.ClientEvents.LorenzoPositionEvent;
import it.polimi.ingsw.model.singlePlayer.SinglePlayerMatchState;

import java.util.HashMap;

public class LorenzoPositionHandler extends MatchObserver{

    private int lastPosition = 0;

    public LorenzoPositionHandler(HashMap<String, Sender> networkData) {
        super(networkData);
    }

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
