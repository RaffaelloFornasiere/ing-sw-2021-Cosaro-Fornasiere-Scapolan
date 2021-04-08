package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.EmptyStrongboxException;
import it.polimi.ingsw.exceptions.IndexSlotException;
import it.polimi.ingsw.model.FaithTrack.FaithTrack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;
import java.util.stream.IntStream;

public class DashBoard {


    private HashMap<Resource, Integer> strongBox;
    private ArrayList<Stack<DevCard>> cardSlots;
    private  ArrayList<Depot> warehouse;
    private ProductionPower personalPower;
    private FaithTrack faithTrack;

    /**
     * @Constructor
     * @param numberOfSlots  is the number of slots we whant our dashboard to have
     * @param eachDepotCapacity is how many resources one depot can possibly have at most
     * @param consumed is the resources consumed by the personal production power
     * @param produced is the resources produced by the personal production power
     * @param faithTrack  is the common faith track
     */

    public DashBoard( int numberOfSlots, ArrayList<Integer> eachDepotCapacity, HashMap<Resource, Integer> consumed,HashMap<Resource, Integer>produced, FaithTrack faithTrack){
    strongBox = new HashMap<>();

    for(Resource resource: Resource.values()){
        strongBox.put(resource, 0);
    }

    cardSlots= new ArrayList<>();
        IntStream.range(0,numberOfSlots-1).forEach(n ->{
            cardSlots.add(new Stack<>());
        });

    warehouse=new ArrayList<>();
    for(Integer maxQauntity: eachDepotCapacity){
        warehouse.add(new Depot(maxQauntity));
        }

    personalPower= new ProductionPower(consumed, produced);
    this.faithTrack= faithTrack;

    }

    /**
     * Getter of DashBoard
     * @return Dashboard
     */
    public HashMap<Resource, Integer> getStrongBox() {
        return strongBox;
    }

    /**
     * Fetter  of  CardSlots
     * @return
     */
    public ArrayList<Stack<DevCard>> getCardSlots() {
        return cardSlots;
    }

    /**
     * Getter of WareHouse
     * @return
     */
    public ArrayList<Depot> getWarehouse() {
        return warehouse;
    }

    /**
     *
     * @return
     */
    public ProductionPower getPersonalPower() {
        return personalPower;
    }

    /**
     * Getter of FaithTrack
     * @return
     */
    public FaithTrack getFaithTrack() {
        return faithTrack;
    }

    /**
     * modifier of the dushboard, it adds resources
     * @param resource
     * @param quantity
     */
    public void addResourcesToStrongBox( Resource resource, int quantity){
        strongBox.put( resource, strongBox.get(resource) + quantity);
    }




    /**
     * modifier of the dushboard, it subtracts resources
     * @param resource
     * @param quantity
     */
    public void subResourcesToStrongBox( Resource resource, int quantity)throws EmptyStrongboxException{
        if(strongBox.get(resource)- quantity<0)throw new EmptyStrongboxException();
        else {
            strongBox.put(resource, strongBox.get(resource) - quantity);
        }
    }


    /**
     * This method adds one card to one of the slots.
     * @param slotIndex  is the slot in which the card is added
     * @param card the card to add
     * @throws IndexSlotException if the slot I pass as parameter doesn't exist
     */
    public void addCard(int slotIndex, DevCard card)throws IndexSlotException//, LevelCardException
    {
        if( slotIndex>cardSlots.size()-1) throw new IndexSlotException();
        else {
            DevCard temp= cardSlots.get(slotIndex).peek();
          //  if(temp.getLevel>=card.getLevel) throw new LevelCardException();
        //else cardSlots.get(slotIndex).push(card);
        }

    }

    //STUB CLASS
    private class DevCard{}
}
