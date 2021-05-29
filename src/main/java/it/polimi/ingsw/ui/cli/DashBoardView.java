package it.polimi.ingsw.ui.cli;

import it.polimi.ingsw.events.ClientEvents.DepotState;
import it.polimi.ingsw.model.Resource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class DashBoardView {
    private ArrayList<String> topDevCards;
    private HashMap<Resource, Integer> strongBox;
    private ArrayList<DepotState> warehouse;
    private String player;


    public DashBoardView(ArrayList<String> topDevCards, HashMap<Resource, Integer> strongBox, ArrayList<DepotState> warehouse, String player) {
        this.topDevCards = topDevCards;
        this.strongBox = new HashMap<>();
        Arrays.stream(Resource.values()).forEach(res->this.strongBox.put(res, 0));
        strongBox.keySet().forEach(res->this.strongBox.put(res, strongBox.get(res)));
        this.warehouse = warehouse;
        this.player = player;
    }


    public String getPlayer() {
        return player;
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

    public String resourceNumberToString() {
        HashMap<Resource, Integer> totalRes = new HashMap();
        Arrays.stream(Resource.values()).forEach(res -> totalRes.put(res, 0));
        strongBox.keySet().forEach(resource -> {
            totalRes.put(resource, totalRes.get(resource) + strongBox.get(resource));
        });
        warehouse.stream().forEach(depotState -> {
                    totalRes.put(depotState.getResourceType(), totalRes.get(depotState.getResourceType()) + depotState.getCurrentQuantity());
                });
            StringBuilder builder = new StringBuilder();
            builder.append("\033[31;1;4mTOTAL OF RESOURCES:\033[0m \n");
            totalRes.keySet().forEach(resource -> {
                String color = colorResource(resource);
                String shape = shapeResource(resource);
                builder.append(color + resource.toString() + ": ");
                IntStream.range(0, totalRes.get(resource)).forEach(n -> builder.append(color + shape + " "));
                builder.append(":" + totalRes.get(resource) + Color.reset() + "\n");
            });
            return builder.toString();
        }

    public String warehouseToString() {
        StringBuilder warehouseBuilder = new StringBuilder();
        warehouseBuilder.append("\033[31;1;4mWAREHOUSE\033[0m \n");
        for (DepotState l : warehouse) {
            String color = colorResource(l.getResourceType());
            String shape = shapeResource(l.getResourceType());
            warehouseBuilder.append(color + l.getResourceType().toString() + "\n");
            IntStream.range(0, l.getMaxQuantity()).forEach(n -> warehouseBuilder.append(color + "╔═══╗" + " "));
            warehouseBuilder.append(Color.reset() + "\n");
            IntStream.range(0, l.getCurrentQuantity()).forEach(n -> warehouseBuilder.append(color + "║ " + shape + " ║" + " "));
            IntStream.range(0, (l.getMaxQuantity() - l.getCurrentQuantity())).forEach(n -> warehouseBuilder.append(color + "║   ║" + " "));
            warehouseBuilder.append(Color.reset() + "\n");
            IntStream.range(0, l.getMaxQuantity()).forEach(n -> warehouseBuilder.append(color + "╚═══╝" + " "));
            warehouseBuilder.append(Color.reset() + "\n");
        }
        return warehouseBuilder.toString();
    }

    public String strongBoxToString() {
        StringBuilder strongBoxBuilder = new StringBuilder();
        strongBoxBuilder.append("\033[31;1;4mSTRONGBOX\033[0m \n");
        strongBox.keySet().forEach(resource -> {
            String color = colorResource(resource);
            String shape = shapeResource(resource);
            strongBoxBuilder.append(color + resource.toString() + ": ");
            IntStream.range(0, strongBox.get(resource)).forEach(n -> strongBoxBuilder.append(color + shape + " "));
            strongBoxBuilder.append(Color.reset() + "\n");
        });
        return strongBoxBuilder.toString();
    }

    public void displayDevCardSlots(){
        Panel panel= new Panel(500, 30, System.out);
        DrawableObject devCards = new DrawableObject("\033[31;1;4mDEVCARD SLOTS\033[0m \n", 1, 1);
        panel.addItem(devCards);
        AtomicInteger gap = new AtomicInteger(0);
        topDevCards.stream().forEach(x -> {
            DrawableObject obj = new DrawableObject(new DevCardView(x).toString(), gap.get() * 40, 3);
            gap.getAndIncrement();
            panel.addItem(obj);
        });
        panel.show();
    }

    public void displayAll(String playerID) {

        System.out.println("\033[31;1;4mTHIS IS " + player + "'S DASHBOARD\033[0m \n");

        String warehouseString = warehouseToString();
        String strongBoxString = strongBoxToString();

        DrawableObject warehouse = new DrawableObject(warehouseString, 0, 0);
        DrawableObject strongBox = new DrawableObject(strongBoxString, 0, (int) warehouse.getHeight() + 2);
        Panel panel = new Panel(400, (int) warehouse.getHeight()+(int)strongBox.getHeight()+3, System.out);

        panel.addItem(warehouse);
        panel.addItem(strongBox);
        panel.show();
        displayDevCardSlots();

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
        d.displayAll(player);
    }
}