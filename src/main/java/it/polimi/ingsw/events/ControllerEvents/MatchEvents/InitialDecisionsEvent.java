package it.polimi.ingsw.events.ControllerEvents.MatchEvents;

import it.polimi.ingsw.model.Resource;

import java.util.ArrayList;
import java.util.HashMap;

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
