package it.polimi.ingsw.model;


import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.DevCards.DevCard;
import it.polimi.ingsw.model.FaithTrack.CellWithEffect;
import it.polimi.ingsw.model.FaithTrack.FaithTrack;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;

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
        ft = FaithTrack.initFaithTrack(4, new ArrayList<CellWithEffect>(), a);
        //creates production power
        ProductionPower p = new ProductionPower(new HashMap<Resource, Integer>(), new HashMap<Resource, Integer>(), 0, 0, 0);
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
        ft = FaithTrack.initFaithTrack(4, new ArrayList<CellWithEffect>(), a);

        ProductionPower p = new ProductionPower(new HashMap<Resource, Integer>(), new HashMap<Resource, Integer>(),0 ,0 ,0);

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
        ft = FaithTrack.initFaithTrack(4, new ArrayList<CellWithEffect>(), a);
        //creates production power
        ProductionPower p = new ProductionPower(new HashMap<Resource, Integer>(), new HashMap<Resource, Integer>(), 0, 0,0);
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
        ft = FaithTrack.initFaithTrack(4, new ArrayList<CellWithEffect>(), a);
        //creates production Power
        ProductionPower p = new ProductionPower(new HashMap<Resource, Integer>(), new HashMap<Resource, Integer>(), 0, 0, 0);
        //creates dashboard
        DashBoard d = new DashBoard(dcapacity.size(), dcapacity, p, ft);
        //creates devcard
        HashMap<Resource, Integer> resources =new HashMap<Resource,Integer>();
        resources.put(Resource.ROCK,2);
        DevCard card = new DevCard(resources,1, CardColor.GREEN, 4, new ProductionPower(new HashMap<Resource, Integer>(), new HashMap<Resource, Integer>(), 0, 0, 0));
        //initially stack at index 1 is empty
        try{  d.addCard(1, card);}
        catch( IndexSlotException e){fail();}
        catch( LevelCardException e){fail();}
        assertEquals(card, d.getCardSlots().get(1).peek());

        //creates new card of level 2 and add to the slot of index 2  It should not give exceptions
        DevCard card2 = new DevCard(resources,2, CardColor.GREEN, 4, new ProductionPower(new HashMap<Resource, Integer>(), new HashMap<Resource, Integer>(), 0, 0, 0));
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
        ft = FaithTrack.initFaithTrack(4, new ArrayList<CellWithEffect>(), a);
        //creates production Power
        ProductionPower p = new ProductionPower(new HashMap<Resource, Integer>(), new HashMap<Resource, Integer>(), 0, 0, 0);
        //creates dashboard
        DashBoard d = new DashBoard(dcapacity.size(), dcapacity, p, ft);
        //creates devcard
        HashMap<Resource, Integer> resources =new HashMap<Resource,Integer>();
        resources.put(Resource.ROCK,2);
        DevCard card = new DevCard(resources,1, CardColor.GREEN, 4, new ProductionPower(new HashMap<Resource, Integer>(), new HashMap<Resource, Integer>(), 0, 0, 0));
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
        ft = FaithTrack.initFaithTrack(4, new ArrayList<CellWithEffect>(), a);
        //creates production Power
        ProductionPower p = new ProductionPower(new HashMap<Resource, Integer>(), new HashMap<Resource, Integer>(), 0, 0, 0);
        //creates dashboard
        DashBoard d = new DashBoard(dcapacity.size(), dcapacity, p, ft);
        //creates devcard
        HashMap<Resource, Integer> resources =new HashMap<Resource,Integer>();
        resources.put(Resource.ROCK,2);
        DevCard card = new DevCard(resources,1, CardColor.GREEN, 4, new ProductionPower(new HashMap<Resource, Integer>(), new HashMap<Resource, Integer>(), 0, 0, 0));
        //I add a card level 1 to slot index 1. should not give exception
        try{  d.addCard(1, card);}
        catch( IndexSlotException e){fail();}
        catch( LevelCardException e){fail();}
        assertEquals(card, d.getCardSlots().get(1).peek());

        //creates new card of level 3 and add to the slot of index 2  It should give exception, because  card level conditions are not respected
        DevCard card2 = new DevCard(resources,3, CardColor.GREEN, 4, new ProductionPower(new HashMap<Resource, Integer>(), new HashMap<Resource, Integer>(), 0, 0, 0));
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
        ft = FaithTrack.initFaithTrack(4, new ArrayList<CellWithEffect>(), a);
        //creates production Power
        ProductionPower p = new ProductionPower(new HashMap<Resource, Integer>(), new HashMap<Resource, Integer>(), 0, 0, 0);
        //creates dashboard
        DashBoard d = new DashBoard(dcapacity.size(), dcapacity, p, ft);
        //creates devcard
        HashMap<Resource, Integer> resources =new HashMap<Resource,Integer>();
        resources.put(Resource.ROCK,2);
        DevCard card = new DevCard(resources,2, CardColor.GREEN, 4, new ProductionPower(new HashMap<Resource, Integer>(), new HashMap<Resource, Integer>(), 0, 0, 0));
        // adds a card level 2 to slot index 1, which is empty. It should give exception.
        try{  d.addCard(1, card);fail();}
        catch( IndexSlotException e){fail();}
        catch( LevelCardException e){}
    }

    public void testGetDepotResources(){
        ArrayList<Integer> eachDepotCapacity = new ArrayList<>();
        eachDepotCapacity.add(1);
        eachDepotCapacity.add(2);
        eachDepotCapacity.add(3);
        DashBoard dashBoard = new DashBoard(3, eachDepotCapacity, null, null);

        try {
            dashBoard.getWarehouse().get(0).addResources(1, Resource.COIN);
        } catch (Exception e) {
            fail();
        }

        try {
            dashBoard.getWarehouse().get(1).addResources(1, Resource.SERVANT);
        } catch (Exception e) {
            fail();
        }

        try {
            dashBoard.getWarehouse().get(2).addResources(3, Resource.SHIELD);
        } catch (Exception e) {
            fail();
        }

        HashMap<Resource, Integer> expected = new HashMap<>();
        expected.put(Resource.COIN, 1);
        expected.put(Resource.SERVANT, 1);
        expected.put(Resource.SHIELD, 3);

        for(Resource r: Resource.values())
            if(!expected.containsKey(r))
                expected.put(r,0);

        assertEquals(expected, dashBoard.getWarehouseResources());
    }

    public void testGetAllDashboardResources() {
        ArrayList<Integer> eachDepotCapacity = new ArrayList<>();
        eachDepotCapacity.add(1);
        eachDepotCapacity.add(2);
        eachDepotCapacity.add(3);
        DashBoard dashBoard = new DashBoard(3, eachDepotCapacity, null, null);

        try {
            dashBoard.getWarehouse().get(1).addResources(1, Resource.SERVANT);
        } catch (Exception e) {
            fail();
        }

        try {
            dashBoard.getWarehouse().get(2).addResources(3, Resource.SHIELD);
        } catch (Exception e) {
            fail();
        }

        dashBoard.addResourcesToStrongBox(Resource.SERVANT, 4);
        dashBoard.addResourcesToStrongBox(Resource.COIN, 1);

        HashMap<Resource, Integer> expected = new HashMap<>();
        expected.put(Resource.SERVANT, 5);
        expected.put(Resource.SHIELD, 3);
        expected.put(Resource.COIN, 1);

        for(Resource r: Resource.values())
            if(!expected.containsKey(r))
                expected.put(r,0);

        assertEquals(expected, dashBoard.getAllDashboardResources());
    }
}
