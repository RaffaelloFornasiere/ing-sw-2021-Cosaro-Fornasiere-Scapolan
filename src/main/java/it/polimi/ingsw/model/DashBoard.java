package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.EmptyStrongboxException;
import it.polimi.ingsw.exceptions.IndexSlotException;
import it.polimi.ingsw.exceptions.LevelCardException;
import it.polimi.ingsw.model.DevCards.DevCard;
import it.polimi.ingsw.model.FaithTrack.FaithTrack;
import it.polimi.ingsw.model.FaithTrack.FaithTrackData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;
import java.util.stream.IntStream;

public class DashBoard {

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
     * @param faithTrack  is the faith track common to all players
     */

    public DashBoard( int numberOfSlots, ArrayList<Integer> eachDepotCapacity, ProductionPower personalPower, FaithTrack faithTrack){
    strongBox = new HashMap<>();
    //initializes each resource in the strongbox to quantity zero
    for(Resource resource: Resource.values()){
        strongBox.put(resource, 0);
    }
    //initializes a number of slots for the devcard. The number of slots is passed as parameter numberOfSlots
    cardSlots= new ArrayList<>();
        IntStream.range(0,numberOfSlots-1).forEach(n ->{
            cardSlots.add(new Stack<>());
        });

    //initializes the depots to their max capacity
    warehouse=new ArrayList<>();
    for(Integer maxQauntity: eachDepotCapacity){
        warehouse.add(new Depot(maxQauntity));
        }
    //initializes personalpower
    this.personalPower= personalPower;
    //initializes faithTrackData
    this.faithTrack= new FaithTrackData(faithTrack);

    }

    /**
     * modifier of the dashboard, it adds resources
     * @param resource type of resource
     * @param quantity quantity to add
     */
    public void addResourcesToStrongBox( Resource resource, int quantity){
        strongBox.put( resource, strongBox.get(resource) + quantity);
    }
    /**
     * modifier of the dashboard, it subtracts resources
     * @param resource type of resource
     * @param quantity quantity to subtract
     */
    public void subResourcesToStrongBox( Resource resource, int quantity)throws EmptyStrongboxException{
        if(strongBox.get(resource)- quantity<0) throw new EmptyStrongboxException();

        strongBox.put(resource, strongBox.get(resource) - quantity);
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
        }
        //instead, if it's empty, the card I add must be level 1, otherwise gives exception
        else  if(card.getLevel()!= 1) throw new LevelCardException();
        else cardSlots.get(slotIndex).push(card);
    }

    /**
     * Getter of StrongBox
     * @return a copy of the strongbox
     */
    public HashMap<Resource, Integer> getStrongBox() {
        return (HashMap<Resource, Integer>) strongBox.clone();
    }

    /**
     * getter of cardSlots
     * @return a copy of the card slots
     */
    public ArrayList<Stack<DevCard>> getCardSlots() {
        return (ArrayList<Stack<DevCard>>)cardSlots.clone();
    }

    public HashMap<Resource, Integer> getDepotResources(){
        HashMap<Resource, Integer> resources = new HashMap<>();

        for(Depot d: warehouse)
            resources.put(d.getResourceType(), d.getCurrentQuantity());

        return resources;
    }

    public HashMap<Resource, Integer> getAllDashboardResources(){
        HashMap<Resource, Integer> resources = getDepotResources();

        for(Resource r: strongBox.keySet())
            resources.put(r, strongBox.get(r) + resources.getOrDefault(r, 0));

        return resources;
    }
}
