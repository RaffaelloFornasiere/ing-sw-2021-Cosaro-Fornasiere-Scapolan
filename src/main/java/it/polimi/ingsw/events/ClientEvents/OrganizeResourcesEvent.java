package it.polimi.ingsw.events.ClientEvents;

import it.polimi.ingsw.model.Resource;

import java.util.HashMap;

public class OrganizeResourcesEvent extends ClientEvent{
    private HashMap<Resource, Integer> resourcesToOrganize;

    public OrganizeResourcesEvent(String playerId, HashMap<Resource, Integer> resourcesToOrganize) {
        super(playerId);
        this.resourcesToOrganize = (HashMap<Resource, Integer>) resourcesToOrganize.clone();
    }

    public HashMap<Resource, Integer> getResourcesToOrganize() {
        return (HashMap<Resource, Integer>) resourcesToOrganize.clone();
    }
}
