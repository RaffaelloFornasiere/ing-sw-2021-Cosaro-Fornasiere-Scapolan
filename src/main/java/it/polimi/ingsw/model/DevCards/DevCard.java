package it.polimi.ingsw.model.DevCards;

import it.polimi.ingsw.model.CardColor;
import it.polimi.ingsw.model.ProductionPower;
import it.polimi.ingsw.model.Resource;

import java.util.HashMap;
import java.util.Objects;

/**
 * Immutable class
 * Represents the Development cards of the game
 */
public class DevCard {
    private String cardID;
    private final HashMap<Resource, Integer> cost;
    private final int level;
    private final CardColor color;
    private final int victoryPoints;
    private final ProductionPower productionPower;

    /**
     *
     * @param cost required resources to buy the card
     * @param level level of the card
     * @param color color of the card
     * @param victoryPoints victory points provided by the card
     * @param productionPower power production of the card
     */
    public DevCard(HashMap<Resource, Integer> cost, int level, CardColor color, int victoryPoints, ProductionPower productionPower) {
        if (level < 0) throw new IllegalArgumentException("The card level cannot be negative");
        this.cost = new HashMap<>(cost);
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
        res *= Objects.equals(cardID, card.cardID) ? 1 : 0;
        res *= (card.level == level) ? 1 : 0;
        res *= Objects.equals(card.color, color) ? 1 : 0;
        res *= Objects.equals(card.cost, cost) ? 1 : 0;
        res *= Objects.equals(card.productionPower, productionPower) ? 1 : 0;
        res *= (card.victoryPoints == victoryPoints) ? 1 : 0;
        return res == 1;
    }

    /**
     * getter
     * @return the id of the card
     */
    public String getCardID() {
        return cardID;
    }


    /**
     * getter
     * @return the resources required to buy the card. The key of the has
     * represents the type of required and the value is the qty of that
     * resources
     */
    public HashMap<Resource, Integer> getCost() {
        return new HashMap<>(cost);
    }

    /**
     * getter
     * @return the level of the card
     */
    public int getLevel() {
        return level;
    }

    /**
     * getter
     * @return the color of the card
     */
    public CardColor getColor() {
        return color;
    }

    /**
     * getter
     * @return the victory points of the card
     */
    public int getVictoryPoints() {
        return victoryPoints;
    }

    /**
     * getter
     * @return the production power of the card
     */
    public ProductionPower getProductionPower() {
        return productionPower;
    }

    /**
     * checks if @this card is buy with a given set of resources
     * @param availableResources set of resources
     * @return true if @this card can be bought with the set of given resources
     */
    public boolean checkCost(HashMap<Resource, Integer> availableResources) {
        for (Resource resource : cost.keySet()) {
            if (cost.get(resource) > availableResources.getOrDefault(resource, 0))
                return false;
        }
        return true;
    }


}
