package it.polimi.ingsw.controller.modelChangeHandlers;

import it.polimi.ingsw.events.ClientEvents.DepositLeaderPowerStateEvent;
import it.polimi.ingsw.model.LeaderCards.DepositLeaderPower;
import it.polimi.ingsw.model.LeaderCards.LeaderCard;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.virtualview.RequestsElaborator;

import java.util.HashMap;

//TODO register this handler in the right point
public class DepositLeaderPowerHandler extends MatchObserver{
    private Player leaderCardOwner;

    public DepositLeaderPowerHandler(HashMap<String, RequestsElaborator> networkData, Player leaderCardOwner) {
        super(networkData);
        this.leaderCardOwner = leaderCardOwner;
    }

    @Override
    public void update(Object o) {
        DepositLeaderPower depositLeaderPower = (DepositLeaderPower) o;
        for(LeaderCard lc: leaderCardOwner.getLeaderCards())
            if(lc.getLeaderPowers().contains(depositLeaderPower)){
                sendToAll(new DepositLeaderPowerStateEvent(leaderCardOwner.getPlayerId(), lc.getCardID(), depositLeaderPower.getCurrentResources()));
                return;
            }
    }
}
