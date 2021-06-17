package it.polimi.ingsw.model;

import it.polimi.ingsw.events.ClientEvents.DepotState;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.DevCards.DevCard;
import it.polimi.ingsw.model.FaithTrack.FaithTrackData;
import it.polimi.ingsw.utilities.Observable;

import java.util.*;
import java.util.stream.IntStream;

public class DashBoard extends Observable {

    private HashMap<Resource, Integer> strongBox;
    private ArrayList<Stack<DevCard>> cardSlots;
    private ArrayList<Depot> warehouse;
    private ProductionPower personalPower;
    private FaithTrackData faithTrack;

    /**
     * Constructor for the class
     * @param numberOfSlots  is the number of development card slots we want our dashboard to have
     * @param eachDepotCapacity is how many resources one depot can possibly have at most
     * @param personalPower is the personal production power of the player
     */

    public DashBoard(int numberOfSlots, ArrayList<Integer> eachDepotCapacity, ProductionPower personalPower){
    strongBox = new HashMap<>();
    //initializes each resource in the strongbox to quantity zero
    for(Resource resource: Resource.values()){
        strongBox.put(resource, 0);
    }
    //initializes a number of slots for the devcard. The number of slots is passed as parameter numberOfSlots
    cardSlots= new ArrayList<>();
        IntStream.range(0,numberOfSlots).forEach(n -> cardSlots.add(new Stack<>()));

    //initializes the depots to their max capacity
    warehouse=new ArrayList<>();
    for(Integer maxQuantity: eachDepotCapacity){
        warehouse.add(new Depot(maxQuantity));
        }
    //initializes personalpower
    this.personalPower= personalPower;
    //initializes faithTrackData
    this.faithTrack= new FaithTrackData();

    }

    /**
     * modifier of the dashboard, it adds resources
     * @param resource type of resource
     * @param quantity quantity to add
     */
    public void addResourcesToStrongBox( Resource resource, int quantity){
        strongBox.put( resource, strongBox.get(resource) + quantity);
        notifyObservers();
    }
    /**
     * modifier of the dashboard, it subtracts resources
     * @param resource type of resource
     * @param quantity quantity to subtract
     */
    public void subResourcesToStrongBox( Resource resource, int quantity)throws EmptyStrongboxException{
        if(strongBox.get(resource)- quantity<0) throw new EmptyStrongboxException();

        strongBox.put(resource, strongBox.get(resource) - quantity);
        notifyObservers();
    }

    /**
     * modifier of the dashboard, it adds resources
     * @param resource type of resource
     * @param quantity quantity to add
     */

    public void addResourcesToWarehouse(Resource resource, int quantity) throws IllegalArgumentException,
            ResourcesLimitsException {
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
        catch (DepotResourceException e){}
    }
    /**
     * modifier of the dashboard, it subtracts resources
     * @param resource type of resource
     * @param quantity quantity to subtract
     */
    public void subResourcesToWarehouse( Resource resource, int quantity) throws
            ResourcesLimitsException {
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

    public void subResourcesToWarehouse(HashMap<Resource, Integer> resources) throws
            ResourcesLimitsException {
        HashMap<Resource, Integer> subbedResources = new HashMap<>();
        try {
            for (Resource r : resources.keySet()) {
                int n = resources.get(r);
                if(n!=0) {
                    subResourcesToWarehouse(r, resources.get(r));
                    subbedResources.put(r, resources.get(r));
                }
            }
        } catch (Exception e){
            for (Resource r : subbedResources.keySet()) {
                addResourcesToWarehouse(r, subbedResources.get(r));
            }
            throw e;
        }
    }

    public void setWarehouseResources(ArrayList<DepotState> newWarehouseResources){
        if(newWarehouseResources.size()!=warehouse.size())
            throw new IllegalArgumentException("New warehouse structure incompatible");
        for(int i=0; i<warehouse.size(); i++){
            if(newWarehouseResources.get(i).getMaxQuantity()!=warehouse.get(i).getMaxQuantity())
                throw new IllegalArgumentException("New warehouse structure incompatible");
        }
        for(int i=0; i<warehouse.size(); i++){
            Depot depot = warehouse.get(i);
            DepotState depotState = newWarehouseResources.get(i);
            try {
                depot.subResources(depot.getResourceType(), depot.getCurrentQuantity());
                depot.addResources(depotState.getResourceType(), depotState.getCurrentQuantity());
            } catch (ResourcesLimitsException | DepotResourceException e) {
                e.printStackTrace();
            }
        }
        notifyObservers();
    }


    /**
     * modifier of the dashboard, it adds devCards to the cardSlots in the Dashboard, according to the level constraints
     * @param slotIndex is the index of the slot to which I want to add the card. Indexes go from 0 to cardSlots.size()-1
     * @param card is the card I want to add
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

    public boolean checkSlot(DevCard devCard, int slotIndex){
        if(cardSlots.get(slotIndex).isEmpty())
            return devCard.getLevel()==1;

        return cardSlots.get(slotIndex).peek().getLevel()+1==devCard.getLevel();
    }

    /**
     * Getter of StrongBox
     * @return a copy of the strongbox
     */
    @SuppressWarnings("unchecked")
    public HashMap<Resource, Integer> getStrongBox() {
        return (HashMap<Resource, Integer>) strongBox.clone();
    }

    /**
     * getter of cardSlots
     * @return a copy of the card slots
     */
    @SuppressWarnings("unchecked")
    public ArrayList<Stack<DevCard>> getCardSlots() {
        return (ArrayList<Stack<DevCard>>)cardSlots.clone();
    }

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
     * @return the warehouse as an array of Depot
     */
    public ArrayList<Depot> getWarehouse() {
        return (ArrayList<Depot>) warehouse.clone();
    }

    /**
     * gets all the resources stored in the warehouse as an hashmap
     * if a resource is not present, the map will have an entry for it anyway, with value 0
     * @return all the resources in the warehouse
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
     * gets all the resources stored in the warehouse and the strongbox as an hashmap
     * if a resource is not present, the map will have an entry for it anyway, with value 0
     * @return all the resources in the warehouse and in the strongbox
     */
    public HashMap<Resource, Integer> getAllDashboardResources(){
        HashMap<Resource, Integer> resources = getWarehouseResources();

        for(Resource r: strongBox.keySet())
            resources.put(r, strongBox.get(r) + resources.getOrDefault(r, 0));

        return resources;
    }

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
