package it.polimi.ingsw.ui.gui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;

public class ServerSettingsController extends Controller implements Initializable {

    @FXML
    TextField portText;
    @FXML
    Label portErrorLabel;
    @FXML
    TextField hostnameTextField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        portText.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    portText.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });


    }

    public void onServerHostnameChanged() {
        String hostName = hostnameTextField.getText();
        try {
            gui.setServerAddress(InetAddress.getByName(hostName));
        } catch (UnknownHostException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Hostname is unreachable ", ButtonType.OK);
            alert.showAndWait();
        }
    }


    public void onPortChanged() {
        int port = Integer.parseInt(portText.getText());
        System.out.println("port typed: " + Integer.toString(port));
        if (port < 1024 || port > 49151) {
            portErrorLabel.setOpacity(1);
            System.out.println("error");
            return;
        } else
            portErrorLabel.setOpacity(0);
        gui.setServerPort(port);
    }

    public void onCancel() throws IOException {
        MainApplication.setPreviousScene();
    }

    public void onNext() throws IOException {
        MainApplication.setScene("login");
    }

    public void onServerChanged(ActionEvent event) {
        String ip = ((TextField)event.getSource()).getText();
        System.out.println(ip);
        try {
            gui.setServerAddress(InetAddress.getByName(ip));
        } catch (UnknownHostException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Hostname is unreachable ", ButtonType.OK);
            alert.showAndWait();
        }
    }

}
