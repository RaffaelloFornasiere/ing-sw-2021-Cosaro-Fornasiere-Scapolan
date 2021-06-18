package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.FaithTrack.CellWithEffect;
import it.polimi.ingsw.model.MatchState;

public class EffectOfLastCell extends EffectOfCell{
    /**
     * Makes the game end after a last round of turns
     * @param cell       reference of the specific cell with effect
     * @param matchState the state of the match, which gives information about all the other players
     */
    @Override
    public void activate(CellWithEffect cell, MatchState matchState) {
        matchState.setLastRound();
    }
}
