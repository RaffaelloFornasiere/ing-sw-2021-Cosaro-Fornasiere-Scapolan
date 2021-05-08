package it.polimi.ingsw.events;

public abstract class MatchEvent extends Event{
    /**
     * constructor for the class
     * @param playerID the player that generated(directly or indirectly) this event
     */
    public MatchEvent(String playerID){
        super(playerID);
    }
}
