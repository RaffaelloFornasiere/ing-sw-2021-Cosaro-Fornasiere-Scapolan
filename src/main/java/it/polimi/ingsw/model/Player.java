package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.IllegalOperation;
import it.polimi.ingsw.exceptions.NotPresentException;
import it.polimi.ingsw.model.LeaderCards.DepositLeaderPower;
import it.polimi.ingsw.model.LeaderCards.LeaderCard;
import it.polimi.ingsw.model.LeaderCards.LeaderPower;

import java.util.ArrayList;
import java.util.HashMap;

public class Player {
    private String playerId;
    ArrayList<LeaderCardOwnership> leaderCards;
    DashBoard dashBoard;

    private class LeaderCardOwnership {
        protected LeaderCard leaderCard;
        protected boolean active;
    }

    public Player(String name, DashBoard dashBoard) {
        this.playerId = name;

        this.dashBoard= dashBoard;

        this.leaderCards = new ArrayList<>();
    }

    /**
     * getter for the id of the player
     * @return the id of the player
     */
    public String getPlayerId() {
        return playerId;
    }

    /**
     * getter for the dashboard of the player
     * @return the dashboard of the player
     */
    public DashBoard getDashBoard(){return dashBoard;}

    /**
     * getter for all leader cards
     * @return all leader cards
     */
    public ArrayList<LeaderCard> getLeaderCards() {
        ArrayList<LeaderCard> lcs = new ArrayList<>();
        for(LeaderCardOwnership lco: leaderCards)
            lcs.add(lco.leaderCard);
        return lcs;
    }

    /**
     * getter for active leader cards
     * @return active leader cards
     */
    public ArrayList<LeaderCard> getActiveLeaderCards(){
        ArrayList<LeaderCard> lcs = new ArrayList<>();
        for(LeaderCardOwnership lco: leaderCards)
            if(lco.active)
                lcs.add(lco.leaderCard);
        return lcs;
    }

    /**
     * method to give the player some leader cards
     * @param leaderCards the leader cards to give the player
     */
    public void setLeaderCards(ArrayList<LeaderCard> leaderCards) {
        LeaderCardOwnership lco;
        for(LeaderCard lc: leaderCards){
            lco = new LeaderCardOwnership();
            lco.leaderCard=lc;
            lco.active=false;
            this.leaderCards.add(lco);
        }
    }

    /**
     * Methods that activates a leader card
     * @param leaderCard the leader card to activate
     * @throws NotPresentException if the leader card does not belong to this player
     * @throws IllegalOperation if the leader card is already active
     */
    public void activateLeaderCard(LeaderCard leaderCard) throws NotPresentException, IllegalOperation {
        for (LeaderCardOwnership lco : leaderCards) {
            if (leaderCard == lco.leaderCard) {
                if (lco.active) throw new IllegalOperation("Leader card already Active");
                lco.active = true;
                return;
            }
        }

        throw new NotPresentException("The selected leader card is not owned by this player");
    }

    /**
     * gets all the resources stored in the dashboard, as well as in a leader power as an hashmap
     * if a resource is not present, the map will have an entry for it anyway, with value 0
     * @return all the resources in the warehouse and in the strongbox
     */
    public HashMap<Resource, Integer> getAllPayerResources(){
        HashMap<Resource, Integer> resources = dashBoard.getAllDashboardResources();

        ArrayList<LeaderCard> leaderCards = getActiveLeaderCards();
        for(LeaderCard lc: leaderCards) {
            ArrayList<LeaderPower> leaderPowers = lc.getLeaderPowers();
            for(LeaderPower lp : leaderPowers)
                if(lp instanceof DepositLeaderPower) {
                    HashMap<Resource, Integer> lpResources = ((DepositLeaderPower)lp).getCurrentResources();
                    for(Resource r: lpResources.keySet())
                        resources.put(r, lpResources.get(r) + resources.getOrDefault(r, 0));
                }
        }
        return resources;
    }


}
