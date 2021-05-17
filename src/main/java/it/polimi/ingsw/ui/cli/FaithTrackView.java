package it.polimi.ingsw.ui.cli;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.controller.EffectOfCell;
import it.polimi.ingsw.model.FaithTrack.AbstractCell;
import it.polimi.ingsw.model.FaithTrack.PopeCell;
import it.polimi.ingsw.utilities.GsonInheritanceAdapter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

public class FaithTrackView {
    private HashMap<String, Integer> playersPositions;
    private HashMap<Integer, Integer> favorPopeCardPoints;

    public FaithTrackView(ArrayList<String> playersInitials) {
        playersPositions = new HashMap<>();
        for (String s : playersInitials) {
            playersPositions.put(s, 0);
        }
        favorPopeCardPoints = new HashMap<>();
    }

    public void display(String message) {

        String legend = "\033[31;1;4mLEGEND\033[0m \nNormal cell\n" +
                Color.CYAN.getAnsiCode() + "VaticanSectionCell\n" + Color.reset() +
                Color.RED.getAnsiCode() + "PopeCell\n" + Color.reset();
        DrawableObject o1 = new DrawableObject(message, 0, 0);
        DrawableObject o2 = new DrawableObject(legend, 100, 0);
        Panel panel0 = new Panel(1000, 5, System.out);
        panel0.addItem(o1);
        panel0.addItem(o2);
        panel0.show();

        Panel panel = new Panel(1000, 10, System.out);

        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(AbstractCell.class, new GsonInheritanceAdapter<AbstractCell>());
        builder.registerTypeAdapter(EffectOfCell.class, new GsonInheritanceAdapter<EffectOfCell>());
        Gson gson = builder.create();
        try {
            String cellsEffectJSON = Files.readString(Paths.get("src\\main\\resources\\CompleteFaithTrack.json"));
            cellsEffectJSON = cellsEffectJSON.substring(1, cellsEffectJSON.length() - 1);
            String[] cells = cellsEffectJSON.split("(,)(?=\\{)");

            ArrayList<CellView> array = new ArrayList<>();
            int n = 0;
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
                    if (favorPopeCardPoints.containsKey(cell.getIndex())) {
                        cellView.setFavorPopeCardPoint(favorPopeCardPoints.get(cell.getIndex()));
                    }
                }

                if (playersPositions.containsValue(cell.getIndex())) {
                    String placeholder = playersPositions.keySet().stream().filter(f -> playersPositions.get(f) == cell.getIndex()).collect(Collectors.joining());
                    cellView.setPlaceHolder(placeholder);
                }
            }


            for (CellView c : array) {
                panel.addItem(new DrawableObject(c.toString(), n * 18, 0));
                n++;
            }

            panel.show();

            //    for(CellView c:array) System.out.println(c.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void updatePlayerPosition(String p, int newPosition) throws IllegalArgumentException {
        if (!playersPositions.containsKey(p)) throw new IllegalArgumentException();
        playersPositions.put(p, newPosition);
    }

    public void updateFavorPopeCard(int index, int vPoints) {
        favorPopeCardPoints.put(index, vPoints);
    }

    public static void main(String[] args) {
        ArrayList<String> a = new ArrayList<>();
        a.add("S");
        a.add("P");
        a.add("L");
        a.add("R");

        FaithTrackView f = new FaithTrackView(a);
        f.updateFavorPopeCard(8, 5);
        f.updateFavorPopeCard(12, 2);
        //f.updatePlayerPosition("S",4);

        f.display("Position incremented");
    }
}
