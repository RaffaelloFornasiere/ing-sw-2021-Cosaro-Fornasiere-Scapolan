package it.polimi.ingsw.events.ClientEvents;

import it.polimi.ingsw.model.Marble;

public class MarketStateEvent extends ClientEvent{
    private Marble marbleLeft;
    private Marble[][] grid;
    private int rows;
    private int cols;

    /**
     * constructor of the class
     *
     * @param playerId the player that generated(directly or indirectly) this event
     */
    public MarketStateEvent(String playerId) {
        super(playerId);
    }

    public MarketStateEvent(String playerId, Marble marbleLeft, Marble[][] grid, int rows, int cols) {
        super(playerId);
        this.marbleLeft = marbleLeft;
        this.grid = grid.clone();
        this.rows = rows;
        this.cols = cols;
    }

    public Marble[][] getMarketStatus() {
        return grid.clone();
    }

    public Marble getMarbleLeft() {
        return marbleLeft;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }
}
