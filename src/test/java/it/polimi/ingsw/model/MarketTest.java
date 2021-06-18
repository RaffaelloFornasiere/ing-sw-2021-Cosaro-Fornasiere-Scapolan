package it.polimi.ingsw.model;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.IntStream;

import static org.junit.Assert.*;

//import javassist.ClassPath;

public class MarketTest {
    private final HashMap<Marble, Integer> marbles = new HashMap<>() {{
        put(Marble.GRAY, 2);
        put(Marble.YELLOW, 2);
        put(Marble.PURPLE, 2);
        put(Marble.BLUE, 2);
        put(Marble.WHITE, 4);
        put(Marble.RED, 1);
    }};
    @Test
    public void marketConstructorTest() {
        HashMap<Marble, Integer> marbles = new HashMap<>() {{
            put(Marble.GRAY, 2);
            put(Marble.YELLOW, 2);
        }};
        try {
            Market market = new Market(2, 3, marbles);
            fail();
        } catch (IllegalArgumentException ignored) {

        }
    }

    @Test
    public void shuffleMarketTest() {

        Market unShuffled = new Market(4, 3, marbles);
        Market shuffled = new Market(4, 3, marbles);

        shuffled.shuffleMarket();
        assertNotEquals(shuffled.getMarketStatus(), unShuffled.getMarketStatus());
    }

    @Test
    public void getMarblesTest() {

        Market market = new Market(4, 3, marbles);
        market.shuffleMarket();

        int index = 2;
        Direction direction = Direction.ROW;
        var row = market.getMarbles(direction, index);
        var row2 = new HashMap<Marble, Integer>();
        var grid = market.getMarketStatus();
        int len = grid[index].length;
        int i = index, j = 0,
                di = 0, dj = 1;
        for (; i < len && j < len; i += di, j += dj)
            row2.put(grid[i][j], row2.containsKey(grid[i][j]) ? row2.get(grid[i][j]) + 1 : 1);
        assertEquals(row, row2);


        index = 1;
        direction = Direction.COLUMN;
        row2.clear();
        row = market.getMarbles(direction, index);
        len = grid.length;
        i = 0;
        j = index;
        di = 1;
        dj = 0;

        for (; i < len && j < len; i += di, j += dj)
            row2.put(grid[i][j], row2.containsKey(grid[i][j]) ? row2.get(grid[i][j]) + 1 : 1);

        assertEquals(row, row2);

    }

    @Test
    public void updateTest() {

        Market market = new Market(4, 3, marbles);
        market.shuffleMarket();

        int index = 2;
        Direction direction = Direction.ROW;

        var list = new ArrayList<>(Arrays.asList(market.getMarketStatus()[index]));
        list.remove(0);
        list.add(market.getMarbleLeft());

        market.update(direction, index);
        var row = market.getMarbles(direction, index);

        HashMap<Marble, Integer> row2 = new HashMap<>();
        list.forEach(i -> row2.put(i, row2.containsKey(i) ? (row2.get(i) + 1) : 1));

        assertEquals(row, row2);
    }


    @Test
    public void getMarketStatus() {

        ArrayList<Marble> marblesArray = new ArrayList<Marble>() {{
            marbles.forEach((key, value) ->
                    IntStream.range(0, value)
                            .forEach(n -> add(key))
            );
        }};
        int rows = 4; int columns = 3;
        Marble[][] grid = new Marble[rows][columns];
        //assign the marbles to the market's grid
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < columns; j++)
                grid[i][j] = marblesArray.get(i * columns + j);
        Market market = new Market(4, 3, marbles);
        Assert.assertArrayEquals(grid, market.getMarketStatus());

    }

    @Test
    public void getMarbleLeft() {
        Market market = new Market(4, 3, marbles);
        Assert.assertEquals(market.getMarbleLeft(), Marble.BLUE);
    }

    @Test
    public void getRowsCols() {
        Market market = new Market(4, 3, marbles);
        Assert.assertEquals(market.getCols(), 3);
        Assert.assertEquals(market.getRows(), 4);

    }

}
