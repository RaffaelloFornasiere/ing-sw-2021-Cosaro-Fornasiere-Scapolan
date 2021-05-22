package it.polimi.ingsw.events.ControllerEvents.MatchEvents;

/**
 * Event sent to the server when a player wants to discard a leader card
 * The client will receive a BadRequestEvent if this event was mal-posed
 */
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
