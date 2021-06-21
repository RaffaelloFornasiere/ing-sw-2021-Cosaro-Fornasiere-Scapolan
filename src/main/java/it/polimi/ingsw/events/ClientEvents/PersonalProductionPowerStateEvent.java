package it.polimi.ingsw.events.ClientEvents;

import it.polimi.ingsw.model.ProductionPower;

/**
 * Event signaling a new state for the personal production power of a player
 */
public class PersonalProductionPowerStateEvent extends ClientEvent {
    private final ProductionPower personalProductionPower;

    /**
     * Constructor for the class
     *
     * @param playerId                The ID of the owner of the production power
     * @param personalProductionPower The production power of the player
     */
    public PersonalProductionPowerStateEvent(String playerId, ProductionPower personalProductionPower) {
        super(playerId);
        this.personalProductionPower = personalProductionPower;
    }

    /**
     * Getter for the production power of the player
     *
     * @return The production power of the player
     */
    public ProductionPower getPersonalProductionPower() {
        return personalProductionPower;
    }
}
