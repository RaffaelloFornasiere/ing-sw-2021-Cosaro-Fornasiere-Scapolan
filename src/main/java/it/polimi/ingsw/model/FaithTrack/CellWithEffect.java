package it.polimi.ingsw.model.FaithTrack;

import it.polimi.ingsw.controller.EffectOfCell;

public class CellWithEffect extends Cell {
    final EffectOfCell effect;

    /**
     * constructor
     * @param index index of cell
     * @param vPoints victorypoints of cell
     * @param effect effect  of cell
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
