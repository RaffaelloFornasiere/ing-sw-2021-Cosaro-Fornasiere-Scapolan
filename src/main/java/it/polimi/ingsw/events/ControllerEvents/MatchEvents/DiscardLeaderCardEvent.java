package it.polimi.ingsw.events.ControllerEvents.MatchEvents;

/**
 * Event sent to the server when a player wants to discard a leader card
 * The client will receive a BadRequestEvent if this event was mal-posed
 * The client will receive a PlayerActionError if the action represented by this event can't be done
 */
public class DiscardLeaderCardEvent extends MatchEvent{
    private final String leaderCardID;

    /**
     * Constructor for the class
     * @param playerID The ID of the player that generated(directly or indirectly) this event
     * @param leaderCardID The ID of the leader card to discard
     */
    public DiscardLeaderCardEvent(String playerID, String leaderCardID) {
        super(playerID);
        this.leaderCardID = leaderCardID;
    }

    /**
     * Getter for the ID of the leader card to discard
     * @return The ID of the leader card to discard
     */
    public String getLeaderCardID() {
        return leaderCardID;
    }
}
