package it.polimi.ingsw.events;

public class NewPlayerEvent extends Event{
    private boolean isLobbyLeader;

    /**
     * constructor for the class
     * @param PlayerID the player that generated(directly or indirectly) this event
     */
    public NewPlayerEvent(String PlayerID){
        super(PlayerID);
        this.isLobbyLeader = false;
    }

    /**
     * constructor for the class
     * @param PlayerID the player that generated(directly or indirectly) this event
     * @param isLobbyLeader whether this player is the leader of the lobby they're in
     */
    public NewPlayerEvent(String PlayerID, boolean isLobbyLeader){
        this(PlayerID);
        this.isLobbyLeader = isLobbyLeader;
    }

    /**
     * @return whether this player is the leader of the lobby he's in
     */
    public boolean IsLobbyLeader(){
        return isLobbyLeader;
    }
}
