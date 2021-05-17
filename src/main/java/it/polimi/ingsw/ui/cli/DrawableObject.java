package it.polimi.ingsw.ui.cli;

public class DrawableObject {
    private int x;
    private int y;
    private long width;
    private long height;
    private String textObject;



    public DrawableObject(String to, int x, int y) {
        this.x = x;
        this.y = y;
        height = to.chars().filter(c -> c == '\n').count();
        width = 0;

        for (int i = 0, j = 0; i < to.length(); i++) {
            if (to.charAt(i) != '\n')
                j++;
            else if (j > width) {
                width = j;
                j = 0;
            }
            textObject = to;

        }
    }

    public String getTextObject() {
        return textObject;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public long getWidth() {
        return width;
    }

    public long getHeight() {
        return height;
    }

}
