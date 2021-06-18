package it.polimi.ingsw.controller.modelChangeHandlers;

import it.polimi.ingsw.client.Sender;
import it.polimi.ingsw.events.ClientEvents.DepositLeaderPowerStateEvent;
import it.polimi.ingsw.model.LeaderCards.DepositLeaderPower;
import it.polimi.ingsw.model.LeaderCards.LeaderCard;
import it.polimi.ingsw.model.LeaderCards.LeaderPower;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.Server.ClientHandlerSender;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Observer for DepositLeaderPower
 */
public class DepositLeaderPowerHandler extends MatchObserver{
    private final Player leaderCardOwner;

    /**
     * Constructor for the class
     * @param networkData The Senders of all the player involved into the match
     * @param leaderCardOwner The owner of the leader card containing the observed power
     */
    public DepositLeaderPowerHandler(HashMap<String, Sender> networkData, Player leaderCardOwner) {
        super(networkData);
        this.leaderCardOwner = leaderCardOwner;
    }

    /**
     * Notifies all the players about the new state of the DepositLeaderPower
     * @param o The DepositLeaderPower that changed
     */
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
