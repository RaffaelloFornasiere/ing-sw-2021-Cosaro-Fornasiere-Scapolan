package it.polimi.ingsw.events;

public class NewPlayerEvent extends Event{
    private String lobbyLeaderID;

    /**
     * constructor for the class. Used when this event will signal the creation of a new lobby
     * @param PlayerID the player that generated(directly or indirectly) this event
     */
    public NewPlayerEvent(String PlayerID){
        super(PlayerID);
        lobbyLeaderID = PlayerID;
    }

    /**
     * constructor for the class
     * @param PlayerID the player that generated(directly or indirectly) this event
     * @param lobbyLeaderID the ID of the leader of the lobby to join
     */
    public NewPlayerEvent(String PlayerID, String lobbyLeaderID){
        this(PlayerID);
        this.lobbyLeaderID = lobbyLeaderID;
    }

    /**
     * getter for the ID of the leader of the lobby to join
     * @return the ID of the leader of the lobby to join
     */
    public String getLobbyLeaderID(){
        return lobbyLeaderID;
    }
}
