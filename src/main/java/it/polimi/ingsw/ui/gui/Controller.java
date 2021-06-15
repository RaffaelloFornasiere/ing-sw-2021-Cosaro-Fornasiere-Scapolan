package it.polimi.ingsw.ui.gui;

import javafx.fxml.Initializable;

public abstract class Controller {
    protected GUI gui;
    protected boolean confirmable;
    public void setGUI(GUI gui){this.gui = gui;}
    Controller(){}
    Controller(GUI gui){this.gui = gui;}
    Controller(GUI gui, boolean confirmable){this.gui = gui; this.confirmable = confirmable;}
}
