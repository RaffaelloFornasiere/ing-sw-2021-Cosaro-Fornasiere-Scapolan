package it.polimi.ingsw.model.FaithTrack;

import it.polimi.ingsw.exceptions.OutOfBoundException;
import it.polimi.ingsw.utilities.Observable;

import java.util.HashMap;

public class FaithTrackData extends Observable {
    private int position;
    private final FaithTrack physicalFaithTrack;
    private HashMap<Integer, PopeFavorCard> acquiredPopeFavorCards;

    /**
     * Constructor inizializes one instance of the class: initial position equal to zero,
     * takes as input one instance of Faithtrack given by the Controller,
     * has initial list of popeFavorCard empty.
     * @param physicalFaithTrack is given by the controller
     */
    public FaithTrackData(FaithTrack physicalFaithTrack){
        position=0;
        this.physicalFaithTrack=physicalFaithTrack;
        acquiredPopeFavorCards=new HashMap<>();
    }



    /**
     * Getter  of position in  the faithtrack
     * @return position in the faithtrack
     */
    public int getPosition() {
        return position;
    }



    /**
     * Setter of position in the faithtrack
     * @param position in the faithtrack
     */
    public void setPosition(int position) throws OutOfBoundException{
        if(position <= physicalFaithTrack.size()) this.position = position;
        else throw new OutOfBoundException();
        notifyObservers();
    }



    /**
     *Getter of the physical Track
     * @return PhysicalTrack
     */
    public FaithTrack getPhysicalFaithTrack() {
        return physicalFaithTrack;
    }



    /**
     * Getter of acquiredFavorPopeCard
     * @return the array of favorpopecard
     */
    public HashMap<Integer,PopeFavorCard> getAcquiredPopeFavorCards() {
        return acquiredPopeFavorCards;
    }



    /**
     * Adds one popeFavorCard to the list of popeFavorCards
     * @param card is a popeFavorCard to add
     */
    public void addPopeFavorCard(int n,  PopeFavorCard card){
        acquiredPopeFavorCards.put(n, card);
        notifyObservers();
    }



    /**
     * Counts the total of points obtained from FavorPopeCards
     * @return the total number of points obtained from FavorPopeCards
     */
    public int getFavorPopeCardPoints(){
       return  acquiredPopeFavorCards.values()
                .stream().map(value->value.getVictoryPoints())
                .mapToInt(Integer::valueOf)
                .sum();
    }




    /**
     * increment the position
     * @param n number os steps to increment
     */
    public void incrementPosition(int n) throws OutOfBoundException {
        if(position+n<= physicalFaithTrack.size()) position +=n;
        else throw new OutOfBoundException();
        notifyObservers();
    }

}
