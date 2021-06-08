package it.polimi.ingsw.ui.gui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.CheckBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController extends Controller implements Initializable {
    @FXML
    CheckBox createMatchCheckBox;

    @FXML
    Group joinMatchGroup;



    public void onCheckBoxStatusChanged()
    {
        if(createMatchCheckBox.isSelected())
            joinMatchGroup.setOpacity(0);
        else
            joinMatchGroup.setOpacity(1);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }


    public void onCancel() throws IOException {
        MainApplication.setPreviousScene();
    }

    public void onNext() throws IOException{
        //MainApplication.setRoot("login");
    }


}
