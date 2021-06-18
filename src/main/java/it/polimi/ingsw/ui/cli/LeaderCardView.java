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
    private boolean active;
    private final String idCard;


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
            card.getSelectedLeaderPowers().stream().filter(power -> power instanceof DepositLeaderPower).forEach(power -> ((DepositLeaderPower) power).getCurrentResources().forEach((key, value) -> totalRes.put(key, value + totalRes.get(key))));
        }
        return totalRes;
    }

    public String getLeaderPowerName(int leaderPowerIndex) {
        return card.getLeaderPowers().get(leaderPowerIndex).getClass().getName().substring(34);
    }

    public String depositPowersToString() {
        StringBuilder builder = new StringBuilder();
        IntStream.range(0, card.getLeaderPowers().size()).forEach(index -> {
            LeaderPower power = card.getLeaderPowers().get(index);
            if (power instanceof DepositLeaderPower) {
                builder.append("\033[31;1;4mDEPOSIT OF ").append(card.getCardID().toUpperCase()).append(" \033[0m \n");
                if (getSelected(index)) {
                    builder.append("SELECTED\n");
                } else {
                    builder.append("NOT SELECTED\n");
                }
                int m = 1;

                AtomicInteger count = new AtomicInteger(1);
                ((DepositLeaderPower) power).getCurrentResources().keySet().forEach(resource -> {
                    int current = ((DepositLeaderPower) power).getCurrentResources().get(resource);
                    int max = ((DepositLeaderPower) power).getMaxResources().get(resource);
                    String color = CLI.colorResource(resource);
                    String shape = CLI.shapeResource(resource);
                    builder.append(color).append("   ").append(resource.toString()).append("\n   ");
                    IntStream.range(0, max).forEach(n -> builder.append(color).append("╔═══╗").append(" "));
                    builder.append(Color.reset()).append("\n").append(".").append(count.intValue()).append(" ");
                    IntStream.range(0, current).forEach(n -> builder.append(color).append("║ ").append(shape).append(" ║").append(" "));
                    IntStream.range(0, max - current).forEach(n -> builder.append(color).append("║   ║").append(" "));
                    builder.append(Color.reset()).append("\n   ");
                    IntStream.range(0, max).forEach(n -> builder.append(color).append("╚═══╝").append(" "));
                    builder.append(Color.reset()).append("\n");
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
        build.append(color).append(" ").append(color).append(card.getCardID()).append(" ").append(translateBoolean(isActive())).append(" ").append(color).append(" ").append(Color.reset()).append("\n").append(color).append("╔═").append(color).append("Requirements").append(color).append("═╗").append(Color.reset()).append("\n");
        for (Requirement req : card.getActivationRequirement()) {
            if (req instanceof ResourcesRequirement) {
                ((ResourcesRequirement) req).getResources().keySet().forEach(resource -> build.append(color).append("║     ").append(CLI.colorResource(resource)).append(((ResourcesRequirement) req).getResources().get(resource)).append(" ").append(CLI.shapeResource(resource)).append(color).append("      ║").append(Color.reset()).append("\n"));
            } else if (req instanceof LevelCardRequirement) {
                build.append(color).append("║  ").append(translateColor(((LevelCardRequirement) req).getType())).append(((LevelCardRequirement) req).getQuantity()).append(" ▊").append(" level").append(((LevelCardRequirement) req).getLevel()).append(color).append("  ║").append(Color.reset()).append("\n");
            } else {
                build.append(color).append("║      ").append(translateColor(((LevellessCardRequirement) req).getType())).append(((LevellessCardRequirement) req).getQuantity()).append(" ▊").append(color).append("     ║").append(Color.reset()).append("\n");
            }
        }
        IntStream.range(0,card.getLeaderPowers().size()).forEach(n->{
            ArrayList<LeaderPower> LP=card.getLeaderPowers();
            build.append(color).append("╠══").append(color).append(LP.get(n).getClass().getName(), 34, 38).append("Power ").append(translateBoolean(getSelected(n))).append(color).append("═╣").append(Color.reset()).append("\n");
            if (LP.get(n) instanceof DepositLeaderPower) {
                ((DepositLeaderPower) LP.get(n)).getCurrentResources().keySet().forEach(resource -> build.append(color).append("║ ").append(CLI.colorResource(resource)).append(((DepositLeaderPower) LP.get(n)).getCurrentResources().get(resource)).append(" ").append(CLI.shapeResource(resource)).append(" out of ").append(((DepositLeaderPower) LP.get(n)).getMaxResources().get(resource)).append(color).append(" ║").append(Color.reset()).append("\n"));
            } else if (LP.get(n) instanceof DiscountLeaderPower) {
                ((DiscountLeaderPower) LP.get(n)).getDiscount().keySet().forEach(resource -> build.append(color).append("║     ").append(CLI.colorResource(resource)).append("-").append(((DiscountLeaderPower) LP.get(n)).getDiscount().get(resource)).append(" ").append(CLI.shapeResource(resource)).append(color).append("     ║").append(Color.reset()).append("\n"));
            } else if (LP.get(n) instanceof ExtraResourceLeaderPower) {
                Resource resource = ((ExtraResourceLeaderPower) LP.get(n)).getResourceType();
                build.append(color).append("║     ").append("● = ").append(CLI.colorResource(resource)).append(CLI.shapeResource(resource)).append(color).append("    ║").append(Color.reset()).append("\n");
            } else if (LP.get(n) instanceof ProductionLeaderPower) {
                for (Resource resource : ((ProductionLeaderPower) LP.get(n)).getEffectPower().getConsumedResources().keySet()) {
                    build.append(color).append("║      ").append(CLI.colorResource(resource)).append(((ProductionLeaderPower) LP.get(n)).getEffectPower().getConsumedResources().get(resource)).append(" ").append(CLI.shapeResource(resource)).append(color).append("     ║").append(Color.reset()).append("\n");
                }
                if (((ProductionLeaderPower) LP.get(n)).getEffectPower().getRequiredResourceOfChoice() != 0) {
                    build.append(color).append("║      ").append(color).append(+((ProductionLeaderPower) LP.get(n)).getEffectPower().getRequiredResourceOfChoice()).append(" ").append("?").append(color).append("     ║").append(Color.reset()).append("\n");
                }
                build.append(color).append("║    ").append(color).append("--->>> ").append(color).append("   ║ ").append(Color.reset()).append("\n");
                for (Resource resource : ((ProductionLeaderPower) LP.get(n)).getEffectPower().getProducedResources().keySet()) {
                    build.append(color).append("║      ").append(CLI.colorResource(resource)).append(((ProductionLeaderPower) LP.get(n)).getEffectPower().getProducedResources().get(resource)).append(" ").append(CLI.shapeResource(resource)).append(color).append("     ║").append(Color.reset()).append("\n");
                }
                if (((ProductionLeaderPower) LP.get(n)).getEffectPower().getFaithPointsProduced() != 0) {
                    build.append(color).append("║      ").append(color).append(((ProductionLeaderPower) LP.get(n)).getEffectPower().getFaithPointsProduced()).append(" ").append("+").append(color).append("     ║").append(Color.reset()).append("\n");
                }
                if (((ProductionLeaderPower) LP.get(n)).getEffectPower().getProducedResourceOfChoice() != 0) {
                    build.append(color).append("║      ").append(color).append(+((ProductionLeaderPower) LP.get(n)).getEffectPower().getProducedResourceOfChoice()).append(" ").append("?").append(color).append("     ║").append(Color.reset()).append("\n");
                }
            }
        });

        build.append(color).append("╠═══").append(color).append("VPoints").append(color).append("════╣").append(Color.reset()).append("\n");
        if (card.getVictoryPoints() < 10) {
            build.append(color).append("║       ").append(color).append(card.getVictoryPoints()).append(color).append("      ║").append(Color.reset()).append("\n");
        } else
            build.append(color).append("║     ").append(color).append(card.getVictoryPoints()).append(color).append("      ║").append(Color.reset()).append("\n");
        build.append(color).append("╚════").append(color).append(color).append("══════════╝").append(Color.reset()).append("\n");
        build.append((color + "     " + color + color + "           " + Color.reset() + "\n").repeat(3));
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
