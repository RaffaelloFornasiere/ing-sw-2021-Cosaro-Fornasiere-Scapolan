package it.polimi.ingsw.events;

public class IncompatiblePowersError extends Event {
    private int leaderCardIndex;
    private int leaderPowerIndex;

    /**
     * Constructor for the class
     * @param playerID the player that generated(directly or indirectly) this event
     * @param leaderCardIndex the index of the leader card containing the power which is incompatible with other selected powers
     * @param leaderPowerIndex the index of the power which is incompatible with other selected powers
     * @throws IllegalArgumentException if the indexes are negative
     */
    public IncompatiblePowersError(String playerID, int leaderCardIndex, int leaderPowerIndex) throws IllegalArgumentException{
        super(playerID);
        if(leaderCardIndex<0 || leaderPowerIndex<0)
            throw new IllegalArgumentException("Indexes must be positive");
        this.leaderCardIndex = leaderCardIndex;
        this.leaderPowerIndex = leaderPowerIndex;
    }

    /**
     * getter for the index of the leader card containing the power which is incompatible with other selected powers
     * @return the index of the leader card containing the power which is incompatible with other selected powers
     */
    public int getLeaderPowerIndex() {
        return leaderPowerIndex;
    }

    /**
     * getter for the index of the power which is incompatible with other selected powers
     * @return the index of the power which is incompatible with other selected powers
     */
    public int getLeaderCardIndex() {
        return leaderCardIndex;
    }
}
