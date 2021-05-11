package it.polimi.ingsw.events;

import it.polimi.ingsw.model.DevCards.DevCard;

import java.util.ArrayList;

public class ActivateProductionEvent extends MatchEvent {

    public ActivateProductionEvent(String playerID, ArrayList<DevCard> devCards) {
        super(playerID);
        this.devCards = devCards;
    }

    private final ArrayList<DevCard> devCards;

    public ArrayList<DevCard> getDevCards() {return (ArrayList<DevCard>) devCards.clone();}

    private String eventName;


    @Override
    public String getEventName() {
        return this.getClass().getName();
    }



}
