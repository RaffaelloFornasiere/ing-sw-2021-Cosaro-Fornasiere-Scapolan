package it.polimi.ingsw.ui.cli;

import it.polimi.ingsw.events.clientEvents.DepotState;
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
    private final String player;

    /**
     * constructor
     * @param topDevCards the top cards of slots
     * @param strongBox the strongbox
     * @param warehouse the warehouse
     * @param player the player to which this dashboard belongs
     */
    public DashBoardView(ArrayList<String> topDevCards, HashMap<Resource, Integer> strongBox, ArrayList<DepotState> warehouse, String player) {
        this.topDevCards = new ArrayList<>();
        this.topDevCards.addAll(topDevCards);

        this.strongBox = new HashMap<>(strongBox);
        this.warehouse = new ArrayList<>();
        for (DepotState d : warehouse) {
            this.warehouse.add(new DepotState(d.getResourceType(), d.getMaxQuantity(), d.getCurrentQuantity()));
        }
        this.player = player;
    }

    /**
     * getter
     * @return the player id
     */
    public String getPlayer() {
        return player;
    }

    /**
     * getter
     * @return the strongbox
     */
    public HashMap<Resource, Integer> getStrongBox() {
        return strongBox;
    }

    /**
     * getter
     * @return the warehouse
     */
    public ArrayList<DepotState> getWarehouse() {
        return warehouse;
    }

    /**
     * getter
     * @return the array of cards on top of the  slots
     */
    public ArrayList<String> getTopDevCards() {
        return topDevCards;
    }

    /**
     * method which updates the state of the slots
     * @param topDevCards the topDevCards
     */
    public void updateTopDevCards(ArrayList<String> topDevCards) {
        this.topDevCards = new ArrayList<>(topDevCards);
    }

    /**
     * method which updates the state of strongbox
     * @param strongBox the strongbox
     */
    public void updateStrongBox(HashMap<Resource, Integer> strongBox) {
        this.strongBox = new HashMap<>(strongBox);
    }

    /**
     * method which tries adding resources to one depot
     * @param r resource type
     * @param n quantity to add
     * @param depot the target depot
     * @return result message
     */
    public DepotResultMessage tryAddResource(Resource r, int n, DepotState depot) {
        if (warehouse.stream().filter(dep -> dep != depot).map(dep -> dep.getCurrentQuantity() == 0 || (dep.getResourceType() != r)).reduce(true, (prev, foll) -> prev && foll)) {
            return depot.tryAddResource(r, n);
        }
        return DepotResultMessage.INVALID_DEPOT;
    }

    /**
     * method which tries to subtract resources from  depot
     * @param quantity quantity of resources
     * @param type resource type
     * @return result message
     */
    public String trySubtractResourcesFromStrongbox(int quantity, Resource type) {
        if (strongBox.containsKey(type)) {
            if (strongBox.get(type) - quantity < 0) return "NOT ENOUGH " + type.toString() + " IN STRONGBOX!\n";
        } else return "STRONGBOX DOESN'T CONTAIN ANY " + type.toString() + "\n";
        return "";
    }

    /**
     * method which updates the current state of the warehouse
     * @param warehouse the warehouse
     */
    public void updateWarehouse(ArrayList<DepotState> warehouse) {
        this.warehouse = new ArrayList<>();
        for (DepotState d : warehouse) {
            this.warehouse.add(new DepotState(d.getResourceType(), d.getMaxQuantity(), d.getCurrentQuantity()));
        }
    }

    /**
     * Method which switches two given depots
     * @param depot1 first depot
     * @param depot2 second depot
     * @return result message
     */
    public DepotResultMessage switchDepots(int depot1, int depot2) {
        return warehouse.get(depot1).switchDepot(warehouse.get(depot2));
    }

    /**
     * setter
     * @param personalProductionPower the personal production power
     */
    public void setPersonalProductionPower(ProductionPower personalProductionPower) {
        this.personalProductionPower = personalProductionPower;
    }

    /**
     * method which tells the total amount of resources in  the warehouse
     * @return a string to print
     */
    public String resourceNumberToString() {
        HashMap<Resource, Integer> totalRes = new HashMap();
        Arrays.stream(Resource.values()).forEach(res -> totalRes.put(res, 0));
        strongBox.keySet().forEach(resource -> totalRes.put(resource, totalRes.get(resource) + strongBox.get(resource)));
        warehouse.forEach(depotState -> totalRes.put(depotState.getResourceType(), totalRes.get(depotState.getResourceType()) + depotState.getCurrentQuantity()));
        StringBuilder builder = new StringBuilder();
        builder.append("\033[31;1;4mTOTAL OF RESOURCES:\033[0m \n");
        totalRes.keySet().forEach(resource -> {
            String color = CLI.colorResource(resource);
            String shape = CLI.shapeResource(resource);
            builder.append(color).append(resource.toString()).append(": ");
            IntStream.range(0, totalRes.get(resource)).forEach(n -> builder.append(color).append(shape).append(" "));
            builder.append(":").append(totalRes.get(resource)).append(Color.reset()).append("\n");
        });
        return builder.toString();
    }

    /**
     * method which build a string to display the warehouse
     * @return the string
     */
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
                warehouseBuilder.append(color).append("   ").append("EMPTY").append("\n   ");
            } else {
                warehouseBuilder.append(color).append("   ").append(l.getResourceType().toString()).append("\n   ");
            }

            IntStream.range(0, l.getMaxQuantity()).forEach(n -> warehouseBuilder.append(color).append("╔═══╗").append(" "));
            warehouseBuilder.append(Color.reset()).append("\n").append(".").append(m).append(" ");
            IntStream.range(0, l.getCurrentQuantity()).forEach(n -> warehouseBuilder.append(color).append("║ ").append(shape).append(" ║").append(" "));
            IntStream.range(0, (l.getMaxQuantity() - l.getCurrentQuantity())).forEach(n -> warehouseBuilder.append(color).append("║   ║").append(" "));
            warehouseBuilder.append(Color.reset()).append("\n   ");
            IntStream.range(0, l.getMaxQuantity()).forEach(n -> warehouseBuilder.append(color).append("╚═══╝").append(" "));
            warehouseBuilder.append(Color.reset()).append("\n");
            m++;

        }
        warehouseBuilder.append(Color.reset()).append("\n");

        return warehouseBuilder.toString();
    }

    /**
     * method which build a string to display the strongbox
     * @return the string
     */
    public String strongBoxToString() {
        StringBuilder strongBoxBuilder = new StringBuilder();
        strongBoxBuilder.append("\033[31;1;4mSTRONGBOX\033[0m \n");
        strongBox.keySet().forEach(resource -> {
            String color = CLI.colorResource(resource);
            String shape = CLI.shapeResource(resource);
            strongBoxBuilder.append(color).append(resource.toString()).append(": ");
            IntStream.range(0, strongBox.get(resource)).forEach(n -> strongBoxBuilder.append(color).append(shape).append(" "));
            strongBoxBuilder.append(Color.reset()).append("\n");
        });
        return strongBoxBuilder.toString();
    }

    /**
     * method which builds a string to display the personal production power
     * @return the string
     */
    public String personalProductionToString() {
        return CLI.productionPowerToString(personalProductionPower, Color.reset());
    }

    /**
     * method which displays the personal production power in details
     * @param out the printWriter
     */
    public void displayPersonalProductionPower(PrintWriter out) {
        String personalProductionPowerString = personalProductionToString();
        ArrayList<DrawableObject> drawableObjects = new ArrayList<>();
        drawableObjects.add(new DrawableObject("PERSONAL PRODUCTION POWER", 0, 0));
        drawableObjects.add(new DrawableObject(personalProductionPowerString, 0, 3));
        new Panel(drawableObjects, out, false).show();
    }

    /**
     * method which displays the devCardSlots
     */
    public void displayDevCardSlots() {
        String color = Color.WHITE.getAnsiCode();

       System.out.println("\033[31;1;4mDEVCARD SLOTS\033[0m \n");


        AtomicInteger slotIndex = new AtomicInteger(1);
        ArrayList<DrawableObject> objs = new ArrayList<>();

        topDevCards.forEach(x -> {
            DrawableObject obj;
            if (x != null) {
                obj = new DrawableObject(color + "   " + color + "SLOT " + slotIndex + color + "  " + Color.reset() + "\n" + new DevCardView(x), 0, 0);

            } else {
                obj = new DrawableObject(color + "   " + color + "SLOT " + slotIndex + color + "  " + Color.reset() + "\n" + DevCardView.emptySlot(14), 0, 0);
            }

            objs.add(obj);

            slotIndex.getAndIncrement();
        });
        Panel panel = new Panel(objs,  System.out, true);
        panel.show();
    }

    /**
     * method which counts the total amount of resources in the warehouse
     * @return the hashmap
     */
    public HashMap<Resource, Integer> totalResourcesInWarehouse() {
        HashMap<Resource, Integer> totalRes = new HashMap<>();
        Arrays.stream(Resource.values()).forEach(res -> totalRes.put(res, 0));
        warehouse.forEach(depotState -> totalRes.put(depotState.getResourceType(), totalRes.get(depotState.getResourceType()) + depotState.getCurrentQuantity()));
        return totalRes;
    }

    /**
     * methods which displays all the deposits of any kind
     * @param playerID the player id.
     */
    public void displayAll(String playerID) {

        System.out.println("\033[31;1;4mTHIS IS " + playerID + "'S DASHBOARD\033[0m \n");

        String warehouseString = warehouseToString();
        String strongBoxString = strongBoxToString();
        String personalProductionPowerString = personalProductionToString();

        ArrayList<DrawableObject> objs= new ArrayList<>();
      objs.add( new DrawableObject(warehouseString, 0, 0));
        objs.add(new DrawableObject(strongBoxString, 0, 0));
       objs.add(new DrawableObject("\033[31;1;4mPERSONAL PRODUCTION POWER\033[0m \n", 0, 0));
      objs.add(new DrawableObject(personalProductionPowerString, 0, 0));
        Panel panel = new Panel(objs,System.out, false);

        panel.show();
        displayDevCardSlots();
    }


