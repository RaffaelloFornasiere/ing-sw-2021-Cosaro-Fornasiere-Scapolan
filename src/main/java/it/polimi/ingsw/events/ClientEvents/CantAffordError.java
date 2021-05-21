package it.polimi.ingsw.events.ClientEvents;

public class CantAffordError extends ClientEvent{
    private String devCardID;

    public CantAffordError(String playerId, String devCardID) {
        super(playerId);
        this.devCardID = devCardID;
    }

    public String getDevCardID() {
        return devCardID;
    }
}
