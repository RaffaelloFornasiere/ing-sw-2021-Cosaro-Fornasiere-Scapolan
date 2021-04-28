package it.polimi.ingsw.model;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static org.junit.Assert.*;

public class MarketTest {

    @Test
    public void marketConstructorTest()
    {
        HashMap<Marble, Integer> marbles = new HashMap<>(){{
            put(Marble.GRAY, 2);
            put(Marble.YELLOW, 2);
        }};
        try {
            Market market = new Market (2,3, marbles);
            fail();
        }
        catch (IllegalArgumentException e){}
    }

    @Test
    public void shuffleMarketTest() {
        HashMap<Marble, Integer> marbles = new HashMap<>(){{
            put(Marble.GRAY, 2);
            put(Marble.YELLOW, 2);
            put(Marble.PURPLE, 2);
            put(Marble.BLUE, 2);
            put(Marble.WHITE, 4);
            put(Marble.RED, 1);
        }};
        Market unShuffled = new Market(4,3, marbles);
        Market shuffled = new Market(4,3, marbles);

        shuffled.shuffleMarket();
        assertFalse(shuffled.getMarketStatus().equals(unShuffled.getMarketStatus()));
    }

    @Test
    public void getMarblesTest() {
        HashMap<Marble, Integer> marbles = new HashMap<>(){{
            put(Marble.GRAY, 2);
            put(Marble.YELLOW, 2);
            put(Marble.PURPLE, 2);
            put(Marble.BLUE, 2);
            put(Marble.WHITE, 4);
            put(Marble.RED, 1);
        }};
        Market market = new Market(4,3, marbles);
        market.shuffleMarket();

        int index = 2;
        Direction direction = Direction.ROW;
        var row = market.getMarbles(direction, index);
        var row2 = new HashMap<Marble, Integer>();
        var grid = market.getMarketStatus();
        int len = (direction == Direction.ROW) ? grid[index].length : grid.length;
        int i = (direction == Direction.ROW) ? index : 0, j = (direction == Direction.ROW) ? 0 : index,
                di = (direction == Direction.ROW) ? 0 : 1, dj = (direction == Direction.ROW) ? 1 : 0;
        for (; i < len && j < len; i += di, j += dj)
            row2.put(grid[i][j], row2.containsKey(grid[i][j]) ? row2.get(grid[i][j]) + 1 : 1);
        assertEquals(row, row2);


        index = 1;
        direction = Direction.COLUMN;
        row2.clear();
        row = market.getMarbles(direction, index);
        len = (direction == Direction.ROW) ? grid[index].length : grid.length;
        i = index;
        j = (direction == Direction.ROW) ? 0 : index;
        di = (direction == Direction.ROW) ? 0 : 1;
        dj = (direction == Direction.ROW) ? 1 : 0;

        for (; i < len && j < len; i += di, j += dj)
            row2.put(grid[i][j], row2.containsKey(grid[i][j]) ? row2.get(grid[i][j]) + 1 : 1);
        assertEquals(row, row2);

    }

    @Test
    public void updateTest() {
        HashMap<Marble, Integer> marbles = new HashMap<>(){{
            put(Marble.GRAY, 2);
            put(Marble.YELLOW, 2);
            put(Marble.PURPLE, 2);
            put(Marble.BLUE, 2);
            put(Marble.WHITE, 4);
            put(Marble.RED, 1);
        }};
        Market market = new Market(4,3, marbles);
        market.shuffleMarket();

        int index = 2;
        Direction direction = Direction.ROW;

        var list = new ArrayList<>(Arrays.asList(market.getMarketStatus()[index]));
        list.remove(0);
        list.add(market.getMarbleLeft());

        market.Update(direction, index);
        var row = market.getMarbles(direction, index);

        HashMap<Marble, Integer> row2 = new HashMap<>();
        list.forEach(i -> row2.put(i, row2.containsKey(i)?(row2.get(i)+1):1));

        assertEquals(row, row2);

    }
}