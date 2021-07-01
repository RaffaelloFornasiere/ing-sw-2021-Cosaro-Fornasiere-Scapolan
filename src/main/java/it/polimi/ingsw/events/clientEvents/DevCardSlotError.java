package it.polimi.ingsw.events.clientEvents;

/**
 * Error sent to the client when it wants to insert a development card into a slot in its dashboard that can't contain it
 */
public class DevCardSlotError extends ClientEvent{
    private final String DevCardID;
    private final int cardSlot;

    /**
     * Constructor for the class
     * @param playerId The ID of the player who tried to insert the card
     * @param devCardID The ID of the development card that the player tried to insert
     * @param cardSlot The index of the card slot the player tried to insert the card in
     */
    public DevCardSlotError(String playerId, String devCardID, int cardSlot) {
        super(playerId);
        DevCardID = devCardID;
        this.cardSlot = cardSlot;
    }

    /**
     * Getter for the ID of the development card that the player tried to insert
     * @return The ID of the development card that the player tried to insert
     */
    public String getDevCardID() {
        return DevCardID;
    }

    /**
     * Getter for the index of the card slot the player tried to insert the card in
     * @return The index of the card slot the player tried to insert the card in
     */
    public int getCardSlot() {
        return cardSlot;
    }
}
