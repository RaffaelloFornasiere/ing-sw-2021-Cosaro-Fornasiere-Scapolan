package it.polimi.ingsw.ui.gui;


import com.sun.tools.javac.Main;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.util.ResourceBundle;

public class SplashScreenController extends Controller implements Initializable {

    @FXML
    AnchorPane root;

    @FXML
    Group TitleGroup;
    @FXML
    ProgressIndicator indic;
    @FXML
    Group ButtonsGroup;

    static boolean firstTime = true;

    boolean defaultAddress = false;

    public SplashScreenController(GUI gui) {
        super(gui);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (firstTime) {
            firstTime = false;
            TranslateTransition transition = new TranslateTransition();
            transition.setNode(TitleGroup);
            transition.setDuration(Duration.millis(500));
            transition.setDelay(Duration.millis(2000));
            transition.setFromY(100);
            transition.setToY(10);
            transition.setAutoReverse(false);
            //transition.play();

            FadeTransition fade = new FadeTransition(Duration.millis(500), ButtonsGroup);
            fade.setFromValue(0);
            fade.setToValue(1);
            fade.setDelay(Duration.millis(2800));

            ParallelTransition transitions = new ParallelTransition(transition, fade);
            transitions.setOnFinished((e) -> {
            });
            transitions.play();

        }
    }


    public void switchToLogin() throws IOException {
        gui.setServerAddress(InetAddress.getByName("127.0.0.1"));
        defaultAddress = true;
        Controller controller = new LoginController(gui);
        controller.setPreviousScene(root.getScene());
        ((Stage)root.getScene().getWindow()).setScene(MainApplication.createScene("Login.fxml", controller));
    }

    public void switchToServerSettings() throws IOException {
        Controller controller = new ServerSettingsController(gui);
        controller.setPreviousScene(root.getScene());
        ((Stage)root.getScene().getWindow()).setScene(MainApplication.createScene("ServerSettings.fxml", controller));
    }




}
