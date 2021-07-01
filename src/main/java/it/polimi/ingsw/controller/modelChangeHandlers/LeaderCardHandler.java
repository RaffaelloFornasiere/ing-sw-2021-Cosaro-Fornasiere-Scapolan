package it.polimi.ingsw.controller.modelChangeHandlers;

import it.polimi.ingsw.messageSenders.Sender;
import it.polimi.ingsw.events.clientEvents.LeaderCardStateEvent;
import it.polimi.ingsw.model.leaderCards.LeaderCard;
import it.polimi.ingsw.model.leaderCards.LeaderPower;
import it.polimi.ingsw.model.Player;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Observer for the LeaderCard
 */
public class LeaderCardHandler extends MatchObserver {

    private final Player leaderCardOwner;

    /**
     * Constructor for the class
     * @param networkData The Senders of all the player involved into the match
     * @param leaderCardOwner The owner of the LeaderCard being observed
     */
    public LeaderCardHandler(HashMap<String, Sender> networkData, Player leaderCardOwner) {
        super(networkData);
        this.leaderCardOwner = leaderCardOwner;
    }

    /**
     * Sends to all the player the state of the LeaderCard being observed when it changes
     * @param o The LeaderCard that changed
     */
    @Override
    public void update(Object o) {
        LeaderCard leaderCard = (LeaderCard) o;

        ArrayList<Boolean> selectedState = new ArrayList<>();
        for(LeaderPower lp: leaderCard.getLeaderPowers())
            selectedState.add(leaderCard.getSelectedLeaderPowers().contains(lp));

        sendToAll(new LeaderCardStateEvent(leaderCardOwner.getPlayerId(), leaderCard.getCardID(), selectedState));
    }
}
