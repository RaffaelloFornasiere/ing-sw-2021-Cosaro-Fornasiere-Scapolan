package it.polimi.ingsw.model;

import it.polimi.ingsw.model.LeaderCards.LeaderCard;

import java.util.ArrayList;

public class Player {
    private String name;
    ArrayList<LeaderCardOwnership> leaderCards;
    DashBoard dashBoard;

    private class LeaderCardOwnership {
        protected LeaderCard leaderCard;
        protected boolean active;
        protected boolean selected;
    }

    Player(String name)
    {
        this.name = name;
        this.leaderCards = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public ArrayList<LeaderCard> getLeaderCards() {
        ArrayList<LeaderCard> lcs = new ArrayList<>();
        for(LeaderCardOwnership lco: leaderCards)
            lcs.add(lco.leaderCard);
        return lcs;
    }

    public void setLeaderCards(ArrayList<LeaderCard> leaderCards) {
        LeaderCardOwnership lco;
        for(LeaderCard lc: leaderCards){
            lco = new LeaderCardOwnership();
            lco.leaderCard=lc;
            lco.active=false;
            lco.selected=false;
            this.leaderCards.add(lco);
        }
    }

}
