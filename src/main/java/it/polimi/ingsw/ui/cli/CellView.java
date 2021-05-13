package it.polimi.ingsw.ui.cli;
public class CellView {
    private final static int len = 7;
    private final static int hei = 7;

    private int index;
    private int victoryPoints;
    private Color color;

    private int startRow;
    private int startCol;
    private boolean occupied;
    private boolean vaticanSection;
    private boolean popeCell;
    private boolean lastCell;
    private String placeholder="\u2719";



    public CellView(int index, int victoryPoints, int startRow, int startCol) {
        this.index=-1;
        this.victoryPoints=-1;
        this.color = null;
        this.occupied= false;
        this.vaticanSection= false;
        this.popeCell= false;
        this.lastCell = false;
        this.startCol = startCol;
        this.startRow = startRow;
    }

    /**
     * Sets a player in this cell
     *
     * @param color of the worker
     */
    public void setPlaceHolder(Color color) {
        this.color = color;
        this.occupied = true;
    }

    public static void main(String[] args) {
       System.out.println("\u2719");
    }
}
