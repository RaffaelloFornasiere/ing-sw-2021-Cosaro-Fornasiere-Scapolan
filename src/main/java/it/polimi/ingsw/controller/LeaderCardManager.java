package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.IllegalOperation;
import it.polimi.ingsw.exceptions.NotPresentException;
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

    public ArrayList<LeaderPower> getSelectedPowers(Player p, java.lang.Class<? extends LeaderPower> powerType){
        ArrayList<LeaderPower> ret = new ArrayList<>();
        for(LeaderCard lc: p.getActiveLeaderCards())
            for(LeaderPower lp: lc.getSelectedLeaderPowers())
                if(powerType.isInstance(lp))
                    ret.add(lp);

        return ret;
    }

    public void selectLeaderPower(Player p, LeaderCard leaderCard, LeaderPower leaderPower)
            throws NotPresentException, IllegalOperation {
        if(!p.getActiveLeaderCards().contains(leaderCard))
            throw new NotPresentException("The leader card inserted must belong to the player inserted and must be active");

        for(LeaderCard alc: p.getActiveLeaderCards()) {
            for (LeaderPower slp : alc.getSelectedLeaderPowers()) {
                if (leaderPower.getIncompatiblePowers().contains(slp.getClass()) || slp.getIncompatiblePowers().contains(leaderPower.getClass()))
                    throw new IllegalOperation("The power selected is incompatible with another power");
            }
        }

        leaderCard.selectLeaderPower(leaderPower);
    }

    public void deselectLeaderPower(Player p, LeaderCard leaderCard, LeaderPower leaderPower)
            throws NotPresentException, IllegalOperation {
        if(!p.getActiveLeaderCards().contains(leaderCard))
            throw new NotPresentException("The leader card inserted must belong to the player inserted and must be active");

        leaderCard.deselectLeaderPower(leaderPower);
    }

    public void activateLeaderCard(Player p, LeaderCard leaderCard) throws NotPresentException, IllegalOperation {
        ArrayList<Requirement> requirements = cleanRequirements(leaderCard.getActivationRequirement());

        for(Requirement requirement: requirements)
            if(!requirement.checkRequirement(p))
                throw new IllegalOperation("Activation requirements not met");

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
