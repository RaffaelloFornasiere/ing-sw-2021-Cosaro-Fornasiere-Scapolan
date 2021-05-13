package it.polimi.ingsw.model.DevCards;

import it.polimi.ingsw.model.CardColor;
import it.polimi.ingsw.model.ProductionPower;
import it.polimi.ingsw.model.Resource;

import java.util.HashMap;

public class DevCard
{
    private HashMap<Resource, Integer> cost;
    private int level;
    private CardColor color;
    private int victoryPoints;
    private ProductionPower productionPower;


    public DevCard(HashMap<Resource, Integer> cost, int level, CardColor color, int victoryPoints, ProductionPower productionPower)
    {
        if(level <0) throw new IllegalArgumentException("The card level cannot be negative");
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

    /*public static void main(String[] args) {
        int index = 1;
        CardColor color = CardColor.GREEN;
        int level = 1;

        HashMap<Resource, Integer> cost = new HashMap<>();
        cost.put(Resource.SHIELD, 2);

        HashMap<Resource, Integer> req = new HashMap<>();
        cost.put(Resource.COIN, 1);

        HashMap<Resource, Integer> prod = new HashMap<>();
        //prod.put(Resource.SERVANT, 1);

        int fp = 1;

        int vp = 1;



        ProductionPower pp = new ProductionPower(req, prod, 0, 0, fp);

        DevCard c = new DevCard(cost, level, color, vp, pp);

        Gson gson = new Gson();

        String JSONDevCard = gson.toJson(c, DevCard.class);

        System.out.println(JSONDevCard);

        File file = new File("C:\\Users\\Leo\\IdeaProjects\\ing-sw-2021-Cosaro-Fornasiere-Scapolan\\src\\main\\resources\\DevCard" + index + ".json");
        try {
            FileWriter w = new FileWriter(file);
            w.write(JSONDevCard);
            w.flush();
            w.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/
}
