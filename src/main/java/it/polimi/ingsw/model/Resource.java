package it.polimi.ingsw.model;

public enum Resource {
    COIN(1),
    ROCK(2),
    SERVANT(3),
    SHIELD(4),
    ANY(5),
    FAITH_POINT(6);
    private final int resourceCode;


    Resource(int resourceCode) {
        this.resourceCode = resourceCode;
    }

    public int getResourceCode() {
        return resourceCode;
    }

    public static Resource toResource(Marble marble) {
        switch (marble) {
            case BLUE:
                return SHIELD;
            case GRAY:
                return ROCK;
            case YELLOW:
                return COIN;
            case PURPLE:
                return SERVANT;
            case WHITE:
                return ANY;
            default:
                return null;
        }
    }
}
