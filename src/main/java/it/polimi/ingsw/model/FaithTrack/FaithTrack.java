package it.polimi.ingsw.model.FaithTrack;

import java.util.ArrayList;
import java.util.stream.IntStream;


public class FaithTrack {
    private static FaithTrack instance = null;
    private static ArrayList<AbstractCell> arrayOfCells;

    /**
     * constructor
     *
     * @param num           length of faithTrack is equal to the number of cells
     * @param effects       effects to initialize special cells
     * @param victoryPoints array of points to initialize each cell
     * @throws IndexOutOfBoundsException if the number of cells is different from the length of the array of victory points for the cells
     */
    private FaithTrack(int num, ArrayList<CellWithEffect> effects, ArrayList<Integer> victoryPoints) throws IllegalArgumentException {
        arrayOfCells = new ArrayList<>(num);
        if (num == victoryPoints.size()) {
            IntStream.range(0, num).forEach(n -> arrayOfCells.add(n, new Cell(n, victoryPoints.get(n))));
        } else {
            throw new IllegalArgumentException("length of faithTrack doesn't coincide with victoryPoint array length");
        }

        effects.forEach(e -> {
            if (e.getIndex() > arrayOfCells.size())
                throw new IllegalArgumentException("cell index is out of array length");
            arrayOfCells.set(e.getIndex(), e);
        });
    }

    /**
     * Constructor
     *
     * @param arrayOfCells The complete array of cells, which might be of type Cell, PopeCell, LastCell.
     * @throws IllegalArgumentException If the index of one cell in the array doesn't coincide with private attribute index of the given cell.
     */
    private FaithTrack(ArrayList<AbstractCell> arrayOfCells) throws IllegalArgumentException {
        for (int i = 0; i < arrayOfCells.size(); i++) {
            if (arrayOfCells.get(i).getIndex() != i)
                throw new IllegalArgumentException("The cell with index " + i + " is in the wrong position");
        }

        FaithTrack.arrayOfCells = arrayOfCells;
    }


    /**
     * @param num           length of faithTrack is equal to the number of cells
     * @param effects       effects to initialize special cells
     * @param victoryPoints array of points to initialize each cell
     * @return the instance of faithTrack
     */
    @SuppressWarnings("InstantiationOfUtilityClass")
    public static FaithTrack initFaithTrack(int num, ArrayList<CellWithEffect> effects, ArrayList<Integer> victoryPoints) throws IllegalArgumentException {
        if (instance == null) instance = new FaithTrack(num, effects, victoryPoints);
        return instance;
    }

    /**
     * @param arrayOfCells The complete array of cells, which might be of type Cell, PopeCell, LastCell.
     * @return the only instance of FaithTrack
     * @throws IllegalArgumentException If the index of one cell in the array doesn't coincide with private attribute index of the given cell.
     */
    public static FaithTrack initFaithTrack(ArrayList<AbstractCell> arrayOfCells) throws IllegalArgumentException {
        if (instance == null) //noinspection InstantiationOfUtilityClass
            instance = new FaithTrack(arrayOfCells);
        return instance;
    }

    /**
     * the length of the faithTrack
     *
     * @return the length of faithTrack
     */
    public static int size() {
        return arrayOfCells.size();
    }


    /**
     * Getter of array of Cells of FaithTrack
     *
     * @return array of Cells of FaithTrack
     */
    public static ArrayList<AbstractCell> getArrayOfCells() {
        return new ArrayList<>(arrayOfCells);
    }

    /**
     * method only used for testing
     */
    public static void resetForTest(){
        instance=null;
    }

}