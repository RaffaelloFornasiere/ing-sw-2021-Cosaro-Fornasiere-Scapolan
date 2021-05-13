package it.polimi.ingsw.events.ControllerEvents.MatchEvents;

import java.util.ArrayList;

public class ActivateProductionEvent extends MatchEvent {

    public ActivateProductionEvent(String playerID, ArrayList<Integer> devCards) {
        super(playerID);
        this.devCards = devCards;
    }

    private final ArrayList<Integer> devCards;

    @SuppressWarnings("unchecked")
    public ArrayList<Integer> getDevCards() {return (ArrayList<Integer>) devCards.clone();}

    private String eventName;


    @Override
    public String getEventName() {
        return this.getClass().getName();
    }



}
