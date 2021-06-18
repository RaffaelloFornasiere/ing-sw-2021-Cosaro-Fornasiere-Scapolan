package it.polimi.ingsw.model;


import it.polimi.ingsw.events.ClientEvents.DepotState;
import it.polimi.ingsw.exceptions.EmptyStrongboxException;
import it.polimi.ingsw.exceptions.IndexSlotException;
import it.polimi.ingsw.exceptions.LevelCardException;
import it.polimi.ingsw.exceptions.ResourcesLimitsException;
import it.polimi.ingsw.model.DevCards.DevCard;
import it.polimi.ingsw.model.FaithTrack.FaithTrack;
import it.polimi.ingsw.utilities.Config;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.HashMap;

public class DashBoardTest extends TestCase {

    public void testBasicGetters(){
        ArrayList<Integer> dCapacity = new ArrayList<>(3);
        dCapacity.add(1);
        dCapacity.add(3);
        dCapacity.add(5);

        ProductionPower productionPower = new ProductionPower(new HashMap<>(), new HashMap<>(), 3 ,2 ,1);

        FaithTrack.initFaithTrack(Config.getDefaultConfig().getFaithTrack());

        DashBoard dashBoard = new DashBoard(3, dCapacity, productionPower);

        assertEquals(productionPower, dashBoard.getPersonalPower());
    }

