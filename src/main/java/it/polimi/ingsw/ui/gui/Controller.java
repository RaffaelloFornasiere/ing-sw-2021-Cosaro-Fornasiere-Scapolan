package it.polimi.ingsw.ui.gui;

public abstract class Controller {
    protected GUI gui;
    //ciao
    public void setGUI(GUI gui){this.gui = gui;}
    Controller(){}
    Controller(GUI gui){this.gui = gui;}
}
