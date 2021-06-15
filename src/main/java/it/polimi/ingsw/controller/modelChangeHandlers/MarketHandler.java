package it.polimi.ingsw.controller.modelChangeHandlers;

import it.polimi.ingsw.client.Sender;
import it.polimi.ingsw.events.ClientEvents.MarketStateEvent;
import it.polimi.ingsw.model.Market;
import it.polimi.ingsw.Server.ClientHandlerSender;

import java.util.HashMap;

public class MarketHandler extends MatchObserver {


    public MarketHandler(HashMap<String, Sender> networkData) {
        super(networkData);
    }

    @Override
    public void update(Object o) {
        Market market = (Market) o;
        sendToAll(new MarketStateEvent(null, market.getMarbleLeft(), market.getMarketStatus(), market.getRows(), market.getCols()));
    }
}
