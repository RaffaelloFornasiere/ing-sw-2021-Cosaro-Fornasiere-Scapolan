package it.polimi.ingsw.ui.cli;

import java.util.ArrayList;

public class PlayerState {
    public DashBoardView dashBoard;
    public ArrayList<LeaderCardView> leaderCards;

    public PlayerState(DashBoardView dashBoard, ArrayList<LeaderCardView> leaderCards) {
        this.dashBoard = dashBoard;
        this.leaderCards = leaderCards;
    }
}
