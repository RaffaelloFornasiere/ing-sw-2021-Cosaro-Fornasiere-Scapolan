package it.polimi.ingsw.model.DevCards;

import it.polimi.ingsw.model.CardColor;
import it.polimi.ingsw.model.ProductionPower;
import it.polimi.ingsw.model.Resource;

import java.util.HashMap;

/**
 * Immutable class
 */
public class DevCard {
    private String cardID;
    private final HashMap<Resource, Integer> cost;
    private final int level;
    private final CardColor color;
    private final int victoryPoints;
    private final ProductionPower productionPower;

    @SuppressWarnings("unchecked")
    public DevCard(HashMap<Resource, Integer> cost, int level, CardColor color, int victoryPoints, ProductionPower productionPower) {
        if (level < 0) throw new IllegalArgumentException("The card level cannot be negative");
        this.cost = (HashMap<Resource, Integer>) cost.clone();
        this.level = level;
        this.color = color;
        this.victoryPoints = victoryPoints;
        this.productionPower = productionPower;
    }


    @Override
    public boolean equals(Object o) {
        if (o.getClass() != getClass())return false;
        if (this == o)return true;

        DevCard card = (DevCard) o;


        int res = 1;
        res *= card.cardID.equals(cardID) ? 1 : 0;
        res *= (card.level == level) ? 1 : 0;
        res *= (card.color.equals(color)) ? 1 : 0;
        res *= (card.cost.equals(cost)) ? 1 : 0;
        res *= (card.productionPower.equals(productionPower)) ? 1 : 0;
        res *= (card.victoryPoints == victoryPoints) ? 1 : 0;
        return res == 1;
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

    public boolean checkCost(HashMap<Resource, Integer> availableResources) {
        for (Resource resource : cost.keySet()) {
            if (cost.get(resource) > availableResources.getOrDefault(resource, 0))
                return false;
        }
        return true;
    }


}
