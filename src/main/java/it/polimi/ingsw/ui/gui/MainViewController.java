package it.polimi.ingsw.ui.gui;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.events.ClientEvents.DepotState;
import it.polimi.ingsw.model.LeaderCards.DepositLeaderPower;
import it.polimi.ingsw.model.LeaderCards.LeaderCard;
import it.polimi.ingsw.model.LeaderCards.LeaderPower;
import it.polimi.ingsw.model.LeaderCards.Requirement;
import it.polimi.ingsw.model.Resource;
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

public class MainViewController extends Controller implements Initializable {
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
    Label victoryPoints;
    @FXML
    HBox ownedCardsSlot;
    @FXML
    Label victoryPointsLabel;

    @FXML
    VBox playerSlot;

    LockWrap<FaithTrackController> faithTrackController = new LockWrap<>(null, null);

    public MainViewController(GUI gui) {
        super(gui);
    }

    public boolean waitForReady() {
        return faithTrackController.getWaitIfLocked() != null;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        SelectableImage.setSelectable(root);
        MainViewController aux = this;
        Platform.runLater(() -> {
            faithTrackController.setItem(new FaithTrackController(gui, faithTrack, aux.gui.singlePlayer.getWaitIfLocked()));
            aux.setFaithTrackPosition(0);
        });
        ((Label) playerSlot.getChildren().stream().filter(n -> n instanceof Label).collect(Collectors.toList()).get(0)).setText(gui.playerID.getItem());
        //updateAll();
    }


