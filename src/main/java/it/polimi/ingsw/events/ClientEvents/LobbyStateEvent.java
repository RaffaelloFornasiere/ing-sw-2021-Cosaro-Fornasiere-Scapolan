package it.polimi.ingsw.events.ClientEvents;

import java.util.ArrayList;

/**
 * Event signaling a change of state of the lobby the player is in
 */
public class LobbyStateEvent extends ClientEvent {
    private final String leaderID;
    private final ArrayList<String> otherPLayersID;

    /**
     * Constructor of the class
     * @param leaderID The ID of the leader of the lobby
     * @param otherPLayersID The IDs of all the other player in the lobby
     */
    public LobbyStateEvent(String leaderID, ArrayList<String> otherPLayersID) {
        super(null);
        this.leaderID = leaderID;
        this.otherPLayersID = otherPLayersID;
    }

    /**
     * Getter for the ID of the leader of the lobby
     * @return The ID of the leader of the lobby
     */
    public String getLeaderID() {
        return leaderID;
    }

    /**
     * Getter for the IDs of all the other player in the lobby
     * @return The IDs of all the other player in the lobbyy
     */
    public ArrayList<String> getOtherPLayersID() {
        return otherPLayersID;
    }
}
