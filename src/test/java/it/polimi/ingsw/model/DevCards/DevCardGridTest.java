package it.polimi.ingsw.model.DevCards;

import it.polimi.ingsw.model.CardColor;
import it.polimi.ingsw.model.ProductionPower;
import it.polimi.ingsw.model.Resource;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.HashMap;

public class DevCardGridTest extends TestCase {
    public void testDevCardGrid(){
        //create infos for DevCard1
        HashMap<Resource, Integer> resources1= new HashMap<>();
        resources1.put(Resource.ROCK,2);
        //create a DevCard with 2 Rock, level 1, green, 3 points
        DevCard card1= new DevCard(resources1,0, CardColor.GREEN,3,new ProductionPower());

        //create infos for DevCard2
        HashMap<Resource, Integer> resources2= new HashMap<>();
        resources1.put(Resource.SHIELD,2);
        //create a DevCard with 2 SHIelDS, level 1, green, 3 points
        DevCard card2= new DevCard(resources2,1, CardColor.GREEN,3,new ProductionPower());

        //create infos for DevCard3
        HashMap<Resource, Integer> resources3= new HashMap<>();
        resources1.put(Resource.SERVANT,2);
        //create a DevCard with 2 servants, level 1, green, 3 points
        DevCard card3= new DevCard(resources3,2, CardColor.GREEN,3,new ProductionPower());

        //create ArrayList of Cards
        ArrayList<DevCard> cards= new ArrayList<>();
        cards.add(card1);
        cards.add(card2);
        cards.add(card3);

        DevCardGrid grid =new DevCardGrid(cards);
        assertEquals(4, grid.getColumnsNumber());
        assertEquals(3, grid.getRowsNumber());
    }

}