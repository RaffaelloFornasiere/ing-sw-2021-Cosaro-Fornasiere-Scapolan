package it.polimi.ingsw.events.ClientEvents;

import it.polimi.ingsw.model.TurnState;

public class MatchStateEvent extends ClientEvent{
    private boolean lastRound;
    private TurnState turnState;

    public MatchStateEvent(String playerId, boolean lastRound, TurnState turnState) {
        super(playerId);
        this.lastRound = lastRound;
        this.turnState = turnState;
    }

    public boolean isLastRound() {
        return lastRound;
    }

    public TurnState getTurnState() {
        return turnState;
    }
}
