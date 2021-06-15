package it.polimi.ingsw.ui.gui;

import it.polimi.ingsw.events.ControllerEvents.MatchEvents.BuyResourcesEvent;
import it.polimi.ingsw.model.Marble;
import it.polimi.ingsw.utilities.LockWrap;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Circle;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class MarketController extends Controller implements Initializable {
    @FXML
    Button row1;
    @FXML
    Button row2;
    @FXML
    Button row3;
    @FXML
    Button col1;
    @FXML
    Button col2;
    @FXML
    Button col3;
    @FXML
    Button col4;
    @FXML
    GridPane gridPane;


    private BuyResourcesEvent data;
    GUI.Action action;


    MarketController(GUI gui, Marble[][] marketStatus, Marble marbleLeft, LockWrap<GUI.Action> action) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void onButtonPressed(ActionEvent event) {
        String id = ((Button) event.getSource()).getId();
        String index = Integer.toString(Integer.parseInt(String.valueOf(id.charAt(1))) - 1);
        String regex = "c"
                + (id.contains("c") ? index + "\\d": "\\d" + index);
        //System.out.println(regex);
        gridPane.getChildren().stream()
                .filter(n -> n instanceof Circle)
                .forEach(c -> c.setOpacity(0));
        gridPane.getChildren().stream()
                .filter(n -> n instanceof Circle)
                .filter(n -> n.getId().matches(regex))
                .forEach(c -> c.setOpacity(1));

    }

    public void onCancel()
    {

    }

    public void onNext(){

    }


}
