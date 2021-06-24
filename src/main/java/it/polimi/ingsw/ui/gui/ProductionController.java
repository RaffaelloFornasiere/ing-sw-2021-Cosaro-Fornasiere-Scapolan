package it.polimi.ingsw.ui.gui;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.events.ClientEvents.DepotState;
import it.polimi.ingsw.events.ControllerEvents.MatchEvents.ActivateProductionEvent;
import it.polimi.ingsw.events.ControllerEvents.MatchEvents.ChosenResourcesEvent;
import it.polimi.ingsw.model.DevCards.DevCard;
import it.polimi.ingsw.model.LeaderCards.LeaderCard;
import it.polimi.ingsw.model.LeaderCards.LeaderPower;
import it.polimi.ingsw.model.LeaderCards.ProductionLeaderPower;
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
import javafx.scene.control.*;
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
import java.util.*;
import java.util.stream.Collectors;

public class ProductionController extends Controller implements Initializable {

    @FXML
    ListView<Label> warehouseList;
    @FXML
    ListView<Label> strongboxList;
    @FXML
    ListView<Label> resourcesToUse;
    @FXML
    ListView<Label> leaderDeportsResourcesList;
    @FXML
    AnchorPane root;
    @FXML
    ListView<HBox> resourcesOfChoiceList;
    @FXML
    Label warningLabel;
    @FXML
    Label warningLabelOfChoice;

    ArrayList<Pair<ImageView, Region>> selectedProductions;

    private final HashMap<Resource, Integer> strongBox;
    private final ArrayList<DepotState> warehouse;
    private final HashMap<Resource, Integer> leaderDepots;
    private final ArrayList<DevCard> devCards;
    private final ArrayList<String> selectedDevCards;
    private final ArrayList<LeaderCard> leaderCards;
    private final ArrayList<String> selectedLeaderCards;
    boolean personalPower;

