package it.polimi.ingsw.events.clientEvents;

import java.util.ArrayList;

/**
 * Event sent to the client when it has to make the decision that have to be made before the start of the match
 */
public class InitialChoicesEvent extends ClientEvent{

    private final ArrayList<String> leaderCardsIDs;
    private final int numberOFLeaderCardsToChose;
    private final int numberResourcesOfChoice;

    /**
     * Constructor for the class
     * @param playerId The ID of the player that has to make the decisions
     * @param leaderCardsIDs The IDs of the leader card the player has to choose from to keep
     * @param numberOFLeaderCardsToChose The number of leader cards the player can keep
     * @param numberResourcesOfChoice The number of resources that the player can choose
     */
    public InitialChoicesEvent(String playerId, ArrayList<String> leaderCardsIDs, int numberOFLeaderCardsToChose, int numberResourcesOfChoice) {
        super(playerId);
        this.leaderCardsIDs = new ArrayList<>(leaderCardsIDs);
        this.numberResourcesOfChoice = numberResourcesOfChoice;
        this.numberOFLeaderCardsToChose = numberOFLeaderCardsToChose;
    }

    /**
     * Getter for the IDs of the leader card the player has to choose from to keep
     * @return The IDs of the leader card the player has to choose from to keep
     */
    public ArrayList<String> getLeaderCards() {
        return new ArrayList<>(leaderCardsIDs);
    }

    /**
     * Getter for the number of leader cards the player can keep
     * @return The number of leader cards the player can keep
     */
    public int getNumberOFLeaderCardsToChose() {
        return numberOFLeaderCardsToChose;
    }

    /**
     * Getter for the number of resources that the player can choose
     * @return The number of resources that the player can choose
     */
    public int getNumberResourcesOfChoice() {
        return numberResourcesOfChoice;
    }
}
