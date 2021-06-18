package it.polimi.ingsw.ui.cli;


public class CellView {

    private int index;
    private int victoryPoints;
    private String color;
    private boolean occupied;
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
        setColor(Color.CYAN);
    }

    public String toString() {
        StringBuilder string = new StringBuilder();
        string.append(color).append("╔═════╗").append(Color.reset()).append("\n").append(color).append("║");
        if (index > 9 && victoryPoints > 9)
            string.append(index).append(" ").append(this.victoryPoints).append("║").append(Color.reset()).append("\n");
        else if (index <= 9 && victoryPoints <= 9)
            string.append(index).append("   ").append(this.victoryPoints).append("║").append(Color.reset()).append("\n");
        else string.append(index).append("  ").append(this.victoryPoints).append("║").append(Color.reset()).append("\n");


        if (occupied) {
            if( placeholder.length()==1) string.append(color).append("╚══").append(placeholder).append("══╝").append(Color.reset()).append("\n");
            else if (placeholder.length()==2)
                string.append(color).append("╚═").append(placeholder).append("══╝").append(Color.reset()).append("\n");
            else if (placeholder.length()==3)
                string.append(color).append("╚═").append(placeholder).append("═╝").append(Color.reset()).append("\n");
            else string.append(color).append("╚").append(placeholder).append("═╝").append(Color.reset()).append("\n");
        } else string.append(color).append("╚═════╝").append(Color.reset()).append("\n");

        if (popeCell) {
            if (showFavorPopeCard && favorPopeCardPoint != 0) {
                string.append(color).append("╠══").append(favorPopeCardPoint).append("══╣").append(Color.reset()).append("\n");
                if( popeFavorCardPlaceholder.length()==1) string.append(color).append("╚══").append(popeFavorCardPlaceholder).append("══╝").append(Color.reset()).append("\n");
                else if (popeFavorCardPlaceholder.length()==2)
                    string.append(color).append("╚═").append(popeFavorCardPlaceholder).append("══╝").append(Color.reset()).append("\n");
                else if (popeFavorCardPlaceholder.length()==3)
                    string.append(color).append("╚═").append(popeFavorCardPlaceholder).append("═╝").append(Color.reset()).append("\n");
                else string.append(color).append("╚").append(popeFavorCardPlaceholder).append("═╝").append(Color.reset()).append("\n");
            }
            else {
                string.append(color).append("╠══ ══╣").append(Color.reset()).append("\n");
                string.append(color).append("       ").append(Color.reset()).append("\n");
            }
        } else {
            string.append(color).append("       ").append(Color.reset()).append("\n");
            string.append(color).append("       ").append(Color.reset()).append("\n");
        }


        return string.toString();
    }


//    public static void main(String[] args) {
//
//        Panel panel = new Panel(10000, 100, System.out);
//        for (int i = 0; i < 20; i++) {
//            CellView cell = new CellView(2, 3);
//            cell.setVaticanSection();
//            cell.setPopeCell();
//            cell.setFavorPopeCardPoint(3);
//            cell.setPopeFavorCardPlaceHolder("G");
//
//            cell.setPlaceHolder("+");
//            DrawableObject d = new DrawableObject(cell.toString(), 20 * i, 1);
//            panel.addItem(d);
//
//        }
//        panel.show();
//    }

}
