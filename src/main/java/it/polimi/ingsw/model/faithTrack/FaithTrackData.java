package it.polimi.ingsw.model.faithTrack;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class FaithTrackData {
    private int position;
    private FaithTrack physicalFaithTrack;
    private List<PopeFavorCard> acquiredPopeFavorCards;

    /**
     * Constructor inizializes one instance of the class: initial position equal to zero,
     * takes as input one instance of Faithtrack given by the Controller,
     * has initial list of popeFavorCard empty.
     * @param physicalFaithTrack is given by the controller
     */

    public FaithTrackData(FaithTrack physicalFaithTrack){
        position=0;
        this.physicalFaithTrack=physicalFaithTrack;
        acquiredPopeFavorCards=new LinkedList<>();
    }

    /**
     * Getter  of position in  the faithtrack
     *
     * @return position in the faithtrack
     */

    public int getPosition() {
        return position;
    }

    /**
     * Setter of position in the faithtrack
     * @param position in the faithtrack
     */

    public void setPosition(int position) {
        this.position = position;
    }

    /**
     * Adds one popeFavorCard to the list of popeFavorCards
     * @param card is a popeFavorCard to add
     */
    public void addPopeFavorCard( PopeFavorCard card){
        acquiredPopeFavorCards.add(card);
    }
    /**
     * Counts the total of points obtained from FavorPopeCards
     * @return the total number of points obtained from FavorPopeCards
     */
    public int getFavorPopeCardPoints(){
        int count= 0;
        Iterator iterator= acquiredPopeFavorCards.iterator();
        while (iterator.hasNext()) {
            count+= ((PopeFavorCard)iterator.next()).getVictoryPoints();
        }
        return  count;
    }
}
