package it.polimi.ingsw.model.FaithTrack;

import it.polimi.ingsw.controller.EffectOfCell;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.IntStream;


public class FaithTrack {
    private static FaithTrack instance = null;
    private static ArrayList<Cell> arrayOfCells;

    /**
     * constructor
     * @param num length of faithtrack is equal to the number of cells
     * @param effects effects to initialize special cells
     * @param victoryPoints array of points to initialize each cell
     * @throws IndexOutOfBoundsException if the number of cells is different from the length of the array of vpoints for the cells
     */
    public FaithTrack(int num, HashMap<Integer, EffectOfCell> effects, ArrayList<Integer> victoryPoints) throws IndexOutOfBoundsException{
    arrayOfCells= new ArrayList<>(num);
   if(num== victoryPoints.size()) {
       IntStream.range(0, num - 1).forEach(n -> {
           arrayOfCells.add(n, new Cell(n, victoryPoints.get(n)));
       });
   }
   else {throw new IndexOutOfBoundsException();}


    effects.entrySet().stream().forEach(e->{
        Integer i= e.getKey();
        EffectOfCell effect = e.getValue();
        arrayOfCells.set(i, new CellWithEffect(i, victoryPoints.get(i), effect));
    });
    }


    /**
     * @param num length of faithtrack is equal to the number of cells
     * @param effects effects to initialize special cells
     * @param victoryPoints array of points to initialize each cell
     * @return the instance of faithtrack
     */
    public static FaithTrack initFaithTrack(int num, HashMap<Integer, EffectOfCell> effects, ArrayList<Integer> victoryPoints) {
            if (instance == null) instance = new FaithTrack(num, effects, victoryPoints);
        return instance;
    }

    /**
     * the length of the faithtrack
     * @return the length of faithtrack
     */
    public int size(){
       return arrayOfCells.size();
    }
}

