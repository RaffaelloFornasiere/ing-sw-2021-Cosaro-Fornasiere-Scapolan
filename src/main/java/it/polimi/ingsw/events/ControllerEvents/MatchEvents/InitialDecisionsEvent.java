package it.polimi.ingsw.events.ControllerEvents.MatchEvents;

import it.polimi.ingsw.model.Resource;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Events sent to the server containing the initial choices a player has made
 * After having sent this, the client should expect to receive a OrganizeResourcesEvent, at which it must answer with
 *  a NewResourceOrganizationEvent
 * After that the client should expect to receive a SetupDoneEvent at wich it does not need to answer
 * The client will receive a BadRequestEvent if this event was mal-posed
 */
public class InitialDecisionsEvent extends MatchEvent{
    private ArrayList<String> chosenLeaderCardIDs;
    private HashMap<Resource, Integer> chosenResources;

    public InitialDecisionsEvent(String playerID, ArrayList<String> chosenLeaderCardIDs, HashMap<Resource, Integer> chosenResources) {
        super(playerID);
        this.chosenLeaderCardIDs = (ArrayList<String>) chosenLeaderCardIDs.clone();
        this.chosenResources = (HashMap<Resource, Integer>) chosenResources.clone();
    }

    public ArrayList<String> getChosenLeaderCardIDs() {
        return (ArrayList<String>) chosenLeaderCardIDs.clone();
    }

    public HashMap<Resource, Integer> getChosenResources() {
        return (HashMap<Resource, Integer>) chosenResources.clone();
    }
}
