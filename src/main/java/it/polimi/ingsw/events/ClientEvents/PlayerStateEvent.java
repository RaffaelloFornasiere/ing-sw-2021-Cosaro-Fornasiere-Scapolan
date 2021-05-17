package it.polimi.ingsw.events.ClientEvents;

import java.util.HashMap;

public class PlayerStateEvent extends ClientEvent{
    HashMap<String, Boolean> leaderCards;

    public PlayerStateEvent(String playerId, HashMap<String, Boolean> leaderCards) {
        super(playerId);
        this.leaderCards = (HashMap<String, Boolean>) leaderCards.clone();
    }

    public HashMap<String, Boolean> getLeaderCards() {
        return (HashMap<String, Boolean>) leaderCards.clone();
    }
}
