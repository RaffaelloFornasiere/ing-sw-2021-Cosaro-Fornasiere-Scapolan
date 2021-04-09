package it.polimi.ingsw.model;


public enum Marble{
    WHITE("White"),
    RED("Red"),
    YELLOW("Yellow"),
    GRAY("Gray"),
    PURPLE("PURPLE"),
    BLUE("Blue");
    final private String color;
    Marble(String color)
    {
        this.color = color;
    }

    @Override
    public String toString()
    {
        return color;
    }
}