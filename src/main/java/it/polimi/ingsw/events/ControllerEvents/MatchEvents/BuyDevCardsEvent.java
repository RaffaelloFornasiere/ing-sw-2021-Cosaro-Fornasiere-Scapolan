package it.polimi.ingsw.events.ControllerEvents.MatchEvents;

public class BuyDevCardsEvent extends MatchEvent {
    /**
     *
     * @param playerID the player that generated(directly or indirectly) this event
     * @param row row of grid
     * @param column column of grid
     */
    public BuyDevCardsEvent(String playerID, int row, int column) {
        super(playerID);
        this.row = row;
        this.column = column;
    }

    private final int row;
    private final int column;

    public int getRow() {return row;}
    public int getColumn() {return column;}

    private String eventName;


    @Override
    public String getEventName() {
        return this.getClass().getName();
    }



}