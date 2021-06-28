package it.polimi.ingsw.controller;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.client.Sender;
import it.polimi.ingsw.controller.modelChangeHandlers.DashBoardHandler;
import it.polimi.ingsw.controller.modelChangeHandlers.DepositLeaderPowerHandler;
import it.polimi.ingsw.controller.modelChangeHandlers.LeaderCardHandler;
import it.polimi.ingsw.controller.modelChangeHandlers.MatchStateHandler;
import it.polimi.ingsw.events.ClientEvents.*;
import it.polimi.ingsw.events.ControllerEvents.MatchEvents.*;
import it.polimi.ingsw.events.ControllerEvents.QuitGameEvent;
import it.polimi.ingsw.events.Event;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.DevCards.DevCard;
import it.polimi.ingsw.model.DevCards.DevCardGrid;
import it.polimi.ingsw.model.FaithTrack.FaithTrack;
import it.polimi.ingsw.model.FaithTrack.FaithTrackData;
import it.polimi.ingsw.model.LeaderCards.*;
import it.polimi.ingsw.model.singlePlayer.SinglePlayerMatchState;
import it.polimi.ingsw.model.singlePlayer.SoloActionToken;
import it.polimi.ingsw.utilities.*;
import org.reflections.Reflections;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Controller {
    MarketManager marketManager;
    LeaderCardManager leaderCardManager;
    FaithTrackManager faithTrackManager;
    MatchState matchState;
    HashMap<String, Sender> senders;
    PropertyChangeSubject eventsRegistry;
    HashMap<String, Boolean> disconnected;
    private final ArrayList<String> setuppedPlayers;
    private HashMap<String, InitialChoicesEvent> initialChoices;

    private final Object waitingForResourcesLock = new Object();
    private ChosenResourcesEvent chosenResourcesEvent;

    private final Object waitingForSimpleResourcesLock = new Object();
    private SimpleChosenResourcesEvent simpleChosenResourcesEvent;

    private final Object waitingForResourceOrganizationLock = new Object();
    private String playerWaitingForResourceOrganization;
    private NewResourcesOrganizationEvent newResourcesOrganizationEvent;


    /**
     * Constructor for the class.
     * It automatically registers an handler for every event that inherits from MatchEvent
     * @param subject The object responsible for registering and notify the arrival of events related to the match this is the controller of
     * @param matchState The match this is the controller of
     * @param senders The Senders for all the players involved in this match
     */
    public Controller(PropertyChangeSubject subject, MatchState matchState, HashMap<String, Sender> senders) {
        ArrayList<String> playerIDsInMatch = new ArrayList<>();
        for(Player p: matchState.getPlayers())
            playerIDsInMatch.add(p.getPlayerId());

        if(!playerIDsInMatch.containsAll(senders.keySet())) throw new IllegalArgumentException("must have the clientHandlerSenders of all the players in the match");

        Reflections reflections = new Reflections("it.polimi.ingsw.events");
        Set<Class<? extends MatchEvent>> events = reflections.getSubTypesOf(MatchEvent.class);

        for (var event : events) {
            try {
                Method method = this.getClass().getMethod(event.getSimpleName() + "Handler",
                        PropertyChangeEvent.class);
                subject.addPropertyChangeListener(event.getSimpleName(), x -> {
                    try {
                        method.invoke(this, x);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                });
            } catch (NoSuchMethodException  e) {
                e.printStackTrace();
            }
        }

        subject.addPropertyChangeListener(QuitGameEvent.class.getSimpleName(), this::QuitGameEventHandler);

        this.matchState = matchState;
        this.senders = senders;
        this.eventsRegistry = subject;
        this.disconnected = new HashMap<>();
        for(String playerID: this.senders.keySet())
            disconnected.put(playerID, false);
        initialChoices = new HashMap<>();

        faithTrackManager = new FaithTrackManager(matchState);
        leaderCardManager = new LeaderCardManager();
        marketManager = new MarketManager(matchState.getMarket());

        setuppedPlayers = new ArrayList<>();
    }

    /**
     * Methods that sets the initials choices sent to each player
     * @param initialChoices The initial choices sent to each player
     */
    public void setInitialChoices(HashMap<String, InitialChoicesEvent> initialChoices) {
        //Important it is not a clone!!!
        this.initialChoices = initialChoices;
    }

    /**
     * Handler for InitialDecisionsEvent
     */
    public synchronized void InitialDecisionsEventHandler(PropertyChangeEvent evt) {
        InitialDecisionsEvent event = (InitialDecisionsEvent) evt.getNewValue();

        try {
            Player player = matchState.getPlayerFromID(event.getPlayerId());
            if (matchState.getTurnState() != TurnState.WAITING_FOR_PLAYER || setuppedPlayers.contains(event.getPlayerId())) {
                senders.get(event.getPlayerId()).sendObject(new BadRequestEvent(event.getPlayerId(), "Can't perform this action", event));
                return;
            }

            if (event.getChosenLeaderCardIDs().size() != Config.getInstance().getLeaderCardPerPlayerToChoose()) {
                senders.get(event.getPlayerId()).sendObject(new PlayerActionError(event.getPlayerId(), "Wrong number of leader cards chosen", event));
                senders.get(event.getPlayerId()).sendObject(initialChoices.get(event.getPlayerId()));
                return;
            }

            int chosenResources = event.getChosenResources().values().stream().reduce(0, Integer::sum);
            if (chosenResources != Config.getInstance().getResourcesHandicap().get(matchState.getPlayerPosition(player))) {
                senders.get(event.getPlayerId()).sendObject(new PlayerActionError(event.getPlayerId(), "Wrong number of resources chosen", event));
                senders.get(event.getPlayerId()).sendObject(initialChoices.get(event.getPlayerId()));
                return;
            }

            this.leaderCardManager.assignLeaderCards(player, loadLeaderCards(event.getChosenLeaderCardIDs(), player));

            organizeResources(event.getChosenResources(), player);

            notifyPlayerDoneWithInitialDecisions(event.getPlayerId());
        } catch (IOException e) {
            senders.get(event.getPlayerId()).sendObject(new BadRequestEvent(event.getPlayerId(), "Invalid leader card ID(s)", event));
        } catch (NotPresentException notPresentException) {
            notPresentException.printStackTrace();
            //impossible
        }
    }

    private synchronized ArrayList<LeaderCard> loadLeaderCards(ArrayList<String> leaderCardIDs, Player player) throws IOException {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Requirement.class, new GsonInheritanceAdapter<Requirement>());
        builder.registerTypeAdapter(LeaderPower.class, new GsonInheritanceAdapter<LeaderPower>());
        builder.registerTypeAdapter(Pair.class, new GsonPairAdapter());
        Gson gson = builder.create();
        ArrayList<LeaderCard> leaderCards = new ArrayList<>();
        LeaderCardHandler leaderCardHandler = new LeaderCardHandler(this.senders, player);
        DepositLeaderPowerHandler depositLeaderPowerHandler = new DepositLeaderPowerHandler(this.senders, player);
        for (String leaderCardID : leaderCardIDs) {
            String leaderCardJSON;
            if (Config.getInstance().isLeaderCardDefault())
                leaderCardJSON = Files.readString(Paths.get("src\\main\\resources\\default\\" + leaderCardID + ".json"));
            else
                leaderCardJSON = Files.readString(Paths.get("src\\main\\resources\\" + leaderCardID + ".json"));
            LeaderCard lc = gson.fromJson(leaderCardJSON, LeaderCard.class);
            lc.addObserver(leaderCardHandler);
            for (LeaderPower lp : lc.getLeaderPowers()) {
                if (lp.getClass() == DepositLeaderPower.class)
                    lp.addObserver(depositLeaderPowerHandler);
            }
            leaderCards.add(lc);
        }
        return leaderCards;
    }

    private synchronized void notifyPlayerDoneWithInitialDecisions(String playerID){
        setuppedPlayers.add(playerID);
        for (Sender sender : senders.values())
            sender.sendObject(new SetupDoneEvent(playerID));

        //CHEATS

        //END_CHEATS

        if (setuppedPlayers.size() == matchState.getPlayers().size())
            matchState.beginMatch();
    }

    private synchronized void setDefaultInitialDecisions(Player player) {
        InitialChoicesEvent initialChoicesEvent = initialChoices.get(player.getPlayerId());
        if(initialChoicesEvent == null) return;
        ArrayList<String> leaderCardsIDs = new ArrayList<>();
        ArrayList<String> allLeaderCardsIDs = initialChoicesEvent.getLeaderCards();
        for(int i=0; i<Config.getInstance().getLeaderCardPerPlayerToChoose(); i++){
            leaderCardsIDs.add(allLeaderCardsIDs.get(i));
        }
        try {
            this.leaderCardManager.assignLeaderCards(player, loadLeaderCards(leaderCardsIDs, player));
        } catch (IOException e) {
            //impossible
            e.printStackTrace();
        }

        notifyPlayerDoneWithInitialDecisions(player.getPlayerId());
    }

    /**
     * Checks if it's the player turn and it's the right TurnSate for doing an certain action
     * @param event The event that represents the action that will be performed
     * @param player The player whose turn should be
     * @param turnState The state the turn should be in
     * @return If the action can be performed
     */
    private boolean canActionBePerformed(Event event, Player player, TurnState turnState){
        if(matchState.getTurnState() != turnState){
            senders.get(player.getPlayerId()).sendObject(new BadRequestEvent(player.getPlayerId(), "The action can't be performed now", event));
            return false;
        }
        if(matchState.getPlayers().get(matchState.getCurrentPlayerIndex())!=player) {
            senders.get(player.getPlayerId()).sendObject(new BadRequestEvent(player.getPlayerId(), "It is not the player turn", event));
            return false;
        }
        return true;
    }

    /**
     * Checks if it's the player turn and it's the right TurnSate for doing an certain action
     * @param event The event that represents the action that will be performed
     * @param player The player whose turn should be
     * @param turnStates A list containing all valid states the turn could be in
     * @return If the action can be performed
     */
    private boolean canActionBePerformed(Event event, Player player, ArrayList<TurnState> turnStates){
        if(!turnStates.contains(matchState.getTurnState())){
            senders.get(player.getPlayerId()).sendObject(new BadRequestEvent(player.getPlayerId(), "The action can't be performed now", event));
            return false;
        }
        if(matchState.getPlayers().get(matchState.getCurrentPlayerIndex())!=player) {
            senders.get(player.getPlayerId()).sendObject(new BadRequestEvent(player.getPlayerId(), "It is not the player turn", event));
            return false;
        }
        return true;
    }

    /**
     * Handler for BuyResourcesEvent
     */
    public synchronized void BuyResourcesEventHandler(PropertyChangeEvent evt){
        System.out.println("Entered into the handler of BuyResourcesEvent");
        BuyResourcesEvent event = (BuyResourcesEvent) evt.getNewValue();
        try {
            Player player = matchState.getPlayerFromID(event.getPlayerId());
            if(!canActionBePerformed(event, player, new ArrayList<>(){{add(TurnState.START); add(TurnState.AFTER_LEADER_CARD_ACTION);}})){
                return;
            }

            HashMap<Resource, Integer> resources = new HashMap<>();
            int faithPoints = 0;
            int whiteMarbles = 0;
            try {
                var marbles = marketManager.buy(
                        event.getDirection(),
                        event.getIndex());

                for(Marble m: marbles.keySet()) {
                    switch (m) {
                        case BLUE -> resources.put(Resource.SHIELD, marbles.get(m));
                        case GRAY -> resources.put(Resource.ROCK, marbles.get(m));
                        case YELLOW -> resources.put(Resource.COIN, marbles.get(m));
                        case PURPLE -> resources.put(Resource.SERVANT, marbles.get(m));
                        case RED -> faithPoints = marbles.get(m);
                        case WHITE -> whiteMarbles = marbles.get(m);
                    }
                }
            } catch (IllegalArgumentException e){
                senders.get(player.getPlayerId()).sendObject(new BadRequestEvent(player.getPlayerId(), "Index passed out of bound", event));
                return;
            }

            System.out.println("Marbles converted");

            var powers = leaderCardManager.getSelectedPowers(player, ExtraResourceLeaderPower.class)
                    .stream()
                    .map(x -> (ExtraResourceLeaderPower)x)
                    .collect(Collectors.toList());

            if(powers.size() > 1 && whiteMarbles>0)
            {
                System.out.println("multiple leader powers");
                ArrayList<Resource> resourceTypes= new ArrayList<>();
                for(ExtraResourceLeaderPower lp: powers){
                    Resource r = lp.getResourceType();
                    if(!resourceTypes.contains(r)) resourceTypes.add(r);
                }
                boolean goodChoice = false;
                while(!goodChoice) {
                    senders.get(player.getPlayerId()).sendObject(new ChoseMultipleExtraResourcePowerEvent(event.getPlayerId(), resourceTypes, whiteMarbles));
                    synchronized (waitingForSimpleResourcesLock){
                        try {
                            matchState.setWaitingForSomething();
                            waitingForSimpleResourcesLock.wait();
                            System.out.println("simpleChosenResourcesEvent passed to buyResources event");
                            matchState.somethingArrived();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    if(simpleChosenResourcesEvent == null){
                        System.err.println("The event is null");
                        return;
                    }

                    int chosenResourcesNum = 0;
                    HashMap<Resource, Integer> chosenResources = simpleChosenResourcesEvent.getAllResourcesChosen();
                    for (Resource r : chosenResources.keySet()) {
                        if (chosenResources.get(r) <= 0)
                            chosenResources.remove(r);
                        else
                            chosenResourcesNum += chosenResources.get(r);
                    }
                    if (!resourceTypes.containsAll(chosenResources.keySet())){
                        senders.get(player.getPlayerId()).sendObject(new PlayerActionError(player.getPlayerId(), "Some of the selected resource type are not allowed", simpleChosenResourcesEvent));
                    } else if(chosenResourcesNum > whiteMarbles) {
                        senders.get(player.getPlayerId()).sendObject(new PlayerActionError(player.getPlayerId(), "Too many resources selected", simpleChosenResourcesEvent));
                    } else if(chosenResourcesNum < whiteMarbles) {
                        senders.get(player.getPlayerId()).sendObject(new PlayerActionError(player.getPlayerId(), "Too few resources selected", simpleChosenResourcesEvent));
                    }else {
                        for (Resource r : chosenResources.keySet()) {
                            resources.put(r, resources.getOrDefault(r, 0) + chosenResources.get(r));
                        }
                        goodChoice = true;
                    }
                }
            }
            else if (powers.size() == 1 && whiteMarbles>0)
            {
                System.out.println("single leader power");
                Resource type = powers.get(0).getResourceType();
                resources.put(type, resources.getOrDefault(type, 0) + whiteMarbles);
            }
            System.out.println("ending");
            faithTrackManager.incrementFaithTrackPosition(player, faithPoints);
            organizeResources(resources, player);
            matchState.setTurnState(TurnState.AFTER_MAIN_ACTION);
        } catch (NotPresentException e) {
            System.out.println("The event" + event + "got sent to the wrong match");
            e.printStackTrace();
        }
    }

    /**
     * Asks a player to organize it's resources between warehouse and leader cards deposits
     * @param resources The resources to organize
     * @param player The player that should organize them
     */
    private synchronized void organizeResources(HashMap<Resource, Integer> resources, Player player) {
        boolean empty = true;
        for(Resource r: resources.keySet()){
            if(resources.get(r) > 0){
                empty = false;
                break;
            }
        }
        if(empty) return;

        boolean goodChoice = false;
        while(!goodChoice) {
            try {
                new DashBoardHandler(senders, player).update(player.getDashBoard());
                senders.get(player.getPlayerId()).sendObject(new OrganizeResourcesEvent(player.getPlayerId(), resources));
                synchronized (waitingForResourceOrganizationLock) {
                    try {
                        matchState.setWaitingForSomething();
                        playerWaitingForResourceOrganization = player.getPlayerId();
                        waitingForResourceOrganizationLock.wait();
                        matchState.somethingArrived();
                    }catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                if (newResourcesOrganizationEvent == null) return;
                HashMap<Resource, Integer> newTotalStoredResources = new HashMap<>();
                HashMap<Resource, Integer> discardedResources = newResourcesOrganizationEvent.getDiscardedResources();
                //validate deposit state
                ArrayList<DepotState> eventDepotStates = newResourcesOrganizationEvent.getDepotStates();
                ArrayList<DepotState> orderedDepotStates = new ArrayList<>();
                boolean isNewDepotStateOK = true;
                for (Depot depot : player.getDashBoard().getWarehouse()) {
                    if (isNewDepotStateOK) {
                        isNewDepotStateOK = false;
                        for (DepotState depotState : eventDepotStates) {
                            if (!isNewDepotStateOK && depot.getMaxQuantity() == depotState.getMaxQuantity()) {
                                isNewDepotStateOK = true;
                                orderedDepotStates.add(depotState);
                            }
                        }
                    }
                }

                for (DepotState depotState : eventDepotStates) {
                    Resource type = depotState.getResourceType();
                    newTotalStoredResources.put(type, newTotalStoredResources.getOrDefault(type, 0) + depotState.getCurrentQuantity());
                }

                //validate leader power state
                if (!isNewDepotStateOK || eventDepotStates.size()!=orderedDepotStates.size()) {
                    throw new HandlerCheckException(new PlayerActionError(player.getPlayerId(), "The structure of the warehouse sent is incompatible with the one of the match", newResourcesOrganizationEvent));
                }

                for (DepositLeaderPowerStateEvent e : newResourcesOrganizationEvent.getLeaderPowersState()) {
                    HashMap<Resource, Integer> storedResource = e.getStoredResources();
                    LeaderCard lc = player.getLeaderCardFromID(e.getLeaderCardID());
                    if (!player.getActiveLeaderCards().contains(lc))
                        throw new HandlerCheckException(new LeaderCardNotActiveError(player.getPlayerId(), lc.getCardID()));
                    LeaderPower lp = lc.getLeaderPowers().get(e.getLeaderPowerIndex());
                    if (lp.getClass() != DepositLeaderPower.class)
                        throw new HandlerCheckException(new PlayerActionError(player.getPlayerId(), "One of the leader powers is of the wrong type", newResourcesOrganizationEvent));
                    DepositLeaderPower dlp = (DepositLeaderPower) lp;
                    if (!new DepositLeaderPower(dlp.getMaxResources()).canStore(storedResource))
                        throw new HandlerCheckException(new PlayerActionError(player.getPlayerId(), "One of the selected leader powers can't store the resources indicated with it", newResourcesOrganizationEvent));
                    for (Resource r : storedResource.keySet()) {
                        newTotalStoredResources.put(r, storedResource.get(r) + dlp.getCurrentResources().getOrDefault(r, 0));
                    }
                }

                //check resources chosen
                HashMap<Resource, Integer> oldTotalStoredResources = player.getLeaderCardsResources();
                HashMap<Resource, Integer> oldWarehouseStoredResources = player.getDashBoard().getWarehouseResources();
                for(Resource r: oldWarehouseStoredResources.keySet()){
                    oldTotalStoredResources.put(r, oldTotalStoredResources.getOrDefault(r, 0) + oldWarehouseStoredResources.get(r));
                }

                System.out.println("RESOURCE BALANCE");
                System.out.println(resources);
                System.out.println(newTotalStoredResources);
                System.out.println(discardedResources);
                System.out.println(oldTotalStoredResources);

                for (Resource r : resources.keySet()) {
                    if (resources.get(r) != newTotalStoredResources.getOrDefault(r, 0) + discardedResources.getOrDefault(r, 0) - oldTotalStoredResources.getOrDefault(r, 0))
                        throw new HandlerCheckException(new PlayerActionError(player.getPlayerId(), "Total number of each resource given back does not correspond to the number sent", newResourcesOrganizationEvent));
                }

                goodChoice = true;

                player.getDashBoard().setWarehouseResources(orderedDepotStates);

                for (DepositLeaderPowerStateEvent e : newResourcesOrganizationEvent.getLeaderPowersState()) {
                    HashMap<Resource, Integer> storedResource = e.getStoredResources();
                    try {
                        LeaderCard lc = player.getLeaderCardFromID(e.getLeaderCardID());
                        DepositLeaderPower lp = (DepositLeaderPower) lc.getLeaderPowers().get(e.getLeaderPowerIndex());
                        lp.removeResources(lp.getCurrentResources());
                        lp.addResources(storedResource);
                    } catch (ResourcesLimitsException | NotPresentException exception) {
                        //impossible
                        exception.printStackTrace();
                    }
                }

                int numDiscardedResources = 0;
                for (Resource r : discardedResources.keySet()) {
                    numDiscardedResources += discardedResources.get(r);
                }
                for (Player p : matchState.getPlayers()) {
                    if (p != player)
                        faithTrackManager.incrementFaithTrackPosition(p, numDiscardedResources);
                }
            } catch (NotPresentException notPresentException) {
                goodChoice = false;
                senders.get(player.getPlayerId()).sendObject(new BadRequestEvent(player.getPlayerId(), "One of the leader cards does not belong to this player", newResourcesOrganizationEvent));
            } catch (HandlerCheckException e) {
                goodChoice = false;
                Event eventToSend = e.getEventToSend();
                if(eventToSend != null) senders.get(player.getPlayerId()).sendObject(eventToSend);
            }
        }
    }

    /**
     * Handler for NewResourcesOrganizationEvent
     */
    public void NewResourcesOrganizationEventHandler(PropertyChangeEvent evt){
        NewResourcesOrganizationEvent event = (NewResourcesOrganizationEvent) evt.getNewValue();
        synchronized (waitingForResourceOrganizationLock){
            try {
                if(setuppedPlayers.contains(event.getPlayerId())) {
                    if (!canActionBePerformed(event, matchState.getPlayerFromID(event.getPlayerId()), TurnState.WAITING_FOR_SOMETHING))
                        return;
                }
                else {
                    if(matchState.getTurnState() != TurnState.WAITING_FOR_SOMETHING){
                        senders.get(event.getPlayerId()).sendObject(new BadRequestEvent(event.getPlayerId(), "The action can't be performed now", event));
                        return;
                    }
                    else if(!event.getPlayerId().equals(playerWaitingForResourceOrganization)) {
                        senders.get(event.getPlayerId()).sendObject(new BadRequestEvent(event.getPlayerId(), "It is not the player turn", event));
                        return;
                    }
                }
            } catch (NotPresentException notPresentException) {
                //impossible
                notPresentException.printStackTrace();
            }
            newResourcesOrganizationEvent = event;
            waitingForResourceOrganizationLock.notifyAll();
        }
    }

    /**
     * Handler for LeaderPowerSelectStateEvent
     */
    public synchronized void LeaderPowerSelectStateEventHandler(PropertyChangeEvent evt){
        LeaderPowerSelectStateEvent event = (LeaderPowerSelectStateEvent) evt.getNewValue();
        try {
            Player player = matchState.getPlayerFromID(event.getPlayerId());
            if(!canActionBePerformed(event, player, new ArrayList<>(){{add(TurnState.START); add(TurnState.AFTER_LEADER_CARD_ACTION);}})) return;
            LeaderCard leaderCard = player.getLeaderCardFromID(event.getLeaderCardID());
            if(event.getLeaderPowerIndex()>=leaderCard.getLeaderPowers().size()) {
                senders.get(event.getPlayerId()).sendObject(new BadRequestEvent(event.getPlayerId(), "Leader power index to big", event));
                return;
            }
            try {
                if (event.isStateSelected())
                    leaderCardManager.selectLeaderPower(player, leaderCard,
                            leaderCard.getLeaderPowers().get(event.getLeaderPowerIndex()));
                else
                    leaderCardManager.deselectLeaderPower(player, leaderCard,
                            leaderCard.getLeaderPowers().get(event.getLeaderPowerIndex()));

                new MatchStateHandler(senders).update(matchState);
            } catch (NotPresentException notPresentException) {
                senders.get(event.getPlayerId()).sendObject(new BadRequestEvent(event.getPlayerId(), "Leader card not owned by this player", event));
                System.out.println(notPresentException.getMessage());
            } catch (IllegalOperation illegalOperation) {
                senders.get(event.getPlayerId()).sendObject(new PlayerActionError(event.getPlayerId(), "Leader power state already equal to the one passed", event));
                new MatchStateHandler(senders).update(matchState);
            } catch (LeaderCardNotActiveException e) {
                senders.get(event.getPlayerId()).sendObject(new LeaderCardNotActiveError(event.getPlayerId(), event.getLeaderCardID()));
                new MatchStateHandler(senders).update(matchState);
            } catch (IncompatiblePowersException e) {
                senders.get(event.getPlayerId()).sendObject(new IncompatiblePowersError(event.getPlayerId(), event.getLeaderCardID(), event.getLeaderPowerIndex()));
                new MatchStateHandler(senders).update(matchState);
            }
        } catch (NotPresentException e) {
            //impossible
            e.printStackTrace();
        }
    }

    /**
     * Handler for ActivateLeaderCardEvent
     */
    public synchronized void ActivateLeaderCardEventHandler(PropertyChangeEvent evt){
        ActivateLeaderCardEvent event = (ActivateLeaderCardEvent) evt.getNewValue();
        try {
            Player player = matchState.getPlayerFromID(event.getPlayerId());
            if(matchState.leaderActionExecuted){
                senders.get(event.getPlayerId()).sendObject(new BadRequestEvent(event.getPlayerId(), "The player already executed a leader card action this turn", event));
                return;
            }
            if(!canActionBePerformed(event, player, new ArrayList<>(){{add(TurnState.START); add(TurnState.AFTER_MAIN_ACTION);}})) return;
            LeaderCard leaderCard = player.getLeaderCardFromID(event.getLeaderCardID());
            try{
                leaderCardManager.activateLeaderCard(player, leaderCard);
                matchState.leaderActionExecuted = true;
                if(matchState.getTurnState() == TurnState.START) matchState.setTurnState(TurnState.AFTER_LEADER_CARD_ACTION);
                else matchState.setTurnState(TurnState.END_OF_TURN);
            } catch (IllegalOperation illegalOperation) {
                senders.get(event.getPlayerId()).sendObject(new PlayerActionError(event.getPlayerId(), "Leader Card state already active", event));
                new MatchStateHandler(senders).update(matchState);
            } catch (RequirementsNotMetException requirementsNotMetException) {
                senders.get(event.getPlayerId()).sendObject(new RequirementsNotMetError(event.getPlayerId(), event.getLeaderCardID()));
                new MatchStateHandler(senders).update(matchState);
            } catch (NotPresentException notPresentException) {
                senders.get(event.getPlayerId()).sendObject(new BadRequestEvent(event.getPlayerId(), "Leader card not owned by this player", event));
                System.out.println(notPresentException.getMessage());
            }
        } catch (NotPresentException e) {
            //Impossible
            e.printStackTrace();
        }
    }

    /**
     * Handler for BuyDevCardsEvent
     */
    public synchronized void BuyDevCardsEventHandler(PropertyChangeEvent evt){
        BuyDevCardsEvent event = (BuyDevCardsEvent) evt.getNewValue();

        DevCardGrid devCardGrid = matchState.getDevCardGrid();
        try {
            Player player = matchState.getPlayerFromID(event.getPlayerId());
            if(!canActionBePerformed(event, player, new ArrayList<>(){{add(TurnState.START); add(TurnState.AFTER_LEADER_CARD_ACTION);}})) return;
            Pair<Integer, Integer> devDeckIndexes = devCardGrid.getRowColOfCardFromID(event.getDevCardID());
            DevCard devCard = devCardGrid.topCard(devDeckIndexes);
            HashMap<Resource, Integer> cardCost = devCard.getCost();
            ArrayList<LeaderPower> leaderPowers = leaderCardManager.getSelectedPowers(player, DiscountLeaderPower.class);
            HashMap<Resource, Integer> allPlayerResourcesWithDiscount = player.getAllPayerResources();
            for(LeaderPower lp: leaderPowers){
                DiscountLeaderPower dlp = (DiscountLeaderPower) lp;
                HashMap<Resource, Integer> discount = dlp.getDiscount();
                for(Resource r: discount.keySet()){
                    if(cardCost.containsKey(r)){
                        cardCost.put(r, Math.min(cardCost.get(r) - discount.get(r), 0));
                    }
                    if(allPlayerResourcesWithDiscount.containsKey(r)){
                        allPlayerResourcesWithDiscount.put(r, allPlayerResourcesWithDiscount.get(r) + discount.get(r));
                    }
                }
            }

            HashMap<Resource, Integer> leaderPowerResources = player.getLeaderCardsResources();
            HashMap<Resource, Integer> selectedResourcesFromLeaderPower = new HashMap<>();
            HashMap<Resource, Integer> selectedResourcesFromWarehouse = new HashMap<>();
            HashMap<Resource, Integer> resourcesFromStrongBox = new HashMap<>();
            if (!devCard.checkCost(allPlayerResourcesWithDiscount)) {
                senders.get(event.getPlayerId()).sendObject(new CantAffordError(event.getPlayerId(), event.getDevCardID()));
                new MatchStateHandler(senders).update(matchState);
                return;
            }

            if (!player.getDashBoard().checkSlot(event.getCardSlot(), devCard)) {
                senders.get(event.getPlayerId()).sendObject(new DevCardSlotError(event.getPlayerId(), event.getDevCardID(), event.getCardSlot()));
                new MatchStateHandler(senders).update(matchState);
                return;
            }

            boolean goodChoice = false;
            while(!goodChoice) {
                try {
                    senders.get(event.getPlayerId()).sendObject(new ChoseResourcesEvent(event.getPlayerId(), cardCost, 0));

                    synchronized (waitingForResourcesLock){
                        try {
                            matchState.setWaitingForSomething();
                            waitingForResourcesLock.wait();
                            matchState.somethingArrived();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    if(chosenResourcesEvent==null) return;

                    selectedResourcesFromLeaderPower = chosenResourcesEvent.getSelectedResourcesFromLeaderPowers();
                    selectedResourcesFromWarehouse = chosenResourcesEvent.getSelectedResourcesFromWarehouse();

                    for (Resource r : cardCost.keySet()) {
                        int resourceQuantity = cardCost.get(r) - selectedResourcesFromLeaderPower.get(r) - selectedResourcesFromWarehouse.get(r);
                        if (resourceQuantity < 0) {
                            throw new HandlerCheckException(new PlayerActionError(event.getPlayerId(), "Too many resources selected from leader powers or warehouse", event));
                        }
                        resourcesFromStrongBox.put(r, resourceQuantity);
                    }

                    for (Resource r : selectedResourcesFromLeaderPower.keySet()) {
                        if (selectedResourcesFromLeaderPower.get(r) > leaderPowerResources.get(r)) {
                            throw new HandlerCheckException(new PlayerActionError(event.getPlayerId(), "Selected resources from leader powers not present", event));
                        }
                    }

                    player.getDashBoard().subResourcesToWarehouse(selectedResourcesFromWarehouse);

                    goodChoice = true;
                } catch (HandlerCheckException e){
                    goodChoice = false;
                    Event eventToSend = e.getEventToSend();
                    if(eventToSend != null) senders.get(player.getPlayerId()).sendObject(eventToSend);
                } catch (ResourcesLimitsException e) {
                    goodChoice = false;
                    senders.get(event.getPlayerId()).sendObject(new PlayerActionError(event.getPlayerId(), "Selected resources from warehouse not present", event));
                }
            }
            leaderCardManager.removeResourcesFromLeaderCards(player, selectedResourcesFromLeaderPower);
            for(Resource r: resourcesFromStrongBox.keySet())
                player.getDashBoard().subResourcesToStrongBox(r, resourcesFromStrongBox.get(r));
            player.getDashBoard().addCard(event.getCardSlot(), devCard);

            int totalDevCard = 0;
            for(Stack<DevCard> stack: player.getDashBoard().getCardSlots())
                totalDevCard += stack.size();

            if(totalDevCard >= Config.getInstance().getDevCardsToWin())
                matchState.setLastRound();

            matchState.setTurnState(TurnState.AFTER_MAIN_ACTION);
        } catch (NotPresentException notPresentException) {
            //The card is not at the top of any devDeck
            senders.get(event.getPlayerId()).sendObject(new BadRequestEvent(event.getPlayerId(), notPresentException.getMessage(), event));
        }catch (EmptyStrongboxException | IndexSlotException | LevelCardException e) {
            //impossible
            e.printStackTrace();
        }
    }

    /**
     * Handler for DiscardLeaderCardEvent
     */
    public synchronized void DiscardLeaderCardEventHandler(PropertyChangeEvent evt){
        DiscardLeaderCardEvent event = (DiscardLeaderCardEvent) evt.getNewValue();

        try {
            Player player = matchState.getPlayerFromID(event.getPlayerId());
            if(matchState.leaderActionExecuted){
                senders.get(event.getPlayerId()).sendObject(new BadRequestEvent(event.getPlayerId(), "The player already executed a leader card action this turn", event));
                return;
            }
            if(!canActionBePerformed(event, player, new ArrayList<>(){{add(TurnState.START); add(TurnState.AFTER_MAIN_ACTION);}})) return;
            leaderCardManager.removeLeaderCard(player, player.getLeaderCardFromID(event.getLeaderCardID()));

            faithTrackManager.incrementFaithTrackPosition(player, Config.getInstance().getFaithPointPerDiscardedResource());

            matchState.leaderActionExecuted = true;
            if(matchState.getTurnState() == TurnState.START) matchState.setTurnState(TurnState.AFTER_LEADER_CARD_ACTION);
            else matchState.setTurnState(TurnState.END_OF_TURN);
        } catch (NotPresentException notPresentException) {
            senders.get(event.getPlayerId()).sendObject(new BadRequestEvent(event.getPlayerId(), "Leader card not owned by this player", event));
            notPresentException.printStackTrace();
        } catch (IllegalOperation illegalOperation) {
            senders.get(event.getPlayerId()).sendObject(new PlayerActionError(event.getPlayerId(), "Leader card already active", event));
            new MatchStateHandler(senders).update(matchState);
            illegalOperation.printStackTrace();
        }
    }

    /**
     * Handler for ActivateProductionEvent
     */
    public synchronized void ActivateProductionEventHandler(PropertyChangeEvent evt) {
        ActivateProductionEvent event = (ActivateProductionEvent) evt.getNewValue();

        if (!event.isPersonalPowerActive() && event.getDevCards().size() == 0) {
            senders.get(event.getPlayerId()).sendObject(new PlayerActionError(event.getPlayerId(), "You must select at least one production power", event));
            new MatchStateHandler(senders).update(matchState);
            return;
        }

        HashMap<Resource, Integer> consumedResources = new HashMap<>();
        HashMap<Resource, Integer> producedResources = new HashMap<>();
        int requiredResourceOfChoice = 0;
        int producedResourceOfChoice = 0;
        int faithPointsProduced = 0;

        try {
            Player player = matchState.getPlayerFromID(event.getPlayerId());
            if (!canActionBePerformed(event, player, new ArrayList<>() {{
                add(TurnState.START);
                add(TurnState.AFTER_LEADER_CARD_ACTION);
            }})) return;
            ArrayList<DevCard> topDevCards = player.getDashBoard().getTopCards();
            for (String devCardID : event.getDevCards()) {
                int index = -1;
                for (int i = 0; i < topDevCards.size(); i++) {
                    if (topDevCards.get(i).getCardID().equals(devCardID)) index = i;
                }
                if (index == -1) {
                    senders.get(event.getPlayerId()).sendObject(new BadRequestEvent(event.getPlayerId(), "DevCard not at the top of any of the player slot", event));
                    return;
                }

                //can probably be it's own function returning a cumulative production power
                ProductionPower productionPower = topDevCards.get(index).getProductionPower();
                HashMap<Resource, Integer> cr = productionPower.getConsumedResources();
                for (Resource r : cr.keySet()) {
                    consumedResources.put(r, cr.get(r) + consumedResources.getOrDefault(r, 0));
                }
                HashMap<Resource, Integer> pr = productionPower.getConsumedResources();
                for (Resource r : pr.keySet()) {
                    producedResources.put(r, pr.get(r) + producedResources.getOrDefault(r, 0));
                }
                requiredResourceOfChoice += productionPower.getRequiredResourceOfChoice();
                producedResourceOfChoice += productionPower.getProducedResourceOfChoice();
                faithPointsProduced += productionPower.getFaithPointsProduced();
            }
            if (event.isPersonalPowerActive()) {
                ProductionPower productionPower = player.getDashBoard().getPersonalPower();
                HashMap<Resource, Integer> cr = productionPower.getConsumedResources();
                for (Resource r : cr.keySet()) {
                    consumedResources.put(r, cr.get(r) + consumedResources.getOrDefault(r, 0));
                }
                HashMap<Resource, Integer> pr = productionPower.getConsumedResources();
                for (Resource r : pr.keySet()) {
                    producedResources.put(r, pr.get(r) + producedResources.getOrDefault(r, 0));
                }
                requiredResourceOfChoice += productionPower.getRequiredResourceOfChoice();
                producedResourceOfChoice += productionPower.getProducedResourceOfChoice();
                faithPointsProduced += productionPower.getFaithPointsProduced();
            }

            HashMap<Resource, Integer> allPlayerResources = player.getAllPayerResources();
            int surplusOfResources = 0;
            for (Resource r : allPlayerResources.keySet()) {
                int diff = allPlayerResources.get(r) - consumedResources.getOrDefault(r, 0);
                if (diff < 0) {
                    senders.get(event.getPlayerId()).sendObject(new PlayerActionError(event.getPlayerId(), "Not enough resources for the production powers chosen", event));
                    new MatchStateHandler(senders).update(matchState);
                    return;
                }
                surplusOfResources += diff;
            }
            if (surplusOfResources < requiredResourceOfChoice) {
                senders.get(event.getPlayerId()).sendObject(new PlayerActionError(event.getPlayerId(), "Not enough resources for the production powers chosen", event));
                new MatchStateHandler(senders).update(matchState);
                return;
            }

            HashMap<Resource, Integer> selectedResourcesFromLeaderPowers = new HashMap<>();
            HashMap<Resource, Integer> selectedResourcesFromWarehouse = new HashMap<>();
            HashMap<Resource, Integer> allResourcesSelected = new HashMap<>();

            boolean goodChoice = false;
            while (!goodChoice) {
                try {
                    senders.get(event.getPlayerId()).sendObject(new ChoseResourcesEvent(event.getPlayerId(), consumedResources, requiredResourceOfChoice));

                    synchronized (waitingForResourcesLock) {
                        try {
                            matchState.setWaitingForSomething();
                            waitingForResourcesLock.wait();
                            matchState.somethingArrived();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    if (chosenResourcesEvent == null) return;

                    //check resources chosen to produce
                    selectedResourcesFromLeaderPowers = chosenResourcesEvent.getSelectedResourcesFromLeaderPowers();
                    selectedResourcesFromWarehouse = chosenResourcesEvent.getSelectedResourcesFromWarehouse();

                    HashMap<Resource, Integer> wareHouseResources = player.getDashBoard().getWarehouseResources();
                    for (Resource r : wareHouseResources.keySet()) {
                        if (wareHouseResources.get(r) < selectedResourcesFromWarehouse.getOrDefault(r, 0)) {
                            throw new HandlerCheckException(new PlayerActionError(event.getPlayerId(), "Selected resources from warehouse not present", event));
                        }
                    }

                    HashMap<Resource, Integer> leaderPowerResources = player.getLeaderCardsResources();
                    for (Resource r : leaderPowerResources.keySet()) {
                        if (leaderPowerResources.get(r) < selectedResourcesFromLeaderPowers.getOrDefault(r, 0)) {
                            throw new HandlerCheckException(new PlayerActionError(event.getPlayerId(), "Selected resources from leader powers not present", event));
                        }
                    }

                    allResourcesSelected = chosenResourcesEvent.getAllResourcesChosen();
                    int extraResources = 0;
                    for (Resource r : allResourcesSelected.keySet()) {
                        if (allPlayerResources.get(r) < allResourcesSelected.get(r)) {
                            throw new HandlerCheckException(new PlayerActionError(event.getPlayerId(), "The selected resources are not present in the player inventory", event));
                        }
                        int difference = allResourcesSelected.get(r) - consumedResources.getOrDefault(r, 0);
                        if (difference < 0) {
                            throw new HandlerCheckException(new PlayerActionError(event.getPlayerId(), "Too few resources chosen", event));
                        }
                        extraResources += difference;
                    }
                    if (extraResources < requiredResourceOfChoice) {
                        throw new HandlerCheckException(new PlayerActionError(event.getPlayerId(), "Too few resources to consume chosen", event));
                    }
                    if (extraResources > requiredResourceOfChoice) {
                        throw new HandlerCheckException(new PlayerActionError(event.getPlayerId(), "Too many resources to consume chosen", event));
                    }
                    goodChoice = true;
                } catch (HandlerCheckException e) {
                    goodChoice = false;
                    Event eventToSend = e.getEventToSend();
                    if (eventToSend != null) senders.get(player.getPlayerId()).sendObject(e.getEventToSend());
                }

            }

            //chose PRODUCED resource of choice
            int numChosenResources = 0;
            HashMap<Resource, Integer> chosenResources = new HashMap<>();
            goodChoice = false;
            while (!goodChoice && producedResourceOfChoice>0) {
                senders.get(event.getPlayerId()).sendObject(new SimpleChoseResourcesEvent(event.getPlayerId(), producedResourceOfChoice));

                synchronized (waitingForSimpleResourcesLock) {
                    try {
                        matchState.setWaitingForSomething();
                        waitingForSimpleResourcesLock.wait();
                        matchState.somethingArrived();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                if (simpleChosenResourcesEvent == null) return;

                //check produced resource of choice
                chosenResources = simpleChosenResourcesEvent.getAllResourcesChosen();
                for (Resource r : chosenResources.keySet()) {
                    numChosenResources += chosenResources.get(r);
                }
                if (numChosenResources < producedResourceOfChoice) {
                    senders.get(event.getPlayerId()).sendObject(new PlayerActionError(event.getPlayerId(), "Too few resources to produce chosen", event));
                    goodChoice = false;
                } else if (numChosenResources > producedResourceOfChoice) {
                    senders.get(event.getPlayerId()).sendObject(new PlayerActionError(event.getPlayerId(), "Too many resources to produce chosen", event));
                    goodChoice = false;
                } else
                    goodChoice = true;
            }

            //produce
            player.getDashBoard().subResourcesToWarehouse(selectedResourcesFromWarehouse);
            leaderCardManager.removeResourcesFromLeaderCards(player, selectedResourcesFromLeaderPowers);
            for (Resource r : allResourcesSelected.keySet()) {
                player.getDashBoard().subResourcesToStrongBox(r, allResourcesSelected.get(r) - selectedResourcesFromWarehouse.getOrDefault(r, 0) - selectedResourcesFromLeaderPowers.getOrDefault(r, 0));
            }

            for (Resource r : Resource.values())
                player.getDashBoard().addResourcesToStrongBox(r, producedResources.getOrDefault(r, 0) + chosenResources.getOrDefault(r, 0));

            faithTrackManager.incrementFaithTrackPosition(player, faithPointsProduced);
            matchState.setTurnState(TurnState.AFTER_MAIN_ACTION);

        } catch (NotPresentException | ResourcesLimitsException | EmptyStrongboxException notPresentException) {
            //impossible
            notPresentException.printStackTrace();
        }

    }

    /**
     * Handler for EndTurnEvent
     */
    public synchronized void EndTurnEventHandler(PropertyChangeEvent evt){
        EndTurnEvent event = (EndTurnEvent) evt.getNewValue();

        try {
            Player player = matchState.getPlayerFromID(event.getPlayerId());
            if(!canActionBePerformed(event, player, new ArrayList<>(){{add(TurnState.AFTER_MAIN_ACTION); add(TurnState.END_OF_TURN);}})) return;
            if(matchState.getClass() != SinglePlayerMatchState.class)
                nextTurn(player);
            else
                doIATurn();
        } catch (NotPresentException notPresentException) {
            //impossible
            notPresentException.printStackTrace();
        }
    }

    /**
     * Does the actions Lorenzo il Magnifico must do between player's turns
     */
    private void doIATurn() {
        SinglePlayerMatchState singlePlayerMatchState = (SinglePlayerMatchState) matchState;
        try {
            Player player =  singlePlayerMatchState.getPlayer();
            SoloActionToken token = singlePlayerMatchState.popSoloActionTokens();
            boolean endGame = token.doAction(singlePlayerMatchState);
            senders.get(player.getPlayerId()).sendObject(new IAActionEvent(player.getPlayerId(), token));
            if(!endGame){
                for (LeaderCard lc : player.getActiveLeaderCards()) {
                    for (LeaderPower lp : lc.getSelectedLeaderPowers()) {
                        leaderCardManager.deselectLeaderPower(player, lc, lp);
                    }
                }
                if(singlePlayerMatchState.isLastRound() && singlePlayerMatchState.getCurrentPlayerIndex() == singlePlayerMatchState.getPlayers().size() - 1)
                    endGame();
                else
                    singlePlayerMatchState.nextTurn();
            }
            else{
                senders.get(player.getPlayerId()).sendObject(new SinglePlayerLostEvent(player.getPlayerId()));
                endGame();
            }
        } catch (IllegalOperation illegalOperation) {
            System.err.println("The token pile is empty");
        } catch (NotPresentException | LeaderCardNotActiveException notPresentException) {
            //impossible
            notPresentException.printStackTrace();
        }
    }

    /**
     * Passes the turn to the next player
     * @param previousPlayer The player that was taking it's turn until now
     */
    private void nextTurn(Player previousPlayer){
        try {
            for (LeaderCard lc : previousPlayer.getActiveLeaderCards()) {
                for (LeaderPower lp : lc.getSelectedLeaderPowers()) {
                    leaderCardManager.deselectLeaderPower(previousPlayer, lc, lp);
                }
            }

            boolean allAFK = true;
            for(Player p: matchState.getPlayers()) {
                if (allAFK) allAFK = disconnected.getOrDefault(p.getPlayerId(), true);
            }
            if(allAFK) endGame(); //TODO For now, maybe FA

            if (allAFK || (matchState.isLastRound() && matchState.getCurrentPlayerIndex() == matchState.getPlayers().size() - 1))
                endGame();
            else {
                do {
                    matchState.nextTurn();
                } while (disconnected.get(matchState.getPlayers().get(matchState.getCurrentPlayerIndex()).getPlayerId()));
            }
        } catch (NotPresentException | IllegalOperation | LeaderCardNotActiveException notPresentException) {
            //impossible
            notPresentException.printStackTrace();
        }
    }

    /**
     * Ends the game, notifying all the client of it, and sending them the leaderboard
     */
    private synchronized void endGame() {
        ArrayList<FinalPlayerState> finalPlayerStates = createLeaderboard();
        matchState.setTurnState(TurnState.MATCH_ENDED);
        for(String playerID: senders.keySet()){
            senders.get(playerID).sendObject(new GameEndedEvent(playerID, finalPlayerStates));
        }

        for(PropertyChangeListener listener :this.eventsRegistry.getAllPropertyChangeListener()){
            this.eventsRegistry.removePropertyChangeListener(listener);
        }
    }

    /**
     * Creates the leaderboard for the match
     * @return the state of each player, ordered in ascending order (from last to first)
     */
    private synchronized ArrayList<FinalPlayerState> createLeaderboard() {
        ArrayList<FinalPlayerState> finalPlayerStates = new ArrayList<>();

        for(Player p: matchState.getPlayers()){
            int totalPoints = 0;

            //devCards
            for(Stack<DevCard> stack: p.getDashBoard().getCardSlots()){
                for(DevCard devCard: stack){
                    totalPoints+=devCard.getVictoryPoints();
                }
            }

            //FaithTrack
            FaithTrackData faithTrackData = p.getDashBoard().getFaithTrackData();
            totalPoints += FaithTrack.getArrayOfCells().get(faithTrackData.getPosition()).getVictoryPoints();
            totalPoints += faithTrackData.getFavorPopeCardPoints();

            //LeaderCards
            for(LeaderCard lc: p.getLeaderCards())
                totalPoints+=lc.getVictoryPoints();

            //Resources
            int allResources = 0;
            HashMap<Resource, Integer> allPlayerResources = p.getAllPayerResources();
            for(Resource r: allPlayerResources.keySet())
                allResources += allPlayerResources.get(r);

            totalPoints+= allResources/Config.getInstance().getResourcesPerVictoryPoint();

            finalPlayerStates.add(new FinalPlayerState(p.getPlayerId(), totalPoints, allResources));
        }

        Collections.sort(finalPlayerStates);

        return finalPlayerStates;
    }

    /**
     * Handler for ChosenResourcesEvent
     */
    public void ChosenResourcesEventHandler(PropertyChangeEvent evt){
        ChosenResourcesEvent event = (ChosenResourcesEvent) evt.getNewValue();
        synchronized (waitingForResourcesLock){
            try {
                if(!canActionBePerformed(event, matchState.getPlayerFromID(event.getPlayerId()), TurnState.WAITING_FOR_SOMETHING)) return;
            } catch (NotPresentException notPresentException) {
                //impossible
                notPresentException.printStackTrace();
            }
            chosenResourcesEvent = event;
            waitingForResourcesLock.notifyAll();
        }
    }

    /**
     * Handler for SimpleChosenResourcesEvent
     */
    public void SimpleChosenResourcesEventHandler(PropertyChangeEvent evt){
        SimpleChosenResourcesEvent event = (SimpleChosenResourcesEvent) evt.getNewValue();
        synchronized (waitingForSimpleResourcesLock){
            try {
                if(!canActionBePerformed(event, matchState.getPlayerFromID(event.getPlayerId()), TurnState.WAITING_FOR_SOMETHING)) return;
            } catch (NotPresentException notPresentException) {
                //impossible
                notPresentException.printStackTrace();
            }
            simpleChosenResourcesEvent = event;
            waitingForSimpleResourcesLock.notifyAll();
        }
    }

    /**
     * Handler for QuitGameEvent
     */
    public void QuitGameEventHandler(PropertyChangeEvent evt){
        QuitGameEvent event = (QuitGameEvent) evt.getNewValue();

        if(event.getPlayerId().equals(matchState.getPlayers().get(matchState.getCurrentPlayerIndex()).getPlayerId())) {
            synchronized (waitingForSimpleResourcesLock) {
                simpleChosenResourcesEvent = null;
                waitingForSimpleResourcesLock.notifyAll();
            }

            synchronized (waitingForResourcesLock) {
                chosenResourcesEvent = null;
                waitingForResourcesLock.notifyAll();
            }

            synchronized (waitingForResourceOrganizationLock) {
                newResourcesOrganizationEvent = null;
                waitingForResourceOrganizationLock.notifyAll();
            }
        }

        synchronized (this) {
            disconnected.put(event.getPlayerId(), true);
            senders.remove(event.getPlayerId());
            try {
                Player disconnectedPlayer = matchState.getPlayerFromID(event.getPlayerId());
                if(!setuppedPlayers.contains(event.getPlayerId())){
                    setDefaultInitialDecisions(disconnectedPlayer);
                }
                if(disconnectedPlayer == matchState.getPlayers().get(matchState.getCurrentPlayerIndex()))
                    nextTurn(disconnectedPlayer);
            } catch (NotPresentException notPresentException) {
                notPresentException.printStackTrace();
            }
        }
    }

}
