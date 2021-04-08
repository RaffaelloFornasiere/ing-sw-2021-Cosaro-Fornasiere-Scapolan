package it.polimi.ingsw.model.LeaderCards;

/**
 * Class that represents a leader card
 */
public class LeaderCard {

    private int victoryPoints;
    private Requirement activationRequirement;
    private LeaderPower power;

    /**
     * Constructor for the class
     * @param victoryPoints amount of points awarded to the player at the end of the match if the card is active
     * @param activationRequirement requirement that must be fulfilled to activate the card
     * @param power effects of the card
     */
    public LeaderCard(int victoryPoints, Requirement activationRequirement, LeaderPower power){
        this.victoryPoints = victoryPoints;
        this.activationRequirement = activationRequirement;
        this.power = power;
    }

    /**
     * getter for the victory points
     * @return the value of victory points
     */
    public int getVictoryPoints() {
        return victoryPoints;
    }

    /**
     * getter for the requirements
     * @return the requirements
     */
    public Requirement getActivationRequirement() {
        return activationRequirement;
    }

    /**
     * getter for the victory points
     * @return the value of victory points
     */
    public LeaderPower getPower() {
        return power;
    }
}
