package it.polimi.ingsw.events;

import it.polimi.ingsw.model.Direction;
import org.reflections.Reflections;

import java.lang.reflect.*;

import java.util.Set;

public class BuyResourcesEvent extends MatchEvent {
    BuyResourcesEvent(Direction direction, int index) {
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
