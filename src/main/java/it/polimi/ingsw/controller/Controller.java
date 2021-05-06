package it.polimi.ingsw.controller;


import it.polimi.ingsw.events.BuyResourcesEvent;
import it.polimi.ingsw.exceptions.InvalidPlayerIdException;
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
    Depot depot;


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
        var resources = marketManager.buy(
                event.getDirection(),
                event.getIndex()).stream().map(Resource::toResource).collect(Collectors.toList());
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
        }
        else if (powers.size() > 0 )
        {
            var power = powers.get(0);
            resources.stream().filter(Resource.ANY::equals).forEach(x -> x = power.getResourceType());
        }

        if (resources.contains(Resource.FAITH_POINT)) {
            //add faithtrack points

            resources.stream().filter(Predicate.not(Resource.FAITH_POINT::equals)).forEach(resources::remove);
        }


        //depot.addResources(resources);

    }

    public void SelectMultipleLeaderPowersHandler(PropertyChangeEvent evt)
    {

    }


}
