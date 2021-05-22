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
    private ArrayList<DepotState> depotStates;
    private ArrayList<DepositLeaderPowerStateEvent> leaderPowersState;
    private HashMap<Resource, Integer> discardedResources;

    public NewResourcesOrganizationEvent(String playerID, ArrayList<DepotState> depotStates, ArrayList<DepositLeaderPowerStateEvent> leaderPowersState, HashMap<Resource, Integer> discardedResources) {
        super(playerID);
        this.depotStates = (ArrayList<DepotState>) depotStates.clone();
        this.leaderPowersState = (ArrayList<DepositLeaderPowerStateEvent>) leaderPowersState.clone();
        this.discardedResources = (HashMap<Resource, Integer>) discardedResources.clone();
    }

    public ArrayList<DepotState> getDepotStates() {
        return (ArrayList<DepotState>) depotStates.clone();
    }

    public ArrayList<DepositLeaderPowerStateEvent> getLeaderPowersState() {
        return (ArrayList<DepositLeaderPowerStateEvent>) leaderPowersState.clone();
    }

    public HashMap<Resource, Integer> getDiscardedResources() {
        return (HashMap<Resource, Integer>) discardedResources.clone();
    }
}
