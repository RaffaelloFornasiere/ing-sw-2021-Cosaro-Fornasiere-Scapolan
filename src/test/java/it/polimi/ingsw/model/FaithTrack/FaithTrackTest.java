package it.polimi.ingsw.model.FaithTrack;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.HashMap;

public class FaithTrackTest extends TestCase {
    /**
     * This method tests the correctness of initializing the singleton FaithTrack
     */
    public void testInitFaithTrackSuccessful() {
        try {
            FaithTrack ft;
            ArrayList<Integer> a = new ArrayList<Integer>(4);
            a.add(1);
            a.add(2);
            a.add(3);
            a.add(4);
            assertEquals( 4,a.size());
            ArrayList<Integer> b= new ArrayList<>(5);
            b.addAll(a);
            b.add(5);
            assertEquals( 5,b.size());
            ft = new FaithTrack(4, new ArrayList<CellWithEffect>(), a);
            assertEquals( 4,ft.size());
            assertEquals( 1,ft.getArrayOfCells().get(0).getVictoryPoints());
            assertEquals( 2,ft.getArrayOfCells().get(1).getVictoryPoints());
            assertEquals( 3,ft.getArrayOfCells().get(2).getVictoryPoints());
            assertEquals( 4,ft.getArrayOfCells().get(3).getVictoryPoints());
            assertEquals("it.polimi.ingsw.model.FaithTrack.LastCell", FaithTrack.getArrayOfCells().get(ft.size()-1).getClass().getName());
        } catch (IllegalArgumentException e) {fail();}


    }

    /**
     * This method tests the correctness of initializing the singleton FaithTrack, in case a wrong parameter is given
     * ( the length of the array of points for the cells must be equal to the length of the faith track)
     */

    public void testInitFaithTrackUnsuccessful() {
        try {
            FaithTrack ft;
            ArrayList<Integer> a = new ArrayList<Integer>(4);
            a.add(1);
            a.add(2);
            a.add(3);
            a.add(4);
            assertEquals(4,a.size());
            ft = new FaithTrack(5, new ArrayList<CellWithEffect>(), a);
            fail();
        } catch(IllegalArgumentException e){}
    }

    public void testInitFaithTrackMoreUnsuccessful() {
        try {
            FaithTrack ft;
            ArrayList<Integer> a = new ArrayList<Integer>(4);
            a.add(1);
            a.add(2);
            a.add(3);
            a.add(4);
            assertEquals(4,a.size());
            // the array of cells of the faithTrack is 4 cells long.
            // Here the PopeCell should be put at cell 5, which doens't exsist
            PopeCell popeCell= new PopeCell(5,2,new PopeFavorCard(3),3);
            ArrayList<CellWithEffect> array = new ArrayList<>();
            array.add(popeCell);
            ft = new FaithTrack(4, array, a);
            fail();
        } catch(IllegalArgumentException e){}
    }
}