package it.polimi.ingsw.ui.cli;

import it.polimi.ingsw.events.ClientEvents.DepotState;
import it.polimi.ingsw.model.ProductionPower;
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
    private ProductionPower personalProductionPower;
    private String player;


    public DashBoardView(ArrayList<String> topDevCards, HashMap<Resource, Integer> strongBox, ArrayList<DepotState> warehouse, String player) {
        this.topDevCards = (ArrayList<String>) topDevCards.clone();
        this.strongBox = (HashMap<Resource, Integer>) strongBox.clone();
        this.warehouse = new ArrayList<>();
        for (DepotState d : warehouse) {
            this.warehouse.add(new DepotState(d.getResourceType(), d.getMaxQuantity(), d.getCurrentQuantity()));
        }
        this.player = player;
    }


    public String getPlayer() {
        return player;
    }


    public ArrayList<DepotState> getWarehouse() {
        return warehouse;
    }

    public ArrayList<String> getTopDevCards() {
        return topDevCards;
    }

    public void updateTopDevCards(ArrayList<String> topDevCards) {
        this.topDevCards = (ArrayList<String>) topDevCards.clone();
    }

    public void updateStrongBox(HashMap<Resource, Integer> strongBox) {
        this.strongBox = (HashMap<Resource, Integer>) strongBox.clone();
    }

    public void updateWarehouse(ArrayList<DepotState> warehouse) {
        this.warehouse = new ArrayList<>();
        for (DepotState d : warehouse) {
            this.warehouse.add(new DepotState(d.getResourceType(), d.getMaxQuantity(), d.getCurrentQuantity()));
        }
    }

    public DepotResultMessage switchDepots(int depot1, int depot2) {
        DepotResultMessage result = warehouse.get(depot1).switchDepot(warehouse.get(depot2));
        return result;
    }

    public void setPersonalProductionPower(ProductionPower personalProductionPower) {
        this.personalProductionPower = personalProductionPower;
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
            String color = CLI.colorResource(resource);
            String shape = CLI.shapeResource(resource);
            builder.append(color + resource.toString() + ": ");
            IntStream.range(0, totalRes.get(resource)).forEach(n -> builder.append(color + shape + " "));
            builder.append(":" + totalRes.get(resource) + Color.reset() + "\n");
        });
        return builder.toString();
    }

    public String warehouseToString() {
        StringBuilder warehouseBuilder = new StringBuilder();
        warehouseBuilder.append("\033[31;1;4mWAREHOUSE\033[0m \n");
        int m = 1;
        for (DepotState l : warehouse) {
            String color = CLI.colorResource(l.getResourceType());
            String shape = CLI.shapeResource(l.getResourceType());

            warehouseBuilder.append(color + "   " + l.getResourceType().toString() + "\n   ");
            IntStream.range(0, l.getMaxQuantity()).forEach(n -> warehouseBuilder.append(color + "╔═══╗" + " "));
            warehouseBuilder.append(Color.reset() + "\n" + "." + m + " ");
            IntStream.range(0, l.getCurrentQuantity()).forEach(n -> warehouseBuilder.append(color + "║ " + shape + " ║" + " "));
            IntStream.range(0, (l.getMaxQuantity() - l.getCurrentQuantity())).forEach(n -> warehouseBuilder.append(color + "║   ║" + " "));
            warehouseBuilder.append(Color.reset() + "\n   ");
            IntStream.range(0, l.getMaxQuantity()).forEach(n -> warehouseBuilder.append(color + "╚═══╝" + " "));
            warehouseBuilder.append(Color.reset() + "\n");
            m++;
        }
        return warehouseBuilder.toString();
    }

    public String strongBoxToString() {
        StringBuilder strongBoxBuilder = new StringBuilder();
        strongBoxBuilder.append("\033[31;1;4mSTRONGBOX\033[0m \n");
        strongBox.keySet().forEach(resource -> {
            String color = CLI.colorResource(resource);
            String shape = CLI.shapeResource(resource);
            strongBoxBuilder.append(color + resource.toString() + ": ");
            IntStream.range(0, strongBox.get(resource)).forEach(n -> strongBoxBuilder.append(color + shape + " "));
            strongBoxBuilder.append(Color.reset() + "\n");
        });
        return strongBoxBuilder.toString();
    }

    public void displayDevCardSlots() {
        String color= Color.WHITE.getAnsiCode();
        Panel panel = new Panel(500, 26, System.out);
        DrawableObject devCards = new DrawableObject("\033[31;1;4mDEVCARD SLOTS\033[0m \n", 1, 1);
        panel.addItem(devCards);
       // AtomicInteger height= new AtomicInteger();
        AtomicInteger gap = new AtomicInteger(0);
        AtomicInteger slotIndex = new AtomicInteger(1);
        topDevCards.stream().forEach(x -> {
            DrawableObject obj = new DrawableObject( color+ "   "+color +"SLOT "+ slotIndex + color+"  " + Color.reset() + "\n"+ new DevCardView(x).toString(), gap.get() * 40, 3);
            gap.getAndIncrement();
            slotIndex.getAndIncrement();
           // height.set(Integer.max(height.get(), (int)obj.getHeight()));
            panel.addItem(obj);
        });
      //  panel.setHeight(height.get()+5);
        panel.show();
    }

    public void displayAll(String playerID) {

        System.out.println("\033[31;1;4mTHIS IS " + player + "'S DASHBOARD\033[0m \n");

        String warehouseString = warehouseToString();
        String strongBoxString = strongBoxToString();

        DrawableObject warehouse = new DrawableObject(warehouseString, 0, 0);
        DrawableObject strongBox = new DrawableObject(strongBoxString, 0, (int) warehouse.getHeight() + 2);
        Panel panel = new Panel(400, (int) warehouse.getHeight() + (int) strongBox.getHeight() + 3, System.out);

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