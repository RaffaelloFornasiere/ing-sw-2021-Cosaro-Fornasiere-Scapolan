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
    private  ArrayList<Player> players;
    private  DevCardGrid devCardGrid;
    private  Market market;
    private TurnState turnState;
    public boolean leaderActionExecuted;

    public MatchState( ArrayList<Player> players, ArrayList<DevCard> cards){
        devCardGrid = new DevCardGrid(cards);
        this.players= (ArrayList<Player>) players.clone();
        turnState = TurnState.WAITING_FOR_PLAYER;
    }

    public MatchState( ArrayList<Player> players, ArrayList<DevCard> cards, int marketRow, int marketColumns, HashMap<Marble, Integer> marbles) {
        devCardGrid = new DevCardGrid(cards);

        market = new Market(marketRow,marketColumns, marbles);
        market.shuffleMarket();
        this.players= (ArrayList<Player>) players.clone();
        lastRound=false;
        currentPlayerIndex=0;
        turnState = TurnState.WAITING_FOR_PLAYER;
        leaderActionExecuted = false;
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    public boolean isLastRound() {
        return lastRound;
    }

    public  ArrayList<Player> getPlayers(){
        return  (ArrayList<Player>) players.clone();
    }

    public Player getPlayerFromID(String playerID) throws NotPresentException {
        for (Player p: players)
            if(p.getPlayerId().equals(playerID))
                return p;

        throw new NotPresentException("The player with id "+playerID+" is not in this match");
    }

    public int getPlayerPosition(Player player) throws NotPresentException {
        for (int i = 0, playersSize = players.size(); i < playersSize; i++) {
            if (player == players.get(i))
                return i;
        }

        throw new NotPresentException("The player is not in this match");
    }

    public DevCardGrid getDevCardGrid() {
        return devCardGrid;
    }

    public Market getMarket() {
        return market;
    }

    public void beginMatch(){
        turnState = TurnState.START;
        notifyObservers();
    }

    public void nextTurn(){
        turnState = TurnState.START;
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        leaderActionExecuted = false;
        notifyObservers();
    }

    public TurnState getTurnState() {
        return turnState;
    }

    public void setTurnState(TurnState turnState) {
        this.turnState = turnState;
        notifyObservers();
    }
}
