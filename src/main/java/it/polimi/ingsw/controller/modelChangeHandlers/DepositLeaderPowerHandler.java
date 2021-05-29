package it.polimi.ingsw.controller.modelChangeHandlers;

import it.polimi.ingsw.events.ClientEvents.DepositLeaderPowerStateEvent;
import it.polimi.ingsw.model.LeaderCards.DepositLeaderPower;
import it.polimi.ingsw.model.LeaderCards.LeaderCard;
import it.polimi.ingsw.model.LeaderCards.LeaderPower;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.Server.ClientHandlerSender;

import java.util.ArrayList;
import java.util.HashMap;

public class DepositLeaderPowerHandler extends MatchObserver{
    private Player leaderCardOwner;

    public DepositLeaderPowerHandler(HashMap<String, ClientHandlerSender> networkData, Player leaderCardOwner) {
        super(networkData);
        this.leaderCardOwner = leaderCardOwner;
    }

    @Override
    public void update(Object o) {
        DepositLeaderPower depositLeaderPower = (DepositLeaderPower) o;
        for(LeaderCard lc: leaderCardOwner.getLeaderCards()) {
            ArrayList<LeaderPower> leaderPowers = lc.getLeaderPowers();
            for (int i = 0; i < leaderPowers.size(); i++) {
                if (leaderPowers.get(i) == depositLeaderPower) {
                    sendToAll(new DepositLeaderPowerStateEvent(leaderCardOwner.getPlayerId(), lc.getCardID(), i, depositLeaderPower.getCurrentResources()));
                    return;
                }
            }
        }
    }
}
