package it.polimi.ingsw.events.ClientEvents;

/**
 * Event signaling the change in the state of the grid of development cards
 */
public class DevCardGridStateEvent extends ClientEvent {

    private final String[][] topDevCardIDs;

    /**
     * Constructor for the class
     * @param playerId The ID of the player that caused the change in the grid
     * @param topDevCardIDs The IDs of top development card of each deck in the grid
     */
    public DevCardGridStateEvent(String playerId, String[][] topDevCardIDs) {
        super(playerId);
        this.topDevCardIDs =  new String[topDevCardIDs.length][topDevCardIDs[0].length];
        for(int i=0; i<topDevCardIDs.length; i++)
            System.arraycopy(topDevCardIDs, 0, this.topDevCardIDs, 0, topDevCardIDs[0].length-1);
    }

    /**
     * Getter of the IDs of top development card of each deck in the grid
     * @return The IDs of top development card of each deck in the grid
     */
    public String[][] getTopDevCardIDs() {
        String[][] ret =  new String[topDevCardIDs.length][topDevCardIDs[0].length];
        for(int i=0; i<topDevCardIDs.length; i++)
            System.arraycopy(topDevCardIDs, 0, ret, 0, topDevCardIDs[0].length-1);

        return ret;
    }
}
