package it.polimi.ingsw.model.FaithTrack;

import it.polimi.ingsw.exceptions.OutOfBoundException;
import it.polimi.ingsw.utilities.Observable;

import java.util.HashMap;

public class FaithTrackData extends Observable {
    private int position;
    private final HashMap<Integer, PopeFavorCard> acquiredPopeFavorCards = new HashMap<>();

    /**
     * Constructor initializes one instance of the class: initial position equal to zero,
     * takes as input one instance of FaithTrack given by the Controller,
     * has initial list of popeFavorCard empty.
     *
     */
    public FaithTrackData() {
        position = 0;
    }


    /**
     * Getter  of position in  the faithTrack
     *
     * @return position in the faithTrack
     */
    public int getPosition() {
        return position;
    }


    /**
     * Setter of position in the faithTrack
     *
     * @param position in the faithTrack
     */
    public void setPosition(int position) throws OutOfBoundException {
        if (position < FaithTrack.size()) this.position = position;
        else throw new OutOfBoundException("");
        notifyObservers();
    }



    /**
     * Getter of acquiredFavorPopeCard
     *
     * @return the array of favor pope cards
     */
    public HashMap<Integer, PopeFavorCard> getAcquiredPopeFavorCards() {
        return acquiredPopeFavorCards;
    }


    /**
     * Adds one popeFavorCard to the list of popeFavorCards
     *
     * @param card is a popeFavorCard to add
     */
    public void addPopeFavorCard(int n, PopeFavorCard card)  {
        if(!(FaithTrack.getArrayOfCells().get(n) instanceof PopeCell)) throw  new IllegalArgumentException("Wrong key in the hashMap of Favor Pope Cars, because for that index there is no PopeCell in the FaithTrack");
        acquiredPopeFavorCards.put(n, card);
        notifyObservers();
    }

    /**
     * Counts the total of points obtained from FavorPopeCards
     *
     * @return the total number of points obtained from FavorPopeCards
     */
    public int getFavorPopeCardPoints() {
        return acquiredPopeFavorCards.values()
                .stream().map(PopeFavorCard::getVictoryPoints)
                .mapToInt(Integer::valueOf)
                .sum();
    }

    /**
     * increment the position
     *
     * @param n number os steps to increment
     */
    public void incrementPosition(int n) throws OutOfBoundException {
        if (position + n < FaithTrack.size()) position += n;
        else throw new OutOfBoundException();
        notifyObservers();
    }

}
