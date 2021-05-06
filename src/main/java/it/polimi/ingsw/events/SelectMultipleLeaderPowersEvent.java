package it.polimi.ingsw.events;

import it.polimi.ingsw.model.Resource;

import java.util.ArrayList;
import java.util.HashMap;

public class SelectMultipleLeaderPowersEvent extends Event{
    SelectMultipleLeaderPowersEvent() {
        super("SelectMultipleLeaderPowers");
    }

    public ArrayList<Resource> getResources() {
        return resources;
    }

    ArrayList<Resource> resources;
}
