package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.DepotResourceException;
import it.polimi.ingsw.exceptions.ResourcesLimitsException;
import junit.framework.TestCase;
import org.junit.Test;


public class DepotTest extends TestCase {
    /**
     * This method tests a successful case of adding resources to a depot.
     */
     @Test
    public void testAddResourcesSuccessful() {
        Depot testDepot= new Depot(4);
        try{testDepot.addResources(Resource.COIN, 3); }
        catch ( ResourcesLimitsException e){fail();}
        catch ( DepotResourceException e){fail();}
        assertEquals(3, testDepot.getCurrentQuantity());
        assertEquals(Resource.COIN, testDepot.getResourceType());
    }

    /***
     * this method deals with the case in which  more resources than the depot maximum capacity are added.
     */
    @Test
    public void testAddResourcesResourcesExcess() {
        Depot testDepot= new Depot(4);
        try{testDepot.addResources(Resource.COIN, 5); fail(); }
        catch ( ResourcesLimitsException e){}
        catch ( DepotResourceException e){fail();}

    }
    /***
     * this method deals with the case in which resources which are not expected are added.
     */
    @Test
    public void testAddResourcesResourceNotPresent() {
        Depot testDepot= new Depot(4);
        try {
            testDepot.addResources(Resource.SHIELD, 2);
        } catch (Exception e) {
            fail();
        }
        try{testDepot.addResources(Resource.ROCK, 1); fail(); }
        catch ( ResourcesLimitsException e){fail();}
        catch ( DepotResourceException e){}

    }
    /**
     * This method tests a successful case of subtracting resources from a depot.
     */
    @Test
    public void testSubResourcesSuccessful() {
        Depot testDepot= new Depot(4);
        try {
            testDepot.addResources(Resource.COIN, 2);
        } catch (Exception e) {
            fail();
        }
        try{testDepot.subResources(Resource.COIN, 1); }
        catch ( ResourcesLimitsException e){fail();}
        catch ( DepotResourceException e){fail();}
        assertEquals(1, testDepot.getCurrentQuantity());
    }
    /***
     * this method deals with the case in which  more resources than the depot minimum capacity are subtracted.
     */
    @Test
    public void testSubResourcesResourcesExcess() {
        Depot testDepot= new Depot(4);
        try {
            testDepot.addResources(Resource.COIN, 2);
        } catch (Exception e) {
            fail();
        }
        try{testDepot.subResources(Resource.COIN, 3); fail(); }
        catch ( ResourcesLimitsException e){}
        catch ( DepotResourceException e){fail();}

    }
    /***
     * this method deals with the case in which resources which are not expected are subtracted.
     */
    @Test
    public void testSubResourcesResourceNotPresent() {
        Depot testDepot= new Depot(4);
        try {
            testDepot.addResources(Resource.SHIELD, 2);
        } catch (Exception e) {
            fail();
        }
        try{testDepot.subResources(Resource.ROCK, 1); fail(); }
        catch ( ResourcesLimitsException e){fail();}
        catch ( DepotResourceException e){}

    }
    /***
     * this method deals with a successfull case of switching two depots.
     */
    @Test
    public void testSwitchDepotSuccessful() {
        Depot testDepot1= new Depot(4);
        try {
            testDepot1.addResources(Resource.SHIELD, 2);
        } catch (Exception e) {
            fail();
        }
        Depot testDepot2= new Depot(4);
        try {
            testDepot2.addResources(Resource.COIN, 3);
        } catch (Exception e) {
            fail();
        }
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
        try {
            testDepot1.addResources(Resource.SHIELD, 2);
        } catch (Exception e) {
            fail();
        }
        Depot testDepot2= new Depot(6);
        try {
            testDepot2.addResources(Resource.COIN, 5);
        } catch (Exception e) {
            fail();
        }
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
        try {
            testDepot1.addResources(Resource.SHIELD, 5);
        } catch (Exception e) {
            fail();
        }
        Depot testDepot2= new Depot(4);
        try {
            testDepot2.addResources(Resource.COIN, 2);
        } catch (Exception e) {
            fail();
        }
        try{
            testDepot1.switchDepot(testDepot2);
            fail();
        }
        catch ( ResourcesLimitsException e){}
    }

}