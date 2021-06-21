package it.polimi.ingsw.model;

public enum Direction {
    ROW("Row"),
    COLUMN("Column");

    private final String direction;

    Direction(String direction) {
        this.direction = direction;
    }

    /**
     * Getter for the direction as a human readable string
     *
     * @return The direction as a human readable string
     */
    public String getDirection() {
        return direction;
    }

    @Override
    public String toString() {
        return name();
    }
}
