package it.polimi.ingsw.model;


public enum CardColor {
    BLUE("Blue", 0),
    GREEN("Green", 1),
    VIOLET("Violet", 2),
    YELLOW("Yellow", 3);

    private String color;



    private int code;
    CardColor(String color, int code)
    {
        this.color = color;
        this.code = code;
    }

    @Override
    public String toString()
    {
        return name();
    }


    public int getCode() {
        return code;
    }

    public String getColor() {
        return color;
    }
}
