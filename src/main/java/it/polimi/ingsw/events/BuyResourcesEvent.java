package it.polimi.ingsw.events;

import it.polimi.ingsw.model.Direction;

public class BuyResourcesEvent extends MatchEvent {
    /**
     *
     * @param playerID the player that generated(directly or indirectly) this event
     * @param direction
     * @param index
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
