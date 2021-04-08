package it.polimi.ingsw.model;

public enum Direction {
    ROW("Row"),
    COLUMN("Column");

    final private String direction;

    Direction(String direction)
    {
        this.direction = direction;
    }

    @Override
    public String toString()
    {
        return direction;
    }
}
