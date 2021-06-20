package it.polimi.ingsw.model;


public enum CardColor {
    BLUE("Blue", 0),
    GREEN("Green", 1),
    VIOLET("Violet", 2),
    YELLOW("Yellow", 3);

    private final String color;
    private final int code;

    CardColor(String color, int code) {
        this.color = color;
        this.code = code;
    }

    @Override
    public String toString() {
        return name();
    }

    /**
     * Getter for the code of the card color
     *
     * @return the code of the card color
     */
    public int getCode() {
        return code;
    }

    /**
     * Getter for the card color as a human readable string
     *
     * @return The card color as a human readable string
     */
    public String getColor() {
        return color;
    }
}
