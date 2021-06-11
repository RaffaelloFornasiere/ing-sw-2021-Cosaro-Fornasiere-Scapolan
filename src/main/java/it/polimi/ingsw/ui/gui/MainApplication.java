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


    @Override
    public void init() throws Exception {
        super.init();
//        if (scenes == null || controllers == null)
//            throw new Exception("Main App not initialized");
    }

    static void setFirstScene(String fxml) {
        setFirstScene(fxml, null);
    }

    static void setFirstScene(String fxml, Controller controller) {
        if (scenes == null) {
            scenes = new Stack<>();
            controllers = new Stack<>();
            scenes.add(fxml);
            controllers.add(controller);
        }
    }


    private static Scene scene;
    static Stack<String> scenes;
    static Stack<Controller> controllers;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource(scenes.peek() + ".fxml"));
        loader.setController(controllers.peek());
        Parent parent = loader.load();
        scene = new Scene(parent);
        stage.setScene(scene);
        stage.show();
    }

    static void setScene(String fxml) throws IOException {
        setScene(fxml, null);
    }

    static void setScene(String fxml, Controller controller) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource(fxml + ".fxml"));

        if (controller != null)
            fxmlLoader.setController(controller);
        Parent parent = fxmlLoader.load();
        if (controller == null) {
            controller = fxmlLoader.getController();
            controller.setGUI(gui);
        }

        scenes.add(fxml);
        controllers.add(controller);
        scene.setRoot(parent);
    }

    static void setPreviousScene() throws IOException {
        scenes.pop();
        controllers.pop();
        scene.setRoot(loadFXML(scenes.peek(), controllers.peek()));
    }

    private static Parent loadFXML(String fxml, Controller controller) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource(fxml + ".fxml"));
        fxmlLoader.setController(controller);
        return fxmlLoader.load();
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource(fxml + ".fxml"));
        Parent parent = fxmlLoader.load();
        return parent;
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        System.out.println("stop");
    }


}
