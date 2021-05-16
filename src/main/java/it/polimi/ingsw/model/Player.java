package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.IllegalOperation;
import it.polimi.ingsw.exceptions.NotPresentException;
import it.polimi.ingsw.model.LeaderCards.DepositLeaderPower;
import it.polimi.ingsw.model.LeaderCards.LeaderCard;
import it.polimi.ingsw.model.LeaderCards.LeaderPower;
import it.polimi.ingsw.utilities.Observable;
import it.polimi.ingsw.utilities.Pair;

import java.util.ArrayList;
import java.util.HashMap;

public class Player extends Observable {
    private String playerId;
    private ArrayList<Pair<LeaderCard, Boolean>> leaderCards;
    private DashBoard dashBoard;

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
        for(Pair<LeaderCard, Boolean> lco: leaderCards)
            lcs.add(lco.getKey());
        return lcs;
    }

    /**
     * getter for active leader cards
     * @return active leader cards
     */
    public ArrayList<LeaderCard> getActiveLeaderCards(){
        ArrayList<LeaderCard> lcs = new ArrayList<>();
        for(Pair<LeaderCard, Boolean> lco: leaderCards)
            if(lco.getValue())
                lcs.add(lco.getKey());
        return lcs;
    }

    /**
     * method to give the player some leader cards
     * @param leaderCards the leader cards to give the player
     */
    public void setLeaderCards(ArrayList<LeaderCard> leaderCards) {
        this.leaderCards = new ArrayList<>();
        for(LeaderCard lc: leaderCards){
            this.leaderCards.add(new Pair<>(lc, false));
        }
        notifyObservers();
    }

    public LeaderCard getleaderCardFromID(String leaderCardID) throws NotPresentException{
        for(Pair<LeaderCard, Boolean> lcp: leaderCards)
            if(lcp.getKey().getCardID().equals(leaderCardID))
                return lcp.getKey();

        throw new NotPresentException("This player does not own any leader card with the given ID");
    }

    /**
     * Methods that activates a leader card
     * @param leaderCard the leader card to activate
     * @throws NotPresentException if the leader card does not belong to this player
     * @throws IllegalOperation if the leader card is already active
     */
    public void activateLeaderCard(LeaderCard leaderCard) throws NotPresentException, IllegalOperation {
        for (int i = 0, leaderCardsSize = leaderCards.size(); i < leaderCardsSize; i++) {
            Pair<LeaderCard, Boolean> lco = leaderCards.get(i);
            if (leaderCard == lco.getKey()) {
                if (lco.getValue()) throw new IllegalOperation("Leader card already Active");
                this.leaderCards.remove(i);
                this.leaderCards.add(i, new Pair<>(lco.getKey(), true));
                notifyObservers();
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
