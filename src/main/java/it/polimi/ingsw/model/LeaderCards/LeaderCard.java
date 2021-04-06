package it.polimi.ingsw.model.LeaderCards;

public class LeaderCard {

    private int victoryPoints;
    private Requirement activationRequirement;
    private LeaderPower power;

    public int getVictoryPoints() {
        return victoryPoints;
    }

    public Requirement getActivationRequirement() {
        return activationRequirement;
    }

    public LeaderPower getPower() {
        return power;
    }
}
