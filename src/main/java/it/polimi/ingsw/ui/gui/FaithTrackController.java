package it.polimi.ingsw.ui.gui;

import it.polimi.ingsw.model.FaithTrack.PopeFavorCard;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import org.javatuples.Triplet;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class FaithTrackController {

    private class DoublePoint {
        public double x;
        public double y;

        DoublePoint(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }

    ArrayList<Triplet<Integer, Point, Node>> track;
    DoublePoint currentPos;
    DoublePoint zeroPos;

    private int position;
    ArrayList<ImageView> popeFavorCards;

    ImageView cross;
    GridPane faithTrack;

    double cellsize;

    /**
     * constructor: retrieves the gridpane where
     * the faith track is painted. constructs an
     * array that contains, the index of the cell
     * the x-y coordinates inside the grid
     * and the node of the cell.
     * Then it calculates the zero position of the
     * player's cross and place it there.
     *
     * @param faithTrack
     */
    FaithTrackController(GridPane faithTrack) {
        zeroPos = new DoublePoint(0, 0);
        currentPos = new DoublePoint(0, 0);
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
        imageUrl = "file:/" + imageUrl.substring(0, imageUrl.length() - 2) + "/src/main/resources/it/polimi/ingsw/ui/gui/images/crocePlayer.png";
        cross = new ImageView(new Image(imageUrl));
        System.out.println(imageUrl);
        cross.setFitWidth(45);
        cross.setFitHeight(55);
        ((StackPane) faithTrack.getParent()).getChildren().add(cross);
        zeroPos.x = faithTrack.getWidth() / 2;
        zeroPos.y = ((Group) track.get(0).getValue(2)).getChildren().stream()
                .filter(n -> n.getStyleClass().contains("Cell"))
                .map(n -> (ImageView) n)
                .collect(Collectors.toList())
                .get(0).getFitHeight() + 5;
        cellsize = faithTrack.getWidth() / 19;
        System.out.println("zx: " + zeroPos.x + " zy: " + zeroPos.y);
        cross.setTranslateX(-zeroPos.x + zeroPos.y / 2);
        cross.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 1), 10, 0, 0, 0);");
        cross.setTranslateY(zeroPos.y * 1.2);
        currentPos.x = -zeroPos.x + zeroPos.y / 2;
        currentPos.y = zeroPos.y * 1.2;
        setPosition(0);
    }


    /**
     * update the current position of the player to his new position
     * and executes the animation to move the player's cross in
     * the new player's position on the faith track
     *
     * @param newPosition the new position of the player
     */
    public void setPosition(int newPosition) {
        if (newPosition == position)
            return;

        ArrayList<TranslateTransition> path = new ArrayList<>();
        TranslateTransition base = new TranslateTransition(Duration.millis(150));
        base.setNode(cross);
        base.setAutoReverse(false);
        base.setFromX(currentPos.x);
        base.setFromY(currentPos.y);
        base.setToX(currentPos.x);
        base.setToY(currentPos.y);
        path.add(base);
        int di = Math.abs(newPosition - position) / (newPosition - position);
        for (int i = position; i != newPosition; i += di) {
            Point current = track.get(i).getValue1();
            Point next = track.get(i + di).getValue1();
            int sideMove = next.y - current.y;
            int verticalMove = next.x - current.x;
            var lastTransition = path.get(path.size() - 1);
            if (lastTransition.getFromY() == lastTransition.getToY() && verticalMove == 0) {
                lastTransition.setToX(currentPos.x + cellsize * sideMove);
                lastTransition.setDuration(lastTransition.getDuration().add(Duration.millis(150)));
            } else if (lastTransition.getFromX() == lastTransition.getToX() && sideMove == 0) {
                lastTransition.setToY(currentPos.y + cellsize * verticalMove);
                lastTransition.setDuration(lastTransition.getDuration().add(Duration.millis(150)));
            } else {
                TranslateTransition transition = new TranslateTransition(Duration.millis(150));
                transition.setNode(cross);
                transition.setAutoReverse(false);
                transition.setFromX(currentPos.x);
                transition.setFromY(currentPos.y);
                transition.setToX(currentPos.x + cellsize * sideMove);
                transition.setToY(currentPos.y + cellsize * verticalMove);
                path.add(transition);
            }
            currentPos.x = currentPos.x + cellsize * sideMove;
            currentPos.y = currentPos.y + cellsize * verticalMove;

        }
        SequentialTransition sequentialTransition = new SequentialTransition();
        for (var transition : path)
            sequentialTransition.getChildren().add(transition);
        sequentialTransition.play();
        position = newPosition;
    }

    /**
     * increments the position of one step
     */
    public void incrementPosition() {
        setPosition(position + 1);
    }

    /**
     * decrements the position of one step
     */
    public void decrementPosition() {
        setPosition(position - 1);
    }

    public void setPopeFavorCards(HashMap<String, HashMap<Integer, PopeFavorCard>> popeFavorCards) {
    }
}
