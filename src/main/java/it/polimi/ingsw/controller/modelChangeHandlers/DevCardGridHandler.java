package it.polimi.ingsw.controller.modelChangeHandlers;

import it.polimi.ingsw.messageSenders.Sender;
import it.polimi.ingsw.events.clientEvents.DevCardGridStateEvent;
import it.polimi.ingsw.exceptions.NotPresentException;
import it.polimi.ingsw.model.devCards.DevCardGrid;

import java.util.HashMap;

/**
 * Observer for the DevCardGrid
 */
public class DevCardGridHandler extends MatchObserver{

    public DevCardGridHandler(HashMap<String, Sender> networkData) {
        super(networkData);
    }

    /**
     * Sends to all the players the top card of each deck of the grid
     * @param o The DevCardGrid that changed
     */
    @Override
    public void update(Object o) {
        DevCardGrid devCardGrid = (DevCardGrid) o;

        String[][] topCardIDs = new String[devCardGrid.getRowsNumber()][devCardGrid.getColumnsNumber()];
        for(int i=0; i<devCardGrid.getRowsNumber(); i++) {
            for(int j=0; j<devCardGrid.getColumnsNumber(); j++) {
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
