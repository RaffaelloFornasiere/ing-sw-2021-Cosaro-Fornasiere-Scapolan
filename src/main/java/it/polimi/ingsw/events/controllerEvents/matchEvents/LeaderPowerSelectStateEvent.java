package it.polimi.ingsw.events.controllerEvents.matchEvents;

/**
 * Event sent to the server when a player wants to select or deselect a leader power
 * The client will receive a BadRequestEvent if this event was mal-posed
 * The client will receive a PlayerActionError if the action represented by this event can't be done
 * The client will receive a LeaderCardNotActiveError if the leader card with the leader power is not active
 * The client will receive a IncompatiblePowersError if there's another power which is incompatible is already selected
 */
public class LeaderPowerSelectStateEvent extends MatchEvent {

    private final String leaderCardID;
    private final int leaderPowerIndex;
    private final boolean selectState;

    /**
     * Constructor for the class
     * @param playerID The player that generated(directly or indirectly) this event
     * @param leaderCardID The ID of the leader card containing the power in question
     * @param leaderPowerIndex The index of the power in question
     * @param selectState The new select state of the power
     * @throws IllegalArgumentException if the indexes are negative
     */
    public LeaderPowerSelectStateEvent(String playerID, String leaderCardID, int leaderPowerIndex, boolean selectState) throws IllegalArgumentException{
        super(playerID);
        if(leaderPowerIndex<0)
            throw new IllegalArgumentException("Indexes must be positive");
        this.leaderCardID = leaderCardID;
        this.leaderPowerIndex = leaderPowerIndex;
        this.selectState = selectState;
    }

    /**
     * Getter for the index of the leader card containing the power in question
     * @return The index of the leader card containing the power in question
     */
    public int getLeaderPowerIndex() {
        return leaderPowerIndex;
    }

    /**
     * Getter for the ID of the power in question
     * @return The ID of the power in question
     */
    public String getLeaderCardID() {
        return leaderCardID;
    }

    /**
     * @return The new select state of the power
     */
    public boolean isStateSelected() {
        return selectState;
    }
}
