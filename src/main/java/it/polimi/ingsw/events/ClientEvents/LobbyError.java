package it.polimi.ingsw.events.ClientEvents;

/**
 * error sent to the client when the lobby they asked to join does not exist or is full
 */
public class LobbyError extends ClientEvent{
    private final String errorMsg;

    /**
     * Constructor for the class
     * @param playerId The rejected ID for the player
     * @param errorMsg An error message
     */
    public LobbyError(String playerId, String errorMsg) {
        super(playerId);
        this.errorMsg = errorMsg;
    }

    /**
     * Getter for the error message
     * @return The error message
     */
    public String getErrorMsg() {
        return errorMsg;
    }
}
