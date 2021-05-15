package it.polimi.ingsw.ui.cli;


public class CellView {

    private int index;
    private int victoryPoints;
    private String color;
    private boolean occupied;
    private boolean vaticanSection;
    private boolean popeCell;
    private String placeholder = "\u2719";


    public CellView(int index, int victoryPoints) {
        this.index = index;
        this.victoryPoints = victoryPoints;
        this.color = Color.reset();
        this.occupied = false;
        this.vaticanSection = false;
        this.popeCell = false;
    }

    /**
     * Sets a player in this cell
     *
     * @param color of the worker
     */
    public void setPlaceHolder(Color color) {
        this.placeholder = color.getAnsiCode() + "\u2719" + Color.reset()+"\n";
        this.occupied = true;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setVictoryPoints(int victoryPoints) {
        this.victoryPoints = victoryPoints;
    }

    public void setColor(Color color) {
        this.color = color.getAnsiCode();
    }

    public void setVaticanSection(Boolean b) {
        if (b) {
            setColor(Color.BLUE_BACKGROUND);
        }
    }

    public String toString() {
        StringBuilder string = new StringBuilder();
        string.append(this.color + "╔═════╗" + Color.reset() + "\n")
                .append(color + "║" + this.index + "   " + this.victoryPoints + "║" + Color.reset() + "\n")
                .append(color + "╚═════╝" + Color.reset() + "\n");
        if (occupied) string.append("  " + placeholder);
        return string.toString();
    }


    public static void main(String[] args) {

        Panel panel = new Panel(10000, 100, System.out);
        for (int i = 0; i < 20; i++) {
            CellView cell = new CellView(2, 3);

            cell.setPlaceHolder(Color.MAGENTA);
            DrawableObject d = new DrawableObject(cell.toString(), 15 * i, 1);
            panel.addItem(d);

        }
        panel.show();
    }

    }
