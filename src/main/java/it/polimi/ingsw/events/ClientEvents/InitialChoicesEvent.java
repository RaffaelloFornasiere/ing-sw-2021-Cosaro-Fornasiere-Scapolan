package it.polimi.ingsw.events.ClientEvents;

import it.polimi.ingsw.model.LeaderCards.LeaderCard;

import java.util.ArrayList;

public class InitialChoicesEvent extends ClientEvent{

    private ArrayList<LeaderCard> leaderCards;
    private int numberResourcesOfChoice;

    /**
     * constructor of the class
     *
     * @param playerId the player that generated(directly or indirectly) this event
     */
    public InitialChoicesEvent(String playerId, ArrayList<LeaderCard> leaderCards, int numberResourcesOfChoice) {
        super(playerId);
        this.leaderCards = (ArrayList<LeaderCard>) leaderCards.clone();
        this.numberResourcesOfChoice = numberResourcesOfChoice;
    }

    public ArrayList<LeaderCard> getLeaderCards() {
        return (ArrayList<LeaderCard>) leaderCards.clone();
    }

    public int getNumberResourcesOfChoice() {
        return numberResourcesOfChoice;
    }
}
