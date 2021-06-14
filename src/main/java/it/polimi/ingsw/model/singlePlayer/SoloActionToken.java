package it.polimi.ingsw.model.singlePlayer;

/**
 * Abstract class representing one of the token dictating a possible action of Lorenzo il Magnifico
 */
public abstract class SoloActionToken {
    /**
     * Method that performs the action of the token
     * @param singlePlayerMatchState The match affected by the token
     * @return Whether the match should end after this action
     */
    public abstract boolean doAction(SinglePlayerMatchState singlePlayerMatchState);
}
