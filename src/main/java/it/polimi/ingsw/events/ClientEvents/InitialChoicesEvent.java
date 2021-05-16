package it.polimi.ingsw.events.ClientEvents;

import it.polimi.ingsw.model.LeaderCards.LeaderCard;

import java.util.ArrayList;

public class InitialChoicesEvent extends ClientEvent{

    private ArrayList<String> leaderCardsIDs;
    private int numberResourcesOfChoice;
    private int numberOFLeaderCardsToChose;

    /**
     * constructor of the class
     *
     * @param playerId the player that generated(directly or indirectly) this event
     */
    public InitialChoicesEvent(String playerId, ArrayList<String> leaderCardsIDs, int numberOFLeaderCardsToChose, int numberResourcesOfChoice) {
        super(playerId);
        this.leaderCardsIDs = (ArrayList<String>) leaderCardsIDs.clone();
        this.numberResourcesOfChoice = numberResourcesOfChoice;
        this.numberOFLeaderCardsToChose = numberOFLeaderCardsToChose;
    }

    public ArrayList<String> getLeaderCards() {
        return (ArrayList<String>) leaderCardsIDs.clone();
    }

    public int getNumberOFLeaderCardsToChose() {
        return numberOFLeaderCardsToChose;
    }

    public int getNumberResourcesOfChoice() {
        return numberResourcesOfChoice;
    }
}
