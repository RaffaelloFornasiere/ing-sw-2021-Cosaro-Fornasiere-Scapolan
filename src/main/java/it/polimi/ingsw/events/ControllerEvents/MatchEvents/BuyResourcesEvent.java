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
     *
     * @param playerID the player that generated(directly or indirectly) this event
     * @param direction row or column
     * @param index index of row or column
     */
    public BuyResourcesEvent(String playerID, Direction direction, int index) {
        super(playerID);
        this.direction = direction;
        this.index = index;
    }

    private Direction direction;
    private int index;
    private boolean UseLeaderPowers;
    private String eventName;

    public Direction getDirection() {return direction;}
    public int getIndex() {return index;}

    @Override
    public String getEventName() {
        return this.getClass().getName();
    }



}
