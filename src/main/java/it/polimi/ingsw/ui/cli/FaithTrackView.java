package it.polimi.ingsw.ui.cli;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.controller.EffectOfCell;
import it.polimi.ingsw.model.faithTrack.AbstractCell;
import it.polimi.ingsw.model.faithTrack.PopeCell;
import it.polimi.ingsw.model.faithTrack.PopeFavorCard;
import it.polimi.ingsw.utilities.GsonInheritanceAdapter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.stream.Collectors;

public class FaithTrackView {
    private final HashMap<String, Integer> playersPositions;
    private final HashMap<String, HashMap<Integer, PopeFavorCard>> popeFavorCards;

    /**
     * constructor, put all initial position to 0
     *
     * @param playersNames the players' ids
     */
    public FaithTrackView(ArrayList<String> playersNames) {
        playersPositions = new HashMap<>();
        for (String s : playersNames) {
            playersPositions.put(s, 0);
        }
        popeFavorCards = new HashMap<>();
        for (String s : playersNames) {
            popeFavorCards.put(s, new HashMap<>());
        }
    }

    /**
     * method which displays the faithTrack and favorPopeCards of all players
     *
     * @param message       optional message to display
     * @param currentPlayer the player who is visualizing the faithTrack
     */
    public void display(String message, String currentPlayer) {

        String legend = "\033[31;1;4mLEGEND\033[0m \nNormal cell\n" +
                Color.CYAN.getAnsiCode() + "VaticanSectionCell\n" + Color.reset() +
                Color.RED.getAnsiCode() + "PopeCell\n" + Color.reset();
        ArrayList<DrawableObject> objs = new ArrayList<>();
        objs.add(new DrawableObject(message, 0, 0));
        objs.add(new DrawableObject(legend, 0, 0));
        Panel panel0 = new Panel(objs, System.out, false);
        panel0.show();

        ArrayList<DrawableObject> objs2 = new ArrayList<>();


        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(AbstractCell.class, new GsonInheritanceAdapter<AbstractCell>());
        builder.registerTypeAdapter(EffectOfCell.class, new GsonInheritanceAdapter<EffectOfCell>());
        Gson gson = builder.create();
        try {
            String cellsEffectJSON = Files.readString(Paths.get("src/main/resources/CompleteFaithTrack.json"));
            cellsEffectJSON = cellsEffectJSON.substring(1, cellsEffectJSON.length() - 1);
            String[] cells = cellsEffectJSON.split("(,)(?=\\{)");

            ArrayList<CellView> array = new ArrayList<>();

            for (String s : cells) {
                AbstractCell cell = gson.fromJson(s, AbstractCell.class);
                CellView cellView = new CellView(cell.getIndex(), cell.getVictoryPoints());
                array.add(cellView);
                if (cell instanceof PopeCell) {
                    cellView.setPopeCell();
                    for (int i = cell.getIndex() - ((PopeCell) cell).getVaticanReportSection() + 1; i < cell.getIndex(); i++) {
                        array.get(i).setVaticanSection();
                    }
                    cellView.setShowFavorPopeCard();

                    if (popeFavorCards.keySet().stream().map(entry -> popeFavorCards.get(entry).containsKey(cell.getIndex())).reduce(false, (subtotal, element) -> subtotal || element)) {
                        String popeFavorCardPlaceholder = popeFavorCards.keySet().stream().filter(f -> popeFavorCards.get(f).containsKey(cell.getIndex())).map(name -> name.substring(0, 1)).collect(Collectors.joining());
                        cellView.setPopeFavorCardPlaceHolder(popeFavorCardPlaceholder);
                        int points = popeFavorCards.get(popeFavorCards.keySet().stream().filter(f -> popeFavorCards.get(f).containsKey(cell.getIndex())).collect(Collectors.toCollection(ArrayList::new)).get(0)).get(cell.getIndex()).getVictoryPoints();
                        cellView.setFavorPopeCardPoint(points);
                    }
                }

                if (playersPositions.containsValue(cell.getIndex())) {
                    String placeholder = playersPositions.keySet().stream().filter(f -> playersPositions.get(f) == cell.getIndex()).map(name -> name.substring(0, 1)).collect(Collectors.joining());
                    cellView.setPlaceHolder(placeholder);
                }
            }


            for (CellView c : array) {
                objs2.add(new DrawableObject(c.toString(), 0, 0));

            }
            Panel panel = new Panel(objs2, System.out, true);
            panel.show();

            //    for(CellView c:array) System.out.println(c.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("OTHER PLAYERS' POPE FAVOR CARDS\n");
        StringBuilder othersPopeFavorCards = new StringBuilder();
        popeFavorCards.keySet().stream().filter(f -> !f.equals(currentPlayer)).forEach(p -> {
            othersPopeFavorCards.append(p.toUpperCase()).append("\n");
            Collection<PopeFavorCard> pointsCard = popeFavorCards.get(p).values();
            pointsCard.forEach(c -> othersPopeFavorCards.append("╔═══╗" + " "));
            othersPopeFavorCards.append(Color.reset()).append("\n");
            pointsCard.forEach(c -> othersPopeFavorCards.append("║ ").append(c.getVictoryPoints()).append(" ║").append(" "));
            othersPopeFavorCards.append(Color.reset()).append("\n");
            pointsCard.forEach(c -> othersPopeFavorCards.append("╚═══╝" + " "));

            othersPopeFavorCards.append(Color.reset()).append("\n");
        });
        System.out.println(othersPopeFavorCards);


    }

    /**
     * method which updates the position of one player
     *
     * @param p           the player id
     * @param newPosition the new index in the faithTrack
     * @throws IllegalArgumentException if the new position doesn't fit the faithTrack bounds
     */
    public void updatePlayerPosition(String p, int newPosition) throws IllegalArgumentException {
        if (!playersPositions.containsKey(p)) throw new IllegalArgumentException();
        playersPositions.put(p, newPosition);
    }

    /**
     * method which updates the hashmaps of popeFavorCards
     *
     * @param popeFavorCards the hashmap containing the popeFavorCards
     */
    public void updateFavorPopeCard(HashMap<String, HashMap<Integer, PopeFavorCard>> popeFavorCards) {
        popeFavorCards.keySet().forEach(player ->
                this.popeFavorCards.put(player, popeFavorCards.get(player)));
    }

    /**
     * method to update Lorenzo's position
     *
     * @param position the new position
     */
    //TODO put lorenzo position into visualization
    public void updateLorenzoPosition(int position) {
        playersPositions.put("Lorenzo", position);
    }
//
//    public static void main(String[] args) {
//        ArrayList<String> a = new ArrayList<>();
//        a.add("SILVIA");
//        a.add("PAOLO");
//        a.add("LARA");
//        a.add("ROBERTO");
//        HashMap<String, HashMap<Integer, PopeFavorCard>> popeFavorCards = new HashMap<>();
//        PopeFavorCard cardS0 = new PopeFavorCard(2);
//        PopeFavorCard cardS1 = new PopeFavorCard(3);
//        PopeFavorCard cardP0 = new PopeFavorCard(2);
//        PopeFavorCard cardP1 = new PopeFavorCard(3);
//        PopeFavorCard cardP2 = new PopeFavorCard(5);
//        PopeFavorCard cardL0 = new PopeFavorCard(2);
//        PopeFavorCard cardL1 = new PopeFavorCard(3);
//        PopeFavorCard cardL2 = new PopeFavorCard(5);
//        PopeFavorCard cardR0 = new PopeFavorCard(2);
//        HashMap<Integer, PopeFavorCard> mapS = new HashMap<>();
//        mapS.put(16, cardS0);
//        mapS.put(24, cardS1);
//
//        HashMap<Integer, PopeFavorCard> mapP = new HashMap<>();
//        mapP.put(8, cardP0);
//        mapP.put(16, cardP1);
//        mapP.put(24, cardP2);
//
//        HashMap<Integer, PopeFavorCard> mapL = new HashMap<>();
//        mapL.put(8, cardL0);
//        mapL.put(16, cardL1);
//        mapL.put(24, cardL2);
//
//        HashMap<Integer, PopeFavorCard> mapR = new HashMap<>();
//        mapR.put(24, cardR0);
//
//        popeFavorCards.put("SILVIA", mapS);
//        popeFavorCards.put("PAOLO", mapP);
//        popeFavorCards.put("LARA", mapL);
//        popeFavorCards.put("ROBERTO", mapR);
//
//
//        FaithTrackView f = new FaithTrackView(a);
//        f.updateFavorPopeCard(popeFavorCards);
//
//        f.updatePlayerPosition("SILVIA",4);
//        f.updatePlayerPosition("PAOLO",6);
//
//        f.display("Position incremented", "LARA");
//    }
}
