package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.LeaderCards.LeaderCard;
import it.polimi.ingsw.model.LeaderCards.LeaderPower;
import it.polimi.ingsw.model.LeaderCards.Requirement;
import it.polimi.ingsw.model.MatchState;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Resource;

import java.util.ArrayList;
import java.util.HashMap;

public class LeaderCardManager {

    private MatchState matchState;

    public LeaderCardManager(MatchState matchState){
        this.matchState = matchState;
    }

    /**
     * Returns the selected powers of a player of a certain type
     * @param player the player
     * @param powerType the class of the type of powers that the methods should search for
     * @return the selected powers of the type inserted
     */
    public ArrayList<LeaderPower> getSelectedPowers(Player player, java.lang.Class<? extends LeaderPower> powerType){
        ArrayList<LeaderPower> ret = new ArrayList<>();
        for(LeaderCard lc: player.getActiveLeaderCards())
            for(LeaderPower lp: lc.getSelectedLeaderPowers())
                if(powerType.isInstance(lp))
                    ret.add(lp);

        return ret;
    }

    /**
     * Methods that selects a leader power
     * @param player the player owning the leader card containing the power to select
     * @param leaderCard the leader card containing the power to select
     * @param leaderPower the power to select
     * @throws NotPresentException if the leader power passed does not belong to the card or the leader card does not belong to the player
     * @throws IllegalOperation if the leader power was already selected
     * @throws LeaderCardNotActiveException if the leader card inserted was not active
     * @throws IncompatiblePowersException if the leader power is incompatible with other selected powers
     */
    public void selectLeaderPower(Player player, LeaderCard leaderCard, LeaderPower leaderPower)
            throws NotPresentException, IllegalOperation, LeaderCardNotActiveException, IncompatiblePowersException {
        if(!player.getLeaderCards().contains(leaderCard))
            throw new NotPresentException("The leader card inserted must belong to the player inserted");

        if(!player.getActiveLeaderCards().contains(leaderCard))
            throw new LeaderCardNotActiveException("The leader card inserted must be active");

        for(LeaderCard alc: player.getActiveLeaderCards()) {
            for (LeaderPower slp : alc.getSelectedLeaderPowers()) {
                if (leaderPower.getIncompatiblePowers().contains(slp.getClass()) || slp.getIncompatiblePowers().contains(leaderPower.getClass()))
                    throw new IncompatiblePowersException("The power selected is incompatible with another power");
            }
        }

        leaderCard.selectLeaderPower(leaderPower);
    }

    /**
     * Methods that deselects a leader power
     * @param player the player owning the leader card containing the power to deselect
     * @param leaderCard the leader card containing the power to deselect
     * @param leaderPower the power to deselect
     * @throws NotPresentException if the leader power passed does not belong to the card or the leader card does not belong to the player
     * @throws IllegalOperation if the leader power was not selected
     * @throws LeaderCardNotActiveException if the leader card inserted was not active
     */
    public void deselectLeaderPower(Player player, LeaderCard leaderCard, LeaderPower leaderPower)
            throws NotPresentException, IllegalOperation, LeaderCardNotActiveException {
        if(!player.getLeaderCards().contains(leaderCard))
            throw new NotPresentException("The leader card inserted must belong to the player inserted");

        if(!player.getActiveLeaderCards().contains(leaderCard))
            throw new LeaderCardNotActiveException("The leader card inserted must be active");

        leaderCard.deselectLeaderPower(leaderPower);
    }

    /**
     *
     * @param p the player owning the leader card to activate
     * @param leaderCard the leader card to activate
     * @throws NotPresentException if the leader card does not belong to this player
     * @throws IllegalOperation if the leader card is already active
     * @throws RequirementsNotMetException if the leader card activation requirements are not met
     */
    public void activateLeaderCard(Player p, LeaderCard leaderCard) throws NotPresentException, IllegalOperation, RequirementsNotMetException {
        ArrayList<Requirement> requirements = cleanRequirements(leaderCard.getActivationRequirement());

        for(Requirement requirement: requirements)
            if(!requirement.checkRequirement(p))
                throw new RequirementsNotMetException("Activation requirements not met");

        p.activateLeaderCard(leaderCard);
    }

    private ArrayList<Requirement> cleanRequirements(ArrayList<Requirement> toClean){
        if(toClean == null || toClean.size()==0) return toClean;

        ArrayList<Requirement> cleaned = (ArrayList<Requirement>)toClean.clone();

        int i = 0;
        int j;

        Requirement cleaning;
        boolean merged;

        while(i<cleaned.size()){
            cleaning = cleaned.get(i);
            merged = false;
            j=0;
            while(j<cleaned.size() && !merged){
                if(j!=i){
                    if(cleaning.isEquivalent(cleaned.get(j))){
                        try {
                            cleaned.add(cleaning.merge(cleaned.get(j)));
                        } catch (IllegalOperation illegalOperation) {
                            illegalOperation.printStackTrace();
                        }
                        merged = true;
                    }
                    else
                        j++;
                }
            }
            if(merged){
                cleaned.remove(i);
                cleaned.remove(j);
                if(j<i)
                    i--;
            }
            else
                i++;
        }

        return cleaned;
    }

}
