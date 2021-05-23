package it.polimi.ingsw.controller;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.events.ControllerEvents.MatchEvents.ChosenMultipleExtraResourcePowerEvent;
import it.polimi.ingsw.controller.modelChangeHandlers.DepositLeaderPowerHandler;
import it.polimi.ingsw.controller.modelChangeHandlers.LeaderCardHandler;
import it.polimi.ingsw.events.ClientEvents.*;
import it.polimi.ingsw.events.ControllerEvents.MatchEvents.*;
import it.polimi.ingsw.events.Event;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.DevCards.DevCard;
import it.polimi.ingsw.model.DevCards.DevCardGrid;
import it.polimi.ingsw.model.LeaderCards.*;
import it.polimi.ingsw.utilities.GsonInheritanceAdapter;
import it.polimi.ingsw.utilities.Pair;
import it.polimi.ingsw.utilities.PropertyChangeSubject;
import it.polimi.ingsw.virtualview.ClientHandlerSender;
import org.reflections.Reflections;

import java.beans.PropertyChangeEvent;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.stream.Collectors;

//TODO config
//TODO end of game
//TODO disconnection
//TODO reorganize

public class Controller {
    MarketManager marketManager;
    LeaderCardManager leaderCardManager;
    FaithTrackManager faithTrackManager;
    MatchState matchState;
    HashMap<String, ClientHandlerSender> clientHandlerSenders;
    private final ArrayList<String> setuppedPlayers;

    private final Object waitingForResourcesLock = new Object();
    private ChosenResourcesEvent chosenResourcesEvent;

    private final Object waitingForSimpleResourcesLock = new Object();
    private SimpleChosenResourcesEvent simpleChosenResourcesEvent;

    //may be merged with chosenResourcesEvent
    private final Object waitingForMultipleExtraResourceLeaderPowerLock = new Object();
    private ChosenMultipleExtraResourcePowerEvent chosenMultipleExtraResourcePowerEvent;

    private final Object waitingForResourceOrganizationLock = new Object();
    private NewResourcesOrganizationEvent newResourcesOrganizationEvent;


    public Controller(PropertyChangeSubject subject, MatchState matchState, HashMap<String, ClientHandlerSender> clientHandlerSenders) {
        /*subject.addPropertyChangeListener(BuyResourcesEvent.class.getName(), this::BuyResourcesEventHandler);
        subject.addPropertyChangeListener(SelectMultiLPowersEvent.class.getName(),
                this::SelectMultipleLeaderPowersHandler);*/

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

        this.matchState = matchState;
        this.clientHandlerSenders = (HashMap<String, ClientHandlerSender>)clientHandlerSenders.clone();

        faithTrackManager = new FaithTrackManager(matchState);
        leaderCardManager = new LeaderCardManager();
        marketManager = new MarketManager(matchState.getMarket());

        setuppedPlayers = new ArrayList<>();
    }

