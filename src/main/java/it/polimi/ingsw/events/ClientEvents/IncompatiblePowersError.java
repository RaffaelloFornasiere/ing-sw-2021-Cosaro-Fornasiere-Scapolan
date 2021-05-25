package it.polimi.ingsw.events.ClientEvents;

/**
 *
 */
public class IncompatiblePowersError extends ClientEvent {
    private final String leaderCardID;
    private final int leaderPowerIndex;

    /**
     * Constructor for the class
     * @param playerID The player that generated(directly or indirectly) this event
     * @param leaderCardID The ID of the leader card containing the power which is incompatible with other selected powers
     * @param leaderPowerIndex The index of the power which is incompatible with other selected powers
     * @throws IllegalArgumentException If the index of the leader power is negative
     */
    public IncompatiblePowersError(String playerID, String leaderCardID, int leaderPowerIndex) throws IllegalArgumentException{
        super(playerID);
        if(leaderPowerIndex<0)
            throw new IllegalArgumentException("Indexes must be positive");
        this.leaderCardID = leaderCardID;
        this.leaderPowerIndex = leaderPowerIndex;
    }

    /**
     * getter for the ID of the leader card containing the power which is incompatible with other selected powers
     * @return the ID of the leader card containing the power which is incompatible with other selected powers
     */
    public String getLeaderCardID() {
        return leaderCardID;
    }

    /**
     * getter for the index of the power which is incompatible with other selected powers
     * @return the index of the power which is incompatible with other selected powers
     */
    public int getLeaderPowerIndex() {
        return leaderPowerIndex;
    }
}
