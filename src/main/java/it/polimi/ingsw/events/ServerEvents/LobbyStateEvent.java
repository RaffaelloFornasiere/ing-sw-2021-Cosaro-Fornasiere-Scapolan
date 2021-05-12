package it.polimi.ingsw.events.ServerEvents;

import it.polimi.ingsw.events.Event;

import java.util.ArrayList;

public class LobbyStateEvent extends Event {
    private String leaderID;
    private ArrayList<String> otherPLayersID;

    /**
     * constructor of the class
     *
     * @param leaderID the ID of the leader of the lobby
     * @param otherPLayersID the IDs of all the other player in the lobby
     */
    public LobbyStateEvent(String leaderID, ArrayList<String> otherPLayersID) {
        super("");
        this.leaderID = leaderID;
        this.otherPLayersID = otherPLayersID;
    }

    public String getLeaderID() {
        return leaderID;
    }

    public ArrayList<String> getOtherPLayersID() {
        return otherPLayersID;
    }
}
