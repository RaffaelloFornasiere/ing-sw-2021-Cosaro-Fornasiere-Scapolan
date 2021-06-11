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


    private InetAddress serverAddress;
    private Integer port = 50885;

    @FXML
    TextField portText;
    @FXML
    Label portErrorLabel;
    @FXML
    Label invalidAddress;

    @FXML
    TextField hostnameTextField;



    public ServerSettingsController(GUI gui){super(gui);serverAddress =null;}

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("init");
        portText.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    portText.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        portText.setText("50885");
        if(serverAddress != null) {
            hostnameTextField.setText(serverAddress.getHostAddress());
        }
        else {
            hostnameTextField.setPromptText("www.mastersofrenaissance.ns0.it");
        }


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
        if (port < 1024 ) {
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
        if(hostnameTextField.getText() != "")
            onServerChanged(hostnameTextField.getText());
        if(serverAddress == null)
        {
            invalidAddress.setOpacity(1);
            return;
        }
        MainApplication.setScene("login", gui.loginController);
    }

    public void onServerChanged(ActionEvent event) {
        onServerChanged(((TextField)event.getSource()).getText());
    }

    public void onServerChanged(String ip) {
        System.out.println(ip);
        try {
            serverAddress = InetAddress.getByName(ip);
        } catch (UnknownHostException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Hostname is unreachable ", ButtonType.OK);
            alert.showAndWait();
        }
    }

}
