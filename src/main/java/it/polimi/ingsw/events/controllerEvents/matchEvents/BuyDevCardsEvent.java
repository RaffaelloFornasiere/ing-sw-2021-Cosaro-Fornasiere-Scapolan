package it.polimi.ingsw.events.controllerEvents.matchEvents;

/**
 * Event sent to the server when a player wants to buy a development card
 * After having sent this, the client should expect to receive a ChoseResourcesEvent, at which it must answer with
 *  a ChosenResourcesEvent
 * The client will receive a BadRequestEvent if this event was mal-posed
 * The client will receive a DevCardSlotError if he cannot put the card in the selected slot
 * The client will receive a CantAffordError if he does not have the resources to buy the card
 */
public class BuyDevCardsEvent extends MatchEvent{
    private final String DevCardID;
    private final int cardSlot;

    /**
     * Constructor for the class
     * @param playerID The ID of the player that generated(directly or indirectly) this event
     * @param devCardID The ID of the card that the player wants to buy
     * @param cardSlot The index of the slot the player wants to put the dev card in
     */
    public BuyDevCardsEvent(String playerID, String devCardID, int cardSlot) {
        super(playerID);
        DevCardID = devCardID;
        this.cardSlot = cardSlot;
    }

    /**
     * Getter for the ID of the card that the player wants to buy
     * @return The ID of the card that the player wants to buy
     */
    public String getDevCardID() {
        return DevCardID;
    }

    /**
     * Getter for the index of the slot the player wants to put the dev card in
     * @return The index of the slot the player wants to put the dev card in
     */
    public int getCardSlot() {
        return cardSlot;
    }
}
