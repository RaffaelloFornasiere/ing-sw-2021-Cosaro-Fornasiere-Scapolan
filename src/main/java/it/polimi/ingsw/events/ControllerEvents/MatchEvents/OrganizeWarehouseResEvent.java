package it.polimi.ingsw.events.ControllerEvents.MatchEvents;

import it.polimi.ingsw.model.Resource;

import java.util.ArrayList;
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
        this.resources = null;
    }
    public OrganizeWarehouseResEvent(String playerID, ArrayList<Resource> resources)
    {
        super(playerID);
        this.resources = resources;
        this.resourceOrganization = null;
    }

    private final HashMap<Resource, Integer> resourceOrganization;

    public HashMap<Resource, Integer> getResourceOrganization() {
        return resourceOrganization;
    }

    public ArrayList<Resource> getResources() {
        return resources;
    }

    private final ArrayList<Resource> resources;
}