    public void openMarket() throws IOException {
        Stage marketStage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("Market.fxml"));
        fxmlLoader.setController(new MarketController(gui));
        Scene scene = new Scene(fxmlLoader.load());
        marketStage.initModality(Modality.APPLICATION_MODAL);
        marketStage.setScene(scene);
        marketStage.showAndWait();
    }

    public void openCardGrid(MouseEvent event) throws IOException {
        Stage gridStage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("DevCardGrid.fxml"));
        fxmlLoader.setController(new DevCardGridController(gui));
        Scene scene = new Scene(fxmlLoader.load());
        gridStage.initModality(Modality.APPLICATION_MODAL);
        gridStage.setScene(scene);
        gridStage.showAndWait();
    }

    public void openProduction() throws IOException {
        if (!PlayerState.canPerformActions || PlayerState.availableActions.contains(Action.PRODUCE))
            return;

        Stage productionStage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("Production.fxml"));
        fxmlLoader.setController(new ProductionController(gui));
        Scene scene = new Scene(fxmlLoader.load());
        productionStage.initModality(Modality.APPLICATION_MODAL);
        productionStage.setScene(scene);
        productionStage.showAndWait();

    }

    public void openDepots() throws IOException {
        Stage depotsStage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("Warehouse.fxml"));
        fxmlLoader.setController(new WarehouseController(gui, new HashMap<>()));
        Scene scene = new Scene(fxmlLoader.load());
        depotsStage.initModality(Modality.APPLICATION_MODAL);
        depotsStage.setScene(scene);
        depotsStage.showAndWait();

    }


    public void decrementPos() {
        faithTrackController.getWaitIfLocked().setPlayerPosition(gui.thisPlayerState().getFaithTrackPosition() - 1);
    }

    public void incrementPos() {
        faithTrackController.getWaitIfLocked().setPlayerPosition(gui.thisPlayerState().getFaithTrackPosition() + 1);
    }

    public void setFaithTrackPosition(int pos) {
        faithTrackController.getWaitIfLocked().setPlayerPosition(pos);
    }

    public void setLorenzoFaithTrackPosition(int pos) {
        faithTrackController.getWaitIfLocked().setLorenzoPosition(pos);
    }


    public void updateMarket() {
        ArrayList<ArrayList<String>> marketStatus = new ArrayList<>();
        String imagePath = new java.io.File(".").getAbsolutePath() + "/src/main/resources/it/polimi/ingsw/ui/gui/images/";
        for (int i = 0; i < PlayerState.marketStatus.getKey().length; i++) {
            marketStatus.add(new ArrayList<>());
            for (int j = 0; j < PlayerState.marketStatus.getKey()[0].length; j++) {
                String url = "file:/" + imagePath + PlayerState.marketStatus.getKey()[i][j].toString().toLowerCase() + "-marble.png";
                marketStatus.get(i).add(url);
            }
        }
        String marbleLeft = "file:/" + imagePath + PlayerState.marketStatus.getValue().toString().toLowerCase() + "-marble.png";
        System.out.println(marbleLeft);
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
        System.out.println("market updated");
    }

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
        imageUrl = "file:/" + imageUrl.substring(0, imageUrl.length() - 2) + "/src/main/resources/it/polimi/ingsw/ui/gui/images/front/";
        var ownedCardIds = gui.thisPlayerState().ownedCards;
        for (int i = 0; i < ownedCardIds.size(); i++) {
            int j = 0;
            for (j = 0; j < ownedCardIds.get(i).size(); j++) {
                ownedCards.get(i).get(j).setImage(new Image(imageUrl + ownedCardIds.get(i).get(j) + ".png"));
                ownedCards.get(i).get(j).setVisible(true);
            }
            if (j != 0)
                ownedCards.get(i).get(j - 1).getStyleClass().add("hoverBorder");
        }
        SelectableImage.setSelectable(ownedCardsSlot);
        System.out.println("owned cards update");
    }


    public void updateDepots() {

        ArrayList<ArrayList<ImageView>> depots = new ArrayList<>();
        String imagePath = new java.io.File(".").getAbsolutePath();
        imagePath = "file:/" + imagePath.substring(0, imagePath.length() - 2) + "/src/main/resources/it/polimi/ingsw/ui/gui/images/";
        String finalImagePath = imagePath;
        var images = gui.thisPlayerState().warehouse.stream().flatMap(n -> new ArrayList<Image>() {{
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
        System.out.println("depots updated");
    }

    public void updateStrongBox() {
        SelectableImage.getChildrenOf(strongBoxSlot).stream().filter(n -> n.getId() != null &&
                n.getId().contains("Counter")).map(l -> (javafx.scene.control.Label) l)
                .forEach(l ->
                {
                    Resource r = Resource.valueOf(l.getId().replace("Counter", "").toUpperCase());
                    l.setText(String.valueOf(gui.thisPlayerState().strongBox.getOrDefault(r, 0)));
                });
        System.out.println("strongbox updated");
    }

    public void updateVictoryPoints() {
        victoryPointsLabel.setText(String.valueOf(gui.thisPlayerState().victoryPoints));
        System.out.println("victory points updated");
    }

    public void openPopeFavorCard() {

    }

    public void updateLeaderCards() {

    }

    @FXML
    public void exitApplication(ActionEvent event) {
        ((Stage) root.getScene().getWindow()).close();
        gui.close();
    }

    public void updateAll() {
        updateStrongBox();
        updateDepots();
        updateMarket();
        updateOwnedCards();
        updateVictoryPoints();
    }


    public void setTurnActive(boolean active) {
        System.out.println("turn active: " + active);
        if (active)
            playerSlot.setStyle("-fx-border-color: #2a801b; -fx-background-color: rgba(131, 255, 131, 0.25)");
        else
            playerSlot.setStyle("-fx-border-color: #801b1b; -fx-background-color: rgba(165, 27, 27, 0.25)");
    }


    public void leaderAction(MouseEvent mouseEvent) throws IOException {
        if (!PlayerState.canPerformActions && PlayerState.availableActions.contains(Action.LEADER_ACTION))
            return;

        Stage depotsStage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("LeaderCardAction.fxml"));
        fxmlLoader.setController(new LeaderCardActionController(gui));
        Scene scene = new Scene(fxmlLoader.load());
        depotsStage.initModality(Modality.APPLICATION_MODAL);
        depotsStage.setScene(scene);
        depotsStage.showAndWait();
    }
}
