package it.polimi.ingsw.ui.cli;


public class CellView {

    private int index;
    private int victoryPoints;
    private String color;
    private boolean occupied;
    private boolean vaticanSection;
    private boolean popeCell;
    private String placeholder;
    private int favorPopeCardPoint;
    private boolean showFavorPopeCard;
    private String popeFavorCardPlaceholder;


    public CellView(int index, int victoryPoints) {
        this.index = index;
        this.victoryPoints = victoryPoints;
        this.color = Color.reset();
        this.occupied = false;
        this.vaticanSection = false;
        this.popeCell = false;
        this.favorPopeCardPoint = 0;
        this.showFavorPopeCard = false;
    }


    public void setPlaceHolder(String s) {
        this.placeholder = s;
        this.occupied = true;
    }

    public void setPopeFavorCardPlaceHolder(String s) {
        this.popeFavorCardPlaceholder= s;
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

    public void setFavorPopeCardPoint(int i) {
        this.favorPopeCardPoint = i;
        setShowFavorPopeCard();
    }

    public void setShowFavorPopeCard() {
        this.showFavorPopeCard = true;
    }

    public void setPopeCell() {
        this.popeCell = true;
        setColor(Color.RED);
    }

    public void setVaticanSection() {
        this.vaticanSection = true;
        setColor(Color.CYAN);
    }

    public String toString() {
        StringBuilder string = new StringBuilder();
        string
                .append(color + "╔═════╗" + Color.reset() + "\n")
                .append(color + "║");
        if (index > 9 && victoryPoints > 9)
            string.append(index + " " + this.victoryPoints + "║" + Color.reset() + "\n");
        else if (index <= 9 && victoryPoints <= 9)
            string.append(index + "   " + this.victoryPoints + "║" + Color.reset() + "\n");
        else string.append(index + "  " + this.victoryPoints + "║" + Color.reset() + "\n");


        if (occupied) {
            if( placeholder.length()==1) string.append(color + "╚══" + placeholder + "══╝" + Color.reset() + "\n");
            else if (placeholder.length()==2)string.append(color + "╚═" + placeholder + "══╝" + Color.reset() + "\n");
            else if (placeholder.length()==3)string.append(color + "╚═" + placeholder + "═╝" + Color.reset() + "\n");
            else string.append(color + "╚" + placeholder + "═╝" + Color.reset() + "\n");
        } else string.append(color +"╚═════╝" + Color.reset() + "\n");

        if (popeCell) {
            if (showFavorPopeCard && favorPopeCardPoint != 0) {
                string.append(color + "╠══" + favorPopeCardPoint + "══╣" + Color.reset() + "\n");
                if( popeFavorCardPlaceholder.length()==1) string.append(color + "╚══" + popeFavorCardPlaceholder + "══╝" + Color.reset() + "\n");
                else if (popeFavorCardPlaceholder.length()==2)string.append(color + "╚═" + popeFavorCardPlaceholder + "══╝" + Color.reset() + "\n");
                else if (popeFavorCardPlaceholder.length()==3)string.append(color + "╚═" + popeFavorCardPlaceholder + "═╝" + Color.reset() + "\n");
                else string.append(color + "╚" + popeFavorCardPlaceholder + "═╝" + Color.reset() + "\n");
            }
            else {
                string.append(color + "╠══ ══╣" + Color.reset() + "\n");
                string.append(color + "       " + Color.reset() + "\n");
            }
        } else {
            string.append(color + "       " + Color.reset() + "\n");
            string.append(color + "       " + Color.reset() + "\n");
        }


        return string.toString();
    }
/*

    public static void main(String[] args) {

        Panel panel = new Panel(10000, 100, System.out);
        for (int i = 0; i < 20; i++) {
            CellView cell = new CellView(2, 3);
            cell.setVaticanSection();
            cell.setPopeCell();
            cell.setFavorPopeCardPoint(3);

            cell.setPlaceHolder("+");
            DrawableObject d = new DrawableObject(cell.toString(), 20 * i, 1);
            panel.addItem(d);

        }
        panel.show();
    }
*/
}
