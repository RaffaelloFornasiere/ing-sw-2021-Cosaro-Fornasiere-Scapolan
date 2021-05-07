package it.polimi.ingsw.model.FaithTrack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.IntStream;


public class FaithTrack {
    private static FaithTrack instance = null;
    private static ArrayList<AbstractCell> arrayOfCells;

    /**
     * constructor
     *
     * @param num           length of faithtrack is equal to the number of cells
     * @param effects       effects to initialize special cells
     * @param victoryPoints array of points to initialize each cell
     * @throws IndexOutOfBoundsException if the number of cells is different from the length of the array of vpoints for the cells
     */
    protected FaithTrack(int num, HashMap<Integer, CellWithEffect> effects, ArrayList<Integer> victoryPoints) throws IllegalArgumentException{
        arrayOfCells = new ArrayList<>(num);
        if (num == victoryPoints.size()) {
            IntStream.range(0, num).forEach(n -> {
                arrayOfCells.add(n, new Cell(n, victoryPoints.get(n)));
            });
        } else {
            throw new IllegalArgumentException("length of faithTrack doesn't coincide with victoryPoint array length");
        }


        effects.entrySet().stream().forEach(e -> {
            Integer i = e.getKey();
            CellWithEffect cell = e.getValue();
            arrayOfCells.set(i,cell);
        });

        arrayOfCells.set(arrayOfCells.size()-1, new LastCell(arrayOfCells.get(arrayOfCells.size()-1)));

    }


    /**
     * @param num           length of faithtrack is equal to the number of cells
     * @param effects       effects to initialize special cells
     * @param victoryPoints array of points to initialize each cell
     * @return the instance of faithtrack
     */
    public static FaithTrack initFaithTrack(int num, HashMap<Integer, CellWithEffect> effects, ArrayList<Integer> victoryPoints) throws IllegalArgumentException {
        if (instance == null) instance = new FaithTrack(num, effects, victoryPoints);
        return instance;
    }

    /**
     * the length of the faithtrack
     *
     * @return the length of faithtrack
     */
    public int size() {
        return arrayOfCells.size();
    }



    /**
     * Getter of array Of Cells of FaithTrack
     * @return
     */
    public static ArrayList<AbstractCell> getArrayOfCells() {
        return (ArrayList<AbstractCell>)arrayOfCells.clone();
    }
}