package it.polimi.ingsw.ui.cli;

import it.polimi.ingsw.events.ClientEvents.DepotState;
import it.polimi.ingsw.model.Resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class DashBoardView {
    private ArrayList<String> topDevCards;
    private HashMap<Resource, Integer> strongBox;
    private ArrayList<DepotState> warehouse;
    String player;


    public DashBoardView(ArrayList<String> topDevCards, HashMap<Resource, Integer> strongBox, ArrayList<DepotState> warehouse, String player) {
        this.topDevCards = topDevCards;
        this.strongBox = strongBox;
        this.warehouse = warehouse;
        this.player = player;
    }


    private String colorResource(Resource res) {
        if (res == Resource.SERVANT) return Color.BLUE.getAnsiCode();
        if (res == Resource.SHIELD) return Color.RED.getAnsiCode();
        if (res == Resource.COIN) return Color.GREEN.getAnsiCode();
        else return Color.YELLOW.getAnsiCode();
    }

    private String shapeResource(Resource res) {
        if (res == Resource.SERVANT) return "■";
        if (res == Resource.SHIELD) return "◆";
        if (res == Resource.COIN) return "●";
        else return "▼";
    }




    public static void main(String[] args) {
        DepotState depot = new DepotState(Resource.COIN, 3, 2);
        DepotState depot2 = new DepotState(Resource.SERVANT, 4, 2);
        DepotState depot3 = new DepotState(Resource.SHIELD, 6, 4);
        ArrayList<DepotState> totalLevels = new ArrayList();
        totalLevels.add(depot);
        totalLevels.add(depot2);
        totalLevels.add(depot3);

        HashMap<Resource, Integer> str = new HashMap<>();
        str.put(Resource.COIN, 6);
        str.put(Resource.ROCK, 7);

        ArrayList<String> cards = new ArrayList<>();
        cards.add("DevCard10");
        cards.add("DevCard40");
        cards.add("DevCard16");

        String player = "PAOLO";

        DashBoardView d = new DashBoardView(cards, str, totalLevels, player);


        System.out.println("\033[31;1;4mTHIS IS " + player + "'S DASHBOARD\033[0m \n");
        Panel panel = new Panel(400, 50, System.out);


        StringBuilder warehouseBuilder = new StringBuilder();
        warehouseBuilder.append("\033[31;1;4mWAREHOUSE\033[0m \n");
        for (DepotState l : totalLevels) {
            String color = d.colorResource(l.getResourceType());
            String shape = d.shapeResource(l.getResourceType());
            warehouseBuilder.append(color + l.getResourceType().toString() + "\n");
            IntStream.range(0, l.getMaxQuantity()).forEach(n -> warehouseBuilder.append( color +"╔═══╗"+" "));
            warehouseBuilder.append(Color.reset()+"\n");
            IntStream.range(0, l.getCurrentQuantity()).forEach(n -> warehouseBuilder.append( color + "║ "+ shape+" ║" + " "));
            IntStream.range(0, (l.getMaxQuantity()-l.getCurrentQuantity())).forEach(n -> warehouseBuilder.append( color + "║   ║" + " "));
            warehouseBuilder.append(Color.reset() + "\n");
            IntStream.range(0, l.getMaxQuantity()).forEach(n -> warehouseBuilder.append( color + "╚═══╝" + " "));
            warehouseBuilder.append(Color.reset() + "\n");
        }


        StringBuilder strongBoxBuilder = new StringBuilder();
        strongBoxBuilder.append("\033[31;1;4mSTRONGBOX\033[0m \n");
        str.keySet().forEach(resource -> {
            String color = d.colorResource(resource);
            String shape = d.shapeResource(resource);
            strongBoxBuilder.append(color + resource.toString() + ": ");
            IntStream.range(0, str.get(resource)).forEach(n -> strongBoxBuilder.append(color + shape + " "));
            strongBoxBuilder.append(Color.reset() + "\n");
        });

        String warehouseString = warehouseBuilder.toString();
        String strongBoxString = strongBoxBuilder.toString();

        DrawableObject warehouse = new DrawableObject(warehouseString, 0, 0);
        DrawableObject strongBox = new DrawableObject(strongBoxString, 0, (int) warehouse.getHeight() + 2);
        panel.addItem(warehouse);
        panel.addItem(strongBox);
        DrawableObject devCards = new DrawableObject("\033[31;1;4mDEVCARD SLOTS\033[0m \n", 0, (int) (warehouse.getHeight() + strongBox.getHeight() + 4));
        panel.addItem(devCards);
        AtomicInteger gap = new AtomicInteger(0);
        cards.stream().forEach(x -> {
            DrawableObject obj = new DrawableObject(new CardView(x).toString(), gap.get() * 40, (int) (warehouse.getHeight() + strongBox.getHeight() + 6));
            gap.getAndIncrement();
            panel.addItem(obj);
        });

        panel.show();
    }
}