package it.polimi.ingsw.events.clientEvents;

/**
 * Event sent to the client when it has to choose the type for a certain number of resources
 */
public class SimpleChoseResourcesEvent extends ClientEvent {
    private final int requiredResourcesOFChoice;

    /**
     * Constructor for the class
     *
     * @param playerId                  The Id of the player that will have to make the choice
     * @param requiredResourcesOFChoice The number of resources for witch to chose a type
     */
    public SimpleChoseResourcesEvent(String playerId, int requiredResourcesOFChoice) {
        super(playerId);
        this.requiredResourcesOFChoice = requiredResourcesOFChoice;
    }

    /**
     * Getter for the number of resources for witch to chose a type
     *
     * @return The number of resources for witch to chose a type
     */
    public int getRequiredResourcesOFChoice() {
        return requiredResourcesOFChoice;
    }
}
