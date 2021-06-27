package it.polimi.ingsw.ui.gui;

import javafx.scene.Scene;

public abstract class Controller {
    protected GUI gui;
    protected boolean confirmable;
    Scene previousScene;

    public void setPreviousScene(Scene previousScene) {
        this.previousScene = previousScene;
    }

    public void setGUI(GUI gui) {
        this.gui = gui;
    }

    Controller() {
    }

    Controller(GUI gui) {
        this.gui = gui;
    }

    Controller(GUI gui, boolean confirmable) {
        this.gui = gui;
        this.confirmable = confirmable;
    }


}
