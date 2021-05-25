package it.polimi.ingsw.events.ClientEvents;

import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.utilities.Pair;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Event signaling a new state for the dashboard of a player
 */
public class DashBoardStateEvent extends ClientEvent{
    private final ArrayList<String> topDevCards;
    private final HashMap<Resource, Integer> strongBox;
    private final ArrayList<DepotState> warehouse;

    /**
     * Constructor for the class
     * @param playerId The ID of the player owning the dashboard that got changed
     * @param topDevCards The ID of the development cards at the top of each card slot
     * @param strongBox The resources stored into the player strongbox
     * @param warehouse The current state of each depot of the warehouse
     */
    @SuppressWarnings("unchecked")
    public DashBoardStateEvent(String playerId, ArrayList<String> topDevCards, HashMap<Resource, Integer> strongBox, ArrayList<DepotState> warehouse) {
        super(playerId);
        this.topDevCards = (ArrayList<String>) topDevCards.clone();
        this.strongBox = (HashMap<Resource, Integer>) strongBox.clone();
        this.warehouse = (ArrayList<DepotState>) warehouse.clone();
    }

    /**
     * Getter for the ID of the development cards at the top of each card slot. If there's no card in some slot, there
     * will be a null object in its position
     * @return The ID of the development cards at the top of each card slot
     */
    @SuppressWarnings("unchecked")
    public ArrayList<String> getTopDevCards() {
        return (ArrayList<String>) topDevCards.clone();
    }

    /**
     * Getter for the resources stored into the player strongbox
     * @return The resources stored into the player strongbox
     */
    @SuppressWarnings("unchecked")
    public HashMap<Resource, Integer> getStrongBox() {
        return (HashMap<Resource, Integer>) strongBox.clone();
    }

    /**
     * Getter for the current state of each depot of the warehouse
     * @return The current state of each depot of the warehouse
     */
    @SuppressWarnings("unchecked")
    public ArrayList<DepotState> getWarehouse() {
        return (ArrayList<DepotState>) warehouse.clone();
    }
}
