package it.polimi.ingsw.model.FaithTrack;

import it.polimi.ingsw.controller.EffectOfCell;

public class CellWithEffect extends Cell {
    final EffectOfCell effect;
    /**
     * @Constructor
     */
    public  CellWithEffect(int index, int vPoints, EffectOfCell effect){
        super(index, vPoints);
        this.effect=effect;
    }

    public void registerEffect(){

    }

    public void unregisterEffect(){

    }
}
