package it.polimi.ingsw.events.ClientEvents;

import it.polimi.ingsw.model.Resource;

import java.util.HashMap;

/**
 * Event signaling a change in the stored resources of a leader power
 */
public class DepositLeaderPowerStateEvent extends ClientEvent{
    private final String leaderCardID;
    private final int leaderPowerIndex;
    private final HashMap<Resource, Integer> storedResources;

    /**
     * Constructor for the class
     * @param playerId The ID of the player owning the leader card whose power state changed
     * @param leaderCardID The ID of the leader card whose power state changed
     * @param leaderPowerIndex The index of the power whose state changed
     * @param storedResources The resources that are now stored in the leader power
     * @throws IllegalArgumentException If the index of the leader power is negative
     */
    public DepositLeaderPowerStateEvent(String playerId, String leaderCardID, int leaderPowerIndex, HashMap<Resource, Integer> storedResources) throws IllegalArgumentException{
        super(playerId);
        if(leaderPowerIndex<0) throw new IllegalArgumentException("Indexes must be positive");
        this.leaderCardID = leaderCardID;
        this.leaderPowerIndex = leaderPowerIndex;
        this.storedResources = new HashMap<>(storedResources);
    }

    /**
     * Getter for the ID of the leader card whose power state changed
     * @return The ID of the leader card whose power state changed
     */
    public String getLeaderCardID() {
        return leaderCardID;
    }

    /**
     * Getter for the index of the power whose state changed
     * @return The index of the power whose state changed
     */
    public int getLeaderPowerIndex() {
        return leaderPowerIndex;
    }

    /**
     * Getter for the resources that are now stored in the leader power
     * @return The resources that are now stored in the leader power
     */
    public HashMap<Resource, Integer> getStoredResources() {
        return new HashMap<>(storedResources);
    }
}
