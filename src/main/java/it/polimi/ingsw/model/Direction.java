package it.polimi.ingsw.model;

public enum Direction {
    ROW("Row"),
    COLUMN("Column");

    private String direction;

    Direction(String direction)
    {
        this.direction = direction;
    }
}
