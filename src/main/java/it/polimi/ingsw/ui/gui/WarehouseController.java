package it.polimi.ingsw.ui.gui;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.events.clientEvents.DepositLeaderPowerStateEvent;
import it.polimi.ingsw.events.clientEvents.DepotState;
import it.polimi.ingsw.events.controllerEvents.matchEvents.NewResourcesOrganizationEvent;
import it.polimi.ingsw.model.leaderCards.DepositLeaderPower;
import it.polimi.ingsw.model.leaderCards.LeaderCard;
import it.polimi.ingsw.model.leaderCards.LeaderPower;
import it.polimi.ingsw.model.leaderCards.Requirement;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.utilities.GsonInheritanceAdapter;
import it.polimi.ingsw.utilities.GsonPairAdapter;
import it.polimi.ingsw.utilities.Pair;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
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
import java.util.*;
import java.util.stream.Collectors;

public class WarehouseController extends Controller implements Initializable {

    @FXML
    AnchorPane root;
    HashMap<Resource, Integer> discardResources;

    @FXML
    Button nextButton;

    @FXML
    ArrayList<ArrayList<ImageView>> leaderDepotsResources;

    @FXML
    AnchorPane warehouse;

    @FXML
    AnchorPane leaderDepotsAnchorPane;
    @FXML
    AnchorPane toDiscardAnchorPane;

    ArrayList<ArrayList<ImageView>> depots;

    ArrayList<DepotState> leaderDepots;


