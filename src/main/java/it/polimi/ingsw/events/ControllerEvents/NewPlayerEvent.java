package it.polimi.ingsw.events.ControllerEvents;

/**
 * Event signaling the arrival of a new player
 * The client will receive a BadRequestEvent if this event was mal-posed
 */
public class NewPlayerEvent extends ControllerEvent {
    private final String lobbyLeaderID;

    /**
     * Constructor for the class. To create a new lobby put the same lobbyLeaderID and playerID
     * @param playerID The player that generated(directly or indirectly) this event
     * @param lobbyLeaderID The ID of the leader of the lobby to join
     */
    public NewPlayerEvent(String playerID, String lobbyLeaderID){
        super(playerID);
        this.lobbyLeaderID = lobbyLeaderID;
    }

    /**
     * Getter for the ID of the leader of the lobby to join
     * @return The ID of the leader of the lobby to join
     */
    public String getLobbyLeaderID(){
        return lobbyLeaderID;
    }
}
