package it.polimi.ingsw.controller;


import it.polimi.ingsw.events.ClientEvents.*;
import it.polimi.ingsw.events.ControllerEvents.MatchEvents.*;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.LeaderCards.ExtraResourceLeaderPower;
import it.polimi.ingsw.model.LeaderCards.LeaderCard;
import it.polimi.ingsw.utilities.PropertyChangeSubject;
import it.polimi.ingsw.virtualview.ClientHandlerSender;
import org.reflections.Reflections;

import java.beans.PropertyChangeEvent;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.stream.Collectors;


public class Controller {
    MarketManager marketManager;
    LeaderCardManager leaderCardManager;
    FaithTrackManager faithTrackManager;
    MatchState matchState;
    HashMap<String, ClientHandlerSender> clientHandlerSenders;
    private int setuppedPlayers;




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

        setuppedPlayers = 0;

    }

    public void BuyResourcesEventHandler(PropertyChangeEvent evt){
        BuyResourcesEvent event = (BuyResourcesEvent) evt.getNewValue();
        var marbles = new ArrayList<>(marketManager.buy(
                event.getDirection(),
                event.getIndex()));
        try {
            Player player = matchState.getPlayerFromID(event.getPlayerId());
            var powers = leaderCardManager.getSelectedPowers(player, ExtraResourceLeaderPower.class)
                    .stream()
                    .map(x -> (ExtraResourceLeaderPower)x)
                    .collect(Collectors.toList());
            var resources = new ArrayList<Resource>();
            if(powers.size() > 1)
            {
                //ask user for power selection
                return;
            }
            else if (powers.size() > 0 )
            {
                marbles.add(Marble.RED);
                var i = new Object(){public int i = 0;};
                marbles.removeIf(x -> {
                    i.i++;
                    return Marble.RED.equals(x);
                });
                faithTrackManager.incrementFaithTrackPosition(player, i.i);
            }
            marbles.stream().map(marble -> switch (marble) {
                case BLUE -> Resource.SHIELD;
                case GRAY -> Resource.ROCK;
                case YELLOW -> Resource.COIN;
                case PURPLE -> Resource.SERVANT;
                default -> throw new IllegalStateException("Unexpected value: " + marble);
            }).forEach(resources::add);
            ManageBoughtResources(resources);
        } catch (NotPresentException e) {
            System.out.println("The event" + event + "got sent to the wrong match");
            e.printStackTrace();
        }


    }

    public void SelectMultipleLeaderPowersHandler(PropertyChangeEvent evt)
    {
        SelectMultiLPowersEvent event = (SelectMultiLPowersEvent) evt.getNewValue();
        try {
            Player player = matchState.getPlayerFromID(event.getPlayerId());
            faithTrackManager.incrementFaithTrackPosition(player, 1);
            ManageBoughtResources(event.getResources());
        } catch (NotPresentException e) {
            System.out.println("The event" + event + "got sent to the wrong match");
            e.printStackTrace();
        }
    }


    private void ManageBoughtResources(ArrayList<Resource> resources)
    {
        //boolean failed = false;
        //var warehouse = dashBoard.getDepotResources();
    }

    public void ToggleLeaderPowerSelectEventHandler(PropertyChangeEvent evt){
        LeaderPowerSelectStateEvent event = (LeaderPowerSelectStateEvent) evt.getNewValue();
        try {
            Player player = matchState.getPlayerFromID(event.getPlayerId());
            LeaderCard leaderCard = player.getleaderCardFromID(event.getLeaderCardID());
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

    public void ActivateLeaderCardEventHandler(PropertyChangeEvent evt){
        ActivateLeaderCardEvent event = (ActivateLeaderCardEvent) evt.getNewValue();
        try {
            Player player = matchState.getPlayerFromID(event.getPlayerId());
            LeaderCard leaderCard = player.getleaderCardFromID(event.getLeaderCardID());
            try{
                leaderCardManager.activateLeaderCard(player, leaderCard);
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

    public void InitialDecisionsEventHandler(PropertyChangeEvent evt) {
        InitialDecisionsEvent event = (InitialDecisionsEvent) evt.getNewValue();

        if(event.getChosenLeaderCardIDs().size()!=2){
            clientHandlerSenders.get(event.getPlayerId()).sendEvent(new BadRequestEvent(event.getPlayerId(), "Wrong number of leader cards chosen", event));
            return;
        }

        try{
            this.leaderCardManager.assignLeaderCard(matchState.getPlayerFromID(event.getPlayerId()), event.getChosenLeaderCardIDs());
        }
        catch(IllegalArgumentException e){
            clientHandlerSenders.get(event.getPlayerId()).sendEvent(new BadRequestEvent(event.getPlayerId(), "Invalid leader card ID(s)", event));
            return;
        } catch (NotPresentException notPresentException) {
            notPresentException.printStackTrace();
            //impossible
        }

        int chosenResources = event.getChosenResources().values().stream().reduce(0, Integer::sum);
        int expectedResources;
        try {
            switch (matchState.getPlayerPosition(matchState.getPlayerFromID(event.getPlayerId()))) { //TODO config
                case 1:
                    expectedResources = 1;
                case 2:
                    expectedResources = 1;
                case 3:
                    expectedResources = 2;
                default:
                    expectedResources = 0;
            }
            if(chosenResources != expectedResources){
                clientHandlerSenders.get(event.getPlayerId()).sendEvent(new BadRequestEvent(event.getPlayerId(), "Wrong number of resources chosen", event));
                this.leaderCardManager.assignLeaderCard(matchState.getPlayerFromID(event.getPlayerId()), new ArrayList<>());
                return;
            }
        }catch (NotPresentException e){
            e.printStackTrace();
            //impossible
        }

        //TODO organize resources

        setuppedPlayers++;
        clientHandlerSenders.get(event.getPlayerId()).sendEvent(new SetupDoneEvent(event.getPlayerId()));

        if(setuppedPlayers == matchState.getPlayers().size())
            matchState.beginMatch();
    }


}
