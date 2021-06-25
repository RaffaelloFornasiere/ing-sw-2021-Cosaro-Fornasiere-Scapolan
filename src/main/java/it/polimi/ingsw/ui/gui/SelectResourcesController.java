package it.polimi.ingsw.ui.gui;

import it.polimi.ingsw.events.ControllerEvents.MatchEvents.BuyDevCardsEvent;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

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
    AnchorPane root;


    SelectResourcesController(GUI gui)
    {
        super();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }




    public void onCancel() {
        ((Stage)root.getScene().getWindow()).close();
    }

    public void onNext() {
//
//        gui.playerState.events.add(new BuyDevCardsEvent(gui.askUserID(), selected, devCardSlot));
//        ((Stage)root.getScene().getWindow()).close();

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
        /*ArrayList<Resource> resourcesProvided = resourcesToUse.getItems().stream().map(n -> Resource.valueOf(n.getText())).collect(Collectors.toCollection(ArrayList::new));
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
        warningLabelOfChoice.setOpacity(opacity);*/
    }
}
