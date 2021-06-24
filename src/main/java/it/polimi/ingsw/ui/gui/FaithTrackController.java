package it.polimi.ingsw.ui.gui;

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

    public void setPosition(int newPosition) {
        //
        if(newPosition == position)
            return;
        int sideMove = 0;
        int verticalMove = 0;
        ArrayList<TranslateTransition> path = new ArrayList<>();
        TranslateTransition base = new TranslateTransition(Duration.millis(150));
        base.setNode(cross);
        base.setAutoReverse(false);
        base.setFromX(currentPos.x);
        base.setFromY(currentPos.y);
        base.setToX(currentPos.x);
        base.setToY(currentPos.y);
        path.add(base);
        System.out.println("pos: " + position + " new pos: " + newPosition);
        SequentialTransition sequentialTransition = new SequentialTransition();
        int di = Math.abs(newPosition - position) / (newPosition - position);
        for (int i = position; i != newPosition; i += di) {
            Point current = track.get(i).getValue1();
            Point next = track.get(i + di).getValue1();
            sideMove = next.y - current.y;
            verticalMove = next.x - current.x;
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
        for (var transition : path)
            sequentialTransition.getChildren().add(transition);
        sequentialTransition.play();


        position = newPosition;
    }

    public void incrementPosition() {
        setPosition(position + 1);
    }

    public void decrementPosition() {
        setPosition(position - 1);
    }


}

//
//    public void setPosition(int newPosition) {
//        //
//
//        int sideMove = 0;
//        int verticalMove = 0;
//        ArrayList<TranslateTransition> path = new ArrayList<>();
//        System.out.println("pos: " + position + " new pos: " + newPosition);
//        SequentialTransition sequentialTransition = new SequentialTransition();
//        for (int i = position; i != newPosition; i += Math.abs(newPosition - position) / (newPosition - position)) {
//            Point current = track.get(i).getValue1();
//            Point next = track.get(i + 1).getValue1();
//            sideMove = next.y - current.y;
//            verticalMove = next.x - current.x;
//            System.out.println("side: " + sideMove + " vertical: " + verticalMove);
//            TranslateTransition transition = new TranslateTransition();
//            transition.setNode(cross);
//            transition.setDuration(Duration.millis(300));
//            transition.setAutoReverse(false);
//            transition.setFromX(currentPos.x);
//            transition.setFromY(currentPos.y);
//            transition.setToX(currentPos.x + cellsize * sideMove);
//            transition.setToY(currentPos.y + cellsize * verticalMove);
//            currentPos.x = currentPos.x + cellsize * sideMove;
//            currentPos.y = currentPos.y + cellsize * verticalMove;
//
//            path.add(transition);
//        }
//        for (var transition : path)
//            sequentialTransition.getChildren().add(transition);
//        sequentialTransition.play();
//
//
//        position = newPosition;
//    }
