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
    private HashMap<LeaderCard, Boolean> leaderCards;
    private DashBoard dashBoard;

    public Player(String name, DashBoard dashBoard) {
        this.playerId = name;

        this.dashBoard= dashBoard;

        this.leaderCards = new HashMap<>();
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
        return new ArrayList<>(leaderCards.keySet());
    }

    /**
     * getter for active leader cards
     * @return active leader cards
     */
    public ArrayList<LeaderCard> getActiveLeaderCards(){
        ArrayList<LeaderCard> alc = new ArrayList<>();
        for(LeaderCard lc: leaderCards.keySet())
            if(leaderCards.get(lc))
                alc.add(lc);
        return alc;
    }

    /**
     * method to give the player some leader cards
     * @param leaderCards the leader cards to give the player
     */
    public void setLeaderCards(ArrayList<LeaderCard> leaderCards) {
        this.leaderCards = new HashMap<>();
        for(LeaderCard lc: leaderCards){
            this.leaderCards.put(lc, false);
        }
        notifyObservers();
    }

    public LeaderCard getLeaderCardFromID(String leaderCardID) throws NotPresentException{
        for(LeaderCard lc: leaderCards.keySet())
            if(lc.getCardID().equals(leaderCardID))
                return lc;

        throw new NotPresentException("This player does not own any leader card with the given ID");
    }

    /**
     * Methods that activates a leader card
     * @param leaderCard the leader card to activate
     * @throws NotPresentException if the leader card does not belong to this player
     * @throws IllegalOperation if the leader card is already active
     */
    public void activateLeaderCard(LeaderCard leaderCard) throws NotPresentException, IllegalOperation {
        boolean active = leaderCards.get(leaderCard);
        if (leaderCard == null) throw new NotPresentException("The selected leader card is not owned by this player");
        if (active) throw new IllegalOperation("Leader card already Active");
        this.leaderCards.put(leaderCard, true);
        notifyObservers();
    }

    public void removeLeaderCard(LeaderCard leaderCard) throws NotPresentException {
        if(leaderCards.remove(leaderCard) == null)
            throw new NotPresentException("Leader card not present");
    }

    /**
     * gets all the resources stored in the dashboard, as well as in a leader power as an hashmap
     * if a resource is not present, the map will have an entry for it anyway, with value 0
     * @return all the resources in the warehouse and in the strongbox
     */
    public HashMap<Resource, Integer> getAllPayerResources(){
        HashMap<Resource, Integer> dashboardResources = dashBoard.getAllDashboardResources();
        HashMap<Resource, Integer> resources = getLeaderCardsResources();

        resources.replaceAll((r, v) -> v + dashboardResources.getOrDefault(r, 0));

        return resources;
    }

    public HashMap<Resource, Integer> getLeaderCardsResources(){
        HashMap<Resource, Integer> resources = new HashMap<>();
        for(Resource r: Resource.values())
            resources.put(r, 0);

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
