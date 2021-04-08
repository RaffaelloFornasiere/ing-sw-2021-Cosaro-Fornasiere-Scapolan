package it.polimi.ingsw.model;

import java.util.*;
import java.util.stream.IntStream;

public class Market {
    private Marbel marbelLeft;
    private Marbel[][] grid;

    /**
     * Constructor
     *
     * @param rows    number of rows in market's grid
     * @param colums  number of columns in market's grid
     * @param marbels hashmap with the available marbels
     */
    public Market(int rows, int colums, HashMap<Marbel, Integer> marbels) {
        if (marbels.entrySet().stream().mapToInt(Map.Entry::getValue).reduce(0, Integer::sum)
                != rows * colums + 1) ;
        grid = new Marbel[rows][colums];
        //throw some exception

        //converts the hash to a list with all marbels
        ArrayList<Marbel> marbelArrayList = new ArrayList<>();
        marbels.entrySet().forEach(entry ->
                IntStream.range(0, entry.getValue())
                        .forEach(n -> marbelArrayList.add(entry.getKey()))
        );
        //shuffle the marbels to make the market random
        Collections.shuffle(marbelArrayList);

        //assign the marbels to the market's grid
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < colums; j++)
                grid[i][j] = marbelArrayList.get(i * colums + j);

        // assign the left marbel
        marbelLeft = marbelArrayList.get(marbelArrayList.size() - 1);
    }


    /**
     * @param direction selects row or column
     * @param index     selects a specific row or column
     * @return the set of marbels obtained with the selected row or column
     */
    public HashMap<Marbel, Integer> getMarbels(Direction direction, int index) {
        HashMap<Marbel, Integer> res = new HashMap<>();
        if (direction == Direction.ROW)
            for (int i = 0; i < grid[index].length; i++)
                res.put(grid[index][i], res.containsKey(grid[index][i]) ? res.get(grid[index][i]) + 1 : 1);
        else
            for (Marbel[] marbels : grid)
                res.put(marbels[index], res.containsKey(marbels[index]) ? res.get(marbels[index]) + 1 : 1);
        return res;
    }

    /**
     * Updates the market adding the left marbel on the row or column specified
     * and take apart the first marbel that becomes the new left marbel
     *
     * @param direction direction (row or column) to update
     * @param index     specifies the row/column to update
     */
    public void Update(Direction direction, int index) {
        Marbel aux;
        if (direction == Direction.ROW) {
            aux = grid[index][0];
            System.arraycopy(grid[index], 1, grid[index], 0, grid[index].length - 1);
            grid[index][grid[index].length - 1] = marbelLeft;
        } else {
            aux = grid[0][index];
            for (int i = 0; i < grid[index].length - 1; i++)
                grid[i][index] = grid[i + 1][index];
            grid[grid.length - 1][index] = marbelLeft;
        }
        marbelLeft = aux;

    }
}