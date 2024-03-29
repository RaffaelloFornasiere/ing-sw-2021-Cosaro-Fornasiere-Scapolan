package it.polimi.ingsw.ui.gui;

import it.polimi.ingsw.model.faithTrack.PopeFavorCard;
import it.polimi.ingsw.utilities.Pair;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import org.javatuples.Triplet;

import javax.sound.midi.Soundbank;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;


/**
 * ui window controller to manage the position of the two crosses on the faith-track
 */
public class FaithTrackController extends Controller {

    /**
     * self explanatory auxiliary class
     */
    private static class DoublePoint {
        public double x;
        public double y;

        DoublePoint(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }

    /**
     * auxiliary class to wrap data to manage user's and lorenzo's cross on faith track
     */
    private static class PlayerMetaData {
        PlayerMetaData() {
            currentPos = new DoublePoint(0, 0);
            cross = new ImageView();
        }

        int positionIndex;
        DoublePoint currentPos;
        ImageView cross;
    }

    ArrayList<Triplet<Integer, Point, Node>> track;
    DoublePoint zeroPos;
    PlayerMetaData lorenzo;
    PlayerMetaData user;
    ArrayList<ImageView> popeFavorCards;
    GridPane faithTrack;
    double cellSize;
    boolean singlePlayer;


    /**
     * constructor: retrieves the gridpane where
     * the faith track is painted. constructs an
     * array that contains, the index of the cell
     * the x-y coordinates inside the grid
     * and the node of the cell.
     * Then it calculates the zero position of the
     * player's cross and place it there.
     *
     * @param faithTrack   reference to javafx object
     * @param singlePlayer tells if the game is in singlePlayerMode
     */
    FaithTrackController(GUI gui, GridPane faithTrack, boolean singlePlayer, ArrayList<ImageView> popeFavorCards) {
        super(gui);
        user = new PlayerMetaData();
        this.popeFavorCards = popeFavorCards;
        zeroPos = new DoublePoint(0, 0);
        user.currentPos = new DoublePoint(0, 0);
        this.faithTrack = faithTrack;
        AtomicReference<Integer> i = new AtomicReference<>(0);
        track = new ArrayList<>() {{
            faithTrack.getChildren().stream().sequential().filter(n -> n.getStyleClass().contains("Cell")).forEach(c ->
            {
                i.getAndSet(i.get() + 1);
                add(new Triplet<>(i.get(), new Point(GridPane.getRowIndex(c), GridPane.getColumnIndex(c)), c));
            });
        }};
        Collections.swap(track, 17, 16);

        String imageUrl = new java.io.File(".").getAbsolutePath();
        imageUrl = "file:" + imageUrl.substring(0, imageUrl.length() - 2) + "/src/main/resources/it/polimi/ingsw/ui/gui/images/";
        user.cross = new ImageView(new Image(imageUrl + "crocePlayer.png"));

        user.cross.setFitWidth(45);
        user.cross.setFitHeight(55);

        ((StackPane) faithTrack.getParent()).getChildren().add(user.cross);
        zeroPos.x = faithTrack.getWidth() / 2;
        zeroPos.y = ((Group) track.get(0).getValue(2)).getChildren().stream()
                .filter(n -> n.getStyleClass().contains("Cell"))
                .map(n -> (ImageView) n)
                .collect(Collectors.toList())
                .get(0).getFitHeight() + 5;
        cellSize = faithTrack.getWidth() / 19;
        // //System.out.printlnln("zx: " + zeroPos.x + " zy: " + zeroPos.y);
        user.cross.setTranslateX(-zeroPos.x + zeroPos.y / 2);
        user.cross.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 1), 10, 0, 0, 0);");
        user.cross.setTranslateY(zeroPos.y * 1.2);
        user.currentPos.x = -zeroPos.x + zeroPos.y / 2;
        user.currentPos.y = zeroPos.y * 1.2;
        setPlayerPosition(0);

