package it.polimi.ingsw.events;

public class AddedNewPopeFavorCardEvent  extends MatchEvent{
    private int popeFavourCardIndex;

    /**
     * Constructor for the class
     * @param playerID the player that generated(directly or indirectly) this event
     * @param popeFavorCardIndex the index of the popeFavorCard added.
     * @throws IllegalArgumentException if the index is negative
     */
    public AddedNewPopeFavorCardEvent(String playerID, int popeFavorCardIndex) throws IllegalArgumentException{
        super(playerID);
        if(popeFavourCardIndex<0)
            throw new IllegalArgumentException("Index must be positive");
        this.popeFavourCardIndex= popeFavorCardIndex;
    }

    /**
     * getter for the index of the leader card to activate
     * @return the index of the leader card to activate
     */
    public int getLeaderCardIndex() {
        return popeFavourCardIndex;
    }
}
