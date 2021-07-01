package it.polimi.ingsw.events.clientEvents;

/**
 * Error sent to the client when he can't afford a development card he asked to buy
 */
public class CantAffordError extends ClientEvent{
    private final String devCardID;

    /**
     * Constructor for the class
     * @param playerId The ID of the player who made the original request
     * @param devCardID The ID of the card that the player could not afford
     */
    public CantAffordError(String playerId, String devCardID) {
        super(playerId);
        this.devCardID = devCardID;
    }

    /**
     * Getter for the ID of the card that the player could not afford
     * @return The ID of the card that the player could not afford
     */
    public String getDevCardID() {
        return devCardID;
    }
}
