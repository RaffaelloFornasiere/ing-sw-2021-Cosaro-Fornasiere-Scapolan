package it.polimi.ingsw.events.ControllerEvents.MatchEvents;

import it.polimi.ingsw.model.Resource;

import java.util.HashMap;

public class BuyDevCardsEvent extends MatchEvent{
    private String DevCardID;
    private int cardSlot;

    public BuyDevCardsEvent(String playerID, String devCardID, int cardSlot) {
        super(playerID);
        DevCardID = devCardID;
        this.cardSlot = cardSlot;
    }

    public String getDevCardID() {
        return DevCardID;
    }

    public int getCardSlot() {
        return cardSlot;
    }
}
