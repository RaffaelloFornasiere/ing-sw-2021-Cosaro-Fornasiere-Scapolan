package it.polimi.ingsw.model;


public enum Marble{
    WHITE("White"),
    RED("Red"),
    YELLOW("Yellow"),
    GRAY("Gray"),
    PURPLE("Purple"),
    BLUE("Blue");
    final private String color;
    Marble(String color)
    {
        this.color = color;
    }

    //purtroppo, json deserializza usando name() e serializza usando toString, quindi tooString deve per
    //forza fare return name(). Per questi vi ho introotto getColorName() se voleet stampare il colore sexy e non tutto maiusc
    @Override
    public String toString()
    {
        return name();
    }

    public String getColor(){return color;}
}
