package it.polimi.ingsw.ui.cli;

import java.util.HashMap;

public class PlayerState {
    public DashBoardView getDashBoard() {
        return dashBoard;
    }

    public DashBoardView dashBoard;

    public HashMap<String, LeaderCardView> getLeaderCards() {
        return leaderCards;
    }

    public HashMap<String, LeaderCardView> leaderCards = new HashMap<>();

}
