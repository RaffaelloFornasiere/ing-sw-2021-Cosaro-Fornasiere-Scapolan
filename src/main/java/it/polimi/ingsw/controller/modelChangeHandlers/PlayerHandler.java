package it.polimi.ingsw.controller.modelChangeHandlers;

import it.polimi.ingsw.messageSenders.Sender;
import it.polimi.ingsw.events.ClientEvents.PlayerStateEvent;
import it.polimi.ingsw.model.LeaderCards.LeaderCard;
import it.polimi.ingsw.model.Player;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Observer for the Player
 */
public class PlayerHandler extends MatchObserver{
    public PlayerHandler(HashMap<String, Sender> networkData) {
        super(networkData);
    }

    /**
     * Sends to all the players the new state of a Player when it changes
     * @param o The Player that changed
     */
    @Override
    public void update(Object o) {
        Player player = (Player) o;

        HashMap<String, Boolean> leaderCards = new HashMap<>();
        HashMap<String, Boolean> actLeaderCards = new HashMap<>();
        ArrayList<LeaderCard> activeLeaderCards = player.getActiveLeaderCards();
        for(LeaderCard lc: player.getLeaderCards()){
            boolean active = activeLeaderCards.contains(lc);
            leaderCards.put(lc.getCardID(), active);
            if(active)
                actLeaderCards.put(lc.getCardID(), true);
        }

        for(String playerID: networkData.keySet()){
            if(player.getPlayerId().equals(playerID))
                networkData.get(playerID).sendObject(new PlayerStateEvent(player.getPlayerId(), leaderCards));
            else
                networkData.get(playerID).sendObject(new PlayerStateEvent(player.getPlayerId(), actLeaderCards));
        }
    }
}
