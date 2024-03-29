package it.polimi.ingsw.ui.gui;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.events.clientEvents.DepotState;
import it.polimi.ingsw.events.controllerEvents.matchEvents.EndTurnEvent;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.model.leaderCards.DepositLeaderPower;
import it.polimi.ingsw.model.leaderCards.LeaderCard;
import it.polimi.ingsw.model.leaderCards.LeaderPower;
import it.polimi.ingsw.model.leaderCards.Requirement;
import it.polimi.ingsw.ui.cli.Action;
import it.polimi.ingsw.utilities.GsonInheritanceAdapter;
import it.polimi.ingsw.utilities.GsonPairAdapter;
import it.polimi.ingsw.utilities.LockWrap;
import it.polimi.ingsw.utilities.Pair;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class OthersDashBoardController extends  Controller implements Initializable {
    public OthersDashBoardController(GUI gui, PlayerState thisPlayerState, String playerID) {

        super(gui);
        this.thisPlayerState = thisPlayerState;
        this.playerID = playerID;
    }

    PlayerState thisPlayerState;



    @FXML
    BorderPane root;
    @FXML
    GridPane faithTrack;
    @FXML
    AnchorPane marketSlot;
    @FXML
    AnchorPane warehouseSlot;
    @FXML
    AnchorPane leaderDepotsSlot;
    @FXML
    AnchorPane leaderCardsSlot;
    @FXML
    AnchorPane strongBoxSlot;
    @FXML
    HBox ownedCardsSlot;
    @FXML
    VBox playerSlot;
    @FXML
    ImageView popeFavorCard1;
    @FXML
    ImageView popeFavorCard2;
    @FXML
    ImageView popeFavorCard3;
    @FXML
    ImageView playerImage;


    String playerID;

    LockWrap<FaithTrackController> faithTrackController = new LockWrap<>(null, null);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        SelectableImage.setSelectable(root);

        Platform.runLater(() -> {
            ArrayList<ImageView> arr = new ArrayList<>() {{
                add(popeFavorCard1);
                add(popeFavorCard2);
                add(popeFavorCard3);
            }};
            faithTrackController.setItem(new FaithTrackController(gui, faithTrack, false, arr));
            faithTrackController.getItem().setPlayerPosition(thisPlayerState.getFaithTrackPosition());
        });
        ((Label) playerSlot.getChildren().stream().filter(n -> n instanceof Label).collect(Collectors.toList()).get(0)).setText(playerID);
        playerImage = null;
        ////System.out.printlnln("update all");
        updateAll();
    }

    /**
     * updates the market status shown in the dashboard
     */
    public void updateMarket() {
        ArrayList<ArrayList<String>> marketStatus = new ArrayList<>();
        String imagePath = new java.io.File(".").getAbsolutePath() + "/src/main/resources/it/polimi/ingsw/ui/gui/images/";
        for (int i = 0; i < PlayerState.marketStatus.getKey().length; i++) {
            marketStatus.add(new ArrayList<>());
            for (int j = 0; j < PlayerState.marketStatus.getKey()[0].length; j++) {
                String url = "file:" + imagePath + PlayerState.marketStatus.getKey()[i][j].toString().toLowerCase() + "-marble.png";
                marketStatus.get(i).add(url);
            }
        }
        String marbleLeft = "file:" + imagePath + PlayerState.marketStatus.getValue().toString().toLowerCase() + "-marble.png";
        ////System.out.printlnln(marbleLeft);
        GridPane gridPane = (GridPane) marketSlot.getChildren().stream().filter(n -> n instanceof GridPane).findFirst().orElse(null);
        var marbles = gridPane.getChildren()
                .stream().filter(n -> n instanceof ImageView)
                .map(i -> (ImageView) i).collect(Collectors.toList());

        ImageView marbleLeftImage = (ImageView) SelectableImage.getChildrenOf(marketSlot).stream().filter(i ->
                (i instanceof ImageView) &&
                        (i.getId() != null) &&
                        (i.getId().equals("marbleLeft")))
                .collect(Collectors.toList()).get(0);

        for (var marble : marbles) {
            marble.setImage(new javafx.scene.image.Image(marketStatus.get(marble.getId().charAt(2) - '0')
                    .get(marble.getId().charAt(1) - '0')));
        }
        marbleLeftImage.setImage(new Image(marbleLeft));
        ////System.out.printlnln("market updated");
    }
    /**
     * updates the owned cards in the dashboard
     */
    public void updateOwnedCards() {
        ArrayList<ArrayList<ImageView>> ownedCards = new ArrayList<>();
        ownedCards.add(new ArrayList<>(((Group) ownedCardsSlot.getChildren().get(0)).getChildren().stream().filter(i -> i instanceof ImageView)
                .map(i -> (ImageView) i)
                .collect(Collectors.toList())));
        ownedCards.add(new ArrayList<>(((Group) ownedCardsSlot.getChildren().get(1)).getChildren().stream().filter(i -> i instanceof ImageView)
                .map(i -> (ImageView) i)
                .collect(Collectors.toList())));
        ownedCards.add(new ArrayList<>(((Group) ownedCardsSlot.getChildren().get(2)).getChildren().stream().filter(i -> i instanceof ImageView)
                .map(i -> (ImageView) i)
                .collect(Collectors.toList())));

        ownedCards.forEach(list -> list.forEach(imageView -> {
            imageView.setVisible(false);
            imageView.getStyleClass().remove("hoverBorder");
        }));
        SelectableImage.cleanRoot(ownedCardsSlot);


        String imageUrl = new java.io.File(".").getAbsolutePath();
        imageUrl = "file:" + imageUrl.substring(0, imageUrl.length() - 2) + "/src/main/resources/it/polimi/ingsw/ui/gui/images/front/";
        var ownedCardIds = thisPlayerState.ownedCards;
        for (int i = 0; i < ownedCardIds.size(); i++) {
            int j = 0;
            for (j = 0; j < ownedCardIds.get(i).size(); j++) {
                ownedCards.get(i).get(j).setImage(new Image(imageUrl + ownedCardIds.get(i).get(j) + ".png"));
                ownedCards.get(i).get(j).setVisible(true);
            }
        }
        SelectableImage.setSelectable(ownedCardsSlot);
        ////System.out.printlnln("owned cards update");
    }


    /**
     * updates the warehouse and the leader depots shown in the dashboard
     */
    public void updateDepots() {

        ArrayList<ArrayList<ImageView>> depots = new ArrayList<>();
        String imagePath = new java.io.File(".").getAbsolutePath();
        imagePath = "file:" + imagePath.substring(0, imagePath.length() - 2) + "/src/main/resources/it/polimi/ingsw/ui/gui/images/";
        String finalImagePath = imagePath;
        var images = thisPlayerState.warehouse.stream().flatMap(n -> new ArrayList<Image>() {{
            for (int i = 0; i < n.getMaxQuantity(); i++) {
                if (i < n.getCurrentQuantity())
                    add(new Image(finalImagePath + n.getResourceType().toString().toLowerCase() + "2.png"));
                else
                    add(null);
            }
        }}.stream()).collect(Collectors.toList());

        var resources = warehouseSlot.getChildren().stream().filter(n -> n.getStyleClass().contains("Resource")).map(n -> (ImageView) n).collect(Collectors.toList());
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


        HashMap<Resource, Pair<Integer, Integer>> depotsInfo = new HashMap<>() {{
            for (var res : Resource.values())
                put(res, new Pair<>(0, 0));
        }};
        thisPlayerState.leaderCards.forEach((card, cardActive) -> {
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
                                                                thisPlayerState.leaderPowerStates.get(card) != null
                                                                && thisPlayerState.leaderPowerStates.get(card).get(powerIndex)
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
        for (var entry : thisPlayerState.getLeaderDepots().entrySet()) {
            depotsInfo.put(entry.getKey(), new Pair<>(depotsInfo.get(entry.getKey()).getKey(), entry.getValue()));
        }


        ArrayList<DepotState> leaderDepots = new ArrayList<>() {{
            for (Resource r : Resource.values())
                add(new DepotState(r, depotsInfo.getOrDefault(r, new Pair<>(0, 0)).getKey(), depotsInfo.getOrDefault(r, new Pair<>(0, 0)).getValue()));
        }};


        leaderDepotsSlot.getChildren().stream().map(n -> (AnchorPane) n).forEach(n -> {
            Resource r = Resource.valueOf(n.getId().replace("leaderDepot", "").toUpperCase());

            DepotState depot = leaderDepots.stream()
                    .filter(d -> d.getResourceType() == r)
                    .collect(Collectors.toList())
                    .get(0);
            javafx.scene.control.Label label = (javafx.scene.control.Label) n.getChildren().stream().filter(l -> l instanceof javafx.scene.control.Label).findFirst().orElse(null);
            label.setText(String.valueOf(depot.getCurrentQuantity()));
            ImageView image = (ImageView) n.getChildren().stream().filter(l -> l instanceof ImageView).findFirst().orElse(null);
            image.setImage(new Image(finalImagePath + r.toString().toLowerCase() + "2.png"));
            if (depot.getCurrentQuantity() == 0) {
                image.setOpacity(0);
            }
        });
        ////System.out.printlnln("depots updated");
    }
    /**
     * updates the strongbox shown in the dashboard
     */
    public void updateStrongBox() {
        SelectableImage.getChildrenOf(strongBoxSlot).stream().filter(n -> n.getId() != null &&
                n.getId().contains("Counter")).map(l -> (javafx.scene.control.Label) l)
                .forEach(l ->
                {
                    Resource r = Resource.valueOf(l.getId().replace("Counter", "").toUpperCase());
                    l.setText(String.valueOf(thisPlayerState.strongBox.getOrDefault(r, 0)));
                });
        ////System.out.printlnln("strongbox updated");
    }


    /**
     * updates the leaders shown in the dashboard
     */
    public void updateLeaderCards() {
        assert leaderCardsSlot.getChildren().size() != thisPlayerState.leaderCards.size();
        String imageUrl = new java.io.File(".").getAbsolutePath();
        imageUrl = "file:" + imageUrl.substring(0, imageUrl.length() - 2) + "/src/main/resources/it/polimi/ingsw/ui/gui/images/";
        for (int i = 0; i < thisPlayerState.leaderCards.size(); i++) {
            String leaderId = (String) thisPlayerState.leaderCards.keySet().toArray()[i];
            ((ImageView) leaderCardsSlot.getChildren().get(i)).setImage(new Image(imageUrl + leaderId + ".png"));
        }
    }


    /**
     * update all the view with new data
     */
    public void updateAll() {
        updateStrongBox();
        updateDepots();
        updateMarket();
        updateOwnedCards();
        updateLeaderCards();
    }





}
