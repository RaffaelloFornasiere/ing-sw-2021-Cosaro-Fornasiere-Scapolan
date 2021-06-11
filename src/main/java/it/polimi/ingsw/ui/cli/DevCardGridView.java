package it.polimi.ingsw.ui.cli;

import java.util.ArrayList;

public class DevCardGridView {
    public String[][] getTopDevCardIDs() {
        return topDevCardIDs;
    }

    private String[][] topDevCardIDs;

    public DevCardGridView(String[][] topDevCardIDs) {
        this.topDevCardIDs = topDevCardIDs;
    }

    public void display() {
        DevCardView[][] cardGrid = new DevCardView[topDevCardIDs.length][topDevCardIDs[0].length];
        int totalHeight= 0;
        ArrayList<DrawableObject>  objs= new ArrayList<>();
        Panel panel = new Panel(500, 0, System.out);
        for (int x = 0; x < topDevCardIDs.length; x++){
            int height=0;
            for (int y = 0; y < topDevCardIDs[0].length; y++) {
                DrawableObject obj= new DrawableObject((new DevCardView(topDevCardIDs[x][y])).toString(), 40 * y, 20 * x);
                objs.add(obj);
                height= Integer.max(height,(int)obj.getHeight()+1);
            }
            totalHeight+=height;
        }
        panel.setHeight(totalHeight);
        for(DrawableObject obj: objs){
            panel.addItem(obj);
        }
        panel.show();

    }

    public static void main(String[] args) {
        System.out.println("\033[31;1;4mGRID OF DEVCARDS\033[0m \n");
        String[][] grid = {
                {"DevCard1", "DevCard2", "DevCard3", "DevCard10"},
                {"DevCard4", "DevCard5", "DevCard6", "DevCard11"},
                {"DevCard7", "DevCard8", "DevCard9", "DevCard12"},
                {"DevCard7", "DevCard8", "DevCard9", "DevCard12"},
                {"DevCard7", "DevCard8", "DevCard9", "DevCard12"}

        };
        DevCardGridView viewGrid = new DevCardGridView(grid);
        viewGrid.display();
    }
}
