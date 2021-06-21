package it.polimi.ingsw.ui.gui;

import it.polimi.ingsw.events.ClientEvents.DepotState;
import it.polimi.ingsw.events.ControllerEvents.MatchEvents.ChosenResourcesEvent;
import it.polimi.ingsw.events.Event;
import it.polimi.ingsw.model.Marble;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.utilities.Pair;

import java.util.ArrayList;
import java.util.HashMap;

public class PlayerState {

    ArrayList<DepotState> warehouse;
    HashMap<Resource, Integer> strongBox;
    ArrayList<ArrayList<String>> ownedCards;

    ArrayList<String> leaderCards;
    Integer victoryPoints;
    Integer faithTrackPoints;
    Pair<Marble[][], Marble> marketStatus;
    String[][] devCardGrid;

    ArrayList<Event> events;
    ChosenResourcesEvent chosenResources;
    Resource resourceOfChoice;

    public PlayerState() {
        warehouse = new ArrayList<>();
        strongBox = new HashMap<>();
        ownedCards = new ArrayList<>();
        ownedCards.add(new ArrayList<>());
        ownedCards.add(new ArrayList<>());
        ownedCards.add(new ArrayList<>());
        leaderCards = new ArrayList<>();
        victoryPoints = 0;
        faithTrackPoints = 0;
        marketStatus = null;
        devCardGrid = null;

    }
}
