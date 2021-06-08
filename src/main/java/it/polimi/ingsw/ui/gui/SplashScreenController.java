package it.polimi.ingsw.ui.gui;


import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.ProgressIndicator;

import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SplashScreenController extends Controller implements Initializable {

    @FXML
    Group TitleGroup;
    @FXML
    ProgressIndicator indic;
    @FXML
    Group ButtonsGroup;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (!MainApplication.getFirstSplashScreen()) {
            MainApplication.setFirstSplashScreen();
            TranslateTransition transition = new TranslateTransition();
            transition.setNode(TitleGroup);
            transition.setDuration(Duration.millis(500));
            transition.setDelay(Duration.millis(2500));
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
        else{

        }
    }


    void InitialAnimation() {

    }


    public void switchToLogin() throws IOException {
        MainApplication.setScene("login");
    }

    public void switchToServerSettings() throws IOException {
        MainApplication.setScene("serversettings");
    }


}
