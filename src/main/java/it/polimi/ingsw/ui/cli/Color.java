package it.polimi.ingsw.ui.cli;

/**
 * this class defines standard colorS
 */
public enum Color {
    WHITE("\u001B[97m"),
    BLACK("\u001B[30m"),
    BLUE("\u001B[34m"),
    RED("\u001B[31m"),
    GREEN("\u001B[32m"),
    YELLOW("\u001B[33m"),
    MAGENTA("\u001B[35m"),
    CYAN("\u001B[36m"),
    GREY("\u001B[37m"),
    BLACK_BACKGROUND("\u001B[40m"),
    RED_BACKGROUND("\u001B[41m"),
    GREEN_BACKGROUND("\u001B[42m"),
    YELLOW_BACKGROUND("\u001B[43m"),
    BLUE_BACKGROUND("\u001B[44m"),
    PURPLE_BACKGROUND("\u001B[45m"),
    CYAN_BACKGROUND("\u001B[46m"),
    WHITE_BACKGROUND("\u001B[47m");



    private final String ansiCode;


    /**
     * Constructor
     *
     */
    Color(String ansiCode) {
        this.ansiCode = ansiCode;

    }

    /**
     * Method to get the ansi code of a color
     *
     * @return a String containing the AnsiCode
     */
    public String getAnsiCode() {
        return ansiCode;
    }



    /**
     * @return the reset color ANSI code
     */
    public static String reset() {
        return "\u001B[0m";
    }

}


