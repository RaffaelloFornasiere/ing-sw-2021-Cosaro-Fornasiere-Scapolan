package it.polimi.ingsw.ui.gui;

import it.polimi.ingsw.events.ClientEvents.DepotState;
import it.polimi.ingsw.events.ControllerEvents.MatchEvents.ChosenResourcesEvent;
import it.polimi.ingsw.events.Event;
import it.polimi.ingsw.model.DevCards.DevCard;
import it.polimi.ingsw.model.Direction;
import it.polimi.ingsw.model.Marble;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.utilities.LockWrap;
import it.polimi.ingsw.utilities.Pair;
import org.checkerframework.checker.units.qual.A;
import org.reflections.vfs.Vfs;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.locks.Lock;

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
