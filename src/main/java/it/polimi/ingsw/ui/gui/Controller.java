package it.polimi.ingsw.ui.gui;

import javafx.scene.Scene;


/**
 * abstract class for all gui controllers
 */
public abstract class Controller {
    protected GUI gui;
    protected boolean confirmable;
    Scene previousScene;

    /**
     * sets the scene that will call the one of the current controller.
     * @param previousScene caller scene
     */
    public void setPreviousScene(Scene previousScene) {
        this.previousScene = previousScene;
    }

    /**
     * sets a reference to the gui object
     * @param gui reference object
     */
    public void setGUI(GUI gui) {
        this.gui = gui;
    }

    /**
     * constructor: oblige all subclasses to provide the gui reference object
     * @param gui reference object
     */
    Controller(GUI gui) {
        this.gui = gui;
    }
}
