package it.polimi.ingsw.model.FaithTrack;

import junit.framework.TestCase;

public class CellWithEffectTest extends TestCase {

    public void testGetEffectOfCell() {
        CellWithEffect cell = new PopeCell(3, 2, new PopeFavorCard(2),3);
        assertEquals(1,cell.getEffectOfCell().size());
        cell.getEffectOfCell().stream().forEach(effect-> assertEquals("it.polimi.ingsw.controller.EffectOfPopeCell",effect.getClass().getName()));

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