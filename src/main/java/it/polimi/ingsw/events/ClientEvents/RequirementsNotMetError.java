package it.polimi.ingsw.events.ClientEvents;

public class RequirementsNotMetError extends ClientEvent {

    private String leaderCardID;

    /**
     * constructor for the class
     * @param playerID the player that generated(directly or indirectly) this event
     * @param leaderCardID the ID of the leader card that the player wanted to activate, but for which they don't meet the requirements
     */
    public RequirementsNotMetError(String playerID, String leaderCardID) {
        super(playerID);
        this.leaderCardID = leaderCardID;
    }

    /**
     * getter for the ID of the leader card that the player wanted to activate, but for which they don't meet the requirements
     * @return the ID of the leader card that the player wanted to activate, but for which they don't meet the requirements
     */
    public String getLeaderCardID() {
        return leaderCardID;
    }
}
