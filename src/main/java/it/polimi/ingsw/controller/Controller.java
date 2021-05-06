package it.polimi.ingsw.controller;


import it.polimi.ingsw.events.BuyResourcesEvent;
import it.polimi.ingsw.events.SelectMultiLPowersEvent;
import it.polimi.ingsw.exceptions.InvalidPlayerIdException;
import it.polimi.ingsw.exceptions.ResourcesLimitsException;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.FaithTrack.FaithTrack;
import it.polimi.ingsw.model.LeaderCards.ExtraResourceLeaderPower;
import it.polimi.ingsw.utilities.PropertyChangeSubject;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Controller {
    MarketManager marketManager;
    LeaderCardManager leaderCardManager;
    MatchState matchState;
    DashBoard dashBoard;


    Controller(PropertyChangeSubject subject) {
        subject.addPropertyChangeListener("BuyResourcesEvent", evt ->
        {
            try {
                this.BuyResourcesEventHandler(evt);
            } catch (InvalidPlayerIdException e) {
                e.printStackTrace();
            }
        });
        subject.addPropertyChangeListener("SelectMultipleLeaderPowersEvent",
                this::SelectMultipleLeaderPowersHandler);

    }

    public void BuyResourcesEventHandler(PropertyChangeEvent evt) throws InvalidPlayerIdException {
        BuyResourcesEvent event = (BuyResourcesEvent) evt.getNewValue();
        var marbles = new ArrayList<>(marketManager.buy(
                event.getDirection(),
                event.getIndex()));
        var players = matchState.getPlayers();
        var player = players.stream().filter(x -> event.getPlayerId() == x.getPlayerId()).findFirst().orElse(null);

        if (player == null)
            throw new InvalidPlayerIdException();

        var powers = leaderCardManager.getSelectedPowers(player, ExtraResourceLeaderPower.class)
                .stream()
                .map(x -> (ExtraResourceLeaderPower)x)
                .collect(Collectors.toList());

        if(powers.size() > 1)
        {
            //ask user for power selction
            return;
        }
        else if (powers.size() > 0 )
        {
            var power = powers.get(0);
        }
        var resources = marbles.stream().map(marble -> {
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
                    return null;
            }
        }).collect(Collectors.toCollection(ArrayList::new));
        ManageBuoughtResources(resources);
    }

    public void SelectMultipleLeaderPowersHandler(PropertyChangeEvent evt)
    {
        SelectMultiLPowersEvent event = (SelectMultiLPowersEvent) evt.getNewValue();
        //event.getResources().stream().filter(Marble.RED::equals).forEach(redMarble ->
        {
            //add
        }//);
        //ManageBuoughtResources(event.getResources());
    }

    private void ManageBuoughtResources(ArrayList<Resource> resources)
    {
        resources.forEach(x -> {
            try {
                dashBoard.addResourcesToWarehouse(x, 1);
            } catch (ResourcesLimitsException e) {
                e.printStackTrace();
            }
        });
    }


}
