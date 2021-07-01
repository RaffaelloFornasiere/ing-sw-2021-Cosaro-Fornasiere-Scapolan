package it.polimi.ingsw.events.controllerEvents.matchEvents;

import it.polimi.ingsw.events.clientEvents.DepositLeaderPowerStateEvent;
import it.polimi.ingsw.events.clientEvents.DepotState;
import it.polimi.ingsw.model.Resource;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Event sent to the server when the player had to reorganize their resources
 * The client will receive a BadRequestEvent if this event was mal-posed
 * The client will receive a PlayerActionError if the action represented by this event can't be done
 * The client will receive a LeaderCardNotActiveError if he tried to insert resources into the power of a non active
 *  leader card
 */
public class NewResourcesOrganizationEvent extends MatchEvent{
    private final ArrayList<DepotState> depotStates;
    private final ArrayList<DepositLeaderPowerStateEvent> leaderPowersState;
    private final HashMap<Resource, Integer> discardedResources;

    /**
     * Constructor for the class
     * @param playerID The ID of the player that generated(directly or indirectly) this event
     * @param depotStates The new way the resources are arranged in the depot
     * @param leaderPowersState The new way the resources are arranged between the leader powers
     * @param discardedResources The resources that will be discarded
     */
    public NewResourcesOrganizationEvent(String playerID, ArrayList<DepotState> depotStates, ArrayList<DepositLeaderPowerStateEvent> leaderPowersState, HashMap<Resource, Integer> discardedResources) {
        super(playerID);
        this.depotStates = new ArrayList<>(depotStates);
        this.leaderPowersState = new ArrayList<>(leaderPowersState);
        this.discardedResources = new HashMap<>(discardedResources);
    }

    /**
     * Getter for the new way the resources are arranged in the depot
     * @return The new way the resources are arranged in the depot
     */
    public ArrayList<DepotState> getDepotStates() {
        return new ArrayList<>(depotStates);
    }

    /**
     * Getter for the new way the resources are arranged between the leader powers
     * @return The new way the resources are arranged between the leader powers
     */
    public ArrayList<DepositLeaderPowerStateEvent> getLeaderPowersState() {
        return new ArrayList<>(leaderPowersState);
    }

    /**
     * Getter for the resources that will be discarded
     * @return The resources that will be discarded
     */
    public HashMap<Resource, Integer> getDiscardedResources() {
        return new HashMap<>(discardedResources);
    }
}
