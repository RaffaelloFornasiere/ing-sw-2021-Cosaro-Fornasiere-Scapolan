package it.polimi.ingsw.ui.gui;

import it.polimi.ingsw.utilities.LockWrap;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.checkerframework.checker.units.qual.A;

import javax.naming.spi.StateFactory;
import java.io.IOException;
import java.util.Stack;


public class MainApplication extends Application {

    public static void setGui(GUI gui) {
//        if (MainApplication.gui == null)
//            MainApplication.gui = gui;
    }

    static Stage stage;

    static private GUI gui;


    @Override
    public void init() throws Exception {
        super.init();
    }

    static void setFirstScene(String fxml) {
//        setFirstScene(fxml, null);
    }

    static void setFirstScene(String fxml, Controller controller) {
//        if (scenes == null) {
//            scenes = new Stack<>();
//            controllers = new Stack<>();
//            scenes.add(fxml);
//            controllers.add(controller);
//        }
    }


    private static Scene scene;
    static Stack<String> scenes;
    static Stack<Controller> controllers;

    public static Stage getStage()
    {
        return stage;
    }


    private static final LockWrap<Boolean> isReady = new LockWrap<>(false, false);
    public static boolean isReady()
    {
        return isReady.getWaitIfLocked();
    }

    @Override
    public void start(Stage stage) throws IOException {
        MainApplication.stage = stage;
        isReady.setItem(true);
    }

    static void setScene(Scene scene) throws IOException {
        MainApplication.scene = scene;
        stage.setScene(scene);
    }

    static void setScene(String fxml, Controller controller) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource(fxml ));
        fxmlLoader.setController(controller);
        controller.setPreviousScene(scene);
        scene = new Scene((fxmlLoader.load()));
        stage.setScene(scene);
        stage.sizeToScene();
    }

    static Scene createScene(String fxml, Controller controller) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource(fxml));
        fxmlLoader.setController(controller);
        return new Scene((fxmlLoader.load()));
    }


    private static Parent loadFXML(String fxml, Controller controller) throws IOException {
//        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource(fxml + ".fxml"));
//        fxmlLoader.setController(controller);
//        return fxmlLoader.load();
        return null;
    }

    private static Parent loadFXML(String fxml) throws IOException {
//        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource(fxml + ".fxml"));
//        return fxmlLoader.load();
        return null;
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        System.out.println("stop");
        gui.close();
    }

}
