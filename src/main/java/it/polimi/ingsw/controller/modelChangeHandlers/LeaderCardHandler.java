package it.polimi.ingsw.controller.modelChangeHandlers;

import it.polimi.ingsw.events.ClientEvents.LeaderCardStateEvent;
import it.polimi.ingsw.model.LeaderCards.LeaderCard;
import it.polimi.ingsw.model.LeaderCards.LeaderPower;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.virtualview.RequestsElaborator;

import java.util.ArrayList;
import java.util.HashMap;

//TODO register this handler in the right point (after being given to a player)
public class LeaderCardHandler extends MatchObserver {

    private Player leaderCardOwner;

    public LeaderCardHandler(HashMap<String, RequestsElaborator> networkData, Player leaderCardOwner) {
        super(networkData);
        this.leaderCardOwner = leaderCardOwner;
    }

    @Override
    public void update(Object o) {
        LeaderCard leaderCard = (LeaderCard) o;

        ArrayList<Boolean> selectedState = new ArrayList<>();
        for(LeaderPower lp: leaderCard.getLeaderPowers())
            selectedState.add(leaderCard.getSelectedLeaderPowers().contains(lp));

        sendToAll(new LeaderCardStateEvent(leaderCardOwner.getPlayerId(), leaderCard.getCardID(), selectedState));
    }
}
