package it.polimi.ingsw.model;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Market {

    private Marble marbleLeft;
    private Marble[][] grid;
    private int rows;
    private int cols;

    /**
     * Constructor
     *
     * @param rows    number of rows in market's grid
     * @param colums  number of columns in market's grid
     * @param marbles hashmap with the available marbles
     */
    public Market(int rows, int colums, HashMap<Marble, Integer> marbles) {
        if (marbles.entrySet().stream().mapToInt(Map.Entry::getValue).reduce(0, Integer::sum)
                != rows * colums + 1)
            throw new IllegalArgumentException("number of marbles is not compatible with grid size");
        this.rows = rows;
        this.cols = colums;
        grid = new Marble[rows][colums];


        //converts the hash to a list with all marbles
        ArrayList<Marble> marbleArrayList = new ArrayList<>();
        marbles.entrySet().forEach(entry ->
                IntStream.range(0, entry.getValue())
                        .forEach(n -> marbleArrayList.add(entry.getKey()))
        );

        //assign the marbles to the market's grid
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < colums; j++)
                grid[i][j] = marbleArrayList.get(i * colums + j);

        // assign the left marble
        marbleLeft = marbleArrayList.get(marbleArrayList.size() - 1);
    }

    public void shuffleMarket()
    {
        ArrayList<Marble> marbleArrayList = new ArrayList<>();
        Arrays.stream(grid).forEach(x -> Arrays.stream(x).forEach(y -> marbleArrayList.add(y)));
        marbleArrayList.add(marbleLeft);
        Collections.shuffle(marbleArrayList);
        //assign the marbles to the market's grid
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                grid[i][j] = marbleArrayList.get(i * cols + j);

        // assign the left marble
        marbleLeft = marbleArrayList.get(marbleArrayList.size() - 1);
    }

    /**
     * @param direction selects row or column
     * @param index     selects a specific row or column
     * @return the set of marbles obtained with the selected row or column
     */
    public HashMap<Marble, Integer> getMarbles(Direction direction, int index) {
        HashMap<Marble, Integer> res = new HashMap<>();
        if (direction == Direction.ROW)
            for (int i = 0; i < grid[index].length; i++)
                res.put(grid[index][i], res.containsKey(grid[index][i]) ? res.get(grid[index][i]) + 1 : 1);
        else
            for (Marble[] marbles : grid)
                res.put(marbles[index], res.containsKey(marbles[index]) ? res.get(marbles[index]) + 1 : 1);
        return res;
    }

    /**
     * Updates the market adding the left marble on the row or column specified
     * and take apart the first marble that becomes the new left marble
     *
     * @param direction direction (row or column) to update
     * @param index     specifies the row/column to update
     */
    public void Update(Direction direction, int index) {
        Marble aux;
        if (direction == Direction.ROW) {
            aux = grid[index][0];
            System.arraycopy(grid[index], 1, grid[index], 0, grid[index].length - 1);
            grid[index][grid[index].length - 1] = marbleLeft;
        } else {
            aux = grid[0][index];
            for (int i = 0; i < grid[index].length - 1; i++)
                grid[i][index] = grid[i + 1][index];
            grid[grid.length - 1][index] = marbleLeft;
        }
        marbleLeft = aux;
    }

    public Marble[][] getMarketStatus()
    {
        return grid.clone();
    }

    public Marble getMarbleLeft() {
        return marbleLeft;
    }

}