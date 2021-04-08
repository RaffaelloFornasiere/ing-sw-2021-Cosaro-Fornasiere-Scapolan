package it.polimi.ingsw.model;


public enum Marbel{
    WHITE("White"),
    RED("Red"),
    YELLOW("Yellow"),
    GRAY("Gray"),
    VIOLET("Violet");
    private String color;
    Marbel(String color)
    {
        this.color = color;
    }
}
