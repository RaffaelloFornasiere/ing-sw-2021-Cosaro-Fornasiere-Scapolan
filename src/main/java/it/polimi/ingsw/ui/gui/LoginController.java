package it.polimi.ingsw.ui.gui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController extends Controller implements Initializable {
    @FXML
    CheckBox createMatchCheckBox;
    @FXML
    Group joinMatchGroup;
    @FXML
    TextField playerIdTextField;
    @FXML
    Label noPlayerIDLabel;
    @FXML
    TextField leaderIdTextField;

    public void onCheckBoxStatusChanged()
    {
        if(createMatchCheckBox.isSelected())
            joinMatchGroup.setOpacity(1);
        else
            joinMatchGroup.setOpacity(0);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }


    public void onCancel() throws IOException {
        MainApplication.setPreviousScene();
    }

    public void onNext() throws IOException{
        if(playerIdTextField.getText().equals(""))
            noPlayerIDLabel.setOpacity(1);
        else
        {
            noPlayerIDLabel.setOpacity(0);
            gui.setLoginData(playerIdTextField.getText(), leaderIdTextField.getText());
        }

    }


}
