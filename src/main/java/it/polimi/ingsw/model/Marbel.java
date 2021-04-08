package it.polimi.ingsw.model;


public enum Marbel{
    WHITE("White"),
    RED("Red"),
    YELLOW("Yellow"),
    GRAY("Gray"),
    PURPLE("PURPLE"),
    BLUE("Blue");
    final private String color;
    Marbel(String color)
    {
        this.color = color;
    }

    @Override
    public String toString()
    {
        return color;
    }
}
