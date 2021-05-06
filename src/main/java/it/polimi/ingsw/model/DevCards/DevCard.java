package it.polimi.ingsw.model.DevCards;
import java.util.HashMap;
import  it.polimi.ingsw.model.*;

public class DevCard
{
    private HashMap<Resource, Integer> cost;
    private int level;
    private CardColor color;
    private int victoryPoints;
    private ProductionPower productionPower;


    public DevCard(HashMap<Resource, Integer> cost, int level, CardColor color, int victoryPoints, ProductionPower productionPower)
    {
        if(level <= 0) throw new IllegalArgumentException("The card level cannot be negative");
        this.cost = cost;
        this.level = level;
        this.color = color;
        this.victoryPoints = victoryPoints;
        this.productionPower = productionPower;
    }


    public HashMap<Resource, Integer> getCost() {
        return cost;
    }

    public int getLevel() {
        return level;
    }

    public CardColor getColor() {
        return color;
    }

    public int getVictoryPoints() {
        return victoryPoints;
    }

    public ProductionPower getProductionPower() {
        return productionPower;
    }


}
