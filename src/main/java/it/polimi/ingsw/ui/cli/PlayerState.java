package it.polimi.ingsw.ui.cli;

import it.polimi.ingsw.ui.cli.DashBoardView;
import it.polimi.ingsw.ui.cli.LeaderCardView;

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

    public PlayerState(DashBoardView dashBoard, HashMap<String, LeaderCardView> leaderCards){
        this.dashBoard=dashBoard;
        this.leaderCards= leaderCards;
    }
    public PlayerState(){
    }
}
