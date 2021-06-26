package it.polimi.ingsw.ui.gui;


import it.polimi.ingsw.model.Resource;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class WarehouseController extends Controller implements Initializable {

    @FXML
    AnchorPane root;

    public WarehouseController(GUI gui) {
        super(gui);
    }

    AnchorPane warehouse;
    ArrayList<ArrayList<String>> resourcesImages;
    ArrayList<ArrayList<ImageView>> depots;

    public WarehouseController(AnchorPane warehouse) {
        this.warehouse = warehouse;
        resourcesImages = new ArrayList<>(3);
        resourcesImages.add(new ArrayList<>() {{
            add("");
        }});
        resourcesImages.add(new ArrayList<>() {{
            add("");
            add("");
        }});
        resourcesImages.add(new ArrayList<>() {{
            add("");
            add("");
            add("");
        }});

        depots = new ArrayList<>();
        var resources = warehouse.getChildren().stream().filter(n -> n.getStyleClass().contains("Resource"))
                .map(n -> (javafx.scene.image.ImageView) n).collect(Collectors.toList());
        depots.add(new ArrayList<>() {{
            add(resources.get(0));
        }});
        depots.add(new ArrayList<>() {{
            add(resources.get(1));
            add(resources.get(2));
        }});
        depots.add(new ArrayList<>() {{
            add(resources.get(3));
            add(resources.get(4));
            add(resources.get(5));
        }});
    }

    public void onDragDetected(MouseEvent event) {
        ImageView source = (ImageView) event.getSource();
        System.out.println(event.getClass().getName());
        /* drag was detected, start a drag-and-drop gesture*/
        /* allow any transfer mode */
        Dragboard db = source.startDragAndDrop(TransferMode.MOVE);

        /* Put a string on a dragboard */
        ClipboardContent content = new ClipboardContent();
        content.putString(source.getImage().getUrl());
        db.setContent(content);
        event.consume();
    }

    public void onDragDropped(DragEvent event) {
        /* data dropped */
        /* if there is a string data on dragboard, read it and use it */
        Dragboard db = event.getDragboard();
        boolean success = false;
        if (db.hasString()) {
            ((ImageView) event.getTarget()).setImage(new Image(db.getString()));
            success = true;
        }
        /* let the source know whether the string was successfully
         * transferred and used */
        event.setDropCompleted(success);
        event.consume();


    }

    public void onDragDone(DragEvent event) {
        if (event.getTransferMode() == TransferMode.MOVE) {
            ((ImageView) event.getSource()).setImage(null);
        }
        event.consume();

    }

    public void onDragOver(DragEvent event) {
        ImageView target = (ImageView) event.getSource();
        /* data is dragged over the target */
        /* accept it only if it is not dragged from the same node
         * and if it has a string data */
        if (event.getGestureSource() != target) {
            var styleClass = target.getParent().getStyleClass().stream().filter(n -> n.contains("leader")).findFirst().orElse(null);
            if (styleClass != null) {
                String type = styleClass.replace("leaderDepot", "");
                type = type.toUpperCase();
                Resource resourceType = Resource.valueOf(type);
                String url = event.getDragboard().getString();
                if (url.toUpperCase().contains(type)) {
                    event.acceptTransferModes(TransferMode.MOVE);
                }
            } else {

                ImageView source = (ImageView) event.getSource();
                if (source.getImage() == null) {
                    AnchorPane parent = ((AnchorPane) source.getParent());
                    System.out.println("warehouse: ");
                    parent.getChildren().stream().map(n -> (ImageView) n)
                            .forEach(n -> {
                                if (n.getImage() != null)
                                    System.out.println("\t"+n.getImage().getUrl());
                                else
                                    System.out.println("\tnull");
                            });
                    int i = parent.getChildren().indexOf(source);
                    String type = event.getDragboard().getString();
                    System.out.println(type);
                    switch (i) {
                        case 1 -> event.acceptTransferModes(TransferMode.MOVE);
                        case 2 -> {
                            ImageView sibling = (ImageView) parent.getChildren().get(i + 1);
                            if (sibling.getImage() == null ||
                                    sibling.getImage().getUrl().contains(type))
                                event.acceptTransferModes(TransferMode.MOVE);
                        }
                        case 3 -> {
                            ImageView sibling = (ImageView) parent.getChildren().get(i - 1);
                            if (sibling.getImage() == null ||
                                    sibling.getImage().getUrl().contains(type))
                                event.acceptTransferModes(TransferMode.MOVE);
                        }
                        case 4 -> {
                            ImageView sibling = (ImageView) parent.getChildren().get(i + 1);
                            ImageView sibling2 = (ImageView) parent.getChildren().get(i + 2);
                            if ((sibling.getImage() == null && sibling2.getImage() == null) ||
                                    Objects.requireNonNullElse(sibling, sibling2).getImage().getUrl().contains(type))
                                event.acceptTransferModes(TransferMode.MOVE);
                        }
                        case 5 -> {
                            ImageView sibling = (ImageView) parent.getChildren().get(i + 1);
                            ImageView sibling2 = (ImageView) parent.getChildren().get(i - 1);
                            if ((sibling.getImage() == null && sibling2.getImage() == null) ||
                                    Objects.requireNonNullElse(sibling, sibling2).getImage().getUrl().contains(type))
                                event.acceptTransferModes(TransferMode.MOVE);
                        }
                        case 6 -> {
                            ImageView sibling = (ImageView) parent.getChildren().get(i - 1);
                            ImageView sibling2 = (ImageView) parent.getChildren().get(i - 1);
                            if ((sibling.getImage() == null && sibling2.getImage() == null) ||
                                    Objects.requireNonNullElse(sibling, sibling2).getImage().getUrl().contains(type))
                                event.acceptTransferModes(TransferMode.MOVE);
                        }
                    }
                }
            }
        }

        event.consume();
    }


    public void onCancel() {

    }

    public void onNext() {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
