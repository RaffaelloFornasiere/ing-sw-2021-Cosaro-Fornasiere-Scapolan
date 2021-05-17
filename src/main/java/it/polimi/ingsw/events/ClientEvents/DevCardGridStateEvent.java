package it.polimi.ingsw.events.ClientEvents;

public class DevCardGridStateEvent extends ClientEvent {

    private String[][] topDevCardIDs;

    public DevCardGridStateEvent(String playerId, String[][] topDevCardIDs) {
        super(playerId);
        this.topDevCardIDs =  new String[topDevCardIDs.length][topDevCardIDs[0].length];
        for(int i=0; i<topDevCardIDs.length; i++)
            System.arraycopy(topDevCardIDs, 0, this.topDevCardIDs, 0, topDevCardIDs[0].length);
    }

    public String[][] getTopDevCardIDs() {
        String[][] ret =  new String[topDevCardIDs.length][topDevCardIDs[0].length];
        for(int i=0; i<topDevCardIDs.length; i++)
            System.arraycopy(topDevCardIDs, 0, ret, 0, topDevCardIDs[0].length);

        return ret;
    }
}
