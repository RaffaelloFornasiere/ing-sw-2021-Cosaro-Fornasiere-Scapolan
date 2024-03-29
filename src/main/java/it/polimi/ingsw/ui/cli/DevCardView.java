package it.polimi.ingsw.ui.cli;

import com.google.gson.Gson;
import it.polimi.ingsw.model.CardColor;
import it.polimi.ingsw.model.devCards.DevCard;
import it.polimi.ingsw.model.Resource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.IntStream;

public class DevCardView {
    private DevCard card;

    /**
     * constructor
     *
     * @param path the path of the json file from which to take the information about the card
     */
    public DevCardView(String path) {
        Gson gson = new Gson();
        try {
            String cardJSON = Files.readString(Paths.get("src/main/resources/" + path + ".json"));
            card = gson.fromJson(cardJSON, DevCard.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * method which translate a card type into a color
     *
     * @param c card type
     * @return card color
     */
    private String translateColor(CardColor c) {
        if (c == CardColor.BLUE) return Color.BLUE.getAnsiCode();
        if (c == CardColor.VIOLET) return Color.RED.getAnsiCode();
        if (c == CardColor.GREEN) return Color.GREEN.getAnsiCode();
        else return Color.YELLOW.getAnsiCode();
    }

    /**
     * method which builds a string to display a card
     *
     * @return the string
     */
    public String toString() {
        String color = translateColor(card.getColor());
        StringBuilder build = new StringBuilder();
        build.append(color).append("   ").append(color).append(card.getCardID()).append(color).append("  ").append(Color.reset()).append("\n").append(color).append("╔════").append(color).append("Level").append(color).append("════╗").append(Color.reset()).append("\n").append(color).append("║      ").append(color).append(card.getLevel()).append(color).append("      ║").append(Color.reset()).append("\n").append(color).append("╠════").append(color).append("Cost").append(color).append("═════╣").append(Color.reset()).append("\n");
        for (Resource resource : card.getCost().keySet()) {
            build.append(color).append("║     ").append(CLI.colorResource(resource)).append(card.getCost().get(resource)).append(" ").append(CLI.shapeResource(resource)).append(color).append("     ║").append(Color.reset()).append("\n");
        }
        build.append(color).append("╠═").append(color).append("Production").append(color).append("══╣").append(Color.reset()).append("\n");
        for (Resource resource : card.getProductionPower().getConsumedResources().keySet()) {
            build.append(color).append("║     ").append(CLI.colorResource(resource)).append(card.getProductionPower().getConsumedResources().get(resource)).append(" ").append(CLI.shapeResource(resource)).append(color).append("     ║").append(Color.reset()).append("\n");
        }
        if (card.getProductionPower().getRequiredResourceOfChoice() != 0) {
            build.append(color).append("║     ").append(color).append(+card.getProductionPower().getRequiredResourceOfChoice()).append(" ").append("?").append(color).append("     ║").append(Color.reset()).append("\n");
        }
        build.append(color).append("║   ").append(color).append("--->>> ").append(color).append("   ║ ").append(Color.reset()).append("\n");
        for (Resource resource : card.getProductionPower().getProducedResources().keySet()) {
            build.append(color).append("║     ").append(CLI.colorResource(resource)).append(card.getProductionPower().getProducedResources().get(resource)).append(" ").append(CLI.shapeResource(resource)).append(color).append("     ║").append(Color.reset()).append("\n");
        }
        if (card.getProductionPower().getFaithPointsProduced() != 0) {
            build.append(color).append("║     ").append(color).append(+card.getProductionPower().getFaithPointsProduced()).append(" ").append("+").append(color).append("     ║").append(Color.reset()).append("\n");
        }
        if (card.getProductionPower().getProducedResourceOfChoice() != 0) {
            build.append(color).append("║     ").append(color).append(+card.getProductionPower().getProducedResourceOfChoice()).append(" ").append("?").append(color).append("     ║").append(Color.reset()).append("\n");
        }


        build.append(color).append("╠═══").append(color).append("VPoints").append(color).append("═══╣").append(Color.reset()).append("\n");
        if (card.getVictoryPoints() < 10) {
            build.append(color).append("║      ").append(color).append(card.getVictoryPoints()).append(color).append("      ║").append(Color.reset()).append("\n");
        } else
            build.append(color).append("║     ").append(color).append(card.getVictoryPoints()).append(color).append("      ║").append(Color.reset()).append("\n");
        build.append(color).append("╚════").append(color).append(color).append("═════════╝").append(Color.reset()).append("\n");
        build.append((color + "     " + color + color + "          " + Color.reset() + "\n").repeat(5));
        return build.toString();
    }

    public static String emptySlot(int height) {
        String color = Color.WHITE.getAnsiCode();
        return color + "   " + color + "        " + color + "  " + Color.reset() + "\n" + color + "╔════" + color + "═════" + color + "════╗" + Color.reset() + "\n" +
                (color + "║    " + color + "     " + color + "    ║" + Color.reset() + "\n").repeat(Math.max(0, height - 6)) +
                color + "╚════" + color + color + "═════════╝" + Color.reset() + "\n" +
                (color + "     " + color + color + "          " + Color.reset() + "\n").repeat(5);
    }


}
