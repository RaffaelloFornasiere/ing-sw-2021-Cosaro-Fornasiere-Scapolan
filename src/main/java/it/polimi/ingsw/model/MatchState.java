package it.polimi.ingsw.model;

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

    //constructor from JSON?
    public MatchState( ArrayList<Player> players, ArrayList<DevCard> cards) {
        //get data from JSON
        devCardGrid = new DevCardGrid(cards);

        HashMap<Marble, Integer> marbles = new HashMap<Marble, Integer>() {{
            put(Marble.WHITE, 4);
            put(Marble.BLUE, 2);
            put(Marble.GRAY, 2);
            put(Marble.YELLOW, 2);
            put(Marble.PURPLE, 2);
            put(Marble.RED, 1);
        }};
        market = new Market(3,4, marbles);
        this.players= players;
        lastRound=false;
        currentPlayerIndex=0;

    }

    public MatchState(){};

    public  ArrayList<Player> getPlayers(){
        return  (ArrayList<Player>) players.clone();
    }
}
