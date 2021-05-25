package it.polimi.ingsw.events.ControllerEvents.MatchEvents;

import it.polimi.ingsw.model.Direction;

/**
 * Event sent to the server when a player wants to buy resources from the market
 * After having sent this, the client should expect to receive a ChoseMultipleExtraResourcePowerEvent, at which it
 *  must answer with a ChosenMultipleExtraResourcePowerEvent
 * After that the client should expect to receive a OrganizeResourcesEvent, at which it must answer with
 *  a NewResourceOrganizationEvent
 * The client will receive a BadRequestEvent if this event was mal-posed
 */
public class BuyResourcesEvent extends MatchEvent {
    /**
     * Constructor for the class
     * @param playerID The ID of the player that generated(directly or indirectly) this event
     * @param direction Row or column
     * @param index The index of row or column
     */
    public BuyResourcesEvent(String playerID, Direction direction, int index) {
        super(playerID);
        this.direction = direction;
        this.index = index;
    }

    private final Direction direction;
    private final int index;

    /**
     * Getter for the row or column
     * @return The row or column
     */
    public Direction getDirection() {return direction;}

    /**
     * Getter for the index of row or column
     * @return The index of row or column
     */
    public int getIndex() {return index;}
}
