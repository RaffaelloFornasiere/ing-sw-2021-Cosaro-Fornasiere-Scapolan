package it.polimi.ingsw.events.ControllerEvents.MatchEvents;

public class ActivateLeaderCardEvent extends MatchEvent {
    private int leaderCardIndex;

    /**
     * Constructor for the class
     * @param playerID the player that generated(directly or indirectly) this event
     * @param leaderCardIndex the index of the leader card to activate
     * @throws IllegalArgumentException if the index is negative
     */
    public ActivateLeaderCardEvent(String playerID, int leaderCardIndex) throws IllegalArgumentException{
        super(playerID);
        if(leaderCardIndex<0)
            throw new IllegalArgumentException("Index must be positive");
        this.leaderCardIndex = leaderCardIndex;
    }





    /**
     * getter for the index of the leader card to activate
     * @return the index of the leader card to activate
     */
    public int getLeaderCardIndex() {
        return leaderCardIndex;
    }
}
