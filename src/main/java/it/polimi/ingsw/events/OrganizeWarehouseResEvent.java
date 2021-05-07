package it.polimi.ingsw.events;

import it.polimi.ingsw.model.Resource;

import java.util.HashMap;

public class OrganizeWarehouseResEvent extends MatchEvent{
    public OrganizeWarehouseResEvent(HashMap<Resource, Integer> organization){
        this.resourceOrganization = organization;
    };
    private final HashMap<Resource, Integer> resourceOrganization;
}
