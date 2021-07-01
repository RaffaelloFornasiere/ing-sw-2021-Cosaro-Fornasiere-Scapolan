package it.polimi.ingsw.controller.modelChangeHandlers;

import it.polimi.ingsw.messageSenders.Sender;
import it.polimi.ingsw.events.clientEvents.MarketStateEvent;
import it.polimi.ingsw.model.Market;

import java.util.HashMap;

/**
 * Observer for the Market
 */
public class MarketHandler extends MatchObserver {


    public MarketHandler(HashMap<String, Sender> networkData) {
        super(networkData);
    }

    /**
     * Sends to all the players the new state of the market
     * @param o The Market that changed
     */
    @Override
    public void update(Object o) {
        Market market = (Market) o;
        sendToAll(new MarketStateEvent(null, market.getMarbleLeft(), market.getMarketStatus(), market.getRows(), market.getCols()));
    }
}
