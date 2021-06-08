package it.polimi.ingsw.ui.gui;

import com.sun.tools.javac.Main;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Control;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLOutput;
import java.util.*;

public class MainApplication extends Application {

    public static void setGui(GUI gui) {
        if (MainApplication.gui == null)
            MainApplication.gui = gui;
    }

    static private GUI gui;

    public static void main(String[] args) throws IOException {
        gui = new GUI();
        Application.launch(MainApplication.class, args);
    }


    @Override
    public void init() throws Exception {
        super.init();
        scenes = new Stack<>();
        System.out.println("init");
    }


    private static Scene scene;
    static Stack<String> scenes;
    static Stack<Controller> controllers;
    static boolean firstSplashScreen = false;

    @Override
    public void start(Stage stage) throws IOException {
        //System.out.println("start");
        //scenes.add("splashscreen");
        scene = new Scene(loadFXML(scenes.peek()));
        stage.setScene(scene);
        stage.show();

    }

    static void setScene(String fxml) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource(fxml + ".fxml"));
        Parent parent = fxmlLoader.load();
        Controller controller = fxmlLoader.getController();
        controller.setGUI(gui);

        scenes.add(fxml);
        controllers.add(controller);
        scene.setRoot(parent);

        setScene(fxml);
    }

    static FXMLLoader getFxmlLoader(String fxml){
        return new FXMLLoader(MainApplication.class.getResource(fxml + ".fxml"));
    }

    static void setScene(String fxml, Controller controller) {

    }

    static void setPreviousScene() throws IOException {
        scenes.pop();
        scene.setRoot(loadFXML(scenes.peek()));
    }

    private static Parent loadFXML(String fxml, Controller controller) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource(fxml + ".fxml"));
        fxmlLoader.setController(controller);
        return fxmlLoader.load();
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource(fxml + ".fxml"));
        Parent parent = fxmlLoader.load();
        Controller controller = fxmlLoader.getController();
        controller.setGUI(gui);
        return parent;
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        System.out.println("stop");
    }

    static public boolean getFirstSplashScreen() {
        return firstSplashScreen;
    }

    static public void setFirstSplashScreen() {
        firstSplashScreen = true;
    }


}