//    public static void main(String[] args) {
//        DepotState depot = new DepotState(Resource.COIN, 3, 1);
//        DepotState depot2 = new DepotState(Resource.COIN, 4, 0);
//        DepotState depot3 = new DepotState(Resource.SHIELD, 6, 0);
//        ArrayList<DepotState> totalLevels = new ArrayList<>();
//        totalLevels.add(depot);
//        totalLevels.add(depot2);
//        totalLevels.add(depot3);
//
//        HashMap<Resource, Integer> str = new HashMap<>();
//        str.put(Resource.COIN, 6);
//        str.put(Resource.ROCK, 7);
//
//        ArrayList<String> cards = new ArrayList<>(3);
//
//        cards.add("DevCard10");
//        cards.add("DevCard40");
//        cards.add(null);
//
//        String player = "PAOLO";
//
//        ProductionPower p = new ProductionPower(new HashMap<Resource, Integer>(), new HashMap<Resource, Integer>(), 2, 2, 3);
//        DashBoardView d = new DashBoardView(cards, str, totalLevels, player);
//        d.setPersonalProductionPower(p);
//        System.out.println(d.tryAddResource(Resource.COIN, 1, d.getWarehouse().get(1)).getMessage());
//
//        DepotState depot1 = d.getWarehouse().get(1);
//        System.out.println(depot1.getResourceType());
//        d.getWarehouse().stream().filter(dep -> dep != depot1).map(dep -> dep.getCurrentQuantity() == 0 || (dep.getResourceType() != depot1.getResourceType())).forEach(System.out::println);
//        d.displayPersonalProductionPower(new PrintWriter(System.out));
//
//        d.displayAll(player);
//    }


}