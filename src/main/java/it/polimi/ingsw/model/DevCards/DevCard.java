package it.polimi.ingsw.model.DevCards;
import java.util.HashMap;
import  it.polimi.ingsw.model.*;

public class DevCard
{
    private HashMap<Resource, Integer> cost;
    private int level;
    private CardColor color;
    private int victoryPoints;


    public DevCard(HashMap<Resource, Integer> cost, int level, CardColor color, int victoryPoints)
    {
        this.cost = cost;
        this.level = level;
        this.color = color;
        this.victoryPoints = victoryPoints;
    }

}