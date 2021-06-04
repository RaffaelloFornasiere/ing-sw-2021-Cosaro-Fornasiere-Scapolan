package it.polimi.ingsw.controller.modelChangeHandlers;

import it.polimi.ingsw.events.ClientEvents.DashBoardStateEvent;
import it.polimi.ingsw.events.ClientEvents.DepotState;
import it.polimi.ingsw.model.DashBoard;
import it.polimi.ingsw.model.Depot;
import it.polimi.ingsw.model.DevCards.DevCard;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.Server.ClientHandlerSender;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class DashBoardHandler extends MatchObserver{
    private Player dashBoardOwner;

    public DashBoardHandler(HashMap<String, ClientHandlerSender> networkData, Player dashBoardOwner) {
        super(networkData);
        this.dashBoardOwner = dashBoardOwner;
    }

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
