package it.polimi.ingsw.ui.gui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import javax.swing.event.AncestorEvent;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;

public class ServerSettingsController extends Controller implements Initializable {


    private InetAddress serverAddress;


    @FXML
    Label invalidAddress;

    @FXML
    AnchorPane root;

    @FXML
    TextField hostnameTextField;


    public ServerSettingsController(GUI gui) {
        super(gui);
        serverAddress = null;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        if (serverAddress != null) {
            hostnameTextField.setText(serverAddress.getHostAddress());
        } else {
            hostnameTextField.setPromptText("127.0.0.1");
        }


    }
    /**
     * method invoked when user changes hostname
     */
    public void onServerHostnameChanged() {
        String hostName = hostnameTextField.getText();
        try {
            gui.setServerAddress(InetAddress.getByName(hostName));
        } catch (UnknownHostException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Hostname is unreachable ", ButtonType.OK);
            alert.showAndWait();
        }
    }


    /**
     * method invoked when user clicks on cancel button
     */
    public void onCancel() throws IOException {
        MainApplication.setScene(previousScene);
    }
    /**
     * method invoked when user confirms action
     */
    public void onNext() throws IOException {
        if (hostnameTextField.getText() != "") {
            onServerChanged(hostnameTextField.getText());
            Controller controller = new LoginController(gui);
            controller.setPreviousScene(root.getScene());
            ((Stage) root.getScene().getWindow()).setScene(MainApplication.createScene("Login.fxml", controller));
        }
        if (serverAddress == null) {
            invalidAddress.setOpacity(1);
            return;
        }
    }

    /**
     * method invoked when user changes hostname
     * @param event event triggered form fxml
     */
    public void onServerChanged(ActionEvent event) {
        onServerChanged(((TextField) event.getSource()).getText());
    }

    /**
     * method invoked when user changes hostname
     * @param ip new ip address
     */
    public void onServerChanged(String ip) {
        //System.out.println(ip);
        try {
            serverAddress = InetAddress.getByName(ip);
            gui.setServerAddress(serverAddress);
        } catch (UnknownHostException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Hostname is unreachable ", ButtonType.OK);
            alert.showAndWait();
        }
    }

}
