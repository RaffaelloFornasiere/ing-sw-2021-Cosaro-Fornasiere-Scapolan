package it.polimi.ingsw.events.clientEvents;

import it.polimi.ingsw.model.Marble;

/**
 * Event signaling a change of state of the market
 */
public class MarketStateEvent extends ClientEvent{
    private final Marble marbleLeft;
    private final Marble[][] grid;
    private final int rows;
    private final int cols;

    /**
     * Constructor for the class
     * @param playerId The ID of the player that triggered the change in the market
     * @param marbleLeft The marble out of the market
     * @param grid The grid of marbles representing the market
     * @param rows The number of rows in the market
     * @param cols The number of columns in the market
     */
    public MarketStateEvent(String playerId, Marble marbleLeft, Marble[][] grid, int rows, int cols) {
        super(playerId);
        this.marbleLeft = marbleLeft;
        this.grid = grid.clone();
        this.rows = rows;
        this.cols = cols;
    }

    /**
     * Getter for the marble out of the market
     * @return The marble out of the market
     */
    public Marble[][] getMarketStatus() {
        return grid.clone();
    }

    /**
     * Getter for the grid of marbles representing the market
     * @return The grid of marbles representing the market
     */
    public Marble getMarbleLeft() {
        return marbleLeft;
    }

    /**
     * Getter for the number of rows in the market
     * @return The number of rows in the market
     */
    public int getRows() {
        return rows;
    }

    /**
     * Getter for the number of columns in the market
     * @return The number of columns in the market
     */
    public int getCols() {
        return cols;
    }
}
