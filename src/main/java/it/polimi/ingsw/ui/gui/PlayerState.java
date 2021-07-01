package it.polimi.ingsw.ui.gui;

import it.polimi.ingsw.events.ClientEvents.DepotState;
import it.polimi.ingsw.events.ControllerEvents.MatchEvents.ChosenResourcesEvent;
import it.polimi.ingsw.events.Event;
import it.polimi.ingsw.model.FaithTrack.PopeFavorCard;
import it.polimi.ingsw.model.Marble;
import it.polimi.ingsw.model.ProductionPower;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.ui.cli.Action;
import it.polimi.ingsw.utilities.LockWrap;
import it.polimi.ingsw.utilities.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class PlayerState {

    ArrayList<DepotState> warehouse;
    HashMap<Resource, Integer> strongBox;

    //for buying
    private final HashMap<Resource, Integer> leaderDepots;

    // resources contained in leader depots
    private final HashMap<String, ArrayList<HashMap<Resource, Integer>>> leaderDepotsState;

    ArrayList<ArrayList<String>> ownedCards;
    ProductionPower personalProductionPower;

    private int faithTrackPosition;
    private HashMap<Integer, PopeFavorCard> popeFavorCards;

    HashMap<String, Boolean> leaderCards;
    HashMap<String, ArrayList<Boolean>> leaderPowerStates;




    Integer victoryPoints;
    Integer faithTrackPoints;
    static Pair<Marble[][], Marble> marketStatus = null;
    static String[][] devCardGrid = null;

    ArrayList<Event> events;
    LockWrap<Event> event;
    ChosenResourcesEvent chosenResources;
    Resource resourceOfChoice;

    static ArrayList<Action> availableActions = new ArrayList<>();
    static boolean canPerformActions;

    public PlayerState() {
        warehouse = new ArrayList<>();
        strongBox = new HashMap<>();
        leaderDepots = new HashMap<>(){{
           Arrays.stream(Resource.values()).forEach(n ->
                   put(n, 0));
        }};

        for (Resource r : Resource.values()) {
            leaderDepots.put(r, 0);
        }
        leaderDepotsState = new HashMap<>();
        ownedCards = new ArrayList<>();
        ownedCards.add(new ArrayList<>());
        ownedCards.add(new ArrayList<>());
        ownedCards.add(new ArrayList<>());
        faithTrackPosition = 0;
        popeFavorCards = new HashMap<>();
        leaderCards = new HashMap<>();
        leaderPowerStates = new HashMap<>();
        event = new LockWrap<>(null, null);
        events = new ArrayList<>();
        victoryPoints = 45;
        faithTrackPoints = 0;
    }


    public int getFaithTrackPosition() {
        return faithTrackPosition;
    }

    public void setFaithTrackPosition(int position) {
        this.faithTrackPosition = position;
    }

    public HashMap<Integer, PopeFavorCard> getPopeFavorCards() {
        return new HashMap<>(popeFavorCards);
    }

    public void setPopeFavorCards(HashMap<Integer, PopeFavorCard> popeFavorCards) {
        this.popeFavorCards = new HashMap<>(popeFavorCards);
    }

    public HashMap<Resource, Integer> getLeaderDepots() {
        return leaderDepots;
    }

    public void updateLeaderCardDepositState(String leaderCardID, int leaderPowerIndex, HashMap<Resource, Integer> storedResources) {
        leaderDepotsState.putIfAbsent(leaderCardID, new ArrayList<>());
        ArrayList<HashMap<Resource, Integer>> leaderPowersDeposit = leaderDepotsState.get(leaderCardID);


        if (leaderPowerIndex < leaderPowersDeposit.size()) {
            HashMap<Resource, Integer> oldStoredResources = leaderPowersDeposit.get(leaderPowerIndex);
            for (Resource r : Resource.values()) {
                leaderDepots.put(r, leaderDepots.get(r) - oldStoredResources.getOrDefault(r, 0) + storedResources.getOrDefault(r, 0));
            }
            leaderPowersDeposit.set(leaderPowerIndex, storedResources);
        } else {
            for (Resource r : storedResources.keySet()) {
                leaderDepots.put(r, leaderDepots.get(r) + storedResources.get(r));
            }
            for (int i = leaderPowersDeposit.size(); i < leaderPowerIndex; i++) {
                leaderPowersDeposit.add(new HashMap<>());
            }
            leaderPowersDeposit.add(storedResources);
        }
    }
}
