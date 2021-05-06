package it.polimi.ingsw.model.FaithTrack;

import it.polimi.ingsw.controller.EffectOfCell;
import it.polimi.ingsw.model.MatchState;

public abstract class CellWithEffect extends AbstractCell {
    protected AbstractCell cell;
    private boolean effectDone= false;
    protected EffectOfCell effect;

    /**
     * Getter
     * @return effect of cell
     */
    public EffectOfCell getEffectOfCell() {
        return effect;
    }

    public int getIndex(){
        return cell.getIndex();
    }

    public int getVictoryPoints(){
        return  cell.getVictoryPoints();
    }


    @Override
    public void activateEffect( MatchState matchState){
        if (!effectDone && effect!= null) {
            effect.activate(this, matchState);
            cell.activateEffect(matchState);
            effectDone=true;
        }

    }

}