        if (singlePlayer) {
            lorenzo = new PlayerMetaData();
            lorenzo.cross = new ImageView(new Image(imageUrl + "croceLorenzo.png"));
            lorenzo.cross.setFitWidth(45);
            lorenzo.cross.setFitHeight(55);
            ((StackPane) faithTrack.getParent()).getChildren().add(lorenzo.cross);
            lorenzo.cross.setTranslateX(-zeroPos.x + zeroPos.y / 2);
            lorenzo.cross.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 1), 10, 0, 0, 0);");
            lorenzo.cross.setTranslateY(zeroPos.y * 1.2);
            lorenzo.currentPos.x = -zeroPos.x + zeroPos.y / 2;
            lorenzo.currentPos.y = zeroPos.y * 1.2;
            setLorenzoPosition(0);
        }


        String finalImageUrl = imageUrl;

    }


    /**
     * update the current position of the player to his new position
     * and executes the animation to move the player's cross in
     * the new player's position on the faith track
     *
     * @param newPosition the new position of the player
     */
    public void setPosition(int newPosition, PlayerMetaData player) {
        if (newPosition == player.positionIndex)
            return;

        ArrayList<TranslateTransition> path = new ArrayList<>();
        TranslateTransition base = new TranslateTransition(Duration.millis(150));
        base.setNode(player.cross);
        base.setAutoReverse(false);
        base.setFromX(player.currentPos.x);
        base.setFromY(player.currentPos.y);
        base.setToX(player.currentPos.x);
        base.setToY(player.currentPos.y);
        path.add(base);
        int di = Math.abs(newPosition - player.positionIndex) / (newPosition - player.positionIndex);
        for (int i = player.positionIndex; i != newPosition; i += di) {
            Point current = track.get(i).getValue1();
            Point next = track.get(i + di).getValue1();
            int sideMove = next.y - current.y;
            int verticalMove = next.x - current.x;
            var lastTransition = path.get(path.size() - 1);
            if (lastTransition.getFromY() == lastTransition.getToY() && verticalMove == 0) {
                lastTransition.setToX(player.currentPos.x + cellSize * sideMove);
                lastTransition.setDuration(lastTransition.getDuration().add(Duration.millis(150)));
            } else if (lastTransition.getFromX() == lastTransition.getToX() && sideMove == 0) {
                lastTransition.setToY(player.currentPos.y + cellSize * verticalMove);
                lastTransition.setDuration(lastTransition.getDuration().add(Duration.millis(150)));
            } else {
                TranslateTransition transition = new TranslateTransition(Duration.millis(150));
                transition.setNode(player.cross);
                transition.setAutoReverse(false);
                transition.setFromX(player.currentPos.x);
                transition.setFromY(player.currentPos.y);
                transition.setToX(player.currentPos.x + cellSize * sideMove);
                transition.setToY(player.currentPos.y + cellSize * verticalMove);
                path.add(transition);
            }
            player.currentPos.x = player.currentPos.x + cellSize * sideMove;
            player.currentPos.y = player.currentPos.y + cellSize * verticalMove;

        }
        SequentialTransition sequentialTransition = new SequentialTransition();
        for (var transition : path)
            sequentialTransition.getChildren().add(transition);
        sequentialTransition.play();
        player.positionIndex = newPosition;
    }

    /**
     * increments the position of one step
     */
    public void incrementPlayerPosition() {
        setPlayerPosition(user.positionIndex + 1);
    }

    /**
     * decrements the position of one step
     */
    public void decrementPlayerPosition() {
        setPlayerPosition(user.positionIndex - 1);
    }


    /**
     * increments the position of one step
     */
    public void incrementLorenzoPosition() {
        setPlayerPosition(lorenzo.positionIndex + 1);
    }

    /**
     * decrements the position of one step
     */
    public void decrementLorenzoPosition() {
        setPlayerPosition(lorenzo.positionIndex - 1);
    }

    /**
     * sets the new lorenzo's position
     */
    public void setLorenzoPosition(int newPosition) {
        setPosition(newPosition, lorenzo);
    }

    /**
     * sets the new user's position
     */
    public void setPlayerPosition(int newPosition) {
        setPosition(newPosition, user);
    }

    /**
     * upates the state of pope favor cards
     *
     * @param discard if the card must be discarded
     */
    public void updateFavorCards(boolean discard) {
        String imageUrl = new java.io.File(".").getAbsolutePath();
        imageUrl = "file:" + imageUrl.substring(0, imageUrl.length() - 2) + "/src/main/resources/it/polimi/ingsw/ui/gui/images/";
        String finalImageUrl = imageUrl;
        for (Pair<String, HashMap<Integer, PopeFavorCard>> favorCards :
                gui.playerStates.entrySet().stream().map(e -> new Pair<>(e.getKey(), e.getValue().getPopeFavorCards())).collect(Collectors.toList())) {

            if (favorCards.getValue().size() > 0) {
                //System.out.printlnln("Player: " + gui.playerID.getItem());
                //System.out.println("\t");
                //System.out.printlnln(discard ? "Discard" : "Open");
            }
            favorCards.getValue().forEach((key, value) -> {
                int i = (key == 8) ? 1 : (key == 16) ? 2 : 3;
                if ((popeFavorCards.get(i - 1).getImage() != null && popeFavorCards.get(i - 1).getImage().getUrl().contains("back")
                        || (popeFavorCards.get(i - 1).getImage() == null && !discard && favorCards.getKey().equals(gui.playerID.getItem())))) {
                    //System.out.println("\tchanging state into: ");
                    Image im = discard ? null : new Image(finalImageUrl + "pope_favor" + i + "_front.png");
                    //System.out.printlnln(im == null ? "null" : im.getUrl());
                    popeFavorCards.get(i - 1).setImage(im);
                }
            });
        }
    }


    /**
     * opens the pope favor cards passed by argument
     *
     * @param popeFavorCards cards to open
     */
    //TODO
    public void setPopeFavorCards(HashMap<String, HashMap<Integer, PopeFavorCard>> popeFavorCards) {

    }
}
