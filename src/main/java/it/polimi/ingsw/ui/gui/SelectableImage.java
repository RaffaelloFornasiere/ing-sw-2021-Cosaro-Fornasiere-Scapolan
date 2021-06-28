package it.polimi.ingsw.ui.gui;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.stream.Collectors;


/**
 * ImageView wrapper that creates a border to the image when the image is clicked or hovered
 */
public class SelectableImage {
    //private static String css;


    /**
     * retrieves all sub-nodes of a given parent
     *
     * @param parent starting point
     * @return an array-list of  all sub-nodes
     */
    public static ArrayList<Node> getChildrenOf(Parent parent) {
        ArrayList<Node> nodes = new ArrayList<>();
        for (Node node : parent.getChildrenUnmodifiable()) {
            nodes.add(node);
            if (node instanceof Parent)
                nodes.addAll(getChildrenOf((Parent) node));
        }
        return nodes;
    }

    /**
     * set a border for sub-nodes of root market as selectable or hoverBorder
     *
     * @param root starting point
     */
    static public void setSelectable(Parent root) {
        var selectableImages = getChildrenOf(root).stream()
                .filter(n -> n.getStyleClass().contains("selectable") && n instanceof ImageView)
                .map(n -> (ImageView) n)
                .collect(Collectors.toList());
        selectableImages.addAll(getChildrenOf(root).stream()
                .filter(n -> n.getStyleClass().contains("hoverBorder") && n instanceof ImageView)
                .map(n -> (ImageView) n)
                .collect(Collectors.toList()));

        for (var image : selectableImages) {
            Parent parent = image.getParent();


            Region border = new Region();
            double k = Math.min(image.getFitWidth() / image.getImage().getWidth(), image.getFitHeight() / image.getImage().getHeight());
            double out = 1.05;
            double width = image.getImage().getWidth() * k;
            double height = image.getImage().getHeight() * k;
            double x = image.getLayoutX() - width * (out - 1) / 2;
            double y = image.getLayoutY() - height * (out - 1) / 2;

            border.setLayoutX(x);
            border.setLayoutY(y);
            border.setPrefSize(width * out, height * out);


            String css =
                    "    -fx-border-color: rgba(0, 176, 255, 0.85);\n" +
                            "    -fx-border-width: 7px;\n" +
                            "    -fx-border-radius: " + width * 0.1 + "px;" +
                            "    -fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 1), 10, 0, 0, 0);\n";

            border.setStyle(css);
            border.setMouseTransparent(true);
            border.setId("");
            border.setOpacity(0);
            border.getStyleClass().add("border");

            // attach the border to the parent
            try {
                ObservableList<Node> children = (ObservableList<Node>) parent.getClass().getMethod("getChildren").invoke(parent);
                children.indexOf(image);
                children.add(border);
            } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                e.printStackTrace();
            }


            //controls the border
            // System.out.println(image.getImage().getUrl());
            if (image.getStyleClass().contains("selectable")) {
                image.addEventFilter(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
                    if (!border.getId().contains("selected")) {
                        border.setOpacity(1);
                        border.setId("selected");
                    } else {
                        border.setId("");
                        border.setId("");
                        border.setOpacity(0);
                    }
                });

                image.addEventFilter(MouseEvent.MOUSE_ENTERED, mouseEvent -> border.setOpacity(1));
                image.addEventFilter(MouseEvent.MOUSE_EXITED, mouseEvent -> {
                    if (!border.getId().equals("selected"))
                        border.setOpacity(0);
                });

            }
            if (image.getStyleClass().contains("hoverBorder")) {
                image.addEventFilter(MouseEvent.MOUSE_ENTERED, mouseEvent ->
                        border.setOpacity(1));
                image.addEventFilter(MouseEvent.MOUSE_EXITED, mouseEvent -> border.setOpacity(0));
            }
        }
    }

    public static void cleanRoot(Parent root)
    {
        getChildrenOf(root).stream().filter(n -> n.getStyleClass().contains("border")).collect(Collectors.toList()).clear();
    }

}
