package it.polimi.ingsw.controller.modelChangeHandlers;

import it.polimi.ingsw.events.ClientEvents.MarketStateEvent;
import it.polimi.ingsw.model.Market;
import it.polimi.ingsw.utilities.Observer;
import it.polimi.ingsw.virtualview.RequestsElaborator;

import java.util.HashMap;

public class MarketHandler extends MatchObserver {


    public MarketHandler(HashMap<String, RequestsElaborator> networkData) {
        super(networkData);
    }

    @Override
    public void update(Object o) {
        Market market = (Market) o;
        sendToAll(new MarketStateEvent(null, market.getMarbleLeft(), market.getMarketStatus(), market.getRows(), market.getCols()));
    }
}
