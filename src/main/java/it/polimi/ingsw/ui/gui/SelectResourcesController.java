package it.polimi.ingsw.ui.gui;

import it.polimi.ingsw.events.controllerEvents.matchEvents.ChosenResourcesEvent;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.utilities.LockWrap;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class SelectResourcesController extends Controller implements Initializable {

    LockWrap<Boolean> dataReady = new LockWrap<>(null, null);

    @FXML
    ListView<Label> warehouseList;
    @FXML
    ListView<Label> strongboxList;
    @FXML
    ListView<Label> resourcesToUse;
    @FXML
    ListView<Label> leaderDeportsResourcesList;
    @FXML
    Label warningLabel;
    @FXML
    AnchorPane root;

    @FXML
    Button nextButton;


    ArrayList<Resource> resourcesRequired;
    int resourcesOfChoice;

    SelectResourcesController(GUI gui, HashMap<Resource, Integer> required, int resourcesOfChoice) {
        super(gui);
        resourcesRequired = new ArrayList<>() {{
            required.forEach((key, value) -> {
                for (int i = 0; i < value; i++)
                    add(key);
            });
        }};
        this.resourcesOfChoice = resourcesOfChoice;


    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> {
            root.getScene().getWindow().setOnCloseRequest(new EventHandler<WindowEvent>() {
                public void handle(WindowEvent we) {
                    onCancel();
                    //dataReady.setItem(false);
                }
            });
        });

        dataReady.setItem(null);

        ArrayList<Label> marbles = (ArrayList<Label>) gui.thisPlayerState().warehouse.stream().flatMap(n -> (new ArrayList<String>() {{
            for (int i = 0; i < n.getCurrentQuantity(); i++)
                add(n.getResourceType().name());
        }}).stream()).map(Label::new).collect(Collectors.toList());
        warehouseList.getItems().addAll(marbles);
        warehouseList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        marbles = (ArrayList<Label>) gui.thisPlayerState().strongBox.entrySet().stream().flatMap(n -> new ArrayList<String>() {{
            for (int i = 0; i < n.getValue(); i++)
                add(n.getKey().name());
        }}.stream()).map(Label::new).collect(Collectors.toList());
        strongboxList.getItems().addAll(marbles);
        strongboxList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        marbles = (ArrayList<Label>) gui.thisPlayerState().getLeaderDepots().entrySet().stream().flatMap(n -> new ArrayList<String>() {{
            for (int i = 0; i < n.getValue(); i++)
                add(n.getKey().name());
        }}.stream()).map(Label::new).collect(Collectors.toList());
        leaderDeportsResourcesList.getItems().addAll(marbles);
        leaderDeportsResourcesList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        resourcesToUse.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        checkResources();


        if (!PlayerState.canPerformActions)
            nextButton.setDisable(true);
    }

    public ChosenResourcesEvent getResult() {

        if (!dataReady.getWaitIfLocked())
            return null;

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
        return new ChosenResourcesEvent(gui.askUserID(), allResources, fromWareHouse, fromLeadersDepots);
    }
    /**
     * method invoked when user clicks on cancel
     */
    public void onCancel() {
        ((Stage) root.getScene().getWindow()).close();
    }
    /**
     * method invoked when user confirms action
     */
    public void onNext() {
        if (warningLabel.getOpacity() > 0) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "you don't have enough resources to perform action", ButtonType.OK);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.showAndWait();
            return;
        }
        dataReady.setItem(true);
        ((Stage) root.getScene().getWindow()).close();
    }

    /**
     * method invoked when user select some resources and moves then in the ones to use
     */
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

    /**
     * does the opposit of moveIntoToUse
     */
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

    /**
     * deselect all lists in the view
     */

    public void deselectLists() {
        warehouseList.getSelectionModel().clearSelection();
        strongboxList.getSelectionModel().clearSelection();
        resourcesToUse.getSelectionModel().clearSelection();
        leaderDeportsResourcesList.getSelectionModel().clearSelection();
    }

    /**
     * check if the resources required match the ones the user selected
     */
    public void checkResources() {
        ArrayList<Resource> resourcesProvided = resourcesToUse.getItems().stream().map(n -> Resource.valueOf(n.getText())).collect(Collectors.toCollection(ArrayList::new));

        int opacity = 1;
        opacity *= (resourcesProvided.size() >= resourcesRequired.size() + resourcesOfChoice) ? 1 : 0;
        ArrayList<Resource> aux = new ArrayList<>(resourcesProvided);
        opacity *= resourcesRequired.stream().mapToInt(r -> aux.remove(r) ? 1 : 0).reduce(1, (a, b) -> a * b);
        warningLabel.setOpacity(opacity == 1 ? 0 : 1);
    }
}
