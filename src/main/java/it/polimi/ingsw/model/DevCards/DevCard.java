package it.polimi.ingsw.model.DevCards;

import com.google.gson.Gson;
import it.polimi.ingsw.model.CardColor;
import it.polimi.ingsw.model.ProductionPower;
import it.polimi.ingsw.model.Resource;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Immutable class
 */
public class DevCard
{
    private String cardID;
    private final HashMap<Resource, Integer> cost;
    private final int level;
    private final CardColor color;
    private final int victoryPoints;
    private final ProductionPower productionPower;

    @SuppressWarnings("unchecked")
    public DevCard(HashMap<Resource, Integer> cost, int level, CardColor color, int victoryPoints, ProductionPower productionPower)
    {
        if(level <0) throw new IllegalArgumentException("The card level cannot be negative");
        this.cost = (HashMap<Resource, Integer>) cost.clone();
        this.level = level;
        this.color = color;
        this.victoryPoints = victoryPoints;
        this.productionPower = productionPower;
    }

    public String getCardID() {
        return cardID;
    }

    @SuppressWarnings("unchecked")
    public HashMap<Resource, Integer> getCost() {
        return (HashMap<Resource, Integer>) cost.clone();
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

    public boolean checkCost(HashMap<Resource, Integer> availableResources){
        for(Resource resource: cost.keySet()) {
            if(cost.get(resource)>availableResources.getOrDefault(resource, 0))
                return false;
        }
        return true;
    }


}
