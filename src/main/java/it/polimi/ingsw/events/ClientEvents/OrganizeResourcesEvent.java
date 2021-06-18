package it.polimi.ingsw.events.ClientEvents;

import it.polimi.ingsw.model.Resource;

import java.util.HashMap;

/**
 * Event sent to a client to make it organize some resources between warehouse and leader powers
 */
public class OrganizeResourcesEvent extends ClientEvent{
    private final HashMap<Resource, Integer> resourcesToOrganize;

    /**
     * Constructor for the class
     * @param playerId The player that must organize the resources
     * @param resourcesToOrganize The resources to organize
     */
    public OrganizeResourcesEvent(String playerId, HashMap<Resource, Integer> resourcesToOrganize) {
        super(playerId);
        this.resourcesToOrganize = new HashMap<>(resourcesToOrganize);
    }

    /**
     * Getter for the resources to organize
     * @return The resources to organize
     */
    public HashMap<Resource, Integer> getResourcesToOrganize() {
        return new HashMap<>(resourcesToOrganize);
    }
}
