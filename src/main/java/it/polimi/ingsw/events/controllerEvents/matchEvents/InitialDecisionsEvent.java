package it.polimi.ingsw.events.controllerEvents.matchEvents;

import it.polimi.ingsw.model.Resource;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Events sent to the server containing the initial choices a player has made
 * After having sent this, the client should expect to receive a OrganizeResourcesEvent, at which it must answer with
 *  a NewResourceOrganizationEvent
 * After that the client should expect to receive a SetupDoneEvent at which it does not need to answer
 * The client will receive a BadRequestEvent if this event was mal-posed
 * The client will receive a PlayerActionError if the action represented by this event can't be done
 */
public class InitialDecisionsEvent extends MatchEvent{
    private final ArrayList<String> chosenLeaderCardIDs;
    private final HashMap<Resource, Integer> chosenResources;

    /**
     * Constructor for the class
     * @param playerID The ID of the player that generated(directly or indirectly) this event
     * @param chosenLeaderCardIDs The IDs of the leader card that the player decided to keep
     * @param chosenResources The resources the player decided on
     */
    public InitialDecisionsEvent(String playerID, ArrayList<String> chosenLeaderCardIDs, HashMap<Resource, Integer> chosenResources) {
        super(playerID);
        this.chosenLeaderCardIDs = new ArrayList<>(chosenLeaderCardIDs);
        this.chosenResources = new HashMap<>(chosenResources);
    }

    /**
     * Getter for the IDs of the leader card that the player decided to keep
     * @return The IDs of the leader card that the player decided to keep
     */
    public ArrayList<String> getChosenLeaderCardIDs() {
        return new ArrayList<>(chosenLeaderCardIDs);
    }

    /**
     * Getter for the resources the player decided on
     * @return The resources the player decided on
     */
    public HashMap<Resource, Integer> getChosenResources() {
        return new HashMap<>(chosenResources);
    }
}