    /**
     * Tests the successful case in which resources are added to the StrongBox
     */
    public void testAddResourcesToStrongBox() {
        //creates the array of depot capacities
        ArrayList<Integer> dCapacity = new ArrayList<>(3);
        dCapacity.add(1);
        dCapacity.add(3);
        dCapacity.add(5);
      //creates faith Track
        FaithTrack.initFaithTrack(Config.getDefaultConfig().getFaithTrack());        //creates production power
        ProductionPower p = new ProductionPower(new HashMap<>(), new HashMap<>(), 0, 0, 0);
        //create dashboard
        DashBoard d = new DashBoard(dCapacity.size(), dCapacity, p);
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
     * Tests the successful case in which resources are subtracted to the StrongBox
     */
    public void testSubResourcesToStrongBoxSuccessful() {
        //creates the array of depot capacities
        ArrayList<Integer> dCapacity = new ArrayList<>(3);
        dCapacity.add(1);
        dCapacity.add(3);
        dCapacity.add(5);
        //creates faith track
        FaithTrack.initFaithTrack(Config.getDefaultConfig().getFaithTrack());

        ProductionPower p = new ProductionPower(new HashMap<>(), new HashMap<>(), 0, 0, 0);

        DashBoard d = new DashBoard(dCapacity.size(), dCapacity, p);
        d.addResourcesToStrongBox(Resource.ROCK, 4);
        assertEquals(4, (int) d.getStrongBox().get(Resource.ROCK));
        try {
            d.subResourcesToStrongBox(Resource.ROCK, 2);
        } catch (EmptyStrongboxException e) {
            fail();
        }
        assertEquals(2, (int) d.getStrongBox().get(Resource.ROCK));
    }

    /**
     * Tests the unsuccessful case in which more resources than those available are subtracted to the StrongBox
     */
    public void testSubResourcesToStrongBoxUnsuccessful(){
        //creates the array of depot capacities
        ArrayList<Integer> dCapacity = new ArrayList<>(3);
        dCapacity.add(1);
        dCapacity.add(3);
        dCapacity.add(5);
        //creates faith track
        FaithTrack.initFaithTrack(Config.getDefaultConfig().getFaithTrack());
        //creates production power
        ProductionPower p = new ProductionPower(new HashMap<>(), new HashMap<>(), 0, 0,0);
        //creates dashboard
        DashBoard d = new DashBoard(dCapacity.size(), dCapacity, p);
        d.addResourcesToStrongBox(Resource.ROCK, 4);
        assertEquals(4, (int) d.getStrongBox().get(Resource.ROCK));
        //subtracts 5 ROCKS , but there are only 4
        try {
            d.subResourcesToStrongBox(Resource.ROCK, 5);
            fail();
        } catch( EmptyStrongboxException ignore){}
    }

    /**
     * Tests the successful case in which resources are added to the warehouse when the resource type is already present
     */
    public void testAddResourcesToWarehouseAlreadyPresent(){
        ArrayList<Integer> eachDepotCapacity = new ArrayList<>();
        eachDepotCapacity.add(1);
        eachDepotCapacity.add(2);
        eachDepotCapacity.add(3);
        DashBoard dashBoard = new DashBoard(3, eachDepotCapacity, null);

        ArrayList<DepotState> newWarehouse = new ArrayList<>();
        newWarehouse.add(new DepotState(Resource.COIN, 1, 1));
        newWarehouse.add(new DepotState(Resource.COIN, 2, 0));
        newWarehouse.add(new DepotState(Resource.SHIELD, 3, 1));

        dashBoard.setWarehouseResources(newWarehouse);

        try {
            dashBoard.addResourcesToWarehouse(Resource.SHIELD, 1);
        } catch (ResourcesLimitsException e) {
            fail();
        }

        ArrayList<Depot> warehouse = dashBoard.getWarehouse();
        for (int i = 0, warehouseSize = warehouse.size(); i < warehouseSize; i++) {
            Depot d = warehouse.get(i);
            DepotState ds = newWarehouse.get(i);
            assertEquals(ds.getResourceType(), d.getResourceType());
            assertEquals(ds.getMaxQuantity(), d.getMaxQuantity());
            if(d.getResourceType() == Resource.SHIELD){
                assertEquals(ds.getCurrentQuantity()+1, d.getCurrentQuantity());
            }
            else {
                assertEquals(ds.getCurrentQuantity(), d.getCurrentQuantity());
            }
        }
    }

    /**
     * Tests the successful case in which resources are added to the warehouse when the resource type is not already present
     */
    public void testAddResourcesToWarehouseNew(){
        ArrayList<Integer> eachDepotCapacity = new ArrayList<>();
        eachDepotCapacity.add(1);
        eachDepotCapacity.add(2);
        eachDepotCapacity.add(3);
        DashBoard dashBoard = new DashBoard(3, eachDepotCapacity, null);

        try {
            dashBoard.addResourcesToWarehouse(Resource.SHIELD, 2);
        } catch (ResourcesLimitsException e) {
            fail();
        }

        ArrayList<Depot> warehouse = dashBoard.getWarehouse();
        assertEquals(0, warehouse.get(0).getCurrentQuantity());
        assertEquals(Resource.SHIELD, warehouse.get(1).getResourceType());
        assertEquals(2, warehouse.get(1).getCurrentQuantity());
        assertEquals(0, warehouse.get(2).getCurrentQuantity());
    }

    /**
     * Tests the unsuccessful case in which resources are added to the warehouse when there is no deposit big enough to store the resource
     */
    public void testAddResourcesToWarehouseUnsuccessfulTooManyResources1(){
        ArrayList<Integer> eachDepotCapacity = new ArrayList<>();
        eachDepotCapacity.add(1);
        eachDepotCapacity.add(2);
        eachDepotCapacity.add(3);
        DashBoard dashBoard = new DashBoard(3, eachDepotCapacity, null);

        try {
            dashBoard.addResourcesToWarehouse(Resource.SHIELD, 4);
            fail();
        } catch (ResourcesLimitsException e) {
            fail();
        }
        catch (IllegalArgumentException ignore){}
    }

    /**
     * Tests the unsuccessful case in which resources are added to the warehouse when there is no deposit which can store the resource
     */
    public void testAddResourcesToWarehouseUnsuccessfulNooFreeDepot(){
        ArrayList<Integer> eachDepotCapacity = new ArrayList<>();
        eachDepotCapacity.add(1);
        eachDepotCapacity.add(2);
        eachDepotCapacity.add(3);
        DashBoard dashBoard = new DashBoard(3, eachDepotCapacity, null);

        ArrayList<DepotState> newWarehouse = new ArrayList<>();
        newWarehouse.add(new DepotState(Resource.COIN, 1, 1));
        newWarehouse.add(new DepotState(Resource.SERVANT, 2, 2));
        newWarehouse.add(new DepotState(Resource.SHIELD, 3, 1));

        dashBoard.setWarehouseResources(newWarehouse);

        try {
            dashBoard.addResourcesToWarehouse(Resource.ROCK, 4);
            fail();
        } catch (ResourcesLimitsException e) {
            fail();
        }
        catch (IllegalArgumentException ignore){}
    }

    /**
     * Tests the unsuccessful case in which resources are added to the warehouse when the deposit containing the resource type does not have enough space
     */
    public void testAddResourcesToWarehouseUnsuccessfulTooManyResources2(){
        ArrayList<Integer> eachDepotCapacity = new ArrayList<>();
        eachDepotCapacity.add(1);
        eachDepotCapacity.add(2);
        eachDepotCapacity.add(3);
        DashBoard dashBoard = new DashBoard(3, eachDepotCapacity, null);

        ArrayList<DepotState> newWarehouse = new ArrayList<>();
        newWarehouse.add(new DepotState(Resource.COIN, 1, 1));
        newWarehouse.add(new DepotState(Resource.SERVANT, 2, 2));
        newWarehouse.add(new DepotState(Resource.SHIELD, 3, 1));

        dashBoard.setWarehouseResources(newWarehouse);

        try {
            dashBoard.addResourcesToWarehouse(Resource.SHIELD, 3);
            fail();
        } catch (ResourcesLimitsException ignored) {

        } catch (IllegalArgumentException e){
            fail();
        }
    }

    /**
     * Tests the successful case in which resources are subtracted from the warehouse
     */
    public void testSubResourcesToWarehouse(){
        ArrayList<Integer> eachDepotCapacity = new ArrayList<>();
        eachDepotCapacity.add(1);
        eachDepotCapacity.add(2);
        eachDepotCapacity.add(3);
        DashBoard dashBoard = new DashBoard(3, eachDepotCapacity, null);

        ArrayList<DepotState> newWarehouse = new ArrayList<>();
        newWarehouse.add(new DepotState(Resource.COIN, 1, 1));
        newWarehouse.add(new DepotState(Resource.SHIELD, 2, 0));
        newWarehouse.add(new DepotState(Resource.SHIELD, 3, 2));

        dashBoard.setWarehouseResources(newWarehouse);

        try {
            dashBoard.subResourcesToWarehouse(Resource.SHIELD, 1);
        } catch (ResourcesLimitsException e) {
            fail();
        }

        ArrayList<Depot> warehouse = dashBoard.getWarehouse();
        for (int i = 0, warehouseSize = warehouse.size(); i < warehouseSize; i++) {
            Depot d = warehouse.get(i);
            DepotState ds = newWarehouse.get(i);
            assertEquals(ds.getResourceType(), d.getResourceType());
            assertEquals(ds.getMaxQuantity(), d.getMaxQuantity());
            if(i==2){
                assertEquals(ds.getCurrentQuantity()-1, d.getCurrentQuantity());
            }
            else {
                assertEquals(ds.getCurrentQuantity(), d.getCurrentQuantity());
            }
        }
    }

    /**
     * Tests the unsuccessful case in which resources are subtracted from the warehouse because there's no depot with that resource
     */
    public void testSubResourcesToWarehouseNoDepotWithResourceType(){
        ArrayList<Integer> eachDepotCapacity = new ArrayList<>();
        eachDepotCapacity.add(1);
        eachDepotCapacity.add(2);
        eachDepotCapacity.add(3);
        DashBoard dashBoard = new DashBoard(3, eachDepotCapacity, null);

        ArrayList<DepotState> newWarehouse = new ArrayList<>();
        newWarehouse.add(new DepotState(Resource.COIN, 1, 1));
        newWarehouse.add(new DepotState(Resource.SHIELD, 2, 0));
        newWarehouse.add(new DepotState(Resource.SHIELD, 3, 2));

        dashBoard.setWarehouseResources(newWarehouse);

        try {
            dashBoard.subResourcesToWarehouse(Resource.SERVANT, 1);
            fail();
        } catch (ResourcesLimitsException e) {
            fail();
        }
        catch (IllegalArgumentException ignore){}

        ArrayList<Depot> warehouse = dashBoard.getWarehouse();
        for (int i = 0, warehouseSize = warehouse.size(); i < warehouseSize; i++) {
            Depot d = warehouse.get(i);
            DepotState ds = newWarehouse.get(i);
            assertEquals(ds.getResourceType(), d.getResourceType());
            assertEquals(ds.getMaxQuantity(), d.getMaxQuantity());
            assertEquals(ds.getCurrentQuantity(), d.getCurrentQuantity());
        }
    }

    /**
     * Tests the unsuccessful case in which resources are subtracted from the warehouse because there's not enough resources in the depot
     */
    public void testSubResourcesToWarehouseTooFewResources(){
        ArrayList<Integer> eachDepotCapacity = new ArrayList<>();
        eachDepotCapacity.add(1);
        eachDepotCapacity.add(2);
        eachDepotCapacity.add(3);
        DashBoard dashBoard = new DashBoard(3, eachDepotCapacity, null);

        ArrayList<DepotState> newWarehouse = new ArrayList<>();
        newWarehouse.add(new DepotState(Resource.COIN, 1, 1));
        newWarehouse.add(new DepotState(Resource.SHIELD, 2, 0));
        newWarehouse.add(new DepotState(Resource.SHIELD, 3, 2));

        dashBoard.setWarehouseResources(newWarehouse);

        try {
            dashBoard.subResourcesToWarehouse(Resource.SHIELD, 3);
            fail();
        } catch (ResourcesLimitsException ignore) {
        } catch (IllegalArgumentException e){
            fail();
        }

        ArrayList<Depot> warehouse = dashBoard.getWarehouse();
        for (int i = 0, warehouseSize = warehouse.size(); i < warehouseSize; i++) {
            Depot d = warehouse.get(i);
            DepotState ds = newWarehouse.get(i);
            assertEquals(ds.getResourceType(), d.getResourceType());
            assertEquals(ds.getMaxQuantity(), d.getMaxQuantity());
            assertEquals(ds.getCurrentQuantity(), d.getCurrentQuantity());
        }
    }

    /**
     * Tests the successful case in which resources are subtracted from the warehouse with an HashMap
     */
    public void testSubResourcesToWarehouseHashMap(){
        ArrayList<Integer> eachDepotCapacity = new ArrayList<>();
        eachDepotCapacity.add(1);
        eachDepotCapacity.add(2);
        eachDepotCapacity.add(3);
        DashBoard dashBoard = new DashBoard(3, eachDepotCapacity, null);

        ArrayList<DepotState> newWarehouse = new ArrayList<>();
        newWarehouse.add(new DepotState(Resource.COIN, 1, 1));
        newWarehouse.add(new DepotState(Resource.SHIELD, 2, 0));
        newWarehouse.add(new DepotState(Resource.SHIELD, 3, 2));

        dashBoard.setWarehouseResources(newWarehouse);

        HashMap<Resource, Integer> toRemove = new HashMap<>();
        toRemove.put(Resource.SHIELD, 1);
        toRemove.put(Resource.COIN, 1);
        try {
            dashBoard.subResourcesToWarehouse(toRemove);
        } catch (ResourcesLimitsException e) {
            fail();
        }

        ArrayList<Depot> warehouse = dashBoard.getWarehouse();
        for (int i = 0, warehouseSize = warehouse.size(); i < warehouseSize; i++) {
            Depot d = warehouse.get(i);
            DepotState ds = newWarehouse.get(i);
            Resource r = d.getResourceType();
            assertEquals(ds.getResourceType(), r);
            assertEquals(ds.getMaxQuantity(), d.getMaxQuantity());
            int expected = ds.getCurrentQuantity();
            if(expected>0) expected-=toRemove.getOrDefault(r, 0);
            assertEquals(expected, d.getCurrentQuantity());
        }
    }

    /**
     * Tests the unsuccessful case in which resources are subtracted from the warehouse with an HashMap because there's no depot with that resource
     */
    public void testSubResourcesToWarehouseHashMapNoDepotWithResourceType(){
        ArrayList<Integer> eachDepotCapacity = new ArrayList<>();
        eachDepotCapacity.add(1);
        eachDepotCapacity.add(2);
        eachDepotCapacity.add(3);
        DashBoard dashBoard = new DashBoard(3, eachDepotCapacity, null);

        ArrayList<DepotState> newWarehouse = new ArrayList<>();
        newWarehouse.add(new DepotState(Resource.COIN, 1, 1));
        newWarehouse.add(new DepotState(Resource.SHIELD, 2, 0));
        newWarehouse.add(new DepotState(Resource.SHIELD, 3, 2));

        dashBoard.setWarehouseResources(newWarehouse);

        HashMap<Resource, Integer> toRemove = new HashMap<>();
        toRemove.put(Resource.SHIELD, 1);
        toRemove.put(Resource.SERVANT, 1);
        try {
            dashBoard.subResourcesToWarehouse(toRemove);
            fail();
        } catch (ResourcesLimitsException e) {
            fail();
        }
        catch (IllegalArgumentException ignore){}

        ArrayList<Depot> warehouse = dashBoard.getWarehouse();
        for (int i = 0, warehouseSize = warehouse.size(); i < warehouseSize; i++) {
            Depot d = warehouse.get(i);
            DepotState ds = newWarehouse.get(i);
            assertEquals(ds.getResourceType(), d.getResourceType());
            assertEquals(ds.getMaxQuantity(), d.getMaxQuantity());
            assertEquals(ds.getCurrentQuantity(), d.getCurrentQuantity());
        }
    }

    /**
     * Tests the unsuccessful case in which resources are subtracted from the warehouse  with an HashMap because there's not enough resources in the depot
     */
    public void testSubResourcesToWarehouseHashMapTooFewResources(){
        ArrayList<Integer> eachDepotCapacity = new ArrayList<>();
        eachDepotCapacity.add(1);
        eachDepotCapacity.add(2);
        eachDepotCapacity.add(3);
        DashBoard dashBoard = new DashBoard(3, eachDepotCapacity, null);

        ArrayList<DepotState> newWarehouse = new ArrayList<>();
        newWarehouse.add(new DepotState(Resource.COIN, 1, 1));
        newWarehouse.add(new DepotState(Resource.SHIELD, 2, 0));
        newWarehouse.add(new DepotState(Resource.SHIELD, 3, 2));

        dashBoard.setWarehouseResources(newWarehouse);

        HashMap<Resource, Integer> toRemove = new HashMap<>();
        toRemove.put(Resource.SHIELD, 1);
        toRemove.put(Resource.COIN, 3);
        try {
            dashBoard.subResourcesToWarehouse(toRemove);
            fail();
        } catch (ResourcesLimitsException ignore) {
        } catch (IllegalArgumentException e){
            fail();
        }

        ArrayList<Depot> warehouse = dashBoard.getWarehouse();
        for (int i = 0, warehouseSize = warehouse.size(); i < warehouseSize; i++) {
            Depot d = warehouse.get(i);
            DepotState ds = newWarehouse.get(i);
            assertEquals(ds.getResourceType(), d.getResourceType());
            assertEquals(ds.getMaxQuantity(), d.getMaxQuantity());
            assertEquals(ds.getCurrentQuantity(), d.getCurrentQuantity());
        }
    }

    /**
     * Tests the successful case in which the warehouse is set
     */
    public void testSetWarehouseResource(){
        ArrayList<Integer> eachDepotCapacity = new ArrayList<>();
        eachDepotCapacity.add(1);
        eachDepotCapacity.add(2);
        eachDepotCapacity.add(3);
        DashBoard dashBoard = new DashBoard(3, eachDepotCapacity, null);

        ArrayList<DepotState> newWarehouse = new ArrayList<>();
        newWarehouse.add(new DepotState(Resource.COIN, 1, 1));
        newWarehouse.add(new DepotState(Resource.COIN, 2, 0));
        newWarehouse.add(new DepotState(Resource.SHIELD, 3, 3));

        dashBoard.setWarehouseResources(newWarehouse);

        ArrayList<Depot> warehouse = dashBoard.getWarehouse();
        for (int i = 0, warehouseSize = warehouse.size(); i < warehouseSize; i++) {
            Depot d = warehouse.get(i);
            DepotState ds = newWarehouse.get(i);
            assertEquals(ds.getResourceType(), d.getResourceType());
            assertEquals(ds.getMaxQuantity(), d.getMaxQuantity());
            assertEquals(ds.getCurrentQuantity(), d.getCurrentQuantity());
        }
    }

    /**
     * Tests the unsuccessful case in which the warehouse is set with the new warehouse state with a different number of depots
     */
    public void testSetWarehouseResourceUnsuccessfulWrongSize(){
        ArrayList<Integer> eachDepotCapacity = new ArrayList<>();
        eachDepotCapacity.add(1);
        eachDepotCapacity.add(2);
        eachDepotCapacity.add(3);
        DashBoard dashBoard = new DashBoard(3, eachDepotCapacity, null);

        ArrayList<DepotState> newWarehouse = new ArrayList<>();
        newWarehouse.add(new DepotState(Resource.COIN, 1, 1));
        newWarehouse.add(new DepotState(Resource.SHIELD, 3, 3));

        try {
            dashBoard.setWarehouseResources(newWarehouse);
            fail();
        } catch (IllegalArgumentException ignore){ }
    }

    /**
     * Tests the unsuccessful case in which the warehouse is set with the new warehouse state with a different max quantities
     */
    public void testSetWarehouseResourceUnsuccessfulWrongMaxQuantities(){
        ArrayList<Integer> eachDepotCapacity = new ArrayList<>();
        eachDepotCapacity.add(1);
        eachDepotCapacity.add(2);
        eachDepotCapacity.add(3);
        DashBoard dashBoard = new DashBoard(3, eachDepotCapacity, null);

        ArrayList<DepotState> newWarehouse = new ArrayList<>();
        newWarehouse.add(new DepotState(Resource.COIN, 1, 1));
        newWarehouse.add(new DepotState(Resource.SHIELD, 3, 3));
        newWarehouse.add(new DepotState(Resource.SERVANT, 3, 3));

        try {
            dashBoard.setWarehouseResources(newWarehouse);
            fail();
        } catch (IllegalArgumentException ignore){ }
    }

    /**
     * Tests the unsuccessful case in which the warehouse is set with the new warehouse state with duplicated resource type
     */
    public void testSetWarehouseResourceUnsuccessfulDuplicatedResources(){
        ArrayList<Integer> eachDepotCapacity = new ArrayList<>();
        eachDepotCapacity.add(1);
        eachDepotCapacity.add(2);
        eachDepotCapacity.add(3);
        DashBoard dashBoard = new DashBoard(3, eachDepotCapacity, null);

        ArrayList<DepotState> newWarehouse = new ArrayList<>();
        newWarehouse.add(new DepotState(Resource.COIN, 1, 1));
        newWarehouse.add(new DepotState(Resource.SHIELD, 2, 2));
        newWarehouse.add(new DepotState(Resource.COIN, 3, 3));

        try {
            dashBoard.setWarehouseResources(newWarehouse);
            fail();
        } catch (IllegalArgumentException ignore){ }
    }

    /**
     * Tests the successful case in which a devCard is added to the cardSlots
     */
    public void testAddCardSuccessful(){
        //creates the array of depot capacities
        ArrayList<Integer> dCapacity = new ArrayList<>(3);
        dCapacity.add(1);
        dCapacity.add(3);
        dCapacity.add(5);
        //creates faith track
        ArrayList<Integer> a = new ArrayList<>(4);
        a.add(1);
        a.add(2);
        a.add(3);
        a.add(4);
        FaithTrack.initFaithTrack(4, new ArrayList<>(), a);
        //creates production Power
        ProductionPower p = new ProductionPower(new HashMap<>(), new HashMap<>(), 0, 0, 0);
        //creates dashboard
        DashBoard d = new DashBoard(dCapacity.size(), dCapacity, p);
        //creates dev card
        HashMap<Resource, Integer> resources =new HashMap<>();
        resources.put(Resource.ROCK,2);
        DevCard card = new DevCard(resources,1, CardColor.GREEN, 4, new ProductionPower(new HashMap<>(), new HashMap<>(), 0, 0, 0));
        //initially stack at index 1 is empty
        try{  d.addCard(1, card);}
        catch( IndexSlotException | LevelCardException e){fail();}
        assertEquals(card, d.getCardSlots().get(1).peek());

        //creates new card of level 2 and add to the slot of index 2  It should not give exceptions
        DevCard card2 = new DevCard(resources,2, CardColor.GREEN, 4, new ProductionPower(new HashMap<>(), new HashMap<>(), 0, 0, 0));
        try{  d.addCard(1, card2);}
        catch( IndexSlotException | LevelCardException e){fail();}
        assertEquals(card2, d.getCardSlots().get(1).peek());

    }

    /**
     * Tests the unsuccessful case in which a devCard is added to a cardSlot which doesn't exist
     */
    public void testAddCardUnsuccessfulIndex(){
        //creates the array of depot capacities
        ArrayList<Integer> dCapacity = new ArrayList<>(3);
        dCapacity.add(1);
        dCapacity.add(3);
        dCapacity.add(5);
        //creates faith track
        ArrayList<Integer> a = new ArrayList<>(4);
        a.add(1);
        a.add(2);
        a.add(3);
        a.add(4);
        FaithTrack.initFaithTrack(4, new ArrayList<>(), a);
        //creates production Power
        ProductionPower p = new ProductionPower(new HashMap<>(), new HashMap<>(), 0, 0, 0);
        //creates dashboard
        DashBoard d = new DashBoard(dCapacity.size(), dCapacity, p);
        //creates dev card
        HashMap<Resource, Integer> resources =new HashMap<>();
        resources.put(Resource.ROCK,2);
        DevCard card = new DevCard(resources,1, CardColor.GREEN, 4, new ProductionPower(new HashMap<>(), new HashMap<>(), 0, 0, 0));
        //I try to add the card to a not acceptable index
        try{  d.addCard(4, card);fail();}
        catch( IndexSlotException ignored){}
        catch( LevelCardException e){fail();}

    }

    /**
     * Tests the unsuccessful case in which a devCard is added to a cardSlot but card level constraints are not respected
     */
    public void testAddCardUnsuccessfulLevel(){
        //creates the array of depot capacities
        ArrayList<Integer> dCapacity = new ArrayList<>(3);
        dCapacity.add(1);
        dCapacity.add(3);
        dCapacity.add(5);
        //creates faith track
        ArrayList<Integer> a = new ArrayList<>(4);
        a.add(1);
        a.add(2);
        a.add(3);
        a.add(4);
        FaithTrack.initFaithTrack(4, new ArrayList<>(), a);
        //creates production Power
        ProductionPower p = new ProductionPower(new HashMap<>(), new HashMap<>(), 0, 0, 0);
        //creates dashboard
        DashBoard d = new DashBoard(dCapacity.size(), dCapacity, p);
        //creates dev card
        HashMap<Resource, Integer> resources =new HashMap<>();
        resources.put(Resource.ROCK,2);
        DevCard card = new DevCard(resources,1, CardColor.GREEN, 4, new ProductionPower(new HashMap<>(), new HashMap<>(), 0, 0, 0));
        //I add a card level 1 to slot index 1. should not give exception
        try{  d.addCard(1, card);}
        catch( IndexSlotException | LevelCardException e){fail();}
        assertEquals(card, d.getCardSlots().get(1).peek());

        //creates new card of level 3 and add to the slot of index 2  It should give exception, because  card level conditions are not respected
        DevCard card2 = new DevCard(resources,3, CardColor.GREEN, 4, new ProductionPower(new HashMap<>(), new HashMap<>(), 0, 0, 0));
        try{  d.addCard(1, card2);fail();}
        catch( IndexSlotException e){fail();}
        catch( LevelCardException ignored){}

    }


    /**
     * Tests the unsuccessful case in which a devCard of level 2 is added to an empty cardSlot thus violating card level constraints
     */
    public void testAddCardUnsuccessfulLevelOnEmptyStack(){
        //creates the array of depot capacities
        ArrayList<Integer> dCapacity = new ArrayList<>(3);
        dCapacity.add(1);
        dCapacity.add(3);
        dCapacity.add(5);
        //creates faith track
        ArrayList<Integer> a = new ArrayList<>(4);
        a.add(1);
        a.add(2);
        a.add(3);
        a.add(4);
        FaithTrack.initFaithTrack(4, new ArrayList<>(), a);
        //creates production Power
        ProductionPower p = new ProductionPower(new HashMap<>(), new HashMap<>(), 0, 0, 0);
        //creates dashboard
        DashBoard d = new DashBoard(dCapacity.size(), dCapacity, p);
        //creates dev card
        HashMap<Resource, Integer> resources =new HashMap<>();
        resources.put(Resource.ROCK,2);
        DevCard card = new DevCard(resources,2, CardColor.GREEN, 4, new ProductionPower(new HashMap<>(), new HashMap<>(), 0, 0, 0));
        // adds a card level 2 to slot index 1, which is empty. It should give exception.
        try{  d.addCard(1, card);fail();}
        catch( IndexSlotException e){fail();}
        catch( LevelCardException ignored){}
    }

    /**
     * Test for the method that checks if a card can be put in a slot
     */
    public void testCheckSlot(){
        ArrayList<Integer> eachDepotCapacity = new ArrayList<>();
        eachDepotCapacity.add(1);
        eachDepotCapacity.add(2);
        eachDepotCapacity.add(3);
        DashBoard dashBoard = new DashBoard(3, eachDepotCapacity, null);

        DevCard dc1 = new DevCard(new HashMap<>(), 1, CardColor.BLUE, 2, null);
        DevCard dc2 = new DevCard(new HashMap<>(), 2, CardColor.BLUE, 3, null);
        DevCard dc3 = new DevCard(new HashMap<>(), 1, CardColor.BLUE, 3, null);

        try {
            dashBoard.addCard(0, dc1);
        } catch (IndexSlotException | LevelCardException e) {
            fail();
        }

        assertTrue(dashBoard.checkSlot(0, dc2));
        assertTrue(dashBoard.checkSlot(1, dc3));
        assertFalse(dashBoard.checkSlot(0, dc3));
        assertFalse(dashBoard.checkSlot(1, dc2));
    }

    /**
     * tests the getter for the top cards
     */
    public void testGetTopCards(){
        ArrayList<Integer> eachDepotCapacity = new ArrayList<>();
        eachDepotCapacity.add(1);
        eachDepotCapacity.add(2);
        eachDepotCapacity.add(3);
        DashBoard dashBoard = new DashBoard(3, eachDepotCapacity, null);

        DevCard dc1 = new DevCard(new HashMap<>(), 1, CardColor.BLUE, 2, null);
        DevCard dc2 = new DevCard(new HashMap<>(), 2, CardColor.BLUE, 3, null);
        DevCard dc3 = new DevCard(new HashMap<>(), 1, CardColor.BLUE, 3, null);

        try {
            dashBoard.addCard(0, dc1);
        } catch (IndexSlotException | LevelCardException e) {
            fail();
        }

        try {
            dashBoard.addCard(0, dc2);
        } catch (IndexSlotException | LevelCardException e) {
            fail();
        }

        try {
            dashBoard.addCard(1, dc3);
        } catch (IndexSlotException | LevelCardException e) {
            fail();
        }

        ArrayList<DevCard> topCard = dashBoard.getTopCards();
        assertFalse(topCard.contains(dc1));
        assertTrue(topCard.contains(dc2));
        assertTrue(topCard.contains(dc3));
    }

    /**
     * Test for the methods that computes and returns all the resources in the warehouse
     */
    public void testGetWarehouseResources(){
        ArrayList<Integer> eachDepotCapacity = new ArrayList<>();
        eachDepotCapacity.add(1);
        eachDepotCapacity.add(2);
        eachDepotCapacity.add(3);
        DashBoard dashBoard = new DashBoard(3, eachDepotCapacity, null);

        try {
            dashBoard.getWarehouse().get(0).addResources(Resource.COIN, 1);
        } catch (Exception e) {
            fail();
        }

        try {
            dashBoard.getWarehouse().get(1).addResources(Resource.SERVANT, 1);
        } catch (Exception e) {
            fail();
        }

        try {
            dashBoard.getWarehouse().get(2).addResources(Resource.SHIELD, 3);
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

    /**
     * Test for the methods that computes and returns all the resources in the warehouse and the strongbox
     */
    public void testGetAllDashboardResources() {
        ArrayList<Integer> eachDepotCapacity = new ArrayList<>();
        eachDepotCapacity.add(1);
        eachDepotCapacity.add(2);
        eachDepotCapacity.add(3);
        DashBoard dashBoard = new DashBoard(3, eachDepotCapacity, null);

        try {
            dashBoard.getWarehouse().get(1).addResources(Resource.SERVANT, 1);
        } catch (Exception e) {
            fail();
        }

        try {
            dashBoard.getWarehouse().get(2).addResources(Resource.SHIELD, 3);
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
