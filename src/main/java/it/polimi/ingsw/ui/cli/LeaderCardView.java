package it.polimi.ingsw.ui.cli;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.exceptions.ResourcesLimitsException;
import it.polimi.ingsw.model.CardColor;
import it.polimi.ingsw.model.LeaderCards.*;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.utilities.GsonInheritanceAdapter;
import it.polimi.ingsw.utilities.GsonPairAdapter;
import it.polimi.ingsw.utilities.Pair;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.stream.IntStream;

public class LeaderCardView {
    private LeaderCard card;


    public LeaderCardView(String path) {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Requirement.class, new GsonInheritanceAdapter<Requirement>());
        builder.registerTypeAdapter(LeaderPower.class, new GsonInheritanceAdapter<LeaderPower>());
        builder.registerTypeAdapter(Pair.class, new GsonPairAdapter());
        Gson gson = builder.create();
        try {
            String cardJSON = Files.readString(Paths.get("src\\main\\resources\\" + path + ".json"));
            card = gson.fromJson(cardJSON,LeaderCard.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void updateDepositLeaderPower(int leaderPowerIndex, HashMap<Resource, Integer> storedResources)  {
        if(card.getLeaderPowers().get(leaderPowerIndex) instanceof DepositLeaderPower ){
            try {
                HashMap<Resource,Integer> prev= ((DepositLeaderPower) card.getLeaderPowers().get(leaderPowerIndex)).getCurrentResources();
                HashMap<Resource,Integer> diff= new HashMap<>();
                prev.keySet().forEach(resource -> diff.put(resource, storedResources.get(resource)-prev.get(resource)));
                ((DepositLeaderPower) card.getLeaderPowers().get(leaderPowerIndex)).addResources(diff);
            } catch (ResourcesLimitsException e) {
                e.printStackTrace();
            }
        }
    }

    public String getLeaderPowerName( int leaderPowerIndex){
        return card.getLeaderPowers().get(leaderPowerIndex).getClass().getName().substring(34);
    }


    public String translateColor(CardColor c) {
        if (c == CardColor.BLUE) return Color.BLUE.getAnsiCode();
        if (c == CardColor.VIOLET) return Color.RED.getAnsiCode();
        if (c == CardColor.GREEN) return Color.GREEN.getAnsiCode();
        else return Color.YELLOW.getAnsiCode();
    }

    private String shapeResource(Resource res) {
        if (res == Resource.SERVANT) return "■";
        if (res == Resource.SHIELD) return  "◆";
        if (res == Resource.COIN) return    "●";
        else return "▼";
    }

    private String colorResource(Resource res) {
        if (res == Resource.SERVANT) return Color.BLUE.getAnsiCode();
        if (res == Resource.SHIELD) return Color.RED.getAnsiCode();
        if (res == Resource.COIN) return Color.GREEN.getAnsiCode();
        else return Color.YELLOW.getAnsiCode();
    }

    private String translateBoolean(Boolean b) {
        if (b) return "✓";
        else return   "✗";
    }


    public String toString() {
        String color = Color.WHITE.getAnsiCode();
        StringBuilder build = new StringBuilder();
        build.append(
               color+ "   " + color + card.getCardID() + color + "  " + Color.reset() + "\n" +
                       color+ "╔═" + color + "Requirements" + color + "═╗" + Color.reset() + "\n");
        for (Requirement req : card.getActivationRequirement()) {
            if (req instanceof ResourcesRequirement) {
                ((ResourcesRequirement) req).getResources().keySet().forEach(resource -> {
                    build.append(color+"║     " + colorResource(resource) + ((ResourcesRequirement) req).getResources().get(resource) + " " + shapeResource(resource) + color + "      ║" + Color.reset() + "\n");
                });
            } else if (req instanceof LevelCardRequirement) {
                build.append(color+"║  " + translateColor(((LevelCardRequirement) req).getType()) + ((LevelCardRequirement) req).getQuantity() + " ▊" + " level" + ((LevelCardRequirement) req).getLevel() + color + "  ║" + Color.reset() + "\n");
            } else {
                build.append(color+"║      " + translateColor(((LevellessCardRequirement) req).getType()) + ((LevellessCardRequirement) req).getQuantity()+ " ▊" + color + "     ║" + Color.reset() + "\n");
            }
        }
        for (Pair<LeaderPower, Boolean> power : card.getBooleanPowers()) {
            build.append(color+"╠══" + color + power.getKey().getClass().getName().substring(34, 38) + "Power " + translateBoolean(power.getValue()) + color + "═╣" + Color.reset() + "\n");
            if (power.getKey() instanceof DepositLeaderPower) {
                ((DepositLeaderPower) power.getKey()).getCurrentResources().keySet().forEach(resource -> {
                    build.append(color+"║ " + colorResource(resource) + ((DepositLeaderPower) power.getKey()).getCurrentResources().get(resource) + " " + shapeResource(resource) + " out of " + ((DepositLeaderPower) power.getKey()).getMaxResources().get(resource) + color + " ║" + Color.reset() + "\n");

                });
            } else if (power.getKey() instanceof DiscountLeaderPower) {
                ((DiscountLeaderPower) power.getKey()).getDiscount().keySet().forEach(resource -> {
                    build.append(color+"║     " + colorResource(resource) + "-" + ((DiscountLeaderPower) power.getKey()).getDiscount().get(resource) + " " + shapeResource(resource) + color + "     ║" + Color.reset() + "\n");
                });
            } else if (power.getKey() instanceof ExtraResourceLeaderPower) {
                Resource resource = ((ExtraResourceLeaderPower) power.getKey()).getResourceType();
                build.append(color+"║     " + "● = " + colorResource(resource) + shapeResource(resource) + color + "    ║" + Color.reset() + "\n");
            } else if (power.getKey() instanceof ProductionLeaderPower) {
                for (Resource resource : ((ProductionLeaderPower) power.getKey()).getEffectPower().getConsumedResources().keySet()) {
                    build.append(color+"║      " + colorResource(resource) + ((ProductionLeaderPower) power.getKey()).getEffectPower().getConsumedResources().get(resource) + " " + shapeResource(resource) + color + "     ║" + Color.reset() + "\n");
                }
                if(((ProductionLeaderPower) power.getKey()).getEffectPower().getRequiredResourceOfChoice() != 0){
                    build.append(color + "║      " + color+ + ((ProductionLeaderPower) power.getKey()).getEffectPower().getRequiredResourceOfChoice() + " " +"?"  + color + "     ║" + Color.reset() + "\n" );
                }
                build.append(color+"║    " + color + "--->>> " + color + "   ║ " + Color.reset() + "\n");
                for (Resource resource : ((ProductionLeaderPower) power.getKey()).getEffectPower().getProducedResources().keySet()) {
                    build.append(color+"║      " + colorResource(resource) + ((ProductionLeaderPower) power.getKey()).getEffectPower().getProducedResources().get(resource) + " " + shapeResource(resource) + color + "     ║" + Color.reset() + "\n");
                }
                if (((ProductionLeaderPower) power.getKey()).getEffectPower().getFaithPointsProduced() != 0) {
                    build.append(color + "║      " + color +((ProductionLeaderPower) power.getKey()).getEffectPower().getFaithPointsProduced() + " " + "+" + color + "     ║" + Color.reset() + "\n");
                }
                if(((ProductionLeaderPower) power.getKey()).getEffectPower().getProducedResourceOfChoice() != 0){
                    build.append(color + "║      " + color+ + ((ProductionLeaderPower) power.getKey()).getEffectPower().getProducedResourceOfChoice() + " " +"?"  + color + "     ║" + Color.reset() + "\n" );
                }
            }
        }

        build.append(color+"╠═══" + color + "VPoints" + color + "════╣" + Color.reset() + "\n");
        if (card.getVictoryPoints() < 10) {
            build.append(color+"║       " + color + card.getVictoryPoints() + color + "      ║" + Color.reset() + "\n");
        } else
            build.append(color + "║     " + color + card.getVictoryPoints() + color + "      ║" + Color.reset() + "\n");
        build.append(color+"╚════" + color + color + "══════════╝" + Color.reset() + "\n");
        for (int i = 0; i < 3; i++) {
            build.append(color+"     " + color + color + "           " + Color.reset() + "\n");
        }
        return build.toString();
    }

    public static void main(String[] args) {

        Panel panel = new Panel(10000, 15, System.out);
        IntStream.range(1, 17).forEach(n -> {
            LeaderCardView card = new LeaderCardView("LeaderCard" + n);
            DrawableObject obj1 = new DrawableObject(card.toString(), 40 * (n - 1), 0);
            panel.addItem(obj1);

        });

        panel.show();
    }
}