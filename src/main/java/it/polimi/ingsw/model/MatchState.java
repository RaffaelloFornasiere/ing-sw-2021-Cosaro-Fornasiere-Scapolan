package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.NotPresentException;
import it.polimi.ingsw.model.DevCards.DevCard;
import it.polimi.ingsw.model.DevCards.DevCardGrid;
import it.polimi.ingsw.utilities.Observable;

import java.util.ArrayList;
import java.util.HashMap;

public class MatchState extends Observable {
    private  int currentPlayerIndex;
    private  boolean lastRound;
    private  final ArrayList<Player> players;
    private  final DevCardGrid devCardGrid;
    private  final Market market;
    private TurnState turnState;
    public boolean leaderActionExecuted;

    /**
     * Constructor for the class
     * @param players The IDs of the players taking part in the match in turn order
     * @param cards The development cards that can be bought during the match
     * @param marketRow The number of rows for the market
     * @param marketColumns The number of columns for the market
     * @param marbles The marbles for the market, they must be in number (marketRows*marketColumns)+1
     */
    public MatchState( ArrayList<Player> players, ArrayList<DevCard> cards, int marketRow, int marketColumns, HashMap<Marble, Integer> marbles) {
        devCardGrid = new DevCardGrid(cards);
        market = new Market(marketRow,marketColumns, marbles);
        market.shuffleMarket();
        this.players= new ArrayList<>(players);
        lastRound=false;
        currentPlayerIndex=0;
        turnState = TurnState.WAITING_FOR_PLAYER;
        savedTurnState = turnState;
        leaderActionExecuted = false;
    }

    /**
     * Getter for the index of the player currently taking it's turn
     * @return The index of the player currently taking it's turn
     */
    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    /**
     * @return Whether it's the last round
     */
    public boolean isLastRound() {
        return lastRound;
    }

    /**
     * Getter for the players taking part in the match in turn order
     * @return The players taking part in the match in turn order
     */
    public ArrayList<Player> getPlayers(){
        return new ArrayList<>(players);
    }

    /**
     * Getter for the player object given it's ID
     * @param playerID The ID of the player
     * @return The player with the given ID
     * @throws NotPresentException If the player ID given does not belong to any of the players in the match
     */
    public Player getPlayerFromID(String playerID) throws NotPresentException {
        for (Player p: players)
            if(p.getPlayerId().equals(playerID))
                return p;

        throw new NotPresentException("The player with id "+playerID+" is not in this match");
    }

    /**
     * Gets the position in the turn order (starting from 0) of a player
     * @param player The player for which to search the position
     * @return The position of the player
     * @throws NotPresentException If the player given does not belong in the match
     */
    public int getPlayerPosition(Player player) throws NotPresentException {
        for (int i = 0, playersSize = players.size(); i < playersSize; i++) {
            if (player == players.get(i))
                return i;
        }

        throw new NotPresentException("The player is not in this match");
    }

    /**
     * Getter for the grid of development cards associated to the match
     * @return The grid of development cards associated to the match
     */
    public DevCardGrid getDevCardGrid() {
        return devCardGrid;
    }

    /**
     * Getter for the market associated to the match
     * @return The market associated to the match
     */
    public Market getMarket() {
        return market;
    }

    /**
     * Makes the match start
     */
    public void beginMatch(){
        turnState = TurnState.START;
        notifyObservers();
    }

    /**
     * Passes to the next turn
     */
    public void nextTurn(){
        turnState = TurnState.START;
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        leaderActionExecuted = false;
        notifyObservers();
    }

    /**
     * Getter for the turn state
     * @return The turn state
     */
    public TurnState getTurnState() {
        return turnState;
    }

    /**
     * Setter for the turn state
     * @param turnState The turn state
     */
    public void setTurnState(TurnState turnState) {
        this.turnState = turnState;
        notifyObservers();
    }

    /**
     * Sets the turn state to the one where the match is waiting for something to happen
     */
    public void setWaitingForSomething(){
        savedTurnState = turnState;
        this.turnState = TurnState.WAITING_FOR_SOMETHING;
    }

    private TurnState savedTurnState;

    /**
     * Signals the arrival of something the match was waiting for
     */
    public void somethingArrived(){
        turnState = savedTurnState;
    }

    /**
     * Signals that it is the last round of turn
     */
    public void setLastRound(){
        this.lastRound = true;
    }
}