    /**
     * Constructor
     * initializes local variables
     *
     * @param gui
     */
    ProductionController(GUI gui) {
        super(gui);
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Requirement.class, new GsonInheritanceAdapter<Requirement>());
        builder.registerTypeAdapter(LeaderPower.class, new GsonInheritanceAdapter<LeaderPower>());
        builder.registerTypeAdapter(Pair.class, new GsonPairAdapter());
        Gson gson = builder.create();
        devCards = new ArrayList<>();
        leaderCards = new ArrayList<>();
        selectedDevCards = new ArrayList<>();
        selectedLeaderCards = new ArrayList<>();
        try {
            for (var deck : gui.playerState.ownedCards) {
                if (deck.isEmpty())
                    continue;
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
        this.leaderDepots = gui.playerState.leaderDepots;
        selectedProductions = new ArrayList<>();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // ************************************************************************************************** //
        // INITIALIZATION RESOURCES LISTS
        // ************************************************************************************************** //
        ArrayList<Label> marbles = (ArrayList<Label>) warehouse.stream().flatMap(n -> (new ArrayList<String>() {{
            for (int i = 0; i < n.getCurrentQuantity(); i++)
                add(n.getResourceType().name());
        }}).stream()).map(Label::new).collect(Collectors.toList());
        warehouseList.getItems().addAll(marbles);
        warehouseList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        marbles = (ArrayList<Label>) strongBox.entrySet().stream().flatMap(n -> new ArrayList<String>() {{
            for (int i = 0; i < n.getValue(); i++)
                add(n.getKey().name());
        }}.stream()).map(Label::new).collect(Collectors.toList());
        strongboxList.getItems().addAll(marbles);
        strongboxList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        marbles = (ArrayList<Label>) leaderDepots.entrySet().stream().flatMap(n -> new ArrayList<String>() {{
            for (int i = 0; i < n.getValue(); i++)
                add(n.getKey().name());
        }}.stream()).map(Label::new).collect(Collectors.toList());
        leaderDeportsResourcesList.getItems().addAll(marbles);
        leaderDeportsResourcesList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
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
        for (int i = 0; i < devCardsImages.size(); i++) {
            if (i >= devCards.size()) {
                selectableImages.remove(devCardsImages.get(i));
                devCardsImages.get(i).setOpacity(0);
                continue;
            }
            devCardsImages.get(i).setOpacity(1);
            devCardsImages.get(i).setImage(new Image("file:/" + imagePath + "front/" +
                    devCards.get(i).getCardID() + ".png"));
        }
        System.out.println(devCardsImages.size());
        var leaderCardImages = selectableImages.stream().filter(n -> n.getStyleClass().contains("leaderCard")).collect(Collectors.toList());
        System.out.println(leaderCardImages.size());


        for (int i = 0; i < leaderCardImages.size(); i++) {
            Image im = null;
            if (i >= leaderCards.size()) {
                selectableImages.remove(leaderCardImages.get(i));
                leaderCardImages.get(i).getStyleClass().remove("selectable");
                leaderCardImages.get(i).setOpacity(0);
                continue;
            }
            int finalI = i;
            leaderCardImages.get(i).setOpacity(1);
            LeaderCard leaderCard = leaderCards.stream().filter(card -> card.getCardID().equals(leaderCards.get(finalI).getCardID())).findFirst().orElse(null);
            assert leaderCard != null;
            if (leaderCard.getLeaderPowers().stream().noneMatch(n -> n instanceof ProductionLeaderPower)) {
                selectableImages.remove(leaderCardImages.get(i));
            }
            leaderCardImages.get(i).setImage(new Image("file:/" + imagePath + "leaders/" + leaderCards.get(i).getCardID() + ".png"));

        }

        SelectableImage.setSelectable(root);

    }

    public void onProductionClicked(MouseEvent mouseEvent) {
        ImageView image = (ImageView) mouseEvent.getSource();
        String name = image.getImage().getUrl();

        name = name.substring(name.lastIndexOf("/") + 1, name.lastIndexOf("."));
        System.out.println(name);
        ArrayList<String> aux = null;
        if (name.contains("DevCard"))
            aux = selectedDevCards;
        else if (name.contains("LeaderCard"))
            aux = selectedLeaderCards;
        if (aux != null) {
            boolean present = aux.contains(name);
            if (!present)
                aux.add(name);
            else
                aux.remove(name);
        } else
            personalPower = true;


        //check the validity of the output
        checkResources();
    }


    public void leaderClick(MouseEvent event) {

    }


    public void checkResources() {
        ArrayList<Resource> resourcesProvided = resourcesToUse.getItems().stream().map(n -> Resource.valueOf(n.getText())).collect(Collectors.toCollection(ArrayList::new));
        ArrayList<Resource> resourcesRequired = new ArrayList<>();
        for (var card : selectedDevCards) {
            ArrayList<Resource> required = devCards.stream()
                    .filter(c -> c.getCardID().equals(card))
                    .map(c -> c.getProductionPower().getConsumedResources()).flatMap(p ->
                            p.entrySet().stream().flatMap(entry -> new ArrayList<Resource>() {{
                                for (int i = 0; i < entry.getValue(); i++)
                                    add(entry.getKey());
                            }}.stream())).collect(Collectors.toCollection(ArrayList::new));
            resourcesRequired.addAll(required);
        }
        for (var card : selectedLeaderCards) {
            ArrayList<Resource> required = leaderCards.stream()
                    .filter(c -> c.getCardID().equals(card))
                    .flatMap(c -> c.getLeaderPowers().stream())
                    .map(power -> ((ProductionLeaderPower) power).getEffectPower().getConsumedResources())
                    .flatMap(p -> p.entrySet().stream().flatMap(entry -> new ArrayList<Resource>() {{
                        for (int i = 0; i < entry.getValue(); i++)
                            add(entry.getKey());
                    }}.stream())).collect(Collectors.toCollection(ArrayList::new));
            resourcesRequired.addAll(required);
        }
        int opacity = 1;
        opacity *= (resourcesProvided.size() >= resourcesRequired.size() + 2 * (personalPower ? 1 : 0)) ? 1 : 0;
        ArrayList<Resource> aux = new ArrayList<>(resourcesProvided);
        opacity *= resourcesRequired.stream().mapToInt(r -> aux.remove(r) ? 1 : 0).reduce(1, (a, b) -> a * b);
        warningLabel.setOpacity(opacity == 1 ? 0 : 1);

        opacity = (((personalPower ? 1 : 0) + selectedLeaderCards.size()) == resourcesOfChoiceList.getItems().size()) ? 0 : 1;
        warningLabelOfChoice.setOpacity(opacity);
    }

    @FXML
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

        resourcesToMove = leaderDeportsResourcesList.getSelectionModel().getSelectedItems();
        resourcesToMove.forEach(resource -> {
            Label aux = new Label(resource.getText());
            aux.setId("leaderCardListCell");
            resourcesToUse.getItems().add(aux);
        });
        leaderDeportsResourcesList.getItems().removeAll(resourcesToMove);

        deselectLists();
        checkResources();
    }

