package it.polimi.ingsw.model.FaithTrack;

import it.polimi.ingsw.model.MatchState;

public class LastCell extends CellWithEffect {
    /**
     * constructor
     *
     * @param cell The cell which extends the properties of LastCell(decorator),might
     *             be a plain simple Cell, or some other cell with effect, which is summed up with effect of last cell
     */
    public LastCell(AbstractCell cell) {
        this.cell = cell;
    }

    /**
     * @param matchState The state of the match from which method activateEffect() gets information about other players' state
     */
    @Override
    public void activateEffect(MatchState matchState) {
        super.activateEffect(matchState);
    }
}