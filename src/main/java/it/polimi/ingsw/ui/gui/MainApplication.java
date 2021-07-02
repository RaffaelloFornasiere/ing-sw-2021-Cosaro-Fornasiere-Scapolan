package it.polimi.ingsw.ui.gui;

import it.polimi.ingsw.utilities.LockWrap;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;



/**
 * Main class of JavaFX
 * used as utility to create scenes from fxml files and controllers
 */
public class MainApplication extends Application {


    private static final LockWrap<Boolean> isReady = new LockWrap<>(false, false);

    static Stage stage;
    static Scene scene;
    static private GUI gui;


    /**
     * sets the refernce to the gui object, the
     * main controller of the gui
     * @param gui gui object
     */
    public static void setGui(GUI gui) {
        if (MainApplication.gui == null)
            MainApplication.gui = gui;
    }

    @Override
    public void init() throws Exception {
        super.init();
    }

    /**
     *
     * @return the primary stage reference
     */
    public static Stage getStage()
    {
        return stage;
    }

    /**
     * used to know when the function start is called, so the javafx application is ready
     * @return always true, if application is not ready the thread is set on wait sate
     */
    public static boolean isReady()
    {
        return isReady.getWaitIfLocked();
    }

    @Override
    public void start(Stage stage) throws IOException {
        MainApplication.stage = stage;
        isReady.setItem(true);
    }

    /**
     * sets the scene passed by argument as scene of the primary stage
     * @param scene scene to set on primary stage
     */
    static void setScene(Scene scene) {
        MainApplication.scene = scene;
        stage.setScene(scene);
    }


    /**
     * utility to construct a scene from fxml file and an already constructed controller
     * @param fxml fxml file
     * @param controller controller object
     * @return the scene built from fxml and controlller
     * @throws IOException if it doesn't find the fxml file
     */
    static Scene createScene(String fxml, Controller controller) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource(fxml));
        fxmlLoader.setController(controller);
        return new Scene((fxmlLoader.load()));
    }


    @Override
    public void stop() throws Exception {
        super.stop();
        //System.out.printlnln("stop");
        gui.close();
    }

}
