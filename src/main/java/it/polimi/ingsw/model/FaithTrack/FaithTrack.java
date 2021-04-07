package it.polimi.ingsw.model.FaithTrack;

import java.util.ArrayList;


public class FaithTrack {
    private static FaithTrack instance = null;
    private static ArrayList<Cell> arrayOfCells;

    /**
     * @Constructor
     */
    public FaithTrack(ArrayList<Cell> array) {
        arrayOfCells = array;
    }

    /**
     * inizializzo il sigleton
     * @param array
     * @return
     */
    public static FaithTrack initFaithTrack(ArrayList<Cell> array) {
        if (instance == null) instance = new FaithTrack( array);
        return instance;
    }

    public int size(){
       return arrayOfCells.size();
    }

    public Cell getCellOfIndex(int i){
        return arrayOfCells.get(i);
    }
}

