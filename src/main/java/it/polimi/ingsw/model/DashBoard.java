package it.polimi.ingsw.model;

import it.polimi.ingsw.events.ClientEvents.DepotState;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.DevCards.DevCard;
import it.polimi.ingsw.model.FaithTrack.FaithTrack;
import it.polimi.ingsw.model.FaithTrack.FaithTrackData;
import it.polimi.ingsw.utilities.Observable;

import java.util.*;
import java.util.stream.IntStream;

public class DashBoard extends Observable {

    private final HashMap<Resource, Integer> strongBox;
    private final ArrayList<Stack<DevCard>> cardSlots;
    private ArrayList<Depot> warehouse;
    private final ProductionPower personalPower;
    private final FaithTrackData faithTrack;

    /**
     * Constructor for the class
     * @param numberOfSlots  Is the number of development card slots we want our dashboard to have
     * @param eachDepotCapacity Is how many resources one depot can possibly have at most
     * @param personalPower Is the personal production power of the player
     * @param faithTrack  Is the faith track common to all players
     */
    public DashBoard(int numberOfSlots, ArrayList<Integer> eachDepotCapacity, ProductionPower personalPower, FaithTrack faithTrack){
    strongBox = new HashMap<>();
    //initializes each resource in the strongbox to quantity zero
    for(Resource resource: Resource.values()){
        strongBox.put(resource, 0);
    }
    //initializes a number of slots for the dev card. The number of slots is passed as parameter numberOfSlots
    cardSlots= new ArrayList<>();
        IntStream.range(0,numberOfSlots).forEach(n -> cardSlots.add(new Stack<>()));

    //initializes the depots to their max capacity
    warehouse=new ArrayList<>();
    for(Integer maxQuantity: eachDepotCapacity){
        warehouse.add(new Depot(maxQuantity));
        }
    //initializes personal power
    this.personalPower= personalPower;
    //initializes faithTrackData
    this.faithTrack= new FaithTrackData(faithTrack);

    }

    /**
     * Modifier of the dashboard, it adds resources to the strongbox
     * @param resource Type of resource
     * @param quantity Quantity to add
     */
    public void addResourcesToStrongBox( Resource resource, int quantity){
        strongBox.put( resource, strongBox.get(resource) + quantity);
        notifyObservers();
    }

    /**
     * Modifier of the dashboard, it subtracts resources to the strongbox
     * @param resource Type of resource
     * @param quantity Quantity to subtract
     * @throws EmptyStrongboxException if the strongbox does not have enough resources
     */
    public void subResourcesToStrongBox( Resource resource, int quantity)throws EmptyStrongboxException{
        if(strongBox.get(resource)- quantity<0) throw new EmptyStrongboxException();

        strongBox.put(resource, strongBox.get(resource) - quantity);
        notifyObservers();
    }

    /**
     * Modifier of the dashboard, it adds resources to the warehouse
     * @param resource Type of resource
     * @param quantity Quantity to add
     * @throws IllegalArgumentException If there's no warehouse which can store the given resource type
     * @throws ResourcesLimitsException If the depot containing the given type of resource does not have enough space
     */
    public void addResourcesToWarehouse(Resource resource, int quantity) throws ResourcesLimitsException {
        var depot = warehouse
                .stream()
                .filter(x -> x.getResourceType().equals(resource))
                .findFirst().orElse(null);
        if(depot == null) {
            depot = warehouse
                    .stream()
                    .filter(x -> x.getCurrentQuantity() == 0 && x.getMaxQuantity() >= quantity)
                    .min(Comparator.comparingInt(Depot::getMaxQuantity))
                    .orElse(null);
        }
        if(depot == null)
            throw new IllegalArgumentException("Incompatible resource type");
        try{depot.addResources(resource, quantity);
            notifyObservers();}
        catch (DepotResourceException ignore){}
    }

    /**
     * Modifier of the dashboard, it subtracts resources to the warehouse
     * @param resource Type of resource
     * @param quantity Quantity to subtract
     * @throws IllegalArgumentException If there's no warehouse storing the given resource type
     * @throws ResourcesLimitsException When there are not enough resources to remove
     */
    public void subResourcesToWarehouse( Resource resource, int quantity) throws ResourcesLimitsException {
        var depot = warehouse
                .stream()
                .filter(x -> x.getResourceType().equals(resource) && x.getCurrentQuantity()>0)
                .findFirst().orElse(null);
        if(depot == null)
            throw new IllegalArgumentException("Incompatible resource type");
        try{
            depot.subResources(resource, quantity);
            notifyObservers();
        } catch (DepotResourceException ignore){}
    }


    /**
     * Modifier of the dashboard, it subtracts resources to the warehouse
     * @param resources HashMap containing how many resources to subtract of each type
     * @throws IllegalArgumentException If there's no warehouse storing the given resource type
     * @throws ResourcesLimitsException When there are not enough resources to remove
     */
    public void subResourcesToWarehouse(HashMap<Resource, Integer> resources) throws ResourcesLimitsException {
        ArrayList<Depot> oldWarehouse = new ArrayList<>();
        for(Depot d: warehouse){
            Depot nd = new Depot(d.getMaxQuantity());
            try {
                nd.addResources(d.getResourceType(), d.getCurrentQuantity());
            } catch (DepotResourceException ignore) { }
            oldWarehouse.add(nd);
        }

        try {
            for (Resource r : resources.keySet()) {
                int n = resources.get(r);
                if(n!=0) {
                    subResourcesToWarehouse(r, resources.get(r));
                }
            }
        } catch (Exception e){
            warehouse = oldWarehouse;
            throw e;
        }
    }

