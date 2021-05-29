package it.polimi.ingsw.events.ControllerEvents.MatchEvents;

import it.polimi.ingsw.model.Resource;

import java.util.HashMap;

public class OrganizeWarehouseResEvent extends MatchEvent {
    /**
     *
     * @param playerID the player that generated(directly or indirectly) this event
     * @param organization new organization of warehouse
     *
     */
    public OrganizeWarehouseResEvent(String playerID, HashMap<Resource, Integer> organization){
        super(playerID);
        this.resourceOrganization = organization;
    }
    private final HashMap<Resource, Integer> resourceOrganization;
}
