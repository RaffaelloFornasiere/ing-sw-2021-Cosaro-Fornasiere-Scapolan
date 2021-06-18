package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Direction;
import it.polimi.ingsw.model.Marble;
import it.polimi.ingsw.model.Market;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Manager of the marked. Provides additional functionalities to control the market.
 *
 */
public class MarketManager {
    Market market;

    /**
     * Constructor
     *
     * @param market the market to be controlled
     */
    public MarketManager(Market market){
        this.market = market;
    }

    /**
     * shuffles the market
     */
    public void resetMarket()
    {
        market.shuffleMarket();
    }

    /**
     * taks as input the direction and the index of the action. Returns the marbles
     * present in that row or column.
     *
     * @param direction direction of action
     * @param index index of action
     * @return the marbles in the row or column selected
     */
    public HashMap<Marble, Integer> buy(Direction direction, int index)
    {
        var marbles = market.getMarbles(direction, index);
        market.update(direction, index);
        return  marbles;
    }
}
