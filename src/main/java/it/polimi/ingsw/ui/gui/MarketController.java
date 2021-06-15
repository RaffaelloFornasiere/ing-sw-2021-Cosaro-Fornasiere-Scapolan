package it.polimi.ingsw.ui.gui;

import it.polimi.ingsw.model.Marble;
import it.polimi.ingsw.utilities.LockWrap;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

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


    MarketController(GUI gui, boolean confirmable, Marble[][] marketStatus, Marble marbleLeft, LockWrap<?> lock){

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void onButtonPressed(ActionEvent event)
    {

    }


}
