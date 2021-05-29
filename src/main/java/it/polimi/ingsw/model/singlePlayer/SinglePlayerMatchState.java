package it.polimi.ingsw.model.singlePlayer;

import it.polimi.ingsw.exceptions.IllegalOperation;
import it.polimi.ingsw.exceptions.OutOfBoundException;
import it.polimi.ingsw.model.DevCards.DevCard;
import it.polimi.ingsw.model.FaithTrack.FaithTrack;
import it.polimi.ingsw.model.Marble;
import it.polimi.ingsw.model.MatchState;
import it.polimi.ingsw.model.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class SinglePlayerMatchState extends MatchState {
    private int lorenzoPosition;
    private ArrayList<SoloActionToken> soloActionTokens;
    private int currentToken;

    public SinglePlayerMatchState(ArrayList<Player> players, ArrayList<DevCard> cards, int marketRow, int marketColumns, HashMap<Marble, Integer> marbles, ArrayList<SoloActionToken> soloActionTokens) {
        super(players, cards, marketRow, marketColumns, marbles);
        if(players.size()!=1) throw new IllegalArgumentException("There must be only one player to play singleplayer");
        lorenzoPosition = 0;
        this.soloActionTokens = (ArrayList<SoloActionToken>) soloActionTokens.clone();
        currentToken = 0;
    }

    public Player getPlayer(){
        return getPlayers().get(0);
    }

    public int getLorenzoPosition() {
        return lorenzoPosition;
    }

    public SoloActionToken topSoloActionTokens() throws IllegalOperation {
        if(currentToken>=soloActionTokens.size()) throw new IllegalOperation("Token pile empty");
        return soloActionTokens.get(currentToken);
    }

    public SoloActionToken popSoloActionTokens() throws IllegalOperation {
        if(currentToken>=soloActionTokens.size()) throw new IllegalOperation("Token pile empty");
        currentToken++;
        return soloActionTokens.get(currentToken-1);
    }

    public void shuffleToken(){
        Collections.shuffle(soloActionTokens);
        currentToken = 0;
    }

    public void incrementLorenzoPosition(int steps){
        lorenzoPosition = Math.min(lorenzoPosition+steps, FaithTrack.getArrayOfCells().size()-1);
    }
}
