package it.polimi.ingsw.events.clientEvents;

/**
 * Event sent to the clients when the username given in the NewPlayerEvent was invalid
 */
public class UsernameError extends ClientEvent{
    private final String errorMsg;

    /**
     * Constructor for the class
     * @param playerId The rejected ID for the player
     * @param errorMsg An error message
     */
    public UsernameError(String playerId, String errorMsg) {
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
