package it.polimi.ingsw.ui.gui;


import it.polimi.ingsw.events.controllerEvents.matchEvents.ActivateLeaderCardEvent;
import it.polimi.ingsw.events.controllerEvents.matchEvents.LeaderPowerSelectStateEvent;
import it.polimi.ingsw.utilities.Pair;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.ResourceBundle;


/**
 * ui window controller to allow user to activate/select/deselect leader powers/cards
 */
public class LeaderCardActionController extends Controller implements Initializable {


    @FXML
    ImageView leaderImage;
    @FXML
    AnchorPane root;
    @FXML
    Label isActive;



    LinkedList<Pair<String, Boolean>> leaderImages;
    ListIterator<Pair<String, Boolean>> currentImage;
    String current;


    /**
     * constructor: initialize images and a the list of leaders that the user will scroll with arrows
     * to activate their leader powers
     * @param gui gui object reference
     */
    LeaderCardActionController(GUI gui) {
        super(gui);
        String url = new java.io.File(".").getAbsolutePath();
        url = "file:" + url.substring(0, url.length() - 2).replace("\\", "/") + "/src/main/resources/it/polimi/ingsw/ui/gui/images/leaders/";
        leaderImages = new LinkedList<>();
        String finalUrl = url;
        gui.thisPlayerState().leaderCards.forEach((key, value) -> leaderImages.add(new Pair<>(finalUrl + key + ".png", value)));
        currentImage = leaderImages.listIterator();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        nextImage();
    }

    /**
     * shows the next image in the list
     */
    @FXML
    public void nextImage() {
        Pair<String, Boolean> next;
        if (currentImage.hasNext())
            next = currentImage.next();
        else
            next = (currentImage = leaderImages.listIterator()).next();

        if (leaderImage.getImage().getUrl().equals(next.getKey())) {
            nextImage();
            return;
        }
        String url = next.getKey();
        current = url.substring(url.lastIndexOf("/")+1, url.lastIndexOf("."));
        isActive.setVisible(next.getValue());
        leaderImage.setImage(new Image(next.getKey()));
    }

    /**
     * shows the previous image in the list
     */
    @FXML
    public void previousImage() {
        Pair<String, Boolean> previous;
        if (currentImage.hasPrevious())
            previous = currentImage.previous();
        else
            previous = (currentImage = leaderImages.listIterator(leaderImages.size() - 1)).next();

        if (leaderImage.getImage().getUrl().equals(previous.getKey())) {
            previousImage();
            return;
        }
        current = previous.getKey().substring(previous.getKey().lastIndexOf("/")+1, previous.getKey().lastIndexOf("."));
        isActive.setVisible(previous.getValue());
        leaderImage.setImage(new Image(previous.getKey()));
    }

    /**
     * method invoked when the user press the button to activate a leader card
     */
    @FXML
    public void activateLeaderCard() {
        if(!gui.thisPlayerState().leaderCards.get(current))
            gui.addEvent(new ActivateLeaderCardEvent(gui.askUserID(), current));
        gui.addEvent(new LeaderPowerSelectStateEvent(gui.askUserID(), current, 0, true));
        ((Stage)root.getScene().getWindow()).close();
    }
}