    /**
     * Methods that sets the state of the warehouse to the one given
     * @param newWarehouseResources The new state for the warehouse
     * @throws IllegalArgumentException If the structure of the new warehouse state is incompatible or there are multiple depots with the same resource stored
     */
    public void setWarehouseResources(ArrayList<DepotState> newWarehouseResources){
        if(newWarehouseResources.size()!=warehouse.size())
            throw new IllegalArgumentException("New warehouse structure incompatible");
        for(int i=0; i<warehouse.size(); i++){
            if(newWarehouseResources.get(i).getMaxQuantity()!=warehouse.get(i).getMaxQuantity())
                throw new IllegalArgumentException("New warehouse structure incompatible");
        }
        ArrayList<Resource> presentResources = new ArrayList<>();
        for(DepotState depotState: newWarehouseResources){
            if(depotState.getCurrentQuantity()>0){
                Resource r = depotState.getResourceType();
                if(presentResources.contains(r)) throw new IllegalArgumentException("Multiple depots contain the same resource");
                presentResources.add(r);
            }
        }

        for(int i=0; i<warehouse.size(); i++){
            Depot depot = warehouse.get(i);
            DepotState depotState = newWarehouseResources.get(i);
            try {
                depot.subResources(depot.getResourceType(), depot.getCurrentQuantity());
                depot.addResources(depotState.getResourceType(), depotState.getCurrentQuantity());
            } catch (ResourcesLimitsException | DepotResourceException ignore) { }
        }
        notifyObservers();
    }


    /**
     * Modifier of the dashboard, it adds devCards to the cardSlots in the Dashboard, according to the level constraints
     * @param slotIndex The index of the slot where the card will be added. Indexes go from 0 to cardSlots.size()-1
     * @param card The card to add
     * @throws IndexSlotException If the index slot is out of bounds
     * @throws LevelCardException If the card can't go on the slot because of it's level
     */
    public void addCard(int slotIndex, DevCard card)throws IndexSlotException, LevelCardException {
        //if the parameter is an index which doesn't exist, gives exception.
        if( slotIndex<0 || slotIndex>=cardSlots.size()) throw new IndexSlotException();
        //if the stack I want to add the card to is not empty, I must check that the level of cards respect the rules.
        if(!(cardSlots.get(slotIndex).isEmpty())) {
            DevCard temp = cardSlots.get(slotIndex).peek();

            if (temp.getLevel() != card.getLevel() -1) throw new LevelCardException();

            cardSlots.get(slotIndex).push(card);
            notifyObservers();
        }
        //instead, if it's empty, the card I add must be level 1, otherwise gives exception
        else  if(card.getLevel()!= 1) throw new LevelCardException();
        else {
            cardSlots.get(slotIndex).push(card);
            notifyObservers();
        }
    }

    /**
     * Method that checks if a card can go in a certain slot
     * @param slotIndex The index to check
     * @param devCard The card to check
     * @return whether the card can go in the given slot
     */
    public boolean checkSlot(int slotIndex, DevCard devCard){
        if(cardSlots.get(slotIndex).isEmpty())
            return devCard.getLevel()==1;

        return cardSlots.get(slotIndex).peek().getLevel()+1==devCard.getLevel();
    }

    /**
     * Getter of StrongBox
     * @return A copy of the strongbox
     */
    public HashMap<Resource, Integer> getStrongBox() {
        return new HashMap<>(strongBox);
    }

    /**
     * Getter of cardSlots
     * @return A copy of the card slots
     */
    public ArrayList<Stack<DevCard>> getCardSlots() {
        return new ArrayList<>(cardSlots);
    }

    /**
     * Getter for the top cards
     * @return An ArrayList containing all the cards at the top of each slot
     */
    public ArrayList<DevCard> getTopCards(){
        ArrayList<DevCard> ret = new ArrayList<>();
        for(Stack<DevCard> stack: cardSlots) {
            try {
                ret.add(stack.peek());
            } catch (EmptyStackException ignore){}
        }
        return ret;
    }

    /**
     * Getter for the warehouse
     * @return tTe warehouse as an array of Depot
     */
    public ArrayList<Depot> getWarehouse() {
        return new ArrayList<>(warehouse);
    }

    /**
     * Gets all the resources stored in the warehouse as an HashMap.
     * If a resource is not present, the map will have an entry for it anyway, with value 0
     * @return All the resources in the warehouse
     */
    public HashMap<Resource, Integer> getWarehouseResources(){
        HashMap<Resource, Integer> resources = new HashMap<>();

        for(Depot d: warehouse) {
            int n = d.getCurrentQuantity();
            if(n>0) resources.put(d.getResourceType(), n);
        }

        for(Resource r: Resource.values())
            if(!resources.containsKey(r))
                resources.put(r, 0);

        return resources;
    }

    /**
     * Gets all the resources stored in the warehouse and the strongbox as an HashMap.
     * If a resource is not present, the map will have an entry for it anyway, with value 0
     * @return All the resources in the warehouse and in the strongbox
     */
    public HashMap<Resource, Integer> getAllDashboardResources(){
        HashMap<Resource, Integer> resources = getWarehouseResources();

        for(Resource r: strongBox.keySet())
            resources.put(r, strongBox.get(r) + resources.getOrDefault(r, 0));

        return resources;
    }

    /**
     * Getter for the personal production power
     * @return The personal production power
     */
    public ProductionPower getPersonalPower() {
        return personalPower;
    }

    /**
     * Getter for the faith track
     * @return the faith track
     */
    public FaithTrackData getFaithTrackData(){
        return faithTrack;
    }
}