    public WarehouseController(GUI gui, HashMap<Resource, Integer> resourcesToPlace) {
        super(gui);
        discardResources = resourcesToPlace;
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
            for (int i = n.getCurrentQuantity(); i < n.getMaxQuantity(); i++)
                add(null);
        }}.stream()).collect(Collectors.toList());

        var resources = warehouse.getChildren().stream()
                .filter(n -> n.getStyleClass().contains("Resource"))
                .map(n -> (ImageView) n).collect(Collectors.toList());

        for (int i = 0; i < resources.size(); i++) {
            if (i < images.size())
                resources.get(i).setImage(images.get(i));
            else
                resources.get(i).setImage(null);
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


        HashMap<Resource, Pair<Integer, Integer>> depotsInfo = new HashMap<>() {{
            Arrays.stream(Resource.values()).forEach(r ->
                    put(r, new Pair<>(0, 0))
            );
        }};
        gui.thisPlayerState().leaderCards.forEach((card, cardActive) -> {
            try {
                LeaderCard leaderCard = gson.fromJson(Files.readString(Paths.get("src/main/resources/" + card + ".json")), LeaderCard.class);
                leaderCard.getLeaderPowers().stream().filter(power -> power instanceof DepositLeaderPower)
                        .forEach(power -> {
                                    int powerIndex = leaderCard.getLeaderPowers().indexOf(power);
                                    ((DepositLeaderPower) power).getMaxResources()
                                            .forEach((key, value) ->
                                                    {
                                                        if (cardActive
                                                                &&
                                                                gui.thisPlayerState().leaderPowerStates.get(card) != null
                                                                && gui.thisPlayerState().leaderPowerStates.get(card).get(powerIndex)
                                                        )
                                                            depotsInfo.put(key, new Pair<>(depotsInfo.getOrDefault(key, new Pair<>(0, 0)).getKey() + value, 0));
                                                    }
                                            );

                                }
                        );
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        for (var entry : gui.thisPlayerState().getLeaderDepots().entrySet()) {
            depotsInfo.put(entry.getKey(), new Pair<>(depotsInfo.get(entry.getKey()).getKey(), entry.getValue()));
        }

        leaderDepots = new ArrayList<>() {{
            for (Resource r : Resource.values())
                add(new DepotState(r, depotsInfo.getOrDefault(r, new Pair<>(0, 0)).getKey(), depotsInfo.getOrDefault(r, new Pair<>(0, 0)).getValue()));
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

        //discardResources.entrySet().stream().map(e -> new Pair(e.getKey(), e.getValue())).forEach(System.out::print);
        toDiscardAnchorPane.getChildren().stream().map(n -> (AnchorPane) n).forEach(n -> {
            Resource r = Resource.valueOf(n.getId().replace("ToDiscard", "").toUpperCase());

            Label label = (Label) n.getChildren().stream().filter(l -> l instanceof Label).findFirst().orElse(null);
            label.setText(String.valueOf(discardResources.getOrDefault(r, 0)));
            ImageView image = (ImageView) n.getChildren().stream().filter(l -> l instanceof ImageView).findFirst().orElse(null);
            image.setImage(new Image(finalImagePath + r.toString().toLowerCase() + "2.png"));
            //System.out.println("Res: " + r);
            if (discardResources.getOrDefault(r, 0) == 0) {
                image.setOpacity(0.5);
            }
        });


        if (!PlayerState.canPerformActions)
            nextButton.setDisable(true);

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
        } else if (event.getSource() instanceof AnchorPane && ((AnchorPane) event.getSource()).getId() != null) {
            AnchorPane source = ((AnchorPane) event.getSource());
            ImageView image = ((ImageView) source.getChildren().stream().filter(n -> n instanceof ImageView).collect(Collectors.toList()).get(0));
            Resource res = Resource.valueOf(source.getId().replace("leaderDepot", "").replace("ToDiscard", "").toUpperCase());
            if ((source.getId().contains("leaderDepot") &&
                    (leaderDepots.stream().filter(n -> image.getImage().getUrl().toUpperCase().contains(n.getResourceType().toString())).collect(Collectors.toList()).get(0)
                            .getCurrentQuantity() != 0))
                    ||
                    (source.getId().contains("ToDiscard") && discardResources.get(res) != 0)
            ) {
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
            AnchorPane target = ((AnchorPane) event.getTarget());
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

    public void onDragDroppedOnToDiscard(DragEvent event) {
        Dragboard db = event.getDragboard();
        boolean success = false;
        if (db.hasString()) {
            AnchorPane target = ((AnchorPane) event.getTarget());
            Resource res = Resource.valueOf(target.getId().replace("ToDiscard", "").toUpperCase());
            discardResources.put(res, discardResources.getOrDefault(res, 0) + 1);

            target.getChildren().stream().filter(n -> n instanceof Label)
                    .map(n -> (Label) n)
                    .collect(Collectors.toList()).get(0).setText(String.valueOf(discardResources.get(res)));
            if (discardResources.get(res) >= 1)
                target.getChildren().stream().filter(n -> n instanceof ImageView)
                        .map(n -> (ImageView) n)
                        .collect(Collectors.toList()).get(0).setOpacity(1);
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
                AnchorPane target = (AnchorPane) event.getSource();
                if (target.getId() != null && target.getId().contains("leader")) {
                    Label l = (Label) ((AnchorPane) event.getSource()).getChildren().stream().filter(n -> n instanceof Label).collect(Collectors.toList()).get(0);
                    ImageView im = (ImageView) ((AnchorPane) event.getSource()).getChildren().stream().filter(n -> n instanceof ImageView).collect(Collectors.toList()).get(0);

                    DepotState depot = leaderDepots.stream().filter(n -> im.getImage().getUrl().toUpperCase().contains(n.getResourceType().toString())).collect(Collectors.toList()).get(0);
                    depot.trySubResource(depot.getResourceType(), 1);
                    l.setText(String.valueOf(depot.getCurrentQuantity()));
                    if (depot.getCurrentQuantity() == 0) {
                        im.setOpacity(0);
                    }
                } else if (target.getId() != null && target.getId().contains("ToDiscard")) {
                    Label l = (Label) ((AnchorPane) event.getSource()).getChildren().stream().filter(n -> n instanceof Label).collect(Collectors.toList()).get(0);
                    ImageView im = (ImageView) ((AnchorPane) event.getSource()).getChildren().stream().filter(n -> n instanceof ImageView).collect(Collectors.toList()).get(0);
                    Resource type = Resource.valueOf(target.getId().replace("ToDiscard", "").toUpperCase());
                    discardResources.put(type, discardResources.get(type) - 1);
                    l.setText(String.valueOf(discardResources.get(type)));
                    if (discardResources.get(type) == 0)
                        im.setOpacity(0.5);
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

            if (target instanceof AnchorPane &&
                    target.getId() != null) {
                String type;
                if (target.getId().contains("leader")) {
                    type = target.getId().replace("leaderDepot", "");
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
                } else {
                    type = target.getId().replace("ToDiscard", "").toUpperCase();
                    String url = event.getDragboard().getString();
                    // check the compatibility of the resource dragged
                    // with the one of the leader depot
                    if (url.toUpperCase().contains(type)) {

                        event.acceptTransferModes(TransferMode.MOVE);
                    }
                }


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

        class aux {
            aux(){
                this(Resource.ROCK, 0, 0);
            }
            aux(Resource res, int maxQ, int curQ){
                this.res = res;
                this.maxQ = maxQ; this.curQ = curQ;
            }
            Resource res;
            int maxQ;
            int curQ;
        }


        ArrayList<aux> qty = new ArrayList<>(){{
            add(new aux(Resource.ROCK, 1, 0));
            add(new aux(Resource.ROCK, 2, 0));
            add(new aux(Resource.ROCK, 3, 0));
        }};
        var list = warehouse.getChildren().stream().filter(n -> n.getStyleClass().contains("Resource")).collect(Collectors.toList());
        ArrayList<ArrayList<Resource>> resources = new ArrayList<>(){{
            add(new ArrayList<>());
            add(new ArrayList<>());
            add(new ArrayList<>());

        }};
        for (int i = 0; i < list.size(); i++) {
            Resource r = null;
            int inc;
            if (((ImageView) list.get(i)).getImage() != null) {
                String url = ((ImageView) list.get(i)).getImage().getUrl();
                r = Resource.valueOf(url.substring(url.lastIndexOf("/") + 1, url.lastIndexOf("2.png")).toUpperCase());

            }
            resources.get((i == 0) ? 0 : (i <= 2) ? 1 : 2).add(r);
        }

        for(int i = 0; i < resources.size(); i++)
        {
            Resource r = resources.get(i).stream().filter(Objects::nonNull).findAny().orElse(Resource.ROCK);
            depots.add(new DepotState(r, i+1, (int)resources.get(i).stream().filter(Objects::nonNull).count()));
        }


        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Requirement.class, new GsonInheritanceAdapter<Requirement>());
        builder.registerTypeAdapter(LeaderPower.class, new GsonInheritanceAdapter<LeaderPower>());
        builder.registerTypeAdapter(Pair.class, new GsonPairAdapter());
        Gson gson = builder.create();


        HashMap<Resource, Integer> storedInLeaders = new HashMap<>() {{
            leaderDepots.forEach(n -> put(n.getResourceType(), getOrDefault(n.getResourceType(), 0) + n.getCurrentQuantity())
            );
        }};


        ArrayList<DepositLeaderPowerStateEvent> resLeaderDepots = new ArrayList<>();
        gui.thisPlayerState().leaderCards.forEach((card, cardActive) -> {
            if (cardActive) {
                try {
                    LeaderCard leaderCard = gson.fromJson(Files.readString(Paths.get("src/main/resources/" + card + ".json")), LeaderCard.class);
                    leaderCard.getLeaderPowers().stream().filter(power -> power instanceof DepositLeaderPower)
                            .forEach(power -> {
                                        int powerIndex = leaderCard.getLeaderPowers().indexOf(power);
                                        if (gui.thisPlayerState().leaderPowerStates.get(leaderCard.getCardID()).get(powerIndex)) {
                                            HashMap<Resource, Integer> maxResources = ((DepositLeaderPower) power).getMaxResources();
                                            HashMap<Resource, Integer> currentResources = new HashMap<>();
                                            maxResources.forEach((key, value) -> {
                                                int toStore = storedInLeaders.getOrDefault(key, 0);
                                                if (toStore > value) {
                                                    currentResources.put(key, toStore - value);
                                                    storedInLeaders.put(key, toStore - value);
                                                }
                                            });
                                            DepositLeaderPowerStateEvent deposit = new DepositLeaderPowerStateEvent(gui.playerID.getItem(), leaderCard.getCardID(),
                                                    powerIndex, currentResources);
                                            resLeaderDepots.add(deposit);
                                        }
                                    }
                            );
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });


        gui.addEvent(new NewResourcesOrganizationEvent(gui.askUserID(), depots, resLeaderDepots, discardResources));
        ((Stage) root.getScene().getWindow()).close();
    }

}
