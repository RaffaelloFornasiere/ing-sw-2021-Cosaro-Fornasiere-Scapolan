package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.faithTrack.CellWithEffect;
import it.polimi.ingsw.model.MatchState;

public abstract class EffectOfCell {
    /**
     * This method must be overwritten by all the subtypes of EffectOfCell.
     *
     * @param cell       reference of the specific cell with effect
     * @param matchState the state of the match, which gives information about all the other players
     */
    public abstract void activate(CellWithEffect cell, MatchState matchState);
}