package it.polimi.ingsw.model.singlePlayer;

import it.polimi.ingsw.exceptions.IllegalOperation;
import it.polimi.ingsw.model.devCards.DevCard;
import it.polimi.ingsw.model.faithTrack.FaithTrack;
import it.polimi.ingsw.model.Marble;
import it.polimi.ingsw.model.MatchState;
import it.polimi.ingsw.model.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Class representing the state of a match in singleplayer
 */
public class SinglePlayerMatchState extends MatchState {
    private int lorenzoPosition;
    private final ArrayList<SoloActionToken> soloActionTokens;
    private int currentToken;

    /**
     * Constructor for the class
     * @param player The human player taking part in the match
     * @param cards The development cards that will be in th gri d at the start of the match
     * @param marketRow The number of rows for the market
     * @param marketColumns The number of columns for the market
     * @param marbles The marbles for the market, they must be in number (marketRows*marketColumns)+1
     * @param soloActionTokens The tokens for the actions hat Lorenzo il Magnifico can make in this match
     */
    public SinglePlayerMatchState(Player player, ArrayList<DevCard> cards, int marketRow, int marketColumns, HashMap<Marble, Integer> marbles, ArrayList<SoloActionToken> soloActionTokens) {
        super(new ArrayList<>(){{add(player);}}, cards, marketRow, marketColumns, marbles);
        lorenzoPosition = 0;
        this.soloActionTokens = new ArrayList<>(soloActionTokens);
        currentToken = 0;
    }

    /**
     * Getter for the human player taking part in the match
     * @return The human player taking part in the match
     */
    public Player getPlayer(){
        return getPlayers().get(0);
    }

    /**
     * Getter for the position of Lorenzo il Magnifico in the faith track
     * @return The position of Lorenzo il Magnifico in the faith track
     */
    public int getLorenzoPosition() {
        return lorenzoPosition;
    }

    /**
     * Getter for the next token that will be drawn
     * @return The next token that will be drawn
     * @throws IllegalOperation If there are no more tokens
     */
    public SoloActionToken topSoloActionTokens() throws IllegalOperation {
        if (currentToken >= soloActionTokens.size()) throw new IllegalOperation("Token pile empty");
        return soloActionTokens.get(currentToken);
    }

    /**
     * Draws the next solo action token
     * @return The token drawn
     * @throws IllegalOperation If there are no more tokens
     */
    public SoloActionToken popSoloActionTokens() throws IllegalOperation {
        if(currentToken>=soloActionTokens.size()) throw new IllegalOperation("Token pile empty");
        currentToken++;
        return soloActionTokens.get(currentToken-1);
    }

    /**
     * Method that reshuffles the tokens in the pile
     */
    public void shuffleToken(){
        Collections.shuffle(soloActionTokens);
        currentToken = 0;
    }

    /**
     * Move Lorenzo il Magnifico along the faith track
     * @param steps the number of positions Lorenzo moves
     */
    public void incrementLorenzoPosition(int steps){
        lorenzoPosition = Math.min(lorenzoPosition+steps, FaithTrack.getArrayOfCells().size()-1);
    }
}
