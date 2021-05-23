package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Direction;
import it.polimi.ingsw.model.Marble;
import it.polimi.ingsw.model.Market;

import java.util.ArrayList;
import java.util.HashMap;

public class MarketManager {
    Market market;
    public MarketManager(Market market){
        this.market = market;
    }

    public void resetMarket()
    {
        market.shuffleMarket();
    }

    public HashMap<Marble, Integer> buy(Direction direction, int index)
    {
        var marbles = market.getMarbles(direction, index);
        market.update(direction, index);
        return  marbles;
    }
}
