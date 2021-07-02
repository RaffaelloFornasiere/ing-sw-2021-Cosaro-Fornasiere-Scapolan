package it.polimi.ingsw.ui.cli;

import it.polimi.ingsw.model.Marble;

import java.util.Arrays;
import java.util.stream.IntStream;

public class MarketView {
    private final Marble marbleLeft;
    private final Marble[][] grid;

    /**
     * getter
     *
     * @return the number of rows
     */
    public int getRows() {
        return rows;
    }


    private final int rows;

    /**
     * getter
     *
     * @return the number of columns
     */
    public int getCols() {
        return cols;
    }

    private final int cols;

    /**
     * method which displays the market
     *
     * @param left the left marble
     * @param grid the matrix of marbles
     * @param rows the number of rows
     * @param cols the number of columns
     */
    MarketView(Marble left, Marble[][] grid, int rows, int cols) {
        if (grid.length != rows || !Arrays.stream(grid).allMatch(a -> a.length == cols))
            throw new IllegalArgumentException(
                    "number of rows and columns don't match");
        marbleLeft = left;
        this.grid = new Marble[rows][cols];
        for (int i = 0; i < rows; i++)
            System.arraycopy(grid[i], 0, this.grid[i], 0, cols);
        this.rows = rows;
        this.cols = cols;
    }

    /**
     * method which associates to  each marble a color
     *
     * @param marble the marble
     * @return the color
     */
    private String colorMarble(Marble marble) {
        if (marble == Marble.BLUE) return Color.BLUE.getAnsiCode();
        if (marble == Marble.RED) return Color.RED.getAnsiCode();
        if (marble == Marble.GRAY) return Color.GREY.getAnsiCode();
        if (marble == Marble.WHITE) return Color.WHITE.getAnsiCode();
        if (marble == Marble.YELLOW) return Color.YELLOW.getAnsiCode();
        else return Color.MAGENTA.getAnsiCode();

    }

    /**
     * method which builds a string to visualize the  market
     *
     * @return the string
     */
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("MARBLE LEFT: ").append(colorMarble(marbleLeft)).append("●").append(Color.reset()).append("\n");
        IntStream.range(0, rows).forEach(row -> {
            IntStream.range(0, cols).forEach(col -> builder.append(colorMarble(grid[row][col])).append("●").append("   ").append(Color.reset()));
            builder.append("\uD83E\uDC44" + " ").append(row + 1).append("\n\n");
        });
        IntStream.range(0, cols).forEach(col -> builder.append(Color.reset()).append("🡅").append("  ").append(Color.reset()));
        builder.append("\n");
        IntStream.range(0, cols).forEach(col -> builder.append(Color.reset()).append(col + 1).append("   ").append(Color.reset()));
        return builder.toString();
    }

//    public static void main(String[] args) {
//        Marble[][] grid= {
//                {Marble.BLUE,Marble.GRAY, Marble.YELLOW,Marble.GRAY},
//                {Marble.RED,Marble.WHITE, Marble.PURPLE, Marble.YELLOW},
//                {Marble.BLUE, Marble.PURPLE, Marble.WHITE, Marble.RED}
//        };
//        Marble left= Marble.BLUE;
//        MarketView view= new MarketView(left, grid, grid.length, grid[0].length);
//        DrawableObject obj= new DrawableObject(view.toString(), 10, 2);
//        Panel panel= new Panel(1000, obj.getHeight() +10, System.out );
//        panel.addItem(obj);
//        panel.show();
//
//    }
}
