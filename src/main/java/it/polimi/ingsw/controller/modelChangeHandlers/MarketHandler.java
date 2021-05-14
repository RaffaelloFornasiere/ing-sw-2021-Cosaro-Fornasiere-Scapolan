package it.polimi.ingsw.controller.modelChangeHandlers;

import it.polimi.ingsw.events.ClientEvents.MarketStateEvent;
import it.polimi.ingsw.model.Market;
import it.polimi.ingsw.utilities.Observer;
import it.polimi.ingsw.virtualview.RequestsElaborator;

import java.util.HashMap;

public class MarketHandler implements Observer {

    private HashMap<String, RequestsElaborator> networkData;

    public MarketHandler(HashMap<String, RequestsElaborator> networkData){
        this.networkData = (HashMap<String, RequestsElaborator>) networkData.clone();
    }

    @Override
    public void update(Object o) {
        Market market = (Market) o;

        for(String playerID: networkData.keySet())
            networkData.get(playerID).getClientHandlerSender().sendEvent(
                    new MarketStateEvent(playerID, market.getMarbleLeft(), market.getMarketStatus(), market.getRows(), market.getCols()));
    }
}
