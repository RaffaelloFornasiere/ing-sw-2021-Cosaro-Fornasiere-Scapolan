package it.polimi.ingsw.events.ControllerEvents.MatchEvents;

import it.polimi.ingsw.model.Resource;

import java.util.HashMap;

/**
 * Event sent to the server when a player chooses a certain number of resources between different types
 * The client will receive a BadRequestEvent if this event was mal-posed
 */
public class ChosenMultipleExtraResourcePowerEvent extends MatchEvent {
    private HashMap<Resource, Integer> chosenResources;

    /**
     * Constructor for the class
     * @param playerID The ID of the player that generated(directly or indirectly) this event
     * @param chosenResources the resources chosen by the player
     */
    @SuppressWarnings("unchecked")
    public ChosenMultipleExtraResourcePowerEvent(String playerID, HashMap<Resource, Integer> chosenResources) {
        super(playerID);
        this.chosenResources = (HashMap<Resource, Integer>) chosenResources.clone();
    }

    /**
     * Getter for the resources chosen by the player
     * @return The resources chosen by the player
     */
    @SuppressWarnings("unchecked")
    public HashMap<Resource, Integer> getChosenResources() {
        return (HashMap<Resource, Integer>) chosenResources.clone();
    }
}
