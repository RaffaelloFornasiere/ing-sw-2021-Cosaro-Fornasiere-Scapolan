package it.polimi.ingsw.model.FaithTrack;

import it.polimi.ingsw.controller.EffectOfCell;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.IntStream;


public class FaithTrack {
    private  static FaithTrack instance = null;
    private  static ArrayList<Cell> arrayOfCells;


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
     *
     * @param num
     * @param effects
     * @param victoryPoints
     * @return
     */
    public static FaithTrack initFaithTrack(int num, HashMap<Integer, EffectOfCell> effects, ArrayList<Integer> victoryPoints) {
       // try {
            if (instance == null) instance = new FaithTrack(num, effects, victoryPoints);

       // }
      //  catch(IndexOutOfBoundsException e){}
        return instance;
    }

    public int size(){
       return arrayOfCells.size();
    }

    public Cell getCellOfIndex(int i){
        return arrayOfCells.get(i);
    }
}

