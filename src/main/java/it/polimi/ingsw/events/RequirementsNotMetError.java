package it.polimi.ingsw.events;

public class RequirementsNotMetError extends Event{

    int leaderCardIndex;

    /**
     * constructor for the class
     * @param playerID the player that generated(directly or indirectly) this event
     * @param leaderCardIndex the index of the leader card that the player wanted to activate, but for which they don't meet the requirements
     */
    public RequirementsNotMetError(String playerID, int leaderCardIndex) {
        super(playerID);
        this.leaderCardIndex = leaderCardIndex;
    }

    /**
     * getter for the index of the leader card that the player wanted to activate, but for which they don't meet the requirements
     * @return the index of the leader card that the player wanted to activate, but for which they don't meet the requirements
     */
    public int getLeaderCardIndex() {
        return leaderCardIndex;
    }
}
