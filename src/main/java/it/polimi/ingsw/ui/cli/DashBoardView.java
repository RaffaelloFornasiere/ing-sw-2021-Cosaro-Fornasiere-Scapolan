package it.polimi.ingsw.ui.cli;

import it.polimi.ingsw.events.ClientEvents.DepotState;
import it.polimi.ingsw.model.ProductionPower;
import it.polimi.ingsw.model.Resource;

import java.io.PrintWriter;
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
        this.topDevCards = new ArrayList<>();
        for (String s : topDevCards) {
            if (s != null) this.topDevCards.add(s);
            else this.topDevCards.add(null);
        }

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


    public HashMap<Resource, Integer> getStrongBox() {
        return strongBox;
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

    public DepotResultMessage tryAddResource(Resource r, DepotState depot) {
        if (warehouse.stream().filter(dep -> dep != depot).map(dep->((dep.getCurrentQuantity()!=0) && (dep.getResourceType()!=r))||dep.getCurrentQuantity()==0).reduce(true, (prev, foll) -> prev && foll)) {
            return depot.tryAddResource(r);
        }
        return DepotResultMessage.INVALID_DEPOT;
    }

    public String trySubtractResourcesFromStrongbox(int quantity, Resource type) {
        if (strongBox.containsKey(type)) {
            if (strongBox.get(type) - quantity < 0) return "NOT ENOUGH " + type.toString() + " IN STRONGBOX!\n";
        } else return "STRONGBOX DOESN'T CONTAIN ANY " + type.toString() + "\n";
        return "";
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
            String color;
            if (l.getCurrentQuantity() == 0) {
                color = Color.WHITE.getAnsiCode();
            } else {
                color = CLI.colorResource(l.getResourceType());
            }
            String shape = CLI.shapeResource(l.getResourceType());
            if (l.getCurrentQuantity() == 0) {
                warehouseBuilder.append(color + "   " + "EMPTY" + "\n   ");
            } else {
                warehouseBuilder.append(color + "   " + l.getResourceType().toString() + "\n   ");
            }

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

    public String personalProductionToString() {
        return CLI.productionPowerToString(personalProductionPower, Color.reset());
    }

    public void displayPersonalProductionPower(PrintWriter out) {
        String personalProductionPowerString = personalProductionToString();
        ArrayList<DrawableObject> drawableObjects = new ArrayList<>();
        drawableObjects.add(new DrawableObject("PERSONAL PRODUCTION POWER", 0, 0));
        drawableObjects.add(new DrawableObject(personalProductionPowerString, 0, 2));
        new Panel(drawableObjects, out, false).show();
    }

    public void displayDevCardSlots() {
        String color = Color.WHITE.getAnsiCode();

        DrawableObject devCards = new DrawableObject("\033[31;1;4mDEVCARD SLOTS\033[0m \n", 1, 1);

        AtomicInteger gap = new AtomicInteger(0);
        AtomicInteger slotIndex = new AtomicInteger(1);
        ArrayList<DrawableObject> objs = new ArrayList<>();
        AtomicInteger height = new AtomicInteger(0);
        topDevCards.stream().forEach(x -> {
            if (x != null) {
                DrawableObject obj = new DrawableObject(color + "   " + color + "SLOT " + slotIndex + color + "  " + Color.reset() + "\n" + new DevCardView(x).toString(), gap.get() * 40, 3);

                height.set(Integer.max(height.get(), obj.getHeight() + 1));
                objs.add(obj);
            } else {
                DrawableObject obj = new DrawableObject(color + "   " + color + "SLOT " + slotIndex + color + "  " + Color.reset() + "\n" + DevCardView.emptySlot(14), gap.get() * 40, 3);
                height.set(Integer.max(height.get(), obj.getHeight() + 1));
                objs.add(obj);
            }
            gap.getAndIncrement();
            slotIndex.getAndIncrement();
        });

        int totalHeight = height.get() + devCards.getHeight() + 1;
        Panel panel = new Panel(500, totalHeight + 5, System.out);
        panel.addItem(devCards);
        for (DrawableObject obj : objs) {
            panel.addItem(obj);
        }
        panel.show();
    }

    public HashMap<Resource, Integer> totalResourcesInWarehouse() {
        HashMap<Resource, Integer> totalRes = new HashMap<>();
        Arrays.stream(Resource.values()).forEach(res -> totalRes.put(res, 0));
        warehouse.stream().forEach(depotState -> {
            totalRes.put(depotState.getResourceType(), totalRes.get(depotState.getResourceType()) + depotState.getCurrentQuantity());
        });
        return totalRes;
    }


    public void displayAll(String playerID) {

        System.out.println("\033[31;1;4mTHIS IS " + player + "'S DASHBOARD\033[0m \n");

        String warehouseString = warehouseToString();
        String strongBoxString = strongBoxToString();
        String personalProductionPowerString = personalProductionToString();

        DrawableObject warehouse = new DrawableObject(warehouseString, 0, 0);
        DrawableObject strongBox = new DrawableObject(strongBoxString, 0, warehouse.getHeight() + 2);
        DrawableObject personalProductionPower = new DrawableObject(personalProductionPowerString, 0, warehouse.getHeight() + strongBox.getHeight());
        Panel panel = new Panel(400, warehouse.getHeight() + strongBox.getHeight()+ personalProductionPower.getHeight() + 4, System.out);

        panel.addItem(warehouse);
        panel.addItem(strongBox);
        panel.addItem(personalProductionPower);
        panel.show();
        displayDevCardSlots();


    }


    public static void main(String[] args) {
        DepotState depot = new DepotState(Resource.COIN, 3, 1);
        DepotState depot2 = new DepotState(Resource.COIN, 4, 0);
        DepotState depot3 = new DepotState(Resource.SHIELD, 6, 0);
        ArrayList<DepotState> totalLevels = new ArrayList<>();
        totalLevels.add(depot);
        totalLevels.add(depot2);
        totalLevels.add(depot3);

        HashMap<Resource, Integer> str = new HashMap<>();
        str.put(Resource.COIN, 6);
        str.put(Resource.ROCK, 7);

        ArrayList<String> cards = new ArrayList<>(3);

        cards.add("DevCard10");
        cards.add("DevCard40");
        cards.add(null);

        String player = "PAOLO";

        DashBoardView d = new DashBoardView(cards, str, totalLevels, player);
        System.out.println(d.tryAddResource(Resource.COIN, d.getWarehouse().get(1)).getMessage());

        DepotState depot1=d.getWarehouse().get(1);
        System.out.println(depot1.getResourceType());
        d.getWarehouse().stream().filter(dep-> dep!=depot1).map(dep->((dep.getCurrentQuantity()!=0) && (dep.getResourceType()!=depot1.getResourceType()))||dep.getCurrentQuantity()==0).forEach(bool-> System.out.println(bool));


        d.displayAll(player);
    }


}