package it.polimi.ingsw.ui.gui;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.events.ClientEvents.DepositLeaderPowerStateEvent;
import it.polimi.ingsw.events.ClientEvents.DepotState;
import it.polimi.ingsw.events.ControllerEvents.MatchEvents.NewResourcesOrganizationEvent;
import it.polimi.ingsw.model.LeaderCards.DepositLeaderPower;
import it.polimi.ingsw.model.LeaderCards.LeaderCard;
import it.polimi.ingsw.model.LeaderCards.LeaderPower;
import it.polimi.ingsw.model.LeaderCards.Requirement;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.utilities.GsonInheritanceAdapter;
import it.polimi.ingsw.utilities.GsonPairAdapter;
import it.polimi.ingsw.utilities.Pair;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
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
    ArrayList<ArrayList<ImageView>> leaderDepotsResources;

    @FXML
    AnchorPane warehouse;

    @FXML
    AnchorPane leaderDepotsAnchorPane;


    ArrayList<ArrayList<ImageView>> depots;

    ArrayList<DepotState> leaderDepots;

    public WarehouseController(GUI gui) {
        super(gui);
        discardResources = new HashMap<>();
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


        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Requirement.class, new GsonInheritanceAdapter<Requirement>());
        builder.registerTypeAdapter(LeaderPower.class, new GsonInheritanceAdapter<LeaderPower>());
        builder.registerTypeAdapter(Pair.class, new GsonPairAdapter());
        Gson gson = builder.create();


        HashMap<Resource, Integer> depotsInfo = new HashMap<>();
        gui.thisPlayerState().leaderCards.forEach((key1, value1) -> {
            try {
                LeaderCard leaderCard = gson.fromJson(Files.readString(Paths.get("src\\main\\resources\\" + key1 + ".json")), LeaderCard.class);
                leaderCard.getLeaderPowers().stream().filter(card -> card instanceof DepositLeaderPower)
                        .forEach(power -> ((DepositLeaderPower) power).getMaxResources()
                                .forEach((key, value) -> depotsInfo.put(key, depotsInfo.getOrDefault(key, 0) + value))
                        );
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        leaderDepots = new ArrayList<>() {{
            for (Resource r : Resource.values())
                add(new DepotState(r, depotsInfo.getOrDefault(r, 0), 0));
        }};
        leaderDepotsAnchorPane.getChildren().stream().map(n -> (AnchorPane) n).forEach(n -> {
            Resource r = Resource.valueOf(n.getId().replace("leaderDepot", "").toUpperCase());

            DepotState depot = leaderDepots.stream()
                    .filter(d -> d.getResourceType() == r)
                    .collect(Collectors.toList())
                    .get(0);
            Label label = (Label) n.getChildren().stream().filter(l -> l instanceof Label).findFirst().orElse(null);
            label.setText(String.valueOf(depot.getCurrentQuantity()));
            ImageView image = (ImageView) n.getChildren().stream().filter(l -> l instanceof ImageView).findFirst().orElse(null);
            image.setImage(new Image(finalImagePath + r.toString().toLowerCase() + "2.png"));
            if (depot.getCurrentQuantity() == 0) {
                image.setOpacity(0);
            }


        });


    }

    public void onDragDetected(MouseEvent event) {

        if (event.getSource() instanceof ImageView) {
            ImageView source = (ImageView) event.getSource();
            if (source.getImage() != null) {
                Dragboard db = source.startDragAndDrop(TransferMode.MOVE);

                ClipboardContent content = new ClipboardContent();

                content.putString(source.getImage().getUrl());
                db.setContent(content);
                event.consume();
            }
        } else if (event.getSource() instanceof AnchorPane && ((AnchorPane) event.getSource()).getId() != null
                && ((AnchorPane) event.getSource()).getId().contains("leaderDepot")) {
            AnchorPane source = ((AnchorPane) event.getSource());
            ImageView image = ((ImageView) source.getChildren().stream().filter(n -> n instanceof ImageView).collect(Collectors.toList()).get(0));
            if (leaderDepots.stream().filter(n -> image.getImage().getUrl().toUpperCase().contains(n.getResourceType().toString())).collect(Collectors.toList()).get(0)
                    .getCurrentQuantity() != 0) {
                Dragboard db = source.startDragAndDrop(TransferMode.MOVE);
                ClipboardContent content = new ClipboardContent();
                content.putString(image.getImage().getUrl());
                db.setContent(content);
                event.consume();
            }
        }
    }

    public void onDragDroppedOnLeader(DragEvent event) {
        Dragboard db = event.getDragboard();
        boolean success = false;
        if (db.hasString()) {
            AnchorPane target = ((AnchorPane) ((Node) event.getTarget()).getParent());
            Resource res = Resource.valueOf(target.getId().replace("leaderDepot", "").toUpperCase());
            DepotState depotState = leaderDepots.stream().filter(n -> n.getResourceType() == res).collect(Collectors.toList()).get(0);
            depotState.tryAddResource(res, 1);

            target.getChildren().stream().filter(n -> n instanceof Label)
                    .map(n -> (Label) n)
                    .collect(Collectors.toList()).get(0).setText(String.valueOf(depotState.getCurrentQuantity()));
            if (depotState.getCurrentQuantity() >= 1)
                target.getChildren().stream().filter(n -> n instanceof ImageView)
                        .map(n -> (ImageView) n)
                        .collect(Collectors.toList()).get(0).setOpacity(1);
            success = true;
        }

        event.setDropCompleted(success);
        event.consume();
    }

    public void onDragDropped(DragEvent event) {
        Dragboard db = event.getDragboard();
        boolean success = false;
        if (db.hasString()) {
            ((ImageView) event.getTarget()).setImage(new Image(db.getString()));
            success = true;
        }
        event.setDropCompleted(success);
        event.consume();
    }

    public void onDragDroppedOnBin(DragEvent event) {
        Dragboard db = event.getDragboard();
        boolean success = false;
        if (db.hasString()) {
            String s = db.getString();
            s = s.substring(s.lastIndexOf("/") + 1, s.lastIndexOf(".")).toUpperCase();
            s = s.substring(0, s.length() - 1);
            //System.out.println(s);
            Resource r = Resource.valueOf(s);
            discardResources.put(r, discardResources.getOrDefault(r, 0) + 1);
            success = true;
        }

        event.setDropCompleted(success);
        event.consume();
    }

    public void onDragDone(DragEvent event) {
        if (event.getSource() instanceof ImageView) {
            if (event.getTransferMode() == TransferMode.MOVE) {
                ((ImageView) event.getSource()).setImage(null);
            }
        } else if (event.getSource() instanceof AnchorPane) {
            if (event.getTransferMode() == TransferMode.MOVE) {
                Label l = (Label) ((AnchorPane) event.getSource()).getChildren().stream().filter(n -> n instanceof Label).collect(Collectors.toList()).get(0);
                ImageView im = (ImageView) ((AnchorPane) event.getSource()).getChildren().stream().filter(n -> n instanceof ImageView).collect(Collectors.toList()).get(0);

                DepotState depot = leaderDepots.stream().filter(n -> im.getImage().getUrl().toUpperCase().contains(n.getResourceType().toString())).collect(Collectors.toList()).get(0);
                depot.trySubResource(depot.getResourceType(), 1);
                l.setText(String.valueOf(depot.getCurrentQuantity()));
                if (depot.getCurrentQuantity() == 0) {
                    im.setOpacity(0);
                }
            }
        }
        event.consume();

    }

    public void onDragOver(DragEvent event) {
        Node target = (Node) event.getTarget();
        if (event.getGestureSource() != target) {
            Parent parent = target.getParent();
            // if it is hovering a leader depot

            if (parent instanceof AnchorPane &&
                    parent.getId() != null &&
                    parent.getId().contains("leader")) {
                String type = parent.getId().replace("leaderDepot", "");
                type = type.toUpperCase();
                String url = event.getDragboard().getString();
                // check the compatibility of the resource dragged
                // with the one of the leader depot
                if (url.toUpperCase().contains(type)) {
                    Resource resource = Resource.valueOf(type);
                    //if exist some free compatible depot
                    if (leaderDepots.stream().anyMatch(depot -> depot.getResourceType().equals(resource)
                            && depot.getCurrentQuantity() < depot.getMaxQuantity()))
                        event.acceptTransferModes(TransferMode.MOVE);
                }
            }
            // of is hovering the trash bin
            else if (parent instanceof AnchorPane &&
                    parent.getId() != null &&
                    parent.getId().equals("bin")) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
            // if it is hovering a warehouse depot
            else if (event.getSource() instanceof ImageView) {
                ImageView source = (ImageView) event.getSource();
                if (source.getImage() == null) {
                    AnchorPane parent2 = ((AnchorPane) source.getParent());
                    int i = parent2.getChildren().indexOf(source);
                    String type = event.getDragboard().getString();
//                    System.out.println("sourceres: " + type);
                    switch (i) {
                        case 1 -> event.acceptTransferModes(TransferMode.MOVE);
                        case 2 -> {
                            ImageView sibling = (ImageView) parent2.getChildren().get(i + 1);
//                            if (sibling.getImage() != null)
//                                System.out.println("siblings1: " + sibling.getImage().getUrl());
                            if (sibling.getImage() == null ||
                                    sibling.getImage().getUrl().contains(type))
                                event.acceptTransferModes(TransferMode.MOVE);
                        }
                        case 3 -> {
                            ImageView sibling = (ImageView) parent2.getChildren().get(i - 1);
//                            if (sibling.getImage() != null)
//                                System.out.println("siblings1: " + sibling.getImage().getUrl());
                            if (sibling.getImage() == null ||
                                    sibling.getImage().getUrl().contains(type))
                                event.acceptTransferModes(TransferMode.MOVE);
                        }
                        case 4 -> {
                            ImageView sibling = (ImageView) parent2.getChildren().get(i + 1);
                            ImageView sibling2 = (ImageView) parent2.getChildren().get(i + 2);
//                            if (sibling.getImage() != null)
//                                System.out.println("siblings1: " + sibling.getImage().getUrl());
//                            if (sibling2.getImage() != null)
//                                System.out.println("siblings2: " + sibling2.getImage().getUrl());
                            if ((sibling.getImage() == null && sibling2.getImage() == null) ||
                                    Objects.requireNonNullElse(sibling.getImage(), sibling2.getImage()).getUrl().contains(type))
                                event.acceptTransferModes(TransferMode.MOVE);
                        }
                        case 5 -> {
                            ImageView sibling = (ImageView) parent2.getChildren().get(i + 1);
                            ImageView sibling2 = (ImageView) parent2.getChildren().get(i - 1);

//                            if (sibling.getImage() != null)
//                                System.out.println("siblings1: " + sibling.getImage().getUrl());
//                            if (sibling2.getImage() != null)
//                                System.out.println("siblings2: " + sibling2.getImage().getUrl());

                            if ((sibling.getImage() == null && sibling2.getImage() == null) ||
                                    Objects.requireNonNullElse(sibling.getImage(), sibling2.getImage()).getUrl().contains(type))
                                event.acceptTransferModes(TransferMode.MOVE);

                        }
                        case 6 -> {
                            ImageView sibling = (ImageView) parent2.getChildren().get(i - 1);
                            ImageView sibling2 = (ImageView) parent2.getChildren().get(i - 1);
//                            if (sibling.getImage() != null)
//                                System.out.println("siblings1: " + sibling.getImage().getUrl());
//                            if (sibling2.getImage() != null)
//                                System.out.println("siblings2: " + sibling2.getImage().getUrl());
                            if ((sibling.getImage() == null && sibling2.getImage() == null) ||
                                    Objects.requireNonNullElse(sibling.getImage(), sibling2.getImage()).getUrl().contains(type))
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
            if (image == null)
                return;
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
        ImageView image = (ImageView) target.getChildren().stream().filter(n -> {
            if (n.getId() != null && n.getId().equals("binTop"))
                return true;
            return false;
        }).findFirst().orElse(null);
        if (image == null)
            return;
        double k = Math.min(image.getFitWidth() / image.getImage().getWidth(), image.getFitHeight() / image.getImage().getHeight());
        double width = image.getImage().getWidth() * k;
        double height = image.getImage().getHeight() * k;
        image.setRotate(0);
        image.setTranslateX(0);
        image.setTranslateY(0);


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
