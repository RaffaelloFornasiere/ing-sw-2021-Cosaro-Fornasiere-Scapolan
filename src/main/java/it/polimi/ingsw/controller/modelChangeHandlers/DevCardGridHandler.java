package it.polimi.ingsw.controller.modelChangeHandlers;

import it.polimi.ingsw.events.ClientEvents.DevCardGridStateEvent;
import it.polimi.ingsw.model.DevCards.DevCardGrid;
import it.polimi.ingsw.model.DevCards.DevDeck;
import it.polimi.ingsw.virtualview.RequestsElaborator;

import java.util.Arrays;
import java.util.HashMap;

public class DevCardGridHandler extends MatchObserver{

    public DevCardGridHandler(HashMap<String, RequestsElaborator> networkData) {
        super(networkData);
    }

    @Override
    public void update(Object o) {
        DevCardGrid devCardGrid = (DevCardGrid) o;

        String[][] topCardIDs = new String[devCardGrid.getColumnsNumber()][devCardGrid.getRowsNumber()];
        for(int i=0; i<devCardGrid.getColumnsNumber(); i++)
            for(int j=0; j<devCardGrid.getRowsNumber(); j++)
                topCardIDs[i][j] = devCardGrid.getDecks()[i][j].topCard().getCardID();

        sendToAll(new DevCardGridStateEvent("", topCardIDs));
    }
}