package it.polimi.ingsw.events.clientEvents;

/**
 * Event sent to the client when he tries to activate a leader card without having enough resources
 */
public class RequirementsNotMetError extends ClientEvent {

    private final String leaderCardID;

    /**
     * Constructor for the class
     * @param playerID The ID of the player that tried to activate te leader card
     * @param leaderCardID The ID of the leader card that the player wanted to activate, but for which they don't meet the requirements
     */
    public RequirementsNotMetError(String playerID, String leaderCardID) {
        super(playerID);
        this.leaderCardID = leaderCardID;
    }

    /**
     * Getter for the ID of the leader card that the player wanted to activate, but for which they don't meet the requirements
     * @return The ID of the leader card that the player wanted to activate, but for which they don't meet the requirements
     */
    public String getLeaderCardID() {
        return leaderCardID;
    }
}
