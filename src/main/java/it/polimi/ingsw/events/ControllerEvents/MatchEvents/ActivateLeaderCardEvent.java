package it.polimi.ingsw.events.ControllerEvents.MatchEvents;

public class ActivateLeaderCardEvent extends MatchEvent {
    private String leaderCardID;

    /**
     * Constructor for the class
     * @param playerID the player that generated(directly or indirectly) this event
     * @param leaderCardID the index of the leader card to activate
     */
    public ActivateLeaderCardEvent(String playerID, String leaderCardID){
        super(playerID);
        this.leaderCardID = leaderCardID;
    }





    /**
     * getter for the ID of the leader card to activate
     * @return the ID of the leader card to activate
     */
    public String getLeaderCardID() {
        return leaderCardID;
    }
}
