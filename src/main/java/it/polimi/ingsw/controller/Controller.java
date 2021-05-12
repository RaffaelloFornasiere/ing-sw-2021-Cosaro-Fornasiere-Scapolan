package it.polimi.ingsw.controller;


import it.polimi.ingsw.events.ControllerEvents.*;
import it.polimi.ingsw.events.BadRequestEvent;
import it.polimi.ingsw.events.ControllerEvents.MatchEvents.*;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.LeaderCards.ExtraResourceLeaderPower;
import it.polimi.ingsw.utilities.PropertyChangeSubject;

import it.polimi.ingsw.virtualview.ClientHandlerSender;
import org.reflections.Reflections;

import java.beans.PropertyChangeEvent;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;


public class Controller {
    MarketManager marketManager;
    LeaderCardManager leaderCardManager;
    FaithTrackManager faithTrackManager;
    MatchState matchState;
    DashBoard dashBoard;
    ClientHandlerSender clientHandlerSender;




    Controller(PropertyChangeSubject subject) {
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


        // take from config
        matchState = new MatchState();
        faithTrackManager = new FaithTrackManager(matchState);


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
                //ask user for power selction
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

        for (Resource x : resources) {
            try {
                dashBoard.addResourcesToWarehouse(x, 1);
            } catch (ResourcesLimitsException | IllegalArgumentException ignore) {

            }
        }
    }

    public void ToggleLeaderPowerSelectEventHandler(PropertyChangeEvent evt){
        LeaderPowerSelectStateEvent event = (LeaderPowerSelectStateEvent) evt.getNewValue();
        try {
            Player player = matchState.getPlayerFromID(event.getPlayerId());
            if(event.getLeaderCardIndex()>=player.getLeaderCards().size()) {
                clientHandlerSender.sendEvent(new BadRequestEvent(event.getPlayerId(), "Leader card index to big", event));
                return;
            }
            if(event.getLeaderPowerIndex()>=player.getLeaderCards().get(event.getLeaderCardIndex()).getLeaderPowers().size()) {
                clientHandlerSender.sendEvent(new BadRequestEvent(event.getPlayerId(), "Leader power index to big", event));
                return;
            }
            try {
                if (event.isStateSelected())
                    leaderCardManager.selectLeaderPower(player, player.getLeaderCards().get(event.getLeaderCardIndex()),
                            player.getLeaderCards().get(event.getLeaderCardIndex()).getLeaderPowers().get(event.getLeaderPowerIndex()));
                else
                    leaderCardManager.deselectLeaderPower(player, player.getLeaderCards().get(event.getLeaderCardIndex()),
                            player.getLeaderCards().get(event.getLeaderCardIndex()).getLeaderPowers().get(event.getLeaderPowerIndex()));
            } catch (NotPresentException notPresentException) {
                //impossible
                System.out.println(notPresentException.getMessage());
            } catch (IllegalOperation illegalOperation) {
                clientHandlerSender.sendEvent(event);
            } catch (LeaderCardNotActiveException e) {
                clientHandlerSender.sendEvent(new LeaderCardNotActiveError(event.getPlayerId(), event.getLeaderCardIndex()));
            } catch (IncompatiblePowersException e) {
                clientHandlerSender.sendEvent(new IncompatiblePowersError(event.getPlayerId(), event.getLeaderCardIndex(), event.getLeaderPowerIndex()));
            }
        } catch (NotPresentException e) {
            //impossible
            System.out.println("The event" + event + "got sent to the wrong match");
            e.printStackTrace();
        }
    }

    public void ActivateLeaderCardEventHandler(PropertyChangeEvent evt){
        ActivateLeaderCardEvent event = (ActivateLeaderCardEvent) evt.getNewValue();
        try {
            Player player = matchState.getPlayerFromID(event.getPlayerId());
            if(event.getLeaderCardIndex()>=player.getLeaderCards().size()) {
                clientHandlerSender.sendEvent(new BadRequestEvent(event.getPlayerId(), "Leader card index to big", event));
                return;
            }
            try{
                leaderCardManager.activateLeaderCard(player, player.getLeaderCards().get(event.getLeaderCardIndex()));
            } catch (IllegalOperation illegalOperation) {
                clientHandlerSender.sendEvent(event);
            } catch (RequirementsNotMetException requirementsNotMetException) {
                clientHandlerSender.sendEvent(new RequirementsNotMetError(event.getPlayerId(), event.getLeaderCardIndex()));
            } catch (NotPresentException notPresentException) {
                //impossible
                System.out.println(notPresentException.getMessage());
            }
        } catch (NotPresentException e) {
            //impossible
            System.out.println("The event" + event + "got sent to the wrong match");
            e.printStackTrace();
        }
    }


}
