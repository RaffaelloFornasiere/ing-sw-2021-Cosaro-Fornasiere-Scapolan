package it.polimi.ingsw.model.FaithTrack;

import it.polimi.ingsw.controller.EffectOfPopeCell;
import junit.framework.TestCase;

public class CellWithEffectTest extends TestCase {

    public void testGetEffectOfCell() {
        CellWithEffect cell = new PopeCell(3, 2, new PopeFavorCard(2),3);
        assertEquals(EffectOfPopeCell.class,cell.getEffectOfCell().getClass());
    }

    public void testGetIndex() {
        CellWithEffect cell = new PopeCell(3, 2, new PopeFavorCard(2),3);
        assertEquals(3, cell.getIndex());
    }

    public void testGetVictoryPoints() {
        CellWithEffect cell = new PopeCell(3, 2, new PopeFavorCard(2),3);
        assertEquals(2, cell.getVictoryPoints());
    }


}