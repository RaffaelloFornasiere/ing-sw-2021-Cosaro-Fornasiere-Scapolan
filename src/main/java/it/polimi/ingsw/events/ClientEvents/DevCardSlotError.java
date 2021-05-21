package it.polimi.ingsw.events.ClientEvents;

public class DevCardSlotError extends ClientEvent{
    private String DevCardID;
    private int cardSlot;

    public DevCardSlotError(String playerId, String devCardID, int cardSlot) {
        super(playerId);
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
