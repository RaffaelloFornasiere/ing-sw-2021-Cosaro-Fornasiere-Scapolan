package it.polimi.ingsw.events.ControllerEvents.MatchEvents;

public class DiscardLeaderCardEvent extends MatchEvent{
    private String leaderCardID;

    public DiscardLeaderCardEvent(String playerID, String leaderCardID) {
        super(playerID);
        this.leaderCardID = leaderCardID;
    }

    public String getLeaderCardID() {
        return leaderCardID;
    }
}
