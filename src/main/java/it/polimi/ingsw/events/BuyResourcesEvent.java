package it.polimi.ingsw.events;

import it.polimi.ingsw.model.Direction;

public class BuyResourcesEvent extends Event {
    BuyResourcesEvent(Direction direction, int index) {
        super("BuyResourceEvent");
        this.direction = direction;
        this.index = index;
    }

    private Direction direction;
    private int index;

    public Direction getDirection() {
        return direction;
    }

    public int getIndex() {
        return index;
    }
}
