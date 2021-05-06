package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.FaithTrack.CellWithEffect;
import it.polimi.ingsw.model.MatchState;

public abstract class EffectOfCell {

    public abstract void activate(CellWithEffect cell, MatchState matchState);
}