package it.polimi.ingsw.ui.gui;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.events.ClientEvents.DepotState;
import it.polimi.ingsw.events.ControllerEvents.MatchEvents.ChosenResourcesEvent;
import it.polimi.ingsw.model.DevCards.DevCard;
import it.polimi.ingsw.model.LeaderCards.LeaderCard;
import it.polimi.ingsw.model.LeaderCards.LeaderPower;
import it.polimi.ingsw.model.LeaderCards.Requirement;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.utilities.GsonInheritanceAdapter;
import it.polimi.ingsw.utilities.GsonPairAdapter;
import it.polimi.ingsw.utilities.Pair;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ProductionController extends Controller implements Initializable {

    @FXML
    ListView<Label> warehouseList;
    @FXML
    ListView<Label> strongboxList;
    @FXML
    ListView<Label> resourcesToUse;
    @FXML
    AnchorPane root;
    @FXML
    ListView<HBox> resourcesOfChoiceList;

    ArrayList<Pair<ImageView, Region>> selectedProductions;

    private final HashMap<Resource, Integer> strongBox;
    private final ArrayList<DepotState> warehouse;
    private final ArrayList<DevCard> devCards;
    private final ArrayList<LeaderCard> leaderCards;
    boolean personalPower;

    ProductionController(GUI gui) {
        super(gui);
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Requirement.class, new GsonInheritanceAdapter<Requirement>());
        builder.registerTypeAdapter(LeaderPower.class, new GsonInheritanceAdapter<LeaderPower>());
        builder.registerTypeAdapter(Pair.class, new GsonPairAdapter());
        Gson gson = builder.create();
        devCards = new ArrayList<>();
        leaderCards = new ArrayList<>();
        try {
            for (var deck : gui.playerState.ownedCards) {
                var devCard = gson.fromJson(Files.readString(Paths.get("src\\main\\resources\\" + deck.get(deck.size() - 1) + ".json")), DevCard.class);
                devCards.add(devCard);
            }

            for (var card : gui.playerState.leaderCards) {
                var leaderCard = gson.fromJson(Files.readString(Paths.get("src\\main\\resources\\" + card + ".json")), LeaderCard.class);
                leaderCards.add(leaderCard);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        this.strongBox = gui.playerState.strongBox;
        this.warehouse = gui.playerState.warehouse;
        selectedProductions = new ArrayList<>();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // ************************************************************************************************** //
        // INITIALIZATION RESOURCES LISTS
        // ************************************************************************************************** //
        ArrayList<Label> warehouseMarbles = (ArrayList<Label>) warehouse.stream().flatMap(n -> (new ArrayList<String>() {{
            for (int i = 0; i < n.getCurrentQuantity(); i++)
                add(n.getResourceType().name());
        }}).stream()).map(Label::new).collect(Collectors.toList());
        warehouseList.getItems().addAll(warehouseMarbles);
        warehouseList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        ArrayList<Label> strongBoxMarbles = (ArrayList<Label>) strongBox.entrySet().stream().flatMap(n -> new ArrayList<String>() {{
            for (int i = 0; i < n.getValue(); i++)
                add(n.getKey().name());
        }}.stream()).map(Label::new).collect(Collectors.toList());
        strongboxList.getItems().addAll(strongBoxMarbles);
        strongboxList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        resourcesToUse.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);


        // ************************************************************************************************** //
        // SETTING CARDS IMAGES
        // ************************************************************************************************** //
        var selectableImages = root.getChildren().stream()
                .filter(n -> n.getStyleClass().contains("selectable") && n instanceof ImageView)
                .map(n -> (ImageView) n)
                .collect(Collectors.toList());
        String imagePath = new java.io.File(".").getAbsolutePath();
        imagePath = imagePath.substring(0, imagePath.length() - 2) + "/src/main/resources/it/polimi/ingsw/ui/gui/images/";
        var devCardsImages = selectableImages.stream().filter(n -> n.getStyleClass().contains("devCard")).collect(Collectors.toList());
        for (int i = 0; i < devCardsImages.size(); i++)
            devCardsImages.get(i).setImage(new Image("file:/" + imagePath + "front/" +
                    gui.playerState.ownedCards.get(i).get(gui.playerState.ownedCards.get(i).size() - 1) + ".png"));
        System.out.println(devCardsImages.size());
        var leaderCardImages = selectableImages.stream().filter(n -> n.getStyleClass().contains("leaderCard")).collect(Collectors.toList());
        System.out.println(leaderCardImages.size());
        for (int i = 0; i < leaderCardImages.size(); i++) {
            Image im = new Image("file:/" + imagePath + "leaders/" + gui.playerState.leaderCards.get(i) + ".png");
            System.out.println(im.getUrl());
            leaderCardImages.get(i).setImage(im);
        }


        // ************************************************************************************************** //
        // CREATING SELECTABLE BORDER REGION
        // ************************************************************************************************** //
        for (var image : selectableImages) {
            Region border = new Region();
            root.getChildren().add(border);
            double k = Math.min(image.getFitWidth() / image.getImage().getWidth(), image.getFitHeight() / image.getImage().getHeight());
            double out = 1.05;
            double width = image.getImage().getWidth() * k;
            double height = image.getImage().getHeight() * k;
            double x = image.getLayoutX() - width * (out - 1) / 2;
            double y = image.getLayoutY() - height * (out - 1) / 2;
            border.setLayoutX(x);
            border.setLayoutY(y);
            border.setPrefSize(width * out, height * out);
            border.getStyleClass().add("selectableRegion");
            selectedProductions.add(new Pair<>(image, border));
            image.addEventFilter(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
                border.setOpacity(1);
            });
        }

    }


    public void moveIntoToUse() {
        ObservableList<Label> resourcesToMove = warehouseList.getSelectionModel().getSelectedItems();
        for (var resource : resourcesToMove) {
            Label aux = new Label(resource.getText());
            aux.setId("warehouseListCell");
            resourcesToUse.getItems().add(aux);
        }
        warehouseList.getItems().removeAll(resourcesToMove);
        resourcesToMove = strongboxList.getSelectionModel().getSelectedItems();
        for (var resource : resourcesToMove) {
            Label aux = new Label(resource.getText());
            aux.setId("strongboxListCell");
            resourcesToUse.getItems().add(aux);
        }
        strongboxList.getItems().removeAll(resourcesToMove);
        warehouseList.getSelectionModel().clearSelection();
        strongboxList.getSelectionModel().clearSelection();

    }

    public void removeFromToUse() {
        var resourcesToRemove = resourcesToUse.getSelectionModel().getSelectedItems();
        for (var resource : resourcesToRemove) {
            if (resource.getId().contains("warehouseListCell"))
                warehouseList.getItems().add(new Label(resource.getText()));
            else
                strongboxList.getItems().add(new Label(resource.getText()));

        }
        resourcesToUse.getItems().removeAll(resourcesToRemove);
        resourcesToUse.getSelectionModel().clearSelection();
    }

    public void deselectLists() {
        warehouseList.getSelectionModel().clearSelection();
        strongboxList.getSelectionModel().clearSelection();
        resourcesToUse.getSelectionModel().clearSelection();
    }

    public void onCancel() {

    }

    public void onNext() {
        HashMap<Resource, Integer> allRes = new HashMap<>();
        HashMap<Resource, Integer> fromWareHouse = new HashMap<>();
        resourcesToUse.getItems().stream()
                .forEach(r -> allRes.put(Resource.valueOf(r.getText()), allRes.get(Resource.valueOf(r.getText())) + 1));
        resourcesToUse.getItems().stream().filter(n -> n.getId().contains("warehouseListCell"))
                .forEach(r -> fromWareHouse.put(Resource.valueOf(r.getText()), fromWareHouse.get(Resource.valueOf(r.getText())) + 1));

        HashMap<Resource, Integer> leaderPowers = new HashMap<>();
        ArrayList<Label> copy = resourcesOfChoiceList.getItems().stream()
                .map(n -> (Label) n.getChildren()
                        .stream().filter(b -> b instanceof Label)
                        .collect(Collectors.toList()).get(0))
                .collect(Collectors.toCollection(ArrayList::new));
        copy.remove(0);
        copy.stream().filter(n -> n.getId().contains("warehouseListCell"))
                .forEach(r -> leaderPowers.put(Resource.valueOf(r.getText()), leaderPowers.get(Resource.valueOf(r.getText())) + 1));

        gui.playerState.chosenResources = new ChosenResourcesEvent(gui.askUserID(), allRes, fromWareHouse, leaderPowers);
        // gui.playerState.events.add(new ActivateProductionEvent())

    }


    public void onAddResourcesClicked() {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initStyle(StageStyle.UTILITY);

        AnchorPane root = new AnchorPane();
        dialog.setTitle("Select Resources");
        Scene scene = new Scene(root);

        ListView<Label> list = new ListView<>();
        list.setMaxHeight(100);
        list.setMaxWidth(200);

        list.getItems().addAll(Arrays.stream(Resource.values()).map(n -> new Label(n.name())).collect(Collectors.toList()));
        Button confirm = new Button("Add");
        confirm.addEventFilter(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            dialog.close();
        });
        root.getChildren().addAll(list, confirm);
        AnchorPane.setTopAnchor(list, 0.0);
        AnchorPane.setLeftAnchor(list, 0.0);
        AnchorPane.setRightAnchor(list, 0.0);
        AnchorPane.setBottomAnchor(list, 70.0);
        AnchorPane.setRightAnchor(confirm, 20.0);
        AnchorPane.setBottomAnchor(confirm, 10.0);
        String path = new java.io.File(".").getAbsolutePath().replace("\\", "/");
        path = "file:/" + path.substring(0, path.length() - 2) + "/src/main/resources/it/polimi/ingsw/ui/gui/stylesheets/selectresource.css";
        root.getStylesheets().add(path);
        dialog.setScene(scene);
        dialog.showAndWait();
        if (list.getSelectionModel().getSelectedItem() != null) {
            HBox hBox = new HBox();
            hBox.setAlignment(Pos.CENTER);
            Label label = new Label(list.getSelectionModel().getSelectedItem().getText());
            label.setMaxHeight(10);
            Line buttonLine = new Line();
            buttonLine.setStrokeWidth(4);
            buttonLine.setEndX(hBox.getWidth() - 5);
            buttonLine.setStartX(hBox.getWidth() - 20);
            buttonLine.setStroke(Paint.valueOf("red"));
            buttonLine.addEventFilter(MouseEvent.MOUSE_CLICKED, mouseEvent -> {

                resourcesOfChoiceList.getItems().remove(hBox);
            });

            label.setMaxWidth(Double.MAX_VALUE);
            HBox.setHgrow(label, Priority.ALWAYS);
            hBox.getChildren().addAll(label, buttonLine);
            resourcesOfChoiceList.getItems().add(hBox);
            System.out.println(list.getSelectionModel().getSelectedItem().getText());
        }
    }


}
