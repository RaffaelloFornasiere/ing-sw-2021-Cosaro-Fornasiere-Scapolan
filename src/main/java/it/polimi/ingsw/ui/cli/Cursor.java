package it.polimi.ingsw.ui.cli;

public class Cursor {

    /**
     * clear entire screen and delete all lines saved in the scrollback buffer
     */
    public static void clearAll() {
        System.out.print("\u001b[3J");
    }

    /**
     * clear entire screen (and moves cursor to upper left
     */
    public static void clearScreen() {
        System.out.print("\u001b[2J");
    }

    /**
     * clear entire line
     */
    public static void clearLine() {
        System.out.print("\u001b[2K");
    }

    /**
     * Method to enable showing the cursor in Terminal
     */
    public static void showCursor() {
        System.out.print("\u001b[?25h");
    }

    /**
     * Method to disable showing the cursor in Terminal
     */
    public static void hideCursor() {
        System.out.print("\u001b[?25l");
    }

    /**
     * moves cursor up relatively to actual position
     */
    public static void moveCursorUp(int abs) {
        System.out.print("\u001b[" + abs + "A"); //up
    }
    /**
     * moves cursor down relatively to actual position
     */
    public static void moveCursorDown(int abs) {
        System.out.print("\u001b[" + abs + "B"); //down
    }
    /**
     * moves cursor right relatively to actual position
     */
    public static void moveCursorRight(int abs) {
        System.out.print("\u001b[" + abs + "D"); //dx
    }
    /**
     * moves cursor left relatively to actual position
     */
    public static void moveCursorLeft(int abs) {
        System.out.print("\u001b[" + abs + "C"); //sx
    }

    /**
     * move cursor to position (x, y)
     * @param row
     * @param col
     */
    public static void moveAbsoluteCursor(int row, int col) {
        System.out.print("\u001b[" + row + ";" + col + "H");
    }


}
