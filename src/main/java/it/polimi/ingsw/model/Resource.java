package it.polimi.ingsw.model;

public enum Resource{
    COIN(1),
    ROCK(2),
    SERVANT(3),
    SHIELD(4);
    private final int resourceCode;

    Resource(int resourceCode) {this.resourceCode=resourceCode;}

    public int getResourceCode() {
        return resourceCode;
    }

}
