package it.polimi.ingsw.ui.cli;

public class DevCardGridView {
    public String[][] getTopDevCardIDs() {
        return topDevCardIDs;
    }

    private String[][] topDevCardIDs;

    public DevCardGridView(String[][] topDevCardIDs){
        this.topDevCardIDs=topDevCardIDs;
    }

    public void display(){
        DevCardView[][] cardGrid= new DevCardView[topDevCardIDs.length][topDevCardIDs[0].length];
        Panel panel = new Panel(500, 100, System.out);
        for(int x=0; x<topDevCardIDs.length; x++){
            for(int y=0; y<topDevCardIDs[0].length; y++){
                panel.addItem(new DrawableObject((new DevCardView(topDevCardIDs[x][y])).toString(),40*x,20*y));
            }
        }
       panel.show();
    }

    public static void main(String[] args) {
        System.out.println("\033[31;1;4mGRID OF DEVCARDS\033[0m \n");
        String[][] grid={
            {"DevCard1","DevCard2","DevCard3"},
            {"DevCard4","DevCard5","DevCard6"},
            {"DevCard7","DevCard8","DevCard9"}
        };
        DevCardGridView viewGrid= new DevCardGridView(grid);
        viewGrid.display();
    }
}
