package it.polimi.ingsw.ui.gui;

import it.polimi.ingsw.events.ClientEvents.DepotState;
import javafx.scene.layout.AnchorPane;

import java.util.ArrayList;

public class WarehouseController {

    AnchorPane warehouseElement;
    ArrayList<ArrayList<String>> depots;
    public WarehouseController(AnchorPane warehouse)
    {
        this.warehouseElement = warehouse;
        depots = new ArrayList<>(3);
        depots.add(new ArrayList<>(){{add("");}});
        depots.add(new ArrayList<>(){{add(""); add("");}});
        depots.add(new ArrayList<>(){{add(""); add(""); add("");}});
    }

}
