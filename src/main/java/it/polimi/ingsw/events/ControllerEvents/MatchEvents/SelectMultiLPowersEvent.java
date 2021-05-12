package it.polimi.ingsw.events.ControllerEvents.MatchEvents;

import it.polimi.ingsw.events.ControllerEvents.MatchEvent;
import it.polimi.ingsw.model.Resource;

import java.util.ArrayList;

public class SelectMultiLPowersEvent extends MatchEvent {
    /**
     *
     * @param playerID the player that generated(directly or indirectly) this event
     */
    public SelectMultiLPowersEvent(String playerID) {super(playerID);}

    public ArrayList<Resource> getResources() {
        return resources;
    }

    ArrayList<Resource> resources;
}
