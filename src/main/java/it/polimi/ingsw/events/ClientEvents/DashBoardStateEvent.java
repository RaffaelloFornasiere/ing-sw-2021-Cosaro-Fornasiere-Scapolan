package it.polimi.ingsw.events.ClientEvents;

import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.utilities.Pair;

import java.util.ArrayList;
import java.util.HashMap;

public class DashBoardStateEvent extends ClientEvent{
    private ArrayList<String> topDevCards;
    private HashMap<Resource, Integer> strongBox;
    private ArrayList<Pair<Resource, Integer>> warehouse;

    public DashBoardStateEvent(String playerId, ArrayList<String> topDevCards, HashMap<Resource, Integer> strongBox, ArrayList<Pair<Resource, Integer>> warehouse) {
        super(playerId);
        this.topDevCards = (ArrayList<String>) topDevCards.clone();
        this.strongBox = (HashMap<Resource, Integer>) strongBox.clone();
        this.warehouse = new ArrayList<>();
        for(Pair<Resource, Integer> depot: warehouse)
            warehouse.add(new Pair<>(depot.getKey(), depot.getValue()));
    }
}
