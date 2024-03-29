package it.polimi.ingsw.model.faithTrack;

import it.polimi.ingsw.model.MatchState;

public abstract class AbstractCell{
    public abstract void activateEffect(MatchState matchState);
    public abstract int getIndex();
    public abstract int  getVictoryPoints();

}