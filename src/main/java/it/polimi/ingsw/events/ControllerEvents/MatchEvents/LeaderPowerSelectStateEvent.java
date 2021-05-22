package it.polimi.ingsw.events.ControllerEvents.MatchEvents;

/**
 * Event sent to the server when a player wants to select or deselect a leader power
 * The client will receive a BadRequestEvent if this event was mal-posed
 * The client will receive a LeaderCardNotActiveError if the leader card with the leader power is not active
 * The client will receive a IncompatiblePowersError if there's another power which is incompatible is already selected
 */
public class LeaderPowerSelectStateEvent extends MatchEvent {

    private String leaderCardID;
    private int leaderPowerIndex;
    private boolean selectState;

    /**
     * Constructor for the class
     * @param playerID the player that generated(directly or indirectly) this event
     * @param leaderCardID the ID of the leader card containing the power in question
     * @param leaderPowerIndex the index of the power in question
     * @param selectState the select state of the power
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
     * getter for the index of the leader card containing the power in question
     * @return the index of the leader card containing the power in question
     */
    public int getLeaderPowerIndex() {
        return leaderPowerIndex;
    }

    /**
     * getter for the ID of the power in question
     * @return the ID of the power in question
     */
    public String getLeaderCardID() {
        return leaderCardID;
    }

    /**
     * @return the select state of the power
     */
    public boolean isStateSelected() {
        return selectState;
    }
}
