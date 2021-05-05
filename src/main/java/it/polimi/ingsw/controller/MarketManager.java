package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Direction;
import it.polimi.ingsw.model.Marble;
import it.polimi.ingsw.model.Market;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

public class MarketManager {
    Market market;
    public MarketManager(Market market){
        this.market = market;
    }

    public void resetMarket()
    {
        market.shuffleMarket();
    }

    public ArrayList<Marble> buy(Direction direction, int index)
    {
        var marbles = market.getMarblesAsList(direction, index);
        market.update(direction, index);
        return  marbles;
    }
}
