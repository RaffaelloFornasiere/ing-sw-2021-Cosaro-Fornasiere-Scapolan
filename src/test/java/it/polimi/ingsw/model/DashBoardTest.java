package it.polimi.ingsw.model;

import it.polimi.ingsw.controller.EffectOfCell;
import it.polimi.ingsw.exceptions.EmptyStrongboxException;
import it.polimi.ingsw.exceptions.IndexSlotException;
import it.polimi.ingsw.exceptions.LevelCardException;
import it.polimi.ingsw.model.DevCards.DevCard;
import it.polimi.ingsw.model.FaithTrack.FaithTrack;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.HashMap;

public class DashBoardTest extends TestCase {

    /**
     * tests the successful case in which resources are added to the StrongBox
     */
    public void testAddResourcesToStrongBox() {
        //creates the array of depot capacities
        ArrayList<Integer> dcapacity = new ArrayList<>(3);
        dcapacity.add(1);
        dcapacity.add(3);
        dcapacity.add(5);
        //creates faithtrack
        FaithTrack ft;
        ArrayList<Integer> a = new ArrayList<Integer>(4);
        a.add(1);
        a.add(2);
        a.add(3);
        a.add(4);
        ft = FaithTrack.initFaithTrack(4, new HashMap<Integer, EffectOfCell>(), a);
        //creates production power
        ProductionPower p = new ProductionPower(new HashMap<Resource, Integer>(), new HashMap<Resource, Integer>());
        //create dashboard
        DashBoard d = new DashBoard(dcapacity.size(), dcapacity, p, ft);
        d.addResourcesToStrongBox(Resource.ROCK, 4);
        //checks
        assertEquals(4, (int) d.getStrongBox().get(Resource.ROCK));
        assertEquals(0, (int) d.getStrongBox().get(Resource.SERVANT));
        assertEquals(0, (int) d.getStrongBox().get(Resource.COIN));
        assertEquals(0, (int) d.getStrongBox().get(Resource.SHIELD));

        d.addResourcesToStrongBox(Resource.SHIELD, 4);
        assertEquals(4, (int) d.getStrongBox().get(Resource.SHIELD));

    }


    /**
     * tests the successful case in which resources are subtracted to the StrongBox
     */
    public void testSubResourcesToStrongBoxSuccesful(){
        //creates the array of depot capacities
        ArrayList<Integer> dcapacity = new ArrayList<>(3);
        dcapacity.add(1);
        dcapacity.add(3);
        dcapacity.add(5);
        //creates faithtrack
        FaithTrack ft;
        ArrayList<Integer> a = new ArrayList<Integer>(4);
        a.add(1);
        a.add(2);
        a.add(3);
        a.add(4);
        ft = FaithTrack.initFaithTrack(4, new HashMap<Integer, EffectOfCell>(), a);

        ProductionPower p = new ProductionPower(new HashMap<Resource, Integer>(), new HashMap<Resource, Integer>());

        DashBoard d = new DashBoard(dcapacity.size(), dcapacity, p, ft);
        d.addResourcesToStrongBox(Resource.ROCK, 4);
        assertEquals(4, (int) d.getStrongBox().get(Resource.ROCK));
        try{  d.subResourcesToStrongBox(Resource.ROCK, 2);}
        catch( EmptyStrongboxException e){fail();}
        assertEquals(2, (int) d.getStrongBox().get(Resource.ROCK));
    }
    /**
     * tests the unsuccessful case in which more resources than those available  are subtracted to the StrongBox
     */
    public void testSubResourcesToStrongBoxUnsuccesful(){
        //creates the array of depot capacities
        ArrayList<Integer> dcapacity = new ArrayList<>(3);
        dcapacity.add(1);
        dcapacity.add(3);
        dcapacity.add(5);
        //creates faithtrack
        FaithTrack ft;
        ArrayList<Integer> a = new ArrayList<Integer>(4);
        a.add(1);
        a.add(2);
        a.add(3);
        a.add(4);
        ft = FaithTrack.initFaithTrack(4, new HashMap<Integer, EffectOfCell>(), a);
        //creates production power
        ProductionPower p = new ProductionPower(new HashMap<Resource, Integer>(), new HashMap<Resource, Integer>());
        //creates dashboard
        DashBoard d = new DashBoard(dcapacity.size(), dcapacity, p, ft);
        d.addResourcesToStrongBox(Resource.ROCK, 4);
        assertEquals(4, (int) d.getStrongBox().get(Resource.ROCK));
        //subtracts 5 ROCKS , but there are only 4
        try{  d.subResourcesToStrongBox(Resource.ROCK, 5);}
        catch( EmptyStrongboxException e){}
    }


    /**
     * tests the successful case in which a devCard is added to the cardSlots
     */
    public void testAddCardSuccessful(){
        //creates the array of depot capacities
        ArrayList<Integer> dcapacity = new ArrayList<>(3);
        dcapacity.add(1);
        dcapacity.add(3);
        dcapacity.add(5);
        //creates faithtrack
        FaithTrack ft;
        ArrayList<Integer> a = new ArrayList<Integer>(4);
        a.add(1);
        a.add(2);
        a.add(3);
        a.add(4);
        ft = FaithTrack.initFaithTrack(4, new HashMap<Integer, EffectOfCell>(), a);
        //creates production Power
        ProductionPower p = new ProductionPower(new HashMap<Resource, Integer>(), new HashMap<Resource, Integer>());
        //creates dashboard
        DashBoard d = new DashBoard(dcapacity.size(), dcapacity, p, ft);
        //creates devcard
        HashMap<Resource, Integer> resources =new HashMap<Resource,Integer>();
        resources.put(Resource.ROCK,2);
        DevCard card = new DevCard(resources,1, CardColor.GREEN, 4, new ProductionPower(new HashMap<Resource, Integer>(), new HashMap<Resource, Integer>()));
        //initially stack at index 1 is empty
        try{  d.addCard(1, card);}
        catch( IndexSlotException e){fail();}
        catch( LevelCardException e){fail();}
        assertEquals(card, d.getCardSlots().get(1).peek());

        //creates new card of level 2 and add to the slot of index 2  It should not give exceptions
        DevCard card2 = new DevCard(resources,2, CardColor.GREEN, 4, new ProductionPower(new HashMap<Resource, Integer>(), new HashMap<Resource, Integer>()));
        try{  d.addCard(1, card2);}
        catch( IndexSlotException e){fail();}
        catch( LevelCardException e){fail();}
        assertEquals(card2, d.getCardSlots().get(1).peek());

    }

