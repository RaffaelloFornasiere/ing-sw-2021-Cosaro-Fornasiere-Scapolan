package it.polimi.ingsw.controller.modelChangeHandlers;

import it.polimi.ingsw.messageSenders.Sender;
import it.polimi.ingsw.events.clientEvents.DashBoardStateEvent;
import it.polimi.ingsw.events.clientEvents.DepotState;
import it.polimi.ingsw.model.DashBoard;
import it.polimi.ingsw.model.Depot;
import it.polimi.ingsw.model.devCards.DevCard;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

/**
 * Observer for DashBoards
 */
public class DashBoardHandler extends MatchObserver{
    private final Player dashBoardOwner;

    /**
     * Constructor for the class
     * @param networkData The Senders of all the player involved into the match
     * @param dashBoardOwner The owner of the DashBoard being observed
     */
    public DashBoardHandler(HashMap<String, Sender> networkData, Player dashBoardOwner) {
        super(networkData);
        this.dashBoardOwner = dashBoardOwner;
    }

    /**
     * Sends to every player of the match the top of the card slots, the state of the warehouse and the state of the strongbox of the DashBoard
     * @param o The DashBoard that changed
     */
    @Override
    public void update(Object o) {
        DashBoard dashBoard = (DashBoard) o;

        ArrayList<String> topCards = new ArrayList<>();
        for(Stack<DevCard> slot: dashBoard.getCardSlots()) {
            if(!slot.empty())
                topCards.add(slot.peek().getCardID());
            else
                topCards.add(null);
        }

        HashMap<Resource, Integer> strongBox = dashBoard.getStrongBox();

        ArrayList<DepotState> warehouse = new ArrayList<>();
        for(Depot d: dashBoard.getWarehouse()){
            warehouse.add(new DepotState(d.getResourceType(), d.getMaxQuantity(), d.getCurrentQuantity()));
        }

        sendToAll(new DashBoardStateEvent(dashBoardOwner.getPlayerId(), topCards, strongBox, warehouse));
    }
}
