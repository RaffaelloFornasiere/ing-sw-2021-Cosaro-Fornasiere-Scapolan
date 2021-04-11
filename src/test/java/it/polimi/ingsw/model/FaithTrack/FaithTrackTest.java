package it.polimi.ingsw.model.FaithTrack;

import it.polimi.ingsw.controller.EffectOfCell;
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
            ft = FaithTrack.initFaithTrack(4, new HashMap<Integer, EffectOfCell>(), a);
        } catch (IndexOutOfBoundsException e) {}
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
         ft = FaithTrack.initFaithTrack(4, new HashMap<Integer, EffectOfCell>(), a);
         fail();
        } catch(IndexOutOfBoundsException e){}
    }
}