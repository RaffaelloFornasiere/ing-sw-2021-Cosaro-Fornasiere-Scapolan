package it.polimi.ingsw.events.clientEvents;

import org.jetbrains.annotations.NotNull;

/**
 * Class representing the achievement of a player at the end of the match
 */
public class FinalPlayerState implements Comparable<FinalPlayerState>{
    private final String playerID;
    private final int victoryPoints;
    private final int totalResources;

    /**
     * Constructor for the class
     * @param playerID The ID of the player
     * @param victoryPoints The total victory points of the player
     * @param totalResources The total resources accumulated by the player
     */
    public FinalPlayerState(String playerID, int victoryPoints, int totalResources) {
        this.playerID = playerID;
        this.victoryPoints = victoryPoints;
        this.totalResources = totalResources;
    }

    /**
     * Getter for the ID of the player
     * @return The ID of the player
     */
    public String getPlayerID() {
        return playerID;
    }

    /**
     * Getter for the total victory points of the player
     * @return The total victory points of the player
     */
    public int getVictoryPoints() {
        return victoryPoints;
    }

    /**
     * Getter for the total resources accumulated by the player
     * @return The total resources accumulated by the player
     */
    public int getTotalResources() {
        return totalResources;
    }


    @Override
    public int compareTo(@NotNull FinalPlayerState o) {
        int victoryPointsCompare = Integer.compare(this.victoryPoints, o.victoryPoints);
        if(victoryPointsCompare!=0) return victoryPointsCompare;
        return Integer.compare(this.totalResources, o.totalResources);
    }
}
