package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.EmptyStrongboxException;
import it.polimi.ingsw.exceptions.IndexSlotException;
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
    private  ArrayList<Depot> warehouse;
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

    this.personalPower= personalPower;

    this.faithTrack= new FaithTrackData(faithTrack);

    }

    /**
     * modifier of the dashboard, it adds resources
     * @param resource
     * @param quantity
     */
    public void addResourcesToStrongBox( Resource resource, int quantity){
        strongBox.put( resource, strongBox.get(resource) + quantity);
    }
    /**
     * modifier of the dashboard, it subtracts resources
     * @param resource
     * @param quantity
     */
    public void subResourcesToStrongBox( Resource resource, int quantity)throws EmptyStrongboxException{
        if(strongBox.get(resource)- quantity<0) throw new EmptyStrongboxException();

        strongBox.put(resource, strongBox.get(resource) - quantity);
    }

    public void addCard(int slotIndex, DevCard card)throws IndexSlotException//, LevelCardException
    {
        if( slotIndex<0 || slotIndex>=cardSlots.size()) throw new IndexSlotException();

        DevCard temp= cardSlots.get(slotIndex).peek();

        //if(temp.getLevel()!=card.getLevel()+1) throw new LevelCardException();

        //cardSlots.get(slotIndex).push(card);

    }
}
