package it.polimi.ingsw.ui.cli;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.exceptions.IllegalOperation;
import it.polimi.ingsw.exceptions.NotPresentException;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class LeaderCardView {
    private LeaderCard card;
    boolean active;
    String idCard;


    public LeaderCardView(String leaderCardID) {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Requirement.class, new GsonInheritanceAdapter<Requirement>());
        builder.registerTypeAdapter(LeaderPower.class, new GsonInheritanceAdapter<LeaderPower>());
        builder.registerTypeAdapter(Pair.class, new GsonPairAdapter());
        Gson gson = builder.create();
        try {
            String cardJSON = Files.readString(Paths.get("src\\main\\resources\\" + leaderCardID + ".json"));
            card = gson.fromJson(cardJSON, LeaderCard.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        active = false;
        idCard = card.getCardID();
    }

    public void updateDepositLeaderPower(int leaderPowerIndex, HashMap<Resource, Integer> storedResources) {
        if (card.getLeaderPowers().get(leaderPowerIndex) instanceof DepositLeaderPower) {
            try {
                HashMap<Resource, Integer> prev = ((DepositLeaderPower) card.getLeaderPowers().get(leaderPowerIndex)).getCurrentResources();
                HashMap<Resource, Integer> diff = new HashMap<>();
                prev.keySet().forEach(resource -> diff.put(resource, storedResources.get(resource) - prev.get(resource)));
                ((DepositLeaderPower) card.getLeaderPowers().get(leaderPowerIndex)).addResources(diff);
            } catch (ResourcesLimitsException e) {
                e.printStackTrace();
            }
        }
    }

    public DepotResultMessage updateDepositLeaderPower(int leaderPowerIndex, Resource res) {

        if (!((DepositLeaderPower) card.getLeaderPowers().get(leaderPowerIndex)).getCurrentResources().containsKey(res))
            return DepotResultMessage.INVALID_RES_LEADER;

        if (((DepositLeaderPower) card.getLeaderPowers().get(leaderPowerIndex)).getCurrentResources().get(res) + 1 > ((DepositLeaderPower) card.getLeaderPowers().get(leaderPowerIndex)).getMaxResources().get(res))
            return DepotResultMessage.REACH_MAX_CAP_LEADER;
        try {
            HashMap<Resource, Integer> toAdd = new HashMap<>();
            toAdd.put(res, 1);
            ((DepositLeaderPower) card.getLeaderPowers().get(leaderPowerIndex)).addResources(toAdd);
        } catch (ResourcesLimitsException e) {
            e.printStackTrace();
        }
        return DepotResultMessage.SUCCESSFUL_LEADER;
    }

    public HashMap<Resource, Integer> getTotalResourcesInDepositLeaderPowers() {
        HashMap<Resource, Integer> totalRes = new HashMap<>();
        Arrays.stream(Resource.values()).forEach(res -> totalRes.put(res, 0));
        if (card.getSelectedLeaderPowers().contains(DepositLeaderPower.class)) {
            card.getSelectedLeaderPowers().stream().filter(power -> power instanceof DepositLeaderPower).forEach(power -> {
                ((DepositLeaderPower) power).getCurrentResources().forEach((key, value) -> totalRes.put(key, value + totalRes.get(key)));
            });
        }
        return totalRes;
    }

    public String getLeaderPowerName(int leaderPowerIndex) {
        return card.getLeaderPowers().get(leaderPowerIndex).getClass().getName().substring(34);
    }

    public String depositPowersToString() {
        //TODO here must be changed how selected is handled
        StringBuilder builder = new StringBuilder();
        IntStream.range(0, card.getLeaderPowers().size()).forEach(index -> {
            LeaderPower power = card.getLeaderPowers().get(index);
            if (power instanceof DepositLeaderPower) {
                builder.append("\033[31;1;4mDEPOSIT OF " + card.getCardID().toUpperCase() + " \033[0m \n");
                if (getSelected(index)) {
                    builder.append("SELECTED");
                } else {
                    builder.append("NOT SELECTED");
                }
                int m = 1;

                AtomicInteger count = new AtomicInteger(1);
                ((DepositLeaderPower) power).getCurrentResources().keySet().forEach(resource -> {
                    int current = ((DepositLeaderPower) power).getCurrentResources().get(resource);
                    int max = ((DepositLeaderPower) power).getMaxResources().get(resource);
                    String color = CLI.colorResource(resource);
                    String shape = CLI.shapeResource(resource);
                    builder.append(color + "   " + resource.toString() + "\n   ");
                    IntStream.range(0, max).forEach(n -> builder.append(color + "╔═══╗" + " "));
                    builder.append(Color.reset() + "\n" + "." + count.intValue() + " ");
                    IntStream.range(0, current).forEach(n -> builder.append(color + "║ " + shape + " ║" + " "));
                    IntStream.range(0, max - current).forEach(n -> builder.append(color + "║   ║" + " "));
                    builder.append(Color.reset() + "\n   ");
                    IntStream.range(0, max).forEach(n -> builder.append(color + "╚═══╝" + " "));
                    builder.append(Color.reset() + "\n");
                    count.getAndIncrement();
                });

            }

    });
            return builder.toString();

}


    public String translateColor(CardColor c) {
        if (c == CardColor.BLUE) return Color.BLUE.getAnsiCode();
        if (c == CardColor.VIOLET) return Color.RED.getAnsiCode();
        if (c == CardColor.GREEN) return Color.GREEN.getAnsiCode();
        else return Color.YELLOW.getAnsiCode();
    }


    private String translateBoolean(Boolean b) {
        if (b) return "✓";
        else return "✗";
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean getSelected(int index) {
        return card.getSelectedLeaderPowers().contains(card.getLeaderPowers().get(index));
    }

    public boolean isActive() {
        return active;
    }

    public String getIdCard() {
        return idCard;
    }

    public ArrayList<Integer> getSelectablePowersIndexes() {
        ArrayList<LeaderPower> leaderPowers = card.getLeaderPowers();
        ArrayList<Integer> indexes = new ArrayList<>();
        for (int i = 0; i < leaderPowers.size(); i++) {
            if (!(leaderPowers.get(i) instanceof DepositLeaderPower))
                indexes.add(i);
        }
        return indexes;
    }


    public ArrayList<LeaderPower> getLeaderPowersActive() {
        return card.getSelectedLeaderPowers();
    }

    public String toString() {
        String color = Color.WHITE.getAnsiCode();
        StringBuilder build = new StringBuilder();
        //TODO here must be changed how selected is handled
        build.append(
                color + " " + color + card.getCardID() + " " + translateBoolean(/*selected*/true) + " " + color + " " + Color.reset() + "\n" +
                        color + "╔═" + color + "Requirements" + color + "═╗" + Color.reset() + "\n");
        for (Requirement req : card.getActivationRequirement()) {
            if (req instanceof ResourcesRequirement) {
                ((ResourcesRequirement) req).getResources().keySet().forEach(resource -> {
                    build.append(color + "║     " + CLI.colorResource(resource) + ((ResourcesRequirement) req).getResources().get(resource) + " " + CLI.shapeResource(resource) + color + "      ║" + Color.reset() + "\n");
                });
            } else if (req instanceof LevelCardRequirement) {
                build.append(color + "║  " + translateColor(((LevelCardRequirement) req).getType()) + ((LevelCardRequirement) req).getQuantity() + " ▊" + " level" + ((LevelCardRequirement) req).getLevel() + color + "  ║" + Color.reset() + "\n");
            } else {
                build.append(color + "║      " + translateColor(((LevellessCardRequirement) req).getType()) + ((LevellessCardRequirement) req).getQuantity() + " ▊" + color + "     ║" + Color.reset() + "\n");
            }
        }
        for (Pair<LeaderPower, Boolean> power : card.getBooleanPowers()) {
            build.append(color + "╠══" + color + power.getKey().getClass().getName().substring(34, 38) + "Power " + translateBoolean(power.getValue()) + color + "═╣" + Color.reset() + "\n");
            if (power.getKey() instanceof DepositLeaderPower) {
                ((DepositLeaderPower) power.getKey()).getCurrentResources().keySet().forEach(resource -> {
                    build.append(color + "║ " + CLI.colorResource(resource) + ((DepositLeaderPower) power.getKey()).getCurrentResources().get(resource) + " " + CLI.shapeResource(resource) + " out of " + ((DepositLeaderPower) power.getKey()).getMaxResources().get(resource) + color + " ║" + Color.reset() + "\n");

                });
            } else if (power.getKey() instanceof DiscountLeaderPower) {
                ((DiscountLeaderPower) power.getKey()).getDiscount().keySet().forEach(resource -> {
                    build.append(color + "║     " + CLI.colorResource(resource) + "-" + ((DiscountLeaderPower) power.getKey()).getDiscount().get(resource) + " " + CLI.shapeResource(resource) + color + "     ║" + Color.reset() + "\n");
                });
            } else if (power.getKey() instanceof ExtraResourceLeaderPower) {
                Resource resource = ((ExtraResourceLeaderPower) power.getKey()).getResourceType();
                build.append(color + "║     " + "● = " + CLI.colorResource(resource) + CLI.shapeResource(resource) + color + "    ║" + Color.reset() + "\n");
            } else if (power.getKey() instanceof ProductionLeaderPower) {
                for (Resource resource : ((ProductionLeaderPower) power.getKey()).getEffectPower().getConsumedResources().keySet()) {
                    build.append(color + "║      " + CLI.colorResource(resource) + ((ProductionLeaderPower) power.getKey()).getEffectPower().getConsumedResources().get(resource) + " " + CLI.shapeResource(resource) + color + "     ║" + Color.reset() + "\n");
                }
                if (((ProductionLeaderPower) power.getKey()).getEffectPower().getRequiredResourceOfChoice() != 0) {
                    build.append(color + "║      " + color + +((ProductionLeaderPower) power.getKey()).getEffectPower().getRequiredResourceOfChoice() + " " + "?" + color + "     ║" + Color.reset() + "\n");
                }
                build.append(color + "║    " + color + "--->>> " + color + "   ║ " + Color.reset() + "\n");
                for (Resource resource : ((ProductionLeaderPower) power.getKey()).getEffectPower().getProducedResources().keySet()) {
                    build.append(color + "║      " + CLI.colorResource(resource) + ((ProductionLeaderPower) power.getKey()).getEffectPower().getProducedResources().get(resource) + " " + CLI.shapeResource(resource) + color + "     ║" + Color.reset() + "\n");
                }
                if (((ProductionLeaderPower) power.getKey()).getEffectPower().getFaithPointsProduced() != 0) {
                    build.append(color + "║      " + color + ((ProductionLeaderPower) power.getKey()).getEffectPower().getFaithPointsProduced() + " " + "+" + color + "     ║" + Color.reset() + "\n");
                }
                if (((ProductionLeaderPower) power.getKey()).getEffectPower().getProducedResourceOfChoice() != 0) {
                    build.append(color + "║      " + color + +((ProductionLeaderPower) power.getKey()).getEffectPower().getProducedResourceOfChoice() + " " + "?" + color + "     ║" + Color.reset() + "\n");
                }
            }
        }

        build.append(color + "╠═══" + color + "VPoints" + color + "════╣" + Color.reset() + "\n");
        if (card.getVictoryPoints() < 10) {
            build.append(color + "║       " + color + card.getVictoryPoints() + color + "      ║" + Color.reset() + "\n");
        } else
            build.append(color + "║     " + color + card.getVictoryPoints() + color + "      ║" + Color.reset() + "\n");
        build.append(color + "╚════" + color + color + "══════════╝" + Color.reset() + "\n");
        for (int i = 0; i < 3; i++) {
            build.append(color + "     " + color + color + "           " + Color.reset() + "\n");
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
        IntStream.range(1, 17).forEach(n -> {
            LeaderCardView card = new LeaderCardView("LeaderCard" + n);
            //card.setSelected(true);
            System.out.println(card.depositPowersToString());
        });
    }


    public void setPowerSelectionState(int index, Boolean newState) {
        try {
            if (newState)
                card.selectLeaderPower(card.getLeaderPowers().get(index));
            else
                card.deselectLeaderPower(card.getLeaderPowers().get(index));
        } catch (NotPresentException | IllegalOperation ignore) {
        }
    }


}
