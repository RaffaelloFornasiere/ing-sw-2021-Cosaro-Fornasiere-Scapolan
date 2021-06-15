package it.polimi.ingsw.controller.modelChangeHandlers;

import it.polimi.ingsw.client.Sender;
import it.polimi.ingsw.events.ClientEvents.PlayerStateEvent;
import it.polimi.ingsw.model.LeaderCards.LeaderCard;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.Server.ClientHandlerSender;

import java.util.HashMap;

public class PlayerHandler extends MatchObserver{
    public PlayerHandler(HashMap<String, Sender> networkData) {
        super(networkData);
    }

    @Override
    public void update(Object o) {
        Player player = (Player) o;

        HashMap<String, Boolean> leaderCards = new HashMap<>();
        for(LeaderCard lc: player.getLeaderCards())
            leaderCards.put(lc.getCardID(), player.getActiveLeaderCards().contains(lc));

        sendToAll(new PlayerStateEvent(player.getPlayerId(), leaderCards));
    }
}
