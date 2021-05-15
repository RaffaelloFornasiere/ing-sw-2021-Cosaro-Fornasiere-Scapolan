package it.polimi.ingsw.events.ClientEvents;

public class NewTurnEvent extends ClientEvent{
    private boolean lastRound;

    public NewTurnEvent(String playerId, boolean lastRound) {
        super(playerId);
        this.lastRound = lastRound;
    }

    public boolean isLastRound() {
        return lastRound;
    }
}
