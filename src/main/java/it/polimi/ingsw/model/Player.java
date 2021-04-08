package it.polimi.ingsw.model;

import it.polimi.ingsw.model.LeaderCards.LeaderCard;

import java.util.ArrayList;

public class Player {
    private String name;
    ArrayList<LeaderCard> leaderCards;
    DashBoard dashBoard;

    Player(String name)
    {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public ArrayList<LeaderCard> getLeaderCards() {
        return leaderCards;
    }

    public void setLeaderCards(ArrayList<LeaderCard> leaderCards) {
        this.leaderCards = leaderCards;
    }

}
