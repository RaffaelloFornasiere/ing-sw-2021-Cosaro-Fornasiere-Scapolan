package it.polimi.ingsw.ui.cli;

import java.io.PrintWriter;
import java.util.ArrayList;

public class DevCardGridView {
    /**
     * getter
     *
     * @return the matrix with all the top development cards
     */
    public String[][] getTopDevCardIDs() {
        return topDevCardIDs;
    }

    private final String[][] topDevCardIDs;

    /**
     * constructor
     *
     * @param topDevCardIDs the matrix with topDevCards' ids
     */
    public DevCardGridView(String[][] topDevCardIDs) {
        this.topDevCardIDs = new String[topDevCardIDs.length][topDevCardIDs[0].length];
        for (int x = topDevCardIDs.length - 1; x >= 0; x--) {
            System.arraycopy(topDevCardIDs[x], 0, this.topDevCardIDs[topDevCardIDs.length - 1 - x], 0, topDevCardIDs[0].length);
        }
    }

    /**
     * method which displays the devCardGrid
     */
    public void display(PrintWriter out) {
        int totalHeight = 0;
        ArrayList<DrawableObject> objs = new ArrayList<>();
        Panel panel = new Panel(500, 0, out);
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


}
