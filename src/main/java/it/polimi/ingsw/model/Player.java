package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.IllegalOperation;
import it.polimi.ingsw.exceptions.NotPresentException;
import it.polimi.ingsw.model.LeaderCards.LeaderCard;

import java.util.ArrayList;

public class Player {
    private String name;
    ArrayList<LeaderCardOwnership> leaderCards;
    DashBoard dashBoard;

    private class LeaderCardOwnership {
        protected LeaderCard leaderCard;
        protected boolean active;
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

    public ArrayList<LeaderCard> getActiveLeaderCards(){
        ArrayList<LeaderCard> lcs = new ArrayList<>();
        for(LeaderCardOwnership lco: leaderCards)
            if(lco.active)
                lcs.add(lco.leaderCard);
        return lcs;
    }

    public void setLeaderCards(ArrayList<LeaderCard> leaderCards) {
        LeaderCardOwnership lco;
        for(LeaderCard lc: leaderCards){
            lco = new LeaderCardOwnership();
            lco.leaderCard=lc;
            lco.active=false;
            this.leaderCards.add(lco);
        }
    }

    public void activateLeaderCard(LeaderCard lc) throws NotPresentException, IllegalOperation {
        for (LeaderCardOwnership lco : leaderCards) {
            if (lc == lco.leaderCard) {
                if (lco.active) throw new IllegalOperation("Leader card already Active");
                lco.active = true;
                return;
            }
        }

        throw new NotPresentException("The selected leader card is not owned by this player");
    }

}