    /**
     * tests the unsuccessful case in which a devCard is added to a cardSlot which doesn't exist
     */
    public void testAddCardUnsuccessfulIndex(){
        //creates the array of depot capacities
        ArrayList<Integer> dcapacity = new ArrayList<>(3);
        dcapacity.add(1);
        dcapacity.add(3);
        dcapacity.add(5);
        //creates faithtrack
        FaithTrack ft;
        ArrayList<Integer> a = new ArrayList<Integer>(4);
        a.add(1);
        a.add(2);
        a.add(3);
        a.add(4);
        ft = FaithTrack.initFaithTrack(4, new HashMap<Integer, EffectOfCell>(), a);
        //creates production Power
        ProductionPower p = new ProductionPower(new HashMap<Resource, Integer>(), new HashMap<Resource, Integer>());
        //creates dashboard
        DashBoard d = new DashBoard(dcapacity.size(), dcapacity, p, ft);
        //creates devcard
        HashMap<Resource, Integer> resources =new HashMap<Resource,Integer>();
        resources.put(Resource.ROCK,2);
        DevCard card = new DevCard(resources,1, CardColor.GREEN, 4, new ProductionPower(new HashMap<Resource, Integer>(), new HashMap<Resource, Integer>()));
        //I try to add the card to a not acceptable index
        try{  d.addCard(4, card);fail();}
        catch( IndexSlotException e){}
        catch( LevelCardException e){fail();}

    }

    /**
     * tests the unsuccessful case in which a devCard is added to a cardSlot but card level constraints are not respected
     */
    public void testAddCardUnsuccessfulLevel(){
        //creates the array of depot capacities
        ArrayList<Integer> dcapacity = new ArrayList<>(3);
        dcapacity.add(1);
        dcapacity.add(3);
        dcapacity.add(5);
        //creates faithtrack
        FaithTrack ft;
        ArrayList<Integer> a = new ArrayList<Integer>(4);
        a.add(1);
        a.add(2);
        a.add(3);
        a.add(4);
        ft = FaithTrack.initFaithTrack(4, new HashMap<Integer, EffectOfCell>(), a);
        //creates production Power
        ProductionPower p = new ProductionPower(new HashMap<Resource, Integer>(), new HashMap<Resource, Integer>());
        //creates dashboard
        DashBoard d = new DashBoard(dcapacity.size(), dcapacity, p, ft);
        //creates devcard
        HashMap<Resource, Integer> resources =new HashMap<Resource,Integer>();
        resources.put(Resource.ROCK,2);
        DevCard card = new DevCard(resources,1, CardColor.GREEN, 4, new ProductionPower(new HashMap<Resource, Integer>(), new HashMap<Resource, Integer>()));
        //I add a card level 1 to slot index 1. should not give exception
        try{  d.addCard(1, card);}
        catch( IndexSlotException e){fail();}
        catch( LevelCardException e){fail();}
        assertEquals(card, d.getCardSlots().get(1).peek());

        //creates new card of level 3 and add to the slot of index 2  It should give exception, because  card level conditions are not respected
        DevCard card2 = new DevCard(resources,3, CardColor.GREEN, 4, new ProductionPower(new HashMap<Resource, Integer>(), new HashMap<Resource, Integer>()));
        try{  d.addCard(1, card2);fail();}
        catch( IndexSlotException e){fail();}
        catch( LevelCardException e){}

    }


    /**
     * tests the unsuccessful case in which a devCard of level 2 is added to an empty cardSlot thus violating  card level constraints
     */
    public void testAddCardUnsuccessfulLevelOnEmptyStack(){
        //creates the array of depot capacities
        ArrayList<Integer> dcapacity = new ArrayList<>(3);
        dcapacity.add(1);
        dcapacity.add(3);
        dcapacity.add(5);
        //creates faithtrack
        FaithTrack ft;
        ArrayList<Integer> a = new ArrayList<Integer>(4);
        a.add(1);
        a.add(2);
        a.add(3);
        a.add(4);
        ft = FaithTrack.initFaithTrack(4, new HashMap<Integer, EffectOfCell>(), a);
        //creates production Power
        ProductionPower p = new ProductionPower(new HashMap<Resource, Integer>(), new HashMap<Resource, Integer>());
        //creates dashboard
        DashBoard d = new DashBoard(dcapacity.size(), dcapacity, p, ft);
        //creates devcard
        HashMap<Resource, Integer> resources =new HashMap<Resource,Integer>();
        resources.put(Resource.ROCK,2);
        DevCard card = new DevCard(resources,2, CardColor.GREEN, 4, new ProductionPower(new HashMap<Resource, Integer>(), new HashMap<Resource, Integer>()));
        // adds a card level 2 to slot index 1, which is empty. It should give exception.
        try{  d.addCard(1, card);fail();}
        catch( IndexSlotException e){fail();}
        catch( LevelCardException e){}
    }
}