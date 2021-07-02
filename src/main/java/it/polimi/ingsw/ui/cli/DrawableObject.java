package it.polimi.ingsw.ui.cli;

public class DrawableObject {
    private final int x;
    private final int y;
    private int width;
    private final int height;
    private final String textObject;


    /**
     * constructor
     *
     * @param to the string to visualize
     * @param x  the coordinate x in the screen from which to start the visualization
     * @param y  the coordinate y in the screen from which to start the visualization
     */
    public DrawableObject(String to, int x, int y) {
        this.x = x;
        this.y = y;
        height = to.chars().filter(c -> c == '\n').map(c -> 1).sum();
        width = 0;

        for (int i = 0, j = 0; i < to.length(); i++) {
            if (to.charAt(i) != '\n') {
                j++;
            } else {
                if (j > width) {
                    width = j;
                }
                j = 0;
            }
        }

        textObject = to;
    }

    /**
     * getter
     *
     * @return the string
     */
    public String getTextObject() {
        return textObject;
    }

    /**
     * getter
     *
     * @return the x coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * getter
     *
     * @return the y coordinate
     */
    public int getY() {
        return y;
    }

    /**
     * getter
     *
     * @return the width of the drawable object
     */
    public int getWidth() {
        return width;
    }

    /**
     * getter
     *
     * @return the height of the drawable object
     */
    public int getHeight() {
        return height;
    }
}
