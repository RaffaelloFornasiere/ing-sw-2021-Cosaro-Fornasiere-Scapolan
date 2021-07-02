package it.polimi.ingsw.ui.gui;

import it.polimi.ingsw.utilities.LockWrap;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class SelectDevCardSlotController extends Controller implements Initializable {

    LockWrap<Integer> finalRes = new LockWrap<>(null, null);
    int res;
    @FXML
    AnchorPane root;


    SelectDevCardSlotController(GUI gui) {
        super(gui);


    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> root.getScene().getWindow().setOnCloseRequest(we -> {
            Alert alert = new Alert(Alert.AlertType.ERROR, "You must select a slot", ButtonType.OK);
            alert.showAndWait();
            we.consume();
        }));
    }

    /**
     * returns the selected slot
     * @return
     */
    public int getRes() {
        return finalRes.getWaitIfLocked();
    }

    /**
     * method invoked then user clicks on a slot button
     * @param event
     */
    public void setRes(MouseEvent event) {
        Button button = (Button) event.getSource();
        //////System.out.printlnln(button.getText());
        if(res == Integer.parseInt(button.getText())) {
            return;
        } else if(res == 0) {
            select(button, true);
        }else
        {
            select((Button)SelectableImage.getChildrenOf(root).stream().filter(n -> n instanceof Button)
                    .filter(b -> ((Button) b).getText().equals(String.valueOf(res))).collect(Collectors.toList())
                    .get(0), false);
            select(button, true);
        }
        res = Integer.parseInt(button.getText());
    }

    /**
     * method to change css of button
     * @param button
     * @param select
     */
    public void select(Button button, boolean select)
    {
        if (select)
            button.setStyle("-fx-background-color: #ba8a54;");
        else
            button.setStyle("-fx-background-color: transparent;");
    }

    /**
     * method invoked when user confirms action
     */
    public void onNext() {
        finalRes.setItem(res-1);
        ((Stage) root.getScene().getWindow()).close();
    }
}
