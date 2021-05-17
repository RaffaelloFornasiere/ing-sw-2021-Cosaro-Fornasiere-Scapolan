package it.polimi.ingsw.events.ClientEvents;

import it.polimi.ingsw.model.LeaderCards.LeaderCard;

import java.util.ArrayList;

public class LeaderCardStateEvent extends ClientEvent{
    String leaderCardID;
    ArrayList<Boolean> powerSelectedStates;

    public LeaderCardStateEvent(String playerId, String leaderCardID, ArrayList<Boolean> powerSelectedStates) {
        super(playerId);
        this.leaderCardID = leaderCardID;
        this.powerSelectedStates = (ArrayList<Boolean>) powerSelectedStates.clone();
    }

    public ArrayList<Boolean> getPowerSelectedStates() {
        return (ArrayList<Boolean>) powerSelectedStates.clone();
    }

    public String getLeaderCardID() {
        return leaderCardID;
    }
}
