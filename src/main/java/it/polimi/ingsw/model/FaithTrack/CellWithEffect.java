package it.polimi.ingsw.model.FaithTrack;

public class CellWithEffect extends Cell {
    EffectOfCell effect;
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
