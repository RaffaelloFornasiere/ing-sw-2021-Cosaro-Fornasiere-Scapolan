package it.polimi.ingsw.events.ClientEvents;

/**
 * Event sent to the player when Lorenzo il Magnifico moves along the FaithTrack
 */
public class LorenzoPositionEvent extends ClientEvent{
    private final int position;

    /**
     * Constructor for th class
     * @param playerId The ID of the player
     * @param position The position of Lorenzo il Magnifico in the Faith Track
     */
    public LorenzoPositionEvent(String playerId, int position) {
        super(playerId);
        this.position = position;
    }

    /**
     * Getter for the position of Lorenzo il Magnifico in the Faith Track
     * @return The position of Lorenzo il Magnifico in the Faith Track
     */
    public int getPosition() {
        return position;
    }
}
