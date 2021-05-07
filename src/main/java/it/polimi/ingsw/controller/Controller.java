package it.polimi.ingsw.controller;


import it.polimi.ingsw.events.BuyResourcesEvent;
import it.polimi.ingsw.events.SelectMultiLPowersEvent;
import it.polimi.ingsw.exceptions.InvalidPlayerIdException;
import it.polimi.ingsw.exceptions.ResourcesLimitsException;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.LeaderCards.ExtraResourceLeaderPower;
import it.polimi.ingsw.utilities.PropertyChangeSubject;

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


    Controller(PropertyChangeSubject subject) {
        subject.addPropertyChangeListener(BuyResourcesEvent.class.getName(), this::BuyResourcesEventHandler);
        subject.addPropertyChangeListener(SelectMultiLPowersEvent.class.getName(),
                this::SelectMultipleLeaderPowersHandler);

        // take from config
        matchState = new MatchState();
        faithTrackManager = new FaithTrackManager(matchState);


    }

    public void BuyResourcesEventHandler(PropertyChangeEvent evt) {
        BuyResourcesEvent event = (BuyResourcesEvent) evt.getNewValue();
        var marbles = new ArrayList<>(marketManager.buy(
                event.getDirection(),
                event.getIndex()));
        var players = matchState.getPlayers();
        var player = players.stream().filter(x -> event.getPlayerId() == x.getPlayerId()).findFirst().orElse(null);

//        if (player == null)
//            throw new InvalidPlayerIdException();

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
        marbles.stream().map(marble -> {
            switch (marble) {
                case BLUE:
                    return Resource.SHIELD;
                case GRAY:
                    return Resource.ROCK;
                case YELLOW:
                    return Resource.COIN;
                case PURPLE:
                    return Resource.SERVANT;
                default:
                    throw new IllegalStateException("Unexpected value: " + marble);
            }
        }).forEach(resources::add);
        ManageBoughtResources(resources);
    }

    public void SelectMultipleLeaderPowersHandler(PropertyChangeEvent evt)
    {
        SelectMultiLPowersEvent event = (SelectMultiLPowersEvent) evt.getNewValue();
        var players = matchState.getPlayers();
        var player = players.stream().filter(x -> event.getPlayerId() == x.getPlayerId()).findFirst().orElse(null);

//        if (player == null)
//            throw new InvalidPlayerIdException();
        faithTrackManager.incrementFaithTrackPosition(player, 1);
        ManageBoughtResources(event.getResources());
    }


    private void ManageBoughtResources(ArrayList<Resource> resources)
    {
        //boolean failed = false;
        //var warehouse = dashBoard.getDepotResources();

        for (Resource x : resources) {
            try {
                dashBoard.addResourcesToWarehouse(x, 1);
            } catch (ResourcesLimitsException | IllegalArgumentException e) {

            }
        }
    }


}
