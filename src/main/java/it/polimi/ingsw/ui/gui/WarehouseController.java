package it.polimi.ingsw.ui.gui;


import it.polimi.ingsw.events.ClientEvents.DepositLeaderPowerStateEvent;
import it.polimi.ingsw.events.ClientEvents.DepotState;
import it.polimi.ingsw.events.ControllerEvents.MatchEvents.NewResourcesOrganizationEvent;
import it.polimi.ingsw.model.Resource;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class WarehouseController extends Controller implements Initializable {

    @FXML
    AnchorPane root;
    HashMap<Resource, Integer> discardResources;

    @FXML
    AnchorPane leaderDepots;
    @FXML
    AnchorPane warehouse;

    @FXML
    AnchorPane mainViewWarehouse;


    ArrayList<ArrayList<String>> resourcesImages;
    ArrayList<ArrayList<ImageView>> depots;

    public WarehouseController(GUI gui) {
        super(gui);
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


    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        depots = new ArrayList<>();
        String imagePath = new java.io.File(".").getAbsolutePath();
        imagePath = "file:/" + imagePath.substring(0, imagePath.length() - 2) + "/src/main/resources/it/polimi/ingsw/ui/gui/images/";
        String finalImagePath = imagePath;
        var images = gui.thisPlayerState().warehouse.stream().flatMap(n -> new ArrayList<Image>() {{
            for (int i = 0; i < n.getCurrentQuantity(); i++) {
                add(new Image(finalImagePath + n.getResourceType().toString().toLowerCase() + "2.png"));
            }
        }}.stream()).collect(Collectors.toList());

        var resources = warehouse.getChildren().stream().filter(n -> n.getStyleClass().contains("Resource")).map(n -> (ImageView) n).collect(Collectors.toList());
        assert resources.size() == images.size();
        for (int i = 0; i < images.size(); i++) {
            resources.get(i).setImage(images.get(i));
        }
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


        Resource leaderDepotType1 = Resource.COIN;
//        ((AnchorPane)leaderDepots.getChildren().get(0)).getChildren().stream().filter(n -> n.getStyleClass().contains("Resource"))
//                .map(n -> (ImageView)n)
        String imageName = leaderDepotType1.toString().toLowerCase() + ".png";
        imageName = "LeaderDepot" + String.valueOf(imageName.charAt(0)).toUpperCase() + imageName.substring(1);
        ((AnchorPane)leaderDepots.getChildren().get(0)).getChildren().stream().filter(n -> !n.getStyleClass().contains("Resource"))
                .map(n -> (ImageView)n)
                .collect(Collectors.toList()).get(0).setImage(new Image(imagePath + imageName + ".png"));

    }

    public void onDragDetected(MouseEvent event) {
        ImageView source = (ImageView) event.getSource();
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

    public void onDragDroppedOnBin(DragEvent event) {
        /* data dropped */
        /* if there is a string data on dragboard, read it and use it */
        Dragboard db = event.getDragboard();
        boolean success = false;
        if (db.hasString()) {
            String s = db.getString();
            s = s.substring(s.lastIndexOf("/") + 1, s.lastIndexOf(".")).toUpperCase();
            discardResources.put(Resource.valueOf(s), discardResources.getOrDefault(s, 0));
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
        Node target = (Node) event.getSource();
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
            } else if (target instanceof AnchorPane && target.getId().equals("bin")) {
                event.acceptTransferModes(TransferMode.MOVE);
            } else {
                ImageView source = (ImageView) event.getSource();
                if (source.getImage() == null) {
                    AnchorPane parent = ((AnchorPane) source.getParent());
                    int i = parent.getChildren().indexOf(source);
                    String type = event.getDragboard().getString();
                    System.out.println("sourceres: " + type);
                    switch (i) {
                        case 1 -> event.acceptTransferModes(TransferMode.MOVE);
                        case 2 -> {
                            ImageView sibling = (ImageView) parent.getChildren().get(i + 1);
                            if (sibling.getImage() != null)
                                System.out.println("siblings1: " + sibling.getImage().getUrl());
                            if (sibling.getImage() == null ||
                                    sibling.getImage().getUrl().contains(type))
                                event.acceptTransferModes(TransferMode.MOVE);
                        }
                        case 3 -> {
                            ImageView sibling = (ImageView) parent.getChildren().get(i - 1);
                            if (sibling.getImage() != null)
                                System.out.println("siblings1: " + sibling.getImage().getUrl());
                            if (sibling.getImage() == null ||
                                    sibling.getImage().getUrl().contains(type))
                                event.acceptTransferModes(TransferMode.MOVE);
                        }
                        case 4 -> {
                            ImageView sibling = (ImageView) parent.getChildren().get(i + 1);
                            ImageView sibling2 = (ImageView) parent.getChildren().get(i + 2);
                            if (sibling.getImage() != null)
                                System.out.println("siblings1: " + sibling.getImage().getUrl());
                            if (sibling2.getImage() != null)
                                System.out.println("siblings2: " + sibling2.getImage().getUrl());
                            if ((sibling.getImage() == null && sibling2.getImage() == null) ||
                                    Objects.requireNonNullElse(sibling, sibling2).getImage().getUrl().contains(type))
                                event.acceptTransferModes(TransferMode.MOVE);
                        }
                        case 5 -> {
                            ImageView sibling = (ImageView) parent.getChildren().get(i + 1);
                            ImageView sibling2 = (ImageView) parent.getChildren().get(i - 1);
                            if (sibling.getImage() != null)
                                System.out.println("siblings1: " + sibling.getImage().getUrl());
                            if (sibling2.getImage() != null)
                                System.out.println("siblings2: " + sibling2.getImage().getUrl());
                            if ((sibling.getImage() == null && sibling2.getImage() == null) ||
                                    Objects.requireNonNullElse(sibling, sibling2).getImage().getUrl().contains(type))
                                event.acceptTransferModes(TransferMode.MOVE);
                        }
                        case 6 -> {
                            ImageView sibling = (ImageView) parent.getChildren().get(i - 1);
                            ImageView sibling2 = (ImageView) parent.getChildren().get(i - 1);
                            if (sibling.getImage() != null)
                                System.out.println("siblings1: " + sibling.getImage().getUrl());
                            if (sibling2.getImage() != null)
                                System.out.println("siblings2: " + sibling2.getImage().getUrl());
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



    public void openBin(DragEvent event) {
        AnchorPane target = (AnchorPane) event.getTarget();
        if (event.getGestureSource() != target) {
            ImageView image = (ImageView) target.getChildren().stream().filter(n -> {
                if (n.getId() != null && n.getId().equals("binTop"))
                    return true;
                return false;
            }).findFirst().orElse(null);
            double k = Math.min(image.getFitWidth() / image.getImage().getWidth(), image.getFitHeight() / image.getImage().getHeight());
            double width = image.getImage().getWidth() * k;
            double height = image.getImage().getHeight() * k;
            image.setTranslateX(width / 2);
            image.setTranslateY(height);
            image.setRotate(90);
        }

        event.consume();

    }


    public void closeBin(DragEvent event) {
        AnchorPane target = (AnchorPane) event.getTarget();
        if (event.getGestureSource() != target) {
            ImageView image = (ImageView) target.getChildren().stream().filter(n -> {
                if (n.getId() != null && n.getId().equals("binTop"))
                    return true;
                return false;
            }).findFirst().orElse(null);

            double k = Math.min(image.getFitWidth() / image.getImage().getWidth(), image.getFitHeight() / image.getImage().getHeight());
            double width = image.getImage().getWidth() * k;
            double height = image.getImage().getHeight() * k;
            image.setRotate(0);
            image.setTranslateX(0);
            image.setTranslateY(0);
        }

        event.consume();
    }



    public void onCancel() {
        ((Stage) root.getScene().getWindow()).close();
    }

    public void onNext() {
        ArrayList<DepotState> depots = new ArrayList<>();
        ArrayList<DepositLeaderPowerStateEvent> leaderDepots = new ArrayList<>();

        gui.addEvent(new NewResourcesOrganizationEvent(gui.askUserID(), depots, leaderDepots, discardResources));
    }

}
