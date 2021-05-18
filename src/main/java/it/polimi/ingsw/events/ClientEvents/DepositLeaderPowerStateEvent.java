package it.polimi.ingsw.events.ClientEvents;

import it.polimi.ingsw.model.Resource;

import java.util.HashMap;

public class DepositLeaderPowerStateEvent extends ClientEvent{
    private String leaderCardID;
    private HashMap<Resource, Integer> storedResources;

    public DepositLeaderPowerStateEvent(String playerId, String leaderCardID, HashMap<Resource, Integer> storedResources) {
        super(playerId);
        this.leaderCardID = leaderCardID;
        this.storedResources = (HashMap<Resource, Integer>) storedResources.clone();
    }

    public String getLeaderCardID() {
        return leaderCardID;
    }

    public HashMap<Resource, Integer> getStoredResources() {
        return (HashMap<Resource, Integer>) storedResources.clone();
    }
}
