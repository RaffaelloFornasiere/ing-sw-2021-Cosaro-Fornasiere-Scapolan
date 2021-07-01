package it.polimi.ingsw.ui.gui;

import it.polimi.ingsw.utilities.LockWrap;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.nio.Buffer;
import java.util.ResourceBundle;

public class SelectDevCardSlotController extends Controller implements Initializable {

    LockWrap<Integer> res = new LockWrap<>(null, null);


    SelectDevCardSlotController(GUI gui) {
        super(gui);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }


    @FXML
    AnchorPane root;

    public int getRes()
    {
        return res.getWaitIfLocked();
    }

    public void setRes(MouseEvent event){
        Button source = (Button) event.getSource();
        int res = Integer.parseInt(source.getText());
    }

    public void onNext()
    {
        ((Stage)root.getScene().getWindow()).close();
    }
}
