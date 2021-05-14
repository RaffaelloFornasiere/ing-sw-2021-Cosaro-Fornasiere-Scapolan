package it.polimi.ingsw.ui.cli;


public class CellView {

    private int index;
    private int victoryPoints;
    private String color;
    private boolean occupied;
    private boolean vaticanSection;
    private boolean popeCell;
    private String placeholder="\u2719";



    public CellView(int index, int victoryPoints) {
        this.index=index;
        this.victoryPoints=victoryPoints;
        this.color = null;
        this.occupied= false;
        this.vaticanSection= false;
        this.popeCell= false;
    }

    /**
     * Sets a player in this cell
     *
     * @param color of the worker
     */
    public void setPlaceHolder(Color color) {
        this.placeholder = color.getAnsiCode()+ "\u2719";
        this.occupied = true;
    }

    public void setIndex(int index) {
        this.index= index;
    }

    public void setVictoryPoints(int victoryPoints) {
        this.victoryPoints= victoryPoints;
    }

    public void setColor(Color color) {
        this.color = color.getAnsiCode();
    }

    public void setVaticanSection(Boolean b) {
        if(b){
            setColor(Color.BLUE_BACKGROUND);
        }
    }

    public void display(int initX, int initY) {
        Cursor.moveAbsoluteCursor(initX, initY);
        System.out.print(this.color+"╔");
        System.out.print("═════");
        System.out.println("╗"+ Color.reset());
        Cursor.moveAbsoluteCursor(initX+1, initY);
        System.out.print(color+"║");
        System.out.print(this.index+" ");
        System.out.print("  "+this.victoryPoints);
        System.out.println("║"+ Color.reset());
        Cursor.moveAbsoluteCursor(initX+2, initY);
        System.out.print(color+"╚");
        System.out.print("═════");
        System.out.println("╝"+ Color.reset());
        Cursor.moveAbsoluteCursor(initX+3, initY);
        if(occupied) System.out.print("  "+ Color.MAGENTA.getAnsiCode()+"\u2719");
    }




    public static void main(String[] args) {

      CellView cell= new CellView(2,3);
      cell.setVaticanSection(true);
      cell.setPlaceHolder(Color.MAGENTA);
      cell.display(1,1);


    }
}
