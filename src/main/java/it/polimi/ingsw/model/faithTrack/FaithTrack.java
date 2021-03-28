package it.polimi.ingsw.model.faithTrack;

import java.util.HashMap;


public class FaithTrack {
    private static FaithTrack instance = null;
    private static Cell[] arrayOfCells;

    /**
     * Constructor of the FaithTrack: it builds a final array of num cells, some of which have victoryPoints
     * and some of which are cells with effect(popeCell, finalCell...) passed through parameters.
     * @param num numer of cell
     * @param cellsWithEffectMap  map containing all the cellWithEffect and their respective index.
     * @param victoryPointsMap map containing all theindexes of cells which have victoryPoints and the respective number of points.
     */
    public FaithTrack(int num, HashMap<Integer, CellWithEffect> cellsWithEffectMap, HashMap<Integer, Integer> victoryPointsMap) {
        arrayOfCells = new Cell[num];
        for (int i = 0; i < num; i++) {
            arrayOfCells[i].setIndex(i);
        }

        cellsWithEffectMap.forEach((k,v)->arrayOfCells[k]=v);
        victoryPointsMap.forEach((k,v)->arrayOfCells[k].setVictoryPoints(v));
    }

    public static FaithTrack initFaithTrack(int num, HashMap<Integer, CellWithEffect> cellsWithEffectMap, HashMap<Integer, Integer> victoryPointsMap) {
        if (instance == null) instance = new FaithTrack( num, cellsWithEffectMap, victoryPointsMap);
        return instance;
    }
}

