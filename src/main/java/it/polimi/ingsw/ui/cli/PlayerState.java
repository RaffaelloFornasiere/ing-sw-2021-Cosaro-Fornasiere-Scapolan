package it.polimi.ingsw.ui.cli;

import java.util.HashMap;

public class PlayerState {
    /**
     * getter
     * @return the dashboard of this player
     */
    public DashBoardView getDashBoard() {
        return dashBoard;
    }

    public DashBoardView dashBoard;

    /**
     * getter
     * @return the leaderCards of this player
     */
    public HashMap<String, LeaderCardView> getLeaderCards() {
        return leaderCards;
    }

    public HashMap<String, LeaderCardView> leaderCards = new HashMap<>();

    /**
     * constructor
     * @param dashBoard the dashboard
     * @param leaderCards the leader cards
     */
    public PlayerState(DashBoardView dashBoard, HashMap<String, LeaderCardView> leaderCards) {
        this.dashBoard = dashBoard;
        this.leaderCards = leaderCards;
    }

    public PlayerState() {
    }
}
