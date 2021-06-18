package it.polimi.ingsw.ui.gui;

import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class SelectLeaderCardsController extends Controller implements Initializable {
    ArrayList<String> cards;
    ArrayList<String> selected;
    int selectable;
    Stage stage;

    SelectLeaderCardsController(ArrayList<String> cards, int selectable, ArrayList<String> selected, Stage stage) {
        this.cards = cards;
        this.selectable = selectable;
        this.selected = selected;
        this.stage = stage;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public void onMouseClicked(MouseEvent mouseEvent) {
        Region selectableRegion = (Region) mouseEvent.getSource();
        //CheckBox checkBox = (CheckBox)getSiblings(selectableRegion.getParent()
        CheckBox checkBox = (CheckBox) selectableRegion.getParent()
                .getChildrenUnmodifiable().stream()
                .filter(node -> node instanceof CheckBox)
                .collect(Collectors.toList())
                .get(0);
        String card = (String) ((ImageView) selectableRegion.getParent()
                .getChildrenUnmodifiable().stream()
                .filter(node -> node instanceof ImageView)
                .collect(Collectors.toList())
                .get(0)).getImage().getUrl();
        card = card.substring(card.lastIndexOf('/') + 1);

        if (!checkBox.isSelected() && selected.size() < selectable) {
            checkBox.setSelected(true);
            selectableRegion.setStyle("-fx-opacity: 1;");
            selected.add(card);
            System.out.println(card);
        } else if (checkBox.isSelected()) {
            checkBox.setSelected(false);
            selectableRegion.setStyle("-fx-opacity: 0;");
            selected.remove(card);
            System.out.println(card);
        }
    }

    public void onNext() {
        if (selected.size() != selectable) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "You must select " + selectable + " " + ((selectable > 1) ? "leader cards" : "leader card"), ButtonType.OK);
            alert.showAndWait();
            return;
        }
        stage.close();

    }

    public void onCancel() {
        Alert alert = new Alert(Alert.AlertType.ERROR, "You must select " + selectable +" " +  ((selectable > 1) ? "leader cards" : "leader card"), ButtonType.OK);
        alert.showAndWait();
    }

    public void onCardSelected(MouseEvent mouseEvent){
        //((Group)mouseEvent.getSource()).getChildren().stream().filter(n -> n -> n instanceof Region).
    }

}