    @FXML
    public void removeFromToUse() {
        var resourcesToRemove = resourcesToUse.getSelectionModel().getSelectedItems();
        for (var resource : resourcesToRemove) {
            if (resource.getId().contains("warehouseListCell"))
                warehouseList.getItems().add(new Label(resource.getText()));
            else if (resource.getId().contains("fromLeadersDepots"))
                leaderDeportsResourcesList.getItems().add(new Label(resource.getText()));
            else
                strongboxList.getItems().add(new Label(resource.getText()));

        }
        resourcesToUse.getItems().removeAll(resourcesToRemove);
        deselectLists();
        checkResources();
    }


    public void deselectLists() {
        warehouseList.getSelectionModel().clearSelection();
        strongboxList.getSelectionModel().clearSelection();
        resourcesToUse.getSelectionModel().clearSelection();
        leaderDeportsResourcesList.getSelectionModel().clearSelection();
    }

    @FXML
    public void onCancel() {
        Stage stage = (Stage) root.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void onNext() {
        if (warningLabel.getOpacity() > 0) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "you don't have enough resources to perform action", ButtonType.OK);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.showAndWait();
            return;
        }
        if (warningLabelOfChoice.getOpacity() > 0) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "you must select the resources of choice", ButtonType.OK);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.showAndWait();
            return;
        }
        HashMap<Resource, Integer> allResources = new HashMap<>();
        HashMap<Resource, Integer> fromWareHouse = new HashMap<>();
        HashMap<Resource, Integer> fromLeadersDepots = new HashMap<>();
        resourcesToUse.getItems()
                .forEach(r -> allResources.put(Resource.valueOf(r.getText()),
                        Objects.requireNonNullElse(allResources.get(Resource.valueOf(r.getText())), 0) + 1));
        resourcesToUse.getItems().stream().filter(n -> n.getId().contains("warehouseListCell"))
                .forEach(r -> fromWareHouse.put(Resource.valueOf(r.getText()), fromWareHouse.getOrDefault(Resource.valueOf(r.getText()), 0) + 1));
        resourcesToUse.getItems().stream().filter(n -> n.getId().contains("leaderCardListCell"))
                .forEach(r -> fromLeadersDepots.put(Resource.valueOf(r.getText()), fromLeadersDepots.getOrDefault(Resource.valueOf(r.getText()), 0) + 1));


        gui.playerState.chosenResources = new ChosenResourcesEvent(gui.askUserID(), allResources, fromWareHouse, fromLeadersDepots);
        gui.addEvent(new ActivateProductionEvent(gui.askUserID(), new ArrayList<>(
                selectedProductions.stream()
                        .map(Pair::getKey)
                        .filter(r -> r.getImage().getUrl().contains("DevCard"))
                        .map(i -> {
                            String url = i.getImage().getUrl();
                            System.out.println(url);
                            url = url.substring(url.lastIndexOf("/", url.lastIndexOf(".")));
                            System.out.println(url);
                            return url;
                        }).collect(Collectors.toList())), personalPower));
        ((Stage) root.getScene().getWindow()).close();
    }

    @FXML
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
        path = "file:/" + path.substring(0, path.length() - 2) + "/src/main/resources/it/polimi/ingsw/ui/gui/stylesheets/selectresourcedialog.css";
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
        checkResources();
    }


}
