package it.polimi.ingsw.ui.cli;

import java.util.ArrayList;

public class DevCardGridView {
    public String[][] getTopDevCardIDs() {
        return topDevCardIDs;
    }

    private final String[][] topDevCardIDs;

    public DevCardGridView(String[][] topDevCardIDs) {
        this.topDevCardIDs = new String[topDevCardIDs.length][topDevCardIDs[0].length];
        for (int x = topDevCardIDs.length - 1; x >= 0; x--) {
            System.arraycopy(topDevCardIDs[x], 0, this.topDevCardIDs[topDevCardIDs.length - 1 - x], 0, topDevCardIDs[0].length);
        }
    }

    public void display() {
        DevCardView[][] cardGrid = new DevCardView[topDevCardIDs.length][topDevCardIDs[0].length];
        int totalHeight = 0;
        ArrayList<DrawableObject> objs = new ArrayList<>();
        Panel panel = new Panel(500, 0, System.out);
        for (int x = topDevCardIDs.length - 1; x >= 0; x--) {
            int height = 0;
            for (int y = 0; y < topDevCardIDs[0].length; y++) {
                DrawableObject obj;
                if (topDevCardIDs[x][y] != null) {
                    obj = new DrawableObject((new DevCardView(topDevCardIDs[x][y])).toString(), 40 * y, 20 * x);
                } else obj = new DrawableObject(DevCardView.emptySlot(14), y * 40, 20 * x);

                objs.add(obj);
                height = Integer.max(height, obj.getHeight() + 1);
            }
            totalHeight += height;
        }
        panel.setHeight(totalHeight);
        for (DrawableObject obj : objs) {
            panel.addItem(obj);
        }
        panel.show();

    }


    public static void main(String[] args) {
        System.out.println("\033[31;1;4mGRID OF DEVCARDS\033[0m \n");
        String[][] grid = {
                {"DevCard1", "DevCard2", "DevCard3", "DevCard10"},
                {"DevCard4", "DevCard5", "DevCard6", "DevCard11"},
                {"DevCard7", "DevCard8", null , "DevCard12"},
                {"DevCard7", "DevCard8", "DevCard9", "DevCard12"},
                {"DevCard7", "DevCard8", "DevCard9", "DevCard12"}

        };
        DevCardGridView viewGrid = new DevCardGridView(grid);
        viewGrid.display();
    }
}