    public synchronized void InitialDecisionsEventHandler(PropertyChangeEvent evt) {
        InitialDecisionsEvent event = (InitialDecisionsEvent) evt.getNewValue();

        if(matchState.getTurnState()!=TurnState.WAITING_FOR_PLAYER || setuppedPlayers.contains(event.getPlayerId())){
            clientHandlerSenders.get(event.getPlayerId()).sendEvent(new BadRequestEvent(event.getPlayerId(), "Can't perform this action", event));
            return;
        }

        if(event.getChosenLeaderCardIDs().size()!=2){ //config
            clientHandlerSenders.get(event.getPlayerId()).sendEvent(new BadRequestEvent(event.getPlayerId(), "Wrong number of leader cards chosen", event));
            return;
        }

        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Requirement.class, new GsonInheritanceAdapter<Requirement>());
        builder.registerTypeAdapter(LeaderPower.class, new GsonInheritanceAdapter<LeaderPower>());
        Gson gson = builder.create();
        ArrayList<LeaderCard> leaderCards = new ArrayList<>();
        try{
            Player player = matchState.getPlayerFromID(event.getPlayerId());
            LeaderCardHandler leaderCardHandler = new LeaderCardHandler(this.clientHandlerSenders, player);
            DepositLeaderPowerHandler depositLeaderPowerHandler = new DepositLeaderPowerHandler(this.clientHandlerSenders, player);
            for(String leaderCardID: event.getChosenLeaderCardIDs()) {
                String LeaderCardJSON = Files.readString(Paths.get("src\\main\\resources\\" + leaderCardID + ".json"));
                LeaderCard lc = gson.fromJson(LeaderCardJSON, LeaderCard.class);
                lc.addObserver(leaderCardHandler);
                for(LeaderPower lp: lc.getLeaderPowers()){
                    if(lp.getClass() == DepositLeaderPower.class)
                        lp.addObserver(depositLeaderPowerHandler);
                }
                leaderCards.add(lc);
            }
            this.leaderCardManager.assignLeaderCards(player, leaderCards);

            int chosenResources = event.getChosenResources().values().stream().reduce(0, Integer::sum);
            //TODO config
            int expectedResources = switch (matchState.getPlayerPosition(player)) {
                case 1, 2 -> 1;
                case 3 -> 2;
                default -> 0;
            };
            if(chosenResources != expectedResources){
                clientHandlerSenders.get(event.getPlayerId()).sendEvent(new BadRequestEvent(event.getPlayerId(), "Wrong number of resources chosen", event));
                this.leaderCardManager.assignLeaderCards(player, new ArrayList<>());
                return;
            }
            organizeResources(event.getChosenResources(), player);

            setuppedPlayers.add(event.getPlayerId());
            clientHandlerSenders.get(event.getPlayerId()).sendEvent(new SetupDoneEvent(event.getPlayerId()));

            if(setuppedPlayers.size() == matchState.getPlayers().size())
                matchState.beginMatch();
        } catch (IOException e) {
            clientHandlerSenders.get(event.getPlayerId()).sendEvent(new BadRequestEvent(event.getPlayerId(), "Invalid leader card ID(s)", event));
        } catch (NotPresentException notPresentException) {
            notPresentException.printStackTrace();
            //impossible
        }
    }

    private synchronized boolean canActionBePerformed(Event event, Player player, TurnState turnState){
        if(matchState.getTurnState() != turnState){
            clientHandlerSenders.get(player.getPlayerId()).sendEvent(new BadRequestEvent(player.getPlayerId(), "The action can't be performed now", event));
            return false;
        }
        if(matchState.getPlayers().get(matchState.getCurrentPlayerIndex())!=player) {
            clientHandlerSenders.get(player.getPlayerId()).sendEvent(new BadRequestEvent(player.getPlayerId(), "It is not the player turn", event));
            return false;
        }
        return true;
    }

    //TODO check row and column
    public synchronized void BuyResourcesEventHandler(PropertyChangeEvent evt){
        BuyResourcesEvent event = (BuyResourcesEvent) evt.getNewValue();
        try {
            Player player = matchState.getPlayerFromID(event.getPlayerId());
            if(canActionBePerformed(event, player, TurnState.START) || canActionBePerformed(event, player, TurnState.AFTER_LEADER_CARD_ACTION)) return;
            var marbles = new ArrayList<>(marketManager.buy(
                    event.getDirection(),
                    event.getIndex()));
            var powers = leaderCardManager.getSelectedPowers(player, ExtraResourceLeaderPower.class)
                    .stream()
                    .map(x -> (ExtraResourceLeaderPower)x)
                    .collect(Collectors.toList());

            HashMap<Resource, Integer> resources = new HashMap<>();
            int faithPoints = 0;
            int whiteMarbles = 0;

            for(Marble m: marbles) {
                switch (m) {
                    case BLUE -> resources.put(Resource.SHIELD, resources.getOrDefault(Resource.SHIELD, 0) + 1);
                    case GRAY -> resources.put(Resource.ROCK, resources.getOrDefault(Resource.ROCK, 0) + 1);
                    case YELLOW -> resources.put(Resource.COIN, resources.getOrDefault(Resource.COIN, 0) + 1);
                    case PURPLE -> resources.put(Resource.SERVANT, resources.getOrDefault(Resource.SERVANT, 0) + 1);
                    case RED -> faithPoints++;
                    case WHITE -> whiteMarbles++;
                }
            }
            if(powers.size() > 1 && whiteMarbles>0)
            {
                ArrayList<Resource> resourceTypes= new ArrayList<>();
                for(ExtraResourceLeaderPower lp: powers){
                    Resource r = lp.getResourceType();
                    if(!resourceTypes.contains(r)) resourceTypes.add(r);
                }
                boolean goodChoice = false;
                while(!goodChoice) {
                    clientHandlerSenders.get(player.getPlayerId()).sendEvent(new ChoseMultipleExtraResourcePowerEvent(event.getPlayerId(), resourceTypes, whiteMarbles));
                    waitForEvent(waitingForMultipleExtraResourceLeaderPowerLock);
                    int chosenResourcesNum = 0;
                    HashMap<Resource, Integer> chosenResources = chosenMultipleExtraResourcePowerEvent.getChosenResources();
                    for (Resource r : chosenResources.keySet()) {
                        if (chosenResources.get(r) <= 0)
                            chosenResources.remove(r);
                        else
                            chosenResourcesNum += chosenResources.get(r);
                    }
                    if (!resourceTypes.containsAll(chosenResources.keySet())){
                        clientHandlerSenders.get(player.getPlayerId()).sendEvent(new BadRequestEvent(player.getPlayerId(), "Some of the selected resource type are not allowed", chosenMultipleExtraResourcePowerEvent));
                    } else if(chosenResourcesNum > whiteMarbles) {
                        clientHandlerSenders.get(player.getPlayerId()).sendEvent(new BadRequestEvent(player.getPlayerId(), "Too many resources selected", chosenMultipleExtraResourcePowerEvent));
                    } else if(chosenResourcesNum < whiteMarbles) {
                        clientHandlerSenders.get(player.getPlayerId()).sendEvent(new BadRequestEvent(player.getPlayerId(), "Too few resources selected", chosenMultipleExtraResourcePowerEvent));
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
                Resource type = powers.get(0).getResourceType();
                resources.put(type, resources.getOrDefault(type, 0) + whiteMarbles);
            }
            faithTrackManager.incrementFaithTrackPosition(player, faithPoints);
            organizeResources(resources, player);
            matchState.setTurnState(TurnState.AFTER_MAIN_ACTION);
        } catch (NotPresentException e) {
            System.out.println("The event" + event + "got sent to the wrong match");
            e.printStackTrace();
        }
    }

    public void ChosenMultipleExtraResourcePowerEventHandler(PropertyChangeEvent evt){
        ChosenMultipleExtraResourcePowerEvent event = (ChosenMultipleExtraResourcePowerEvent) evt.getNewValue();
        synchronized (waitingForMultipleExtraResourceLeaderPowerLock){
            try {
                if(canActionBePerformed(event, matchState.getPlayerFromID(event.getPlayerId()), TurnState.WAITING_FOR_SOMETHING)) return;
            } catch (NotPresentException notPresentException) {
                //impossible
                notPresentException.printStackTrace();
            }
            chosenMultipleExtraResourcePowerEvent = event;
            waitingForMultipleExtraResourceLeaderPowerLock.notifyAll();
        }
    }

    private synchronized void organizeResources(HashMap<Resource, Integer> resources, Player player) {
        boolean goodChoice = false;
        while(!goodChoice) {
            clientHandlerSenders.get(player.getPlayerId()).sendEvent(new OrganizeResourcesEvent(player.getPlayerId(), resources));
            waitForEvent(waitingForResourceOrganizationLock);

            HashMap<Resource, Integer> totalStoredResources = new HashMap<>();
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
                            eventDepotStates.remove(depotState);
                        }
                    }
                }
            }

            for (DepotState depotState : eventDepotStates) {
                Resource type = depotState.getResourceType();
                totalStoredResources.put(type, totalStoredResources.getOrDefault(type, 0) + depotState.getCurrentQuantity());
            }

            //validate leader power state
            try {
                if (!isNewDepotStateOK || !eventDepotStates.isEmpty()) {
                    throw new HandlerCheckException(new BadRequestEvent(player.getPlayerId(), "The structure of the warehouse sent is incompatible with the one of the match", newResourcesOrganizationEvent));
                }

                for (DepositLeaderPowerStateEvent e : newResourcesOrganizationEvent.getLeaderPowersState()) {
                    HashMap<Resource, Integer> storedResource = e.getStoredResources();
                    LeaderCard lc = player.getLeaderCardFromID(e.getLeaderCardID());
                    if (!player.getActiveLeaderCards().contains(lc))
                        throw new HandlerCheckException(new LeaderCardNotActiveError(player.getPlayerId(), lc.getCardID()));
                    LeaderPower lp = lc.getLeaderPowers().get(e.getLeaderPowerIndex());
                    if (lp.getClass() != DepositLeaderPower.class)
                        throw new HandlerCheckException(new BadRequestEvent(player.getPlayerId(), "One of the leader powers is of the wrong type", newResourcesOrganizationEvent));
                    DepositLeaderPower dlp = (DepositLeaderPower) lp;
                    if (!new DepositLeaderPower(dlp.getMaxResources()).canStore(storedResource))
                        throw new HandlerCheckException(new BadRequestEvent(player.getPlayerId(), "One of the selected leader powers can't store the resources indicated with it", newResourcesOrganizationEvent));
                    for (Resource r : storedResource.keySet()) {
                        totalStoredResources.put(r, storedResource.get(r) + storedResource.getOrDefault(r, 0));
                    }
                }

                //check resources chosen
                for (Resource r : resources.keySet()) {
                    if (resources.get(r) != totalStoredResources.getOrDefault(r, 0) + discardedResources.getOrDefault(r, 0))
                        throw new HandlerCheckException(new BadRequestEvent(player.getPlayerId(), "Total number of each resource given back does not correspond to the number sent", newResourcesOrganizationEvent));
                }
                //TODO check if discarded resources have to be discarded

                goodChoice = true;

            } catch (NotPresentException notPresentException) {
                goodChoice = false;
                clientHandlerSenders.get(player.getPlayerId()).sendEvent(new BadRequestEvent(player.getPlayerId(), "One of the leader cards does not belong to this player", newResourcesOrganizationEvent));
            } catch (HandlerCheckException e) {
                goodChoice = false;
                clientHandlerSenders.get(player.getPlayerId()).sendEvent(e.getEventToSend());
            }

            ArrayList<Depot> warehouse = player.getDashBoard().getWarehouse();
            for (int i = 0; i < orderedDepotStates.size(); i++) {
                Depot depot = warehouse.get(i);
                DepotState depotState = orderedDepotStates.get(i);
                try {
                    depot.subResources(depot.getCurrentQuantity(), depot.getResourceType());
                    depot.addResources(depotState.getCurrentQuantity(), depotState.getResourceType());
                } catch (ResourcesLimitsException | DepotResourceException e) {
                    //impossible
                    e.printStackTrace();
                }
            }

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
        }
    }

    private void NewResourcesOrganizationEventHandler(PropertyChangeEvent evt){
        NewResourcesOrganizationEvent event = (NewResourcesOrganizationEvent) evt.getNewValue();
        synchronized (waitingForResourceOrganizationLock){
            try {
                if(canActionBePerformed(event, matchState.getPlayerFromID(event.getPlayerId()), TurnState.WAITING_FOR_SOMETHING)) return;
            } catch (NotPresentException notPresentException) {
                //impossible
                notPresentException.printStackTrace();
            }
            newResourcesOrganizationEvent = event;
            waitingForResourceOrganizationLock.notifyAll();
        }
    }

    public synchronized void ToggleLeaderPowerSelectEventHandler(PropertyChangeEvent evt){
        LeaderPowerSelectStateEvent event = (LeaderPowerSelectStateEvent) evt.getNewValue();
        try {
            Player player = matchState.getPlayerFromID(event.getPlayerId());
            if(canActionBePerformed(event, player, TurnState.START) || canActionBePerformed(event, player, TurnState.AFTER_LEADER_CARD_ACTION)) return;
            LeaderCard leaderCard = player.getLeaderCardFromID(event.getLeaderCardID());
            if(event.getLeaderPowerIndex()>=leaderCard.getLeaderPowers().size()) {
                clientHandlerSenders.get(event.getPlayerId()).sendEvent(new BadRequestEvent(event.getPlayerId(), "Leader power index to big", event));
                return;
            }
            try {
                if (event.isStateSelected())
                    leaderCardManager.selectLeaderPower(player, leaderCard,
                            leaderCard.getLeaderPowers().get(event.getLeaderPowerIndex()));
                else
                    leaderCardManager.deselectLeaderPower(player, leaderCard,
                            leaderCard.getLeaderPowers().get(event.getLeaderPowerIndex()));
            } catch (NotPresentException notPresentException) {
                //impossible
                System.out.println(notPresentException.getMessage());
            } catch (IllegalOperation illegalOperation) {
                clientHandlerSenders.get(event.getPlayerId()).sendEvent(event);
            } catch (LeaderCardNotActiveException e) {
                clientHandlerSenders.get(event.getPlayerId()).sendEvent(new LeaderCardNotActiveError(event.getPlayerId(), event.getLeaderCardID()));
            } catch (IncompatiblePowersException e) {
                clientHandlerSenders.get(event.getPlayerId()).sendEvent(new IncompatiblePowersError(event.getPlayerId(), event.getLeaderCardID(), event.getLeaderPowerIndex()));
            }
        } catch (NotPresentException e) {
            clientHandlerSenders.get(event.getPlayerId()).sendEvent(new BadRequestEvent(event.getPlayerId(), "Leader card not owned by this player", event));
            e.printStackTrace();
        }
    }

    public synchronized void ActivateLeaderCardEventHandler(PropertyChangeEvent evt){
        ActivateLeaderCardEvent event = (ActivateLeaderCardEvent) evt.getNewValue();
        try {
            Player player = matchState.getPlayerFromID(event.getPlayerId());
            if(matchState.leaderActionExecuted){
                clientHandlerSenders.get(event.getPlayerId()).sendEvent(new BadRequestEvent(event.getPlayerId(), "The player already executed a leader card action this turn", event));
                return;
            }
            if(canActionBePerformed(event, player, TurnState.START) || canActionBePerformed(event, player, TurnState.AFTER_MAIN_ACTION)) return;
            LeaderCard leaderCard = player.getLeaderCardFromID(event.getLeaderCardID());
            try{
                leaderCardManager.activateLeaderCard(player, leaderCard);
                matchState.leaderActionExecuted = true;
                if(matchState.getTurnState() == TurnState.START) matchState.setTurnState(TurnState.AFTER_LEADER_CARD_ACTION);
                else matchState.setTurnState(TurnState.END_OF_TURN);
            } catch (IllegalOperation illegalOperation) {
                clientHandlerSenders.get(event.getPlayerId()).sendEvent(event);
            } catch (RequirementsNotMetException requirementsNotMetException) {
                clientHandlerSenders.get(event.getPlayerId()).sendEvent(new RequirementsNotMetError(event.getPlayerId(), event.getLeaderCardID()));
            } catch (NotPresentException notPresentException) {
                //impossible
                System.out.println(notPresentException.getMessage());
            }
        } catch (NotPresentException e) {
            clientHandlerSenders.get(event.getPlayerId()).sendEvent(new BadRequestEvent(event.getPlayerId(), "Leader card not owned by this player", event));
            e.printStackTrace();
        }
    }

    public synchronized void BuyDevelopmentCardEventHandler(PropertyChangeEvent evt){
        BuyDevCardsEvent event = (BuyDevCardsEvent) evt.getNewValue();

        DevCardGrid devCardGrid = matchState.getDevCardGrid();
        try {
            Player player = matchState.getPlayerFromID(event.getPlayerId());
            if(canActionBePerformed(event, player, TurnState.START) || canActionBePerformed(event, player, TurnState.AFTER_LEADER_CARD_ACTION)) return;
            Pair<Integer, Integer> devDeckIndexes = devCardGrid.getColRowOfCardFromID(event.getDevCardID());
            DevCard devCard = devCardGrid.topCard(devDeckIndexes);
            HashMap<Resource, Integer> cardCost = devCard.getCost();

            HashMap<Resource, Integer> allPlayerResources = player.getAllPayerResources();
            HashMap<Resource, Integer> leaderPowerResources = player.getLeaderCardsResources();
            HashMap<Resource, Integer> selectedResourcesFromLeaderPower = new HashMap<>();
            HashMap<Resource, Integer> selectedResourcesFromWarehouse = new HashMap<>();
            HashMap<Resource, Integer> resourcesFromStrongBox = new HashMap<>();
            boolean goodChoice = false;
            while(!goodChoice) {
                try {
                    clientHandlerSenders.get(event.getPlayerId()).sendEvent(new ChoseResourcesEvent(event.getPlayerId(), cardCost, 0));

                    waitForEvent(waitingForResourcesLock);

                    selectedResourcesFromLeaderPower = chosenResourcesEvent.getSelectedResourcesFromLeaderPowers();
                    selectedResourcesFromWarehouse = chosenResourcesEvent.getSelectedResourcesFromWarehouse();

                    for (Resource r : cardCost.keySet()) {
                        int resourceQuantity = cardCost.get(r) - selectedResourcesFromLeaderPower.get(r) - selectedResourcesFromWarehouse.get(r);
                        if (resourceQuantity < 0) {
                            throw new HandlerCheckException(new BadRequestEvent(event.getPlayerId(), "Too many resources selected from leader powers or warehouse", event));
                        }
                        resourcesFromStrongBox.put(r, resourceQuantity);
                    }

                    if (!player.getDashBoard().checkSlot(devCard, event.getCardSlot())) {
                        throw new HandlerCheckException(new DevCardSlotError(event.getPlayerId(), event.getDevCardID(), event.getCardSlot()));
                    }

                    if (!devCard.checkCost(allPlayerResources)) {
                        throw new HandlerCheckException(new CantAffordError(event.getPlayerId(), event.getDevCardID()));
                    }

                    for (Resource r : selectedResourcesFromLeaderPower.keySet()) {
                        if (selectedResourcesFromLeaderPower.get(r) > leaderPowerResources.get(r)) {
                            throw new HandlerCheckException(new BadRequestEvent(event.getPlayerId(), "Selected resources from leader powers not present", event));
                        }
                    }

                    goodChoice = true;
                } catch (HandlerCheckException e){
                    goodChoice = false;
                    clientHandlerSenders.get(player.getPlayerId()).sendEvent(e.getEventToSend());
                }
            }
            player.getDashBoard().subResourcesToWarehouse(selectedResourcesFromWarehouse);
            removeResourcesFromLeaderCards(player, selectedResourcesFromLeaderPower);
            for(Resource r: resourcesFromStrongBox.keySet())
                player.getDashBoard().subResourcesToStrongBox(r, resourcesFromStrongBox.get(r));
            player.getDashBoard().addCard(event.getCardSlot(), devCard);
            matchState.setTurnState(TurnState.AFTER_MAIN_ACTION);
        } catch (NotPresentException notPresentException) {
            //The card is not at the top of any devDeck
            clientHandlerSenders.get(event.getPlayerId()).sendEvent(new BadRequestEvent(event.getPlayerId(), notPresentException.getMessage(), event));
        } catch (ResourcesLimitsException e) {
            clientHandlerSenders.get(event.getPlayerId()).sendEvent(new BadRequestEvent(event.getPlayerId(), "Selected resources from warehouse not present", event));
        } catch (EmptyStrongboxException | IndexSlotException | LevelCardException e) {
            //impossible
            e.printStackTrace();
        }
    }

    private synchronized void waitForEvent(Object lock) {
        synchronized (lock){
            try {
                TurnState oldTurnState = matchState.getTurnState();
                matchState.setTurnState(TurnState.WAITING_FOR_SOMETHING);
                lock.wait();
                matchState.setTurnState(oldTurnState);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private synchronized void removeResourcesFromLeaderCards(Player player, HashMap<Resource, Integer> resources) throws NotPresentException {
        resources = (HashMap<Resource, Integer>) resources.clone();
        for(Resource r: resources.keySet())
            if(resources.get(r)<=0)
                resources.remove(r);

        for(LeaderCard lc: player.getActiveLeaderCards())
            for(LeaderPower lp: lc.getLeaderPowers()){
                if(lp.getClass()== DepositLeaderPower.class){
                    DepositLeaderPower dlp = (DepositLeaderPower) lp;
                    HashMap<Resource, Integer> currentResources = dlp.getCurrentResources();
                    for(Resource r: currentResources.keySet()) {
                        if(resources.isEmpty()) return;
                        int toRemove = Integer.min(resources.getOrDefault(r, 0), currentResources.get(r));
                        currentResources.put(r, currentResources.get(r)-toRemove);
                        resources.put(r, resources.get(r)-toRemove);
                        if(resources.get(r)<=0)
                            resources.remove(r);
                    }
                }
            }
        throw new NotPresentException("Not enough resources in the leaderCards");
    }

    private synchronized void DiscardLeaderCardEventHandler(PropertyChangeEvent evt){
        DiscardLeaderCardEvent event = (DiscardLeaderCardEvent) evt.getNewValue();

        try {
            Player player = matchState.getPlayerFromID(event.getPlayerId());
            if(matchState.leaderActionExecuted){
                clientHandlerSenders.get(event.getPlayerId()).sendEvent(new BadRequestEvent(event.getPlayerId(), "The player already executed a leader card action this turn", event));
                return;
            }
            if(canActionBePerformed(event, player, TurnState.START) || canActionBePerformed(event, player, TurnState.AFTER_MAIN_ACTION)) return;
            leaderCardManager.removeLeaderCard(player, player.getLeaderCardFromID(event.getLeaderCardID()));
            for(Player p: matchState.getPlayers())
                if(p!=player)
                    faithTrackManager.incrementFaithTrackPosition(p, 1); //TODO config?
            matchState.leaderActionExecuted = true;
            if(matchState.getTurnState() == TurnState.START) matchState.setTurnState(TurnState.AFTER_LEADER_CARD_ACTION);
            else matchState.setTurnState(TurnState.END_OF_TURN);
        } catch (NotPresentException notPresentException) {
            clientHandlerSenders.get(event.getPlayerId()).sendEvent(new BadRequestEvent(event.getPlayerId(), "Leader card not owned by this player", event));
            notPresentException.printStackTrace();
        } catch (IllegalOperation illegalOperation) {
            clientHandlerSenders.get(event.getPlayerId()).sendEvent(new BadRequestEvent(event.getPlayerId(), "Leader card already active", event));
            illegalOperation.printStackTrace();
        }
    }

    private synchronized void ActivateProductionEventHandler(PropertyChangeEvent evt){
        ActivateProductionEvent event = (ActivateProductionEvent) evt.getNewValue();

        new Thread(()->{
            HashMap<Resource, Integer> consumedResources = new HashMap<>();
            HashMap<Resource, Integer> producedResources = new HashMap<>();
            int requiredResourceOfChoice = 0;
            int producedResourceOfChoice = 0;
            int faithPointsProduced = 0;

            try {
                Player player = matchState.getPlayerFromID(event.getPlayerId());
                if(canActionBePerformed(event, player, TurnState.START) || canActionBePerformed(event, player, TurnState.AFTER_LEADER_CARD_ACTION)) return;
                ArrayList<DevCard> topDevCards = player.getDashBoard().getTopCards();
                for(String devCardID: event.getDevCards()){
                    int index = -1;
                    for(int i=0; i<topDevCards.size(); i++){
                        if(topDevCards.get(i).getCardID().equals(devCardID)) index=i;
                    }
                    if(index == -1){
                        clientHandlerSenders.get(event.getPlayerId()).sendEvent(new BadRequestEvent(event.getPlayerId(), "DevCard not at the top of any of the player slot", event));
                        return;
                    }

                    //can probably be it's own function returning a cumulative production power
                    ProductionPower productionPower = topDevCards.get(index).getProductionPower();
                    HashMap<Resource, Integer> cr = productionPower.getConsumedResources();
                    for(Resource r: cr.keySet()){
                        consumedResources.put(r, cr.get(r) + consumedResources.getOrDefault(r, 0));
                    }
                    HashMap<Resource, Integer> pr = productionPower.getConsumedResources();
                    for(Resource r: pr.keySet()){
                        producedResources.put(r, pr.get(r) + producedResources.getOrDefault(r, 0));
                    }
                    requiredResourceOfChoice+=productionPower.getRequiredResourceOfChoice();
                    producedResourceOfChoice+=productionPower.getProducedResourceOfChoice();
                    faithPointsProduced+= productionPower.getFaithPointsProduced();
                }

                HashMap<Resource, Integer> selectedResourcesFromLeaderPowers = new HashMap<>();
                HashMap<Resource, Integer> selectedResourcesFromWarehouse = new HashMap<>();
                HashMap<Resource, Integer> allPlayerResources = new HashMap<>();
                boolean goodChoice = false;
                while(!goodChoice) {
                    try {
                        clientHandlerSenders.get(event.getPlayerId()).sendEvent(new ChoseResourcesEvent(event.getPlayerId(), consumedResources, requiredResourceOfChoice));

                        waitForEvent(waitingForResourcesLock);

                        //check resources chosen to produce
                        selectedResourcesFromLeaderPowers = chosenResourcesEvent.getSelectedResourcesFromLeaderPowers();
                        selectedResourcesFromWarehouse = chosenResourcesEvent.getSelectedResourcesFromWarehouse();

                        HashMap<Resource, Integer> wareHouseResources = player.getDashBoard().getWarehouseResources();
                        for (Resource r : wareHouseResources.keySet()) {
                            if (wareHouseResources.get(r) < selectedResourcesFromWarehouse.getOrDefault(r, 0)) {
                                throw new HandlerCheckException(new BadRequestEvent(event.getPlayerId(), "Selected resources from warehouse not present", event));
                            }
                        }

                        HashMap<Resource, Integer> leaderPowerResources = player.getLeaderCardsResources();
                        for (Resource r : leaderPowerResources.keySet()) {
                            if (leaderPowerResources.get(r) < selectedResourcesFromLeaderPowers.getOrDefault(r, 0)) {
                                throw new HandlerCheckException(new BadRequestEvent(event.getPlayerId(), "Selected resources from leader powers not present", event));
                            }
                        }

                        HashMap<Resource, Integer> allResourcesSelected = chosenResourcesEvent.getAllResourcesChosen();
                        allPlayerResources = player.getAllPayerResources();
                        int extraResources = 0;
                        for (Resource r : allResourcesSelected.keySet()) {
                            if (allPlayerResources.get(r) < allResourcesSelected.get(r)) {
                                throw new HandlerCheckException(new BadRequestEvent(event.getPlayerId(), "The selected resources are not present in the player inventory", event));
                            }
                            int difference = allResourcesSelected.get(r) - consumedResources.get(r);
                            if (difference < 0) {
                                throw new HandlerCheckException(new BadRequestEvent(event.getPlayerId(), "Too few resources chosen", event));
                            }
                            extraResources += difference;
                        }
                        if (extraResources < requiredResourceOfChoice) {
                            throw new HandlerCheckException(new BadRequestEvent(event.getPlayerId(), "Too few resources to consume chosen", event));
                        }
                        if (extraResources > requiredResourceOfChoice) {
                            throw new HandlerCheckException(new BadRequestEvent(event.getPlayerId(), "Too many resources to consume chosen", event));
                        }
                        goodChoice = true;
                    } catch(HandlerCheckException e){
                        goodChoice = false;
                        clientHandlerSenders.get(player.getPlayerId()).sendEvent(e.getEventToSend());
                    }

                }

                //chose produced resource of choice
                int numChosenResources = 0;
                HashMap<Resource, Integer> chosenResources = new HashMap<>();
                goodChoice = false;
                while(!goodChoice) {
                    clientHandlerSenders.get(event.getPlayerId()).sendEvent(new SimpleChoseResourcesEvent(event.getPlayerId(), producedResourceOfChoice));
                    waitForEvent(waitingForSimpleResourcesLock);

                    //check produced resource of choice
                    chosenResources = simpleChosenResourcesEvent.getAllResourcesChosen();
                    for (Resource r : chosenResources.keySet()) {
                        numChosenResources += chosenResources.get(r);
                    }
                    if (numChosenResources < producedResourceOfChoice) {
                        clientHandlerSenders.get(event.getPlayerId()).sendEvent(new BadRequestEvent(event.getPlayerId(), "Too few resources to produce chosen", event));
                        goodChoice = false;
                    }
                    else if (numChosenResources > producedResourceOfChoice) {
                        clientHandlerSenders.get(event.getPlayerId()).sendEvent(new BadRequestEvent(event.getPlayerId(), "Too many resources to produce chosen", event));
                        goodChoice = false;
                    }
                    else
                        goodChoice = true;
                }

                //produce
                player.getDashBoard().subResourcesToWarehouse(selectedResourcesFromWarehouse);
                removeResourcesFromLeaderCards(player, selectedResourcesFromLeaderPowers);
                for(Resource r: allPlayerResources.keySet()){
                    player.getDashBoard().subResourcesToStrongBox(r, allPlayerResources.get(r) - selectedResourcesFromWarehouse.getOrDefault(r, 0) - selectedResourcesFromLeaderPowers.getOrDefault(r, 0));
                }

                for(Resource r: Resource.values())
                    player.getDashBoard().addResourcesToStrongBox(r, producedResources.getOrDefault(r, 0) + chosenResources.getOrDefault(r, 0));

                faithTrackManager.incrementFaithTrackPosition(player, faithPointsProduced);
                matchState.setTurnState(TurnState.AFTER_MAIN_ACTION);

            } catch (NotPresentException | ResourcesLimitsException | EmptyStrongboxException notPresentException) {
                //impossible
                notPresentException.printStackTrace();
            }

        }).start();
    }

    public synchronized void EndTurnEventHandler(PropertyChangeEvent evt){
        EndTurnEvent event = (EndTurnEvent) evt.getNewValue();

        try {
            Player player = matchState.getPlayerFromID(event.getPlayerId());
            if(canActionBePerformed(event, player, TurnState.AFTER_MAIN_ACTION) || canActionBePerformed(event, player, TurnState.END_OF_TURN)) return;
            for(LeaderCard lc: player.getLeaderCards()){
                for(LeaderPower lp: lc.getLeaderPowers()){
                    leaderCardManager.deselectLeaderPower(player, lc, lp);
                }
            }
            matchState.nextTurn();
        } catch (NotPresentException | IllegalOperation | LeaderCardNotActiveException notPresentException) {
            //impossible
            notPresentException.printStackTrace();
        }
    }

    private void ChosenResourcesEventHandler(PropertyChangeEvent evt){
        ChosenResourcesEvent event = (ChosenResourcesEvent) evt.getNewValue();
        synchronized (waitingForResourcesLock){
            try {
                if(canActionBePerformed(event, matchState.getPlayerFromID(event.getPlayerId()), TurnState.WAITING_FOR_SOMETHING)) return;
            } catch (NotPresentException notPresentException) {
                //impossible
                notPresentException.printStackTrace();
            }
            chosenResourcesEvent = event;
            waitingForResourcesLock.notifyAll();
        }
    }

    private void SimpleChosenResourcesEventHandler(PropertyChangeEvent evt){
        SimpleChosenResourcesEvent event = (SimpleChosenResourcesEvent) evt.getNewValue();
        synchronized (waitingForSimpleResourcesLock){
            try {
                if(canActionBePerformed(event, matchState.getPlayerFromID(event.getPlayerId()), TurnState.WAITING_FOR_SOMETHING)) return;
            } catch (NotPresentException notPresentException) {
                //impossible
                notPresentException.printStackTrace();
            }
            simpleChosenResourcesEvent = event;
            waitingForSimpleResourcesLock.notifyAll();
        }
    }
}
