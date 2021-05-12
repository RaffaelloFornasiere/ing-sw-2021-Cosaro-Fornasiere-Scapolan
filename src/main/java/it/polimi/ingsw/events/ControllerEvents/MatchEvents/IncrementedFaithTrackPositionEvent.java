package it.polimi.ingsw.events.ControllerEvents.MatchEvents;

public class IncrementedFaithTrackPositionEvent extends MatchEvent {
    private int newPosition;

    /**
     * Constructor for the class
     * @param playerID the player that generated(directly or indirectly) this event
     * @param position the updated new position
     * @throws IllegalArgumentException if the index is negative
     */
    public IncrementedFaithTrackPositionEvent (String playerID, int position) throws IllegalArgumentException{
        super(playerID);
        if(position<0)
            throw new IllegalArgumentException("position must be positive");
        this.newPosition = position;
    }

    /**
     * getter for the index of the leader card to activate
     * @return the index of the leader card to activate
     */
    public int getNewPosition() {
        return newPosition;
    }
}
