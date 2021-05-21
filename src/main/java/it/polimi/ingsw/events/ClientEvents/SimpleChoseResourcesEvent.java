package it.polimi.ingsw.events.ClientEvents;

public class SimpleChoseResourcesEvent extends ClientEvent{
    private int requiredResourcesOFChoice;

    public SimpleChoseResourcesEvent(String playerId, int requiredResourcesOFChoice) {
        super(playerId);
        this.requiredResourcesOFChoice = requiredResourcesOFChoice;
    }

    public int getRequiredResourcesOFChoice() {
        return requiredResourcesOFChoice;
    }
}
