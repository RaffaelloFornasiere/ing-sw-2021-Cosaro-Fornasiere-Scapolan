package it.polimi.ingsw.model;


public enum CardColor {
    BLUE("Blue"),
    GREEN("Green"),
    VIOLET("Violet"),
    YELLOW("Yellow");

    private String color;
    CardColor(String color)
    {
        this.color = color;
    }

}
