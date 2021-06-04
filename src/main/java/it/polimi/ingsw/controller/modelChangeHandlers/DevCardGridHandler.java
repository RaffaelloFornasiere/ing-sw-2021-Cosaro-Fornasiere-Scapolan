package it.polimi.ingsw.controller.modelChangeHandlers;

import it.polimi.ingsw.events.ClientEvents.DevCardGridStateEvent;
import it.polimi.ingsw.exceptions.NotPresentException;
import it.polimi.ingsw.model.DevCards.DevCardGrid;
import it.polimi.ingsw.Server.ClientHandlerSender;

import java.util.HashMap;

public class DevCardGridHandler extends MatchObserver{

    public DevCardGridHandler(HashMap<String, ClientHandlerSender> networkData) {
        super(networkData);
    }

    @Override
    public void update(Object o) {
        DevCardGrid devCardGrid = (DevCardGrid) o;

        String[][] topCardIDs = new String[devCardGrid.getColumnsNumber()][devCardGrid.getRowsNumber()];
        for(int i=0; i<devCardGrid.getColumnsNumber(); i++) {
            for(int j=0; j<devCardGrid.getRowsNumber(); j++) {
                try {
                    topCardIDs[i][j] = devCardGrid.topCard(i, j).getCardID();
                } catch (NotPresentException notPresentException) {
                    topCardIDs[i][j] = null;
                }
            }
        }

        sendToAll(new DevCardGridStateEvent("", topCardIDs));
    }
}
