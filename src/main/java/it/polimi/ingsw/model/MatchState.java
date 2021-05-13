package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.NotPresentException;
import it.polimi.ingsw.model.DevCards.DevCard;
import it.polimi.ingsw.model.DevCards.DevCardGrid;

import java.util.ArrayList;
import java.util.HashMap;

public class MatchState {
    private  int currentPlayerIndex;
    private  boolean lastRound;
    private  ArrayList<Player> players;
    private  DevCardGrid devCardGrid;
    private  Market market;

    public MatchState( ArrayList<Player> players, ArrayList<DevCard> cards, int marketRow, int marketColumns, HashMap<Marble, Integer> marbles) {
        devCardGrid = new DevCardGrid(cards);

        market = new Market(marketRow,marketColumns, marbles);
        market.shuffleMarket();
        this.players= (ArrayList<Player>) players.clone();
        lastRound=false;
        currentPlayerIndex=0;

    }

    public  ArrayList<Player> getPlayers(){
        return  (ArrayList<Player>) players.clone();
    }

    public Player getPlayerFromID(String playerID) throws NotPresentException {
        for (Player p: players)
            if(p.getPlayerId()==playerID)
                return p;

        throw new NotPresentException("The player with id "+playerID+" is not in this match");
    }

    public Market getMarket() {
        return market;
    }
}
