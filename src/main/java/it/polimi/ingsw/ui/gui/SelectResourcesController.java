package it.polimi.ingsw.ui.gui;

import it.polimi.ingsw.events.ControllerEvents.MatchEvents.ChosenResourcesEvent;
import it.polimi.ingsw.model.Resource;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.checkerframework.checker.units.qual.A;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class SelectResourcesController extends Controller implements Initializable {


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


    ArrayList<Resource> resourcesRequired;
    int resourcesOfChoice;
    SelectResourcesController(GUI gui, HashMap<Resource, Integer> required, int resourcesOfChoice) {
        super();
        resourcesRequired = new ArrayList<>(){{
            required.forEach((key, value) -> {
                for (int i = 0; i < value; i++)
                    add(key);
            });
        }};
        this.resourcesOfChoice = resourcesOfChoice;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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
    }

    public ChosenResourcesEvent getResult() {
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

    public void onCancel() {
        ((Stage) root.getScene().getWindow()).close();
    }

    public void onNext() {
        if (warningLabel.getOpacity() > 0) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "you don't have enough resources to perform action", ButtonType.OK);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.showAndWait();
            return;
        }
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


    public void checkResources() {
        ArrayList<Resource> resourcesProvided = resourcesToUse.getItems().stream().map(n -> Resource.valueOf(n.getText())).collect(Collectors.toCollection(ArrayList::new));

        int opacity = 1;
        opacity *= (resourcesProvided.size() >= resourcesRequired.size() + resourcesOfChoice) ? 1 : 0;
        ArrayList<Resource> aux = new ArrayList<>(resourcesProvided);
        opacity *= resourcesRequired.stream().mapToInt(r -> aux.remove(r) ? 1 : 0).reduce(1, (a, b) -> a * b);
        warningLabel.setOpacity(opacity == 1 ? 0 : 1);


    }
}
