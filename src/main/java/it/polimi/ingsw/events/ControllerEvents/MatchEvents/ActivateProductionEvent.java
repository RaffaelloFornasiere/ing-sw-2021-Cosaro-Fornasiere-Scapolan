package it.polimi.ingsw.events.ControllerEvents.MatchEvents;

import java.util.ArrayList;

public class ActivateProductionEvent extends MatchEvent {

    public ActivateProductionEvent(String playerID, ArrayList<String> devCards, boolean personalPower) {
        super(playerID);
        this.devCards = (ArrayList<String>) devCards.clone();
    }

    private final ArrayList<String> devCards;
    private boolean personalPower;

    @SuppressWarnings("unchecked")
    public ArrayList<String> getDevCards() {return (ArrayList<String>) devCards.clone();}

    public boolean isPersonalPowerActive() {
        return personalPower;
    }

    @Override
    public String getEventName() {
        return this.getClass().getName();
    }



}
