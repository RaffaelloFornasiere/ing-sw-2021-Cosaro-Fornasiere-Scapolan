package it.polimi.ingsw.ui.cli;

import com.google.gson.Gson;
import it.polimi.ingsw.model.CardColor;
import it.polimi.ingsw.model.DevCards.DevCard;
import it.polimi.ingsw.model.Resource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.IntStream;

public class DevCardView {
    private DevCard card;


    public DevCardView(String path) {
        Gson gson = new Gson();
        try {
            String cardJSON = Files.readString(Paths.get("src\\main\\resources\\"+ path+".json"));
            card= gson.fromJson(cardJSON,DevCard.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String translateColor(CardColor c) {
        if (c == CardColor.BLUE) return Color.BLUE.getAnsiCode();
        if (c == CardColor.VIOLET) return Color.RED.getAnsiCode();
        if (c == CardColor.GREEN) return Color.GREEN.getAnsiCode();
        else return Color.YELLOW.getAnsiCode();
    }


    public String toString() {
        String color = translateColor(card.getColor());
        StringBuilder build = new StringBuilder();
        build.append(
                color + "   " + color + card.getCardID() + color + "  " + Color.reset() + "\n" +
                        color + "╔════" + color + "Level" + color + "════╗" + Color.reset() + "\n" +
                        color + "║      " + color + card.getLevel() + color + "      ║" + Color.reset() + "\n" +
                        color + "╠════" + color + "Cost" + color + "═════╣" + Color.reset() + "\n");
        for (Resource resource : card.getCost().keySet()) {
            build.append(color + "║     " + CLI.colorResource(resource) + card.getCost().get(resource) + " " + CLI.shapeResource(resource) + color + "     ║" + Color.reset() + "\n");
        }
        build.append(color + "╠═" + color + "Production" + color + "══╣" + Color.reset() + "\n");
        for (Resource resource : card.getProductionPower().getConsumedResources().keySet()) {
            build.append(color + "║     " + CLI.colorResource(resource) + card.getProductionPower().getConsumedResources().get(resource) + " " + CLI.shapeResource(resource) + color + "     ║" + Color.reset() + "\n");
        }
        if (card.getProductionPower().getRequiredResourceOfChoice() != 0) {
            build.append(color + "║     " + color + +card.getProductionPower().getRequiredResourceOfChoice() + " " + "?" + color + "     ║" + Color.reset() + "\n");
        }
        build.append(color + "║   " + color + "--->>> " + color + "   ║ " + Color.reset() + "\n");
        for (Resource resource : card.getProductionPower().getProducedResources().keySet()) {
            build.append(color + "║     " + CLI.colorResource(resource) + card.getProductionPower().getProducedResources().get(resource) + " " + CLI.shapeResource(resource) + color + "     ║" + Color.reset() + "\n");
        }
        if (card.getProductionPower().getFaithPointsProduced() != 0) {
            build.append(color + "║     " + color + +card.getProductionPower().getFaithPointsProduced() + " " + "+" + color + "     ║" + Color.reset() + "\n");
        }
        if (card.getProductionPower().getProducedResourceOfChoice() != 0) {
            build.append(color + "║     " + color + +card.getProductionPower().getProducedResourceOfChoice() + " " + "?" + color + "     ║" + Color.reset() + "\n");
        }


        build.append(color + "╠═══" + color + "VPoints" + color + "═══╣" + Color.reset() + "\n");
        if (card.getVictoryPoints() < 10) {
            build.append(color + "║      " + color + card.getVictoryPoints() + color + "      ║" + Color.reset() + "\n");
        } else
            build.append(color + "║     " + color + card.getVictoryPoints() + color + "      ║" + Color.reset() + "\n");
        build.append(color + "╚════" + color + color + "═════════╝" + Color.reset() + "\n");
        for (int i = 0; i < 5; i++) {
            build.append(color + "     " + color + color + "          " + Color.reset() + "\n");
        }
        return build.toString();
    }
    public static String emptySlot(int heigth) {
        String color = Color.WHITE.getAnsiCode();
        StringBuilder build = new StringBuilder();
        build.append(color + "   " + color + "        " + color + "  " + Color.reset() + "\n" +
        color + "╔════" + color + "═════" + color + "════╗" + Color.reset() + "\n" );
        for (int i = 0; i < heigth - 6; i++) {
            build.append(color + "║    " + color + "     " + color + "    ║" + Color.reset() + "\n");
        }
        build.append(color + "╚════" + color + color + "═════════╝" + Color.reset() + "\n");
        for (int i = 0; i < 5; i++) {
            build.append(color + "     " + color + color + "          " + Color.reset() + "\n");
        }
        return build.toString();
    }

    public static void main(String[] args) {
        Panel panel = new Panel(10000, 50, System.out);
        IntStream.range(1, 49).forEach(n -> {
            DevCardView card = new DevCardView("DevCard" + n);
            DrawableObject obj1 = new DrawableObject(card.toString(), 40 * (n - 1), 0);
            panel.addItem(obj1);

        });
        DrawableObject obj1 = new DrawableObject(emptySlot(10), 40 * (48), 0);
        panel.addItem(obj1);

        panel.show();
    }
}
