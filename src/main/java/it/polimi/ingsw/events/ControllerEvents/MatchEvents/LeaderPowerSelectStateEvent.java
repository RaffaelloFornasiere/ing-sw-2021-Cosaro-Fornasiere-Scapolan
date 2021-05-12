package it.polimi.ingsw.events.ControllerEvents.MatchEvents;

public class LeaderPowerSelectStateEvent extends MatchEvent {

    private int leaderCardIndex;
    private int leaderPowerIndex;
    private boolean selectState;

    /**
     * Constructor for the class
     * @param playerID the player that generated(directly or indirectly) this event
     * @param leaderCardIndex the index of the leader card containing the power in question
     * @param leaderPowerIndex the index of the power in question
     * @param selectState the select state of the power
     * @throws IllegalArgumentException if the indexes are negative
     */
    public LeaderPowerSelectStateEvent(String playerID, int leaderCardIndex, int leaderPowerIndex, boolean selectState) throws IllegalArgumentException{
        super(playerID);
        if(leaderCardIndex<0 || leaderPowerIndex<0)
            throw new IllegalArgumentException("Indexes must be positive");
        this.leaderCardIndex = leaderCardIndex;
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
     * getter for the index of the power in question
     * @return the index of the power in question
     */
    public int getLeaderCardIndex() {
        return leaderCardIndex;
    }

    /**
     * @return the select state of the power
     */
    public boolean isStateSelected() {
        return selectState;
    }
}
