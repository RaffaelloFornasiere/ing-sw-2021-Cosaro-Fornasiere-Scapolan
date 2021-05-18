package it.polimi.ingsw.events.ClientEvents;

import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.utilities.Pair;

import java.util.ArrayList;
import java.util.HashMap;

public class DashBoardStateEvent extends ClientEvent{
    private ArrayList<String> topDevCards;
    private HashMap<Resource, Integer> strongBox;
    private ArrayList<DepotState> warehouse;

    public DashBoardStateEvent(String playerId, ArrayList<String> topDevCards, HashMap<Resource, Integer> strongBox, ArrayList<DepotState> warehouse) {
        super(playerId);
        this.topDevCards = (ArrayList<String>) topDevCards.clone();
        this.strongBox = (HashMap<Resource, Integer>) strongBox.clone();
        this.warehouse = (ArrayList<DepotState>) warehouse.clone();
    }

    public ArrayList<String> getTopDevCards() {
        return (ArrayList<String>) topDevCards.clone();
    }

    public HashMap<Resource, Integer> getStrongBox() {
        return (HashMap<Resource, Integer>) strongBox.clone();
    }

    public ArrayList<DepotState> getWarehouse() {
        return (ArrayList<DepotState>) warehouse.clone();
    }
}
