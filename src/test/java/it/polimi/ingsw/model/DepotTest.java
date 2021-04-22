package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.ResourcesLimitsException;
import it.polimi.ingsw.exceptions.DepotResourceException;
import junit.framework.TestCase;
import org.junit.Test;


public class DepotTest extends TestCase {
    /**
     * This method tests a successful case of adding resources to a depot.
     */
     @Test
    public void testAddResoucesSuccesful() {
        Depot testDepot= new Depot(4);
        testDepot.setResourceType(Resource.COIN);
        testDepot.setCurrentQuantity(2);
        try{testDepot.addResources(1, Resource.COIN); }
        catch ( ResourcesLimitsException e){fail();}
        catch ( DepotResourceException e){fail();}
        assertEquals(3, testDepot.getCurrentQuantity());
    }

    /***
     * this method deals with the case in which  more resources than the depot maximum capacity are added.
     */
    @Test
    public void testAddResoucesResourcesExcess() {
        Depot testDepot= new Depot(4);
        testDepot.setResourceType(Resource.COIN);
        testDepot.setCurrentQuantity(2);
        try{testDepot.addResources(3, Resource.COIN); fail(); }
        catch ( ResourcesLimitsException e){}
        catch ( DepotResourceException e){fail();}

    }
    /***
     * this method deals with the case in which resources which are not expected are added.
     */
    @Test
    public void testAddResoucesResourceNotPresent() {
        Depot testDepot= new Depot(4);
        testDepot.setResourceType(Resource.COIN);
        testDepot.setCurrentQuantity(2);
        try{testDepot.addResources(1, Resource.ROCK); fail(); }
        catch ( ResourcesLimitsException e){fail();}
        catch ( DepotResourceException e){}

    }
    /**
     * This method tests a successful case of subtracting resources from a depot.
     */
    @Test
    public void testSubResoucesSuccesful() {
        Depot testDepot= new Depot(4);
        testDepot.setResourceType(Resource.COIN);
        testDepot.setCurrentQuantity(2);
        try{testDepot.subResouces(1, Resource.COIN); }
        catch ( ResourcesLimitsException e){fail();}
        catch ( DepotResourceException e){fail();}
        assertEquals(1, testDepot.getCurrentQuantity());
    }
    /***
     * this method deals with the case in which  more resources than the depot minimum capacity are subtracted.
     */
    @Test
    public void testSubResoucesResourcesExcess() {
        Depot testDepot= new Depot(4);
        testDepot.setResourceType(Resource.COIN);
        testDepot.setCurrentQuantity(2);
        try{testDepot.subResouces(3, Resource.COIN); fail(); }
        catch ( ResourcesLimitsException e){}
        catch ( DepotResourceException e){fail();}

    }
    /***
     * this method deals with the case in which resources which are not expected are subtracted.
     */
    @Test
    public void testSubResoucesResourceNotPresent() {
        Depot testDepot= new Depot(4);
        testDepot.setResourceType(Resource.COIN);
        testDepot.setCurrentQuantity(2);
        try{testDepot.subResouces(1, Resource.ROCK); fail(); }
        catch ( ResourcesLimitsException e){fail();}
        catch ( DepotResourceException e){}

    }
    /***
     * this method deals with a successfull case of switching two depots.
     */
    @Test
    public void testSwitchDepotSuccessful() {
        Depot testDepot1= new Depot(4);
        testDepot1.setResourceType(Resource.COIN);
        testDepot1.setCurrentQuantity(2);
        Depot testDepot2= new Depot(4);
        testDepot2.setResourceType(Resource.COIN);
        testDepot2.setCurrentQuantity(3);
        try{
            testDepot1.switchDepot(testDepot2);
            assertEquals(3, testDepot1.getCurrentQuantity());
            assertEquals(2, testDepot2.getCurrentQuantity());
        }
        catch ( ResourcesLimitsException e){fail();}
     }
    /***
     * this method deals with the case in which the second depot has more resources than the first depot can possibly hold.
     */
    @Test
    public void testSwitchDepotUnsuccessfulCase1() {
        Depot testDepot1= new Depot(4);
        testDepot1.setResourceType(Resource.COIN);
        testDepot1.setCurrentQuantity(2);
        Depot testDepot2= new Depot(6);
        testDepot2.setResourceType(Resource.COIN);
        testDepot2.setCurrentQuantity(5);
        try{
            testDepot1.switchDepot(testDepot2);
            fail();
        }
        catch ( ResourcesLimitsException e){}
    }
    /***
     * this method deals with the case in which the first depot has more resources than the second depot can possibly hold.
     */
    @Test
    public void testSwitchDepotUnsuccessfulCase2() {
        Depot testDepot1= new Depot(6);
        testDepot1.setResourceType(Resource.COIN);
        testDepot1.setCurrentQuantity(5);
        Depot testDepot2= new Depot(4);
        testDepot2.setResourceType(Resource.COIN);
        testDepot2.setCurrentQuantity(3);
        try{
            testDepot1.switchDepot(testDepot2);
            fail();
        }
        catch ( ResourcesLimitsException e){}
    }

}