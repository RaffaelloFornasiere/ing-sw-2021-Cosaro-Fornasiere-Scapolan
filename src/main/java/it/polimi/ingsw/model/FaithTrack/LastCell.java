package it.polimi.ingsw.model.FaithTrack;

import it.polimi.ingsw.model.MatchState;

public class LastCell extends CellWithEffect {

    public LastCell(AbstractCell cell){
        this.cell=cell;
    }

    @Override
    public void activateEffect(MatchState matchState) {
        super.activateEffect(matchState);
    }
}