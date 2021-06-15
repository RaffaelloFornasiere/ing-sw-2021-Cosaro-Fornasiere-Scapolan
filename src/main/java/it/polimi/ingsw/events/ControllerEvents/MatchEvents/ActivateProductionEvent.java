package it.polimi.ingsw.events.ControllerEvents.MatchEvents;

import java.util.ArrayList;

/**
 * Event sent to the server when a player wants to activate the production
 * After having sent this, the client should expect to receive a ChoseResourcesEvent, at which it must answer with
 *  a ChosenResourcesEvent
 * After that he client should expect to receive a SimpleChoseResourcesEvent, at which it must answer with
 *  a SimpleChosenResourcesEvent
 * The client will receive a BadRequestEvent if this event was mal-posed
 */
public class ActivateProductionEvent extends MatchEvent {

    private final ArrayList<String> devCards;
    private final boolean personalPower;

    /**
     * Constructor for the class
     * @param playerID The ID of the player that generated(directly or indirectly) this event
     * @param devCards The IDs of the development card for which to activate the production
     * @param personalPower If the personal production power will be used too
     */
    @SuppressWarnings("unchecked")
    public ActivateProductionEvent(String playerID, ArrayList<String> devCards, boolean personalPower) {
        super(playerID);
        this.devCards = (ArrayList<String>) devCards.clone();
        this.personalPower = personalPower;
    }

    /**
     * Getter for the IDs of the development card for which to activate the production
     * @return The IDs of the development card for which to activate the production
     */
    @SuppressWarnings("unchecked")
    public ArrayList<String> getDevCards() {return (ArrayList<String>) devCards.clone();}

    /**
     * @return If the personal production power will be used too
     */
    public boolean isPersonalPowerActive() {
        return personalPower;
    }
}
