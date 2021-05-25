package it.polimi.ingsw.events.ControllerEvents.MatchEvents;

import it.polimi.ingsw.events.ClientEvents.DepositLeaderPowerStateEvent;
import it.polimi.ingsw.events.ClientEvents.DepotState;
import it.polimi.ingsw.model.Resource;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Event sent to the server when the player had to reorganize their resources
 * The client will receive a BadRequestEvent if this event was mal-posed
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
    @SuppressWarnings("unchecked")
    public NewResourcesOrganizationEvent(String playerID, ArrayList<DepotState> depotStates, ArrayList<DepositLeaderPowerStateEvent> leaderPowersState, HashMap<Resource, Integer> discardedResources) {
        super(playerID);
        this.depotStates = (ArrayList<DepotState>) depotStates.clone();
        this.leaderPowersState = (ArrayList<DepositLeaderPowerStateEvent>) leaderPowersState.clone();
        this.discardedResources = (HashMap<Resource, Integer>) discardedResources.clone();
    }

    /**
     * Getter for the new way the resources are arranged in the depot
     * @return The new way the resources are arranged in the depot
     */
    @SuppressWarnings("unchecked")
    public ArrayList<DepotState> getDepotStates() {
        return (ArrayList<DepotState>) depotStates.clone();
    }

    /**
     * Getter for the new way the resources are arranged between the leader powers
     * @return The new way the resources are arranged between the leader powers
     */
    @SuppressWarnings("unchecked")
    public ArrayList<DepositLeaderPowerStateEvent> getLeaderPowersState() {
        return (ArrayList<DepositLeaderPowerStateEvent>) leaderPowersState.clone();
    }

    /**
     * Getter for the resources that will be discarded
     * @return The resources that will be discarded
     */
    @SuppressWarnings("unchecked")
    public HashMap<Resource, Integer> getDiscardedResources() {
        return (HashMap<Resource, Integer>) discardedResources.clone();
    }
}
