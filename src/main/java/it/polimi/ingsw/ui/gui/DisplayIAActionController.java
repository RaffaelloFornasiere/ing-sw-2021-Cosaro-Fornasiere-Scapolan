package it.polimi.ingsw.ui.gui;

import it.polimi.ingsw.model.CardColor;
import it.polimi.ingsw.model.singlePlayer.SoloActionToken;
import it.polimi.ingsw.model.singlePlayer.SoloActionTokenDiscard;
import it.polimi.ingsw.model.singlePlayer.SoloActionTokenMove;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;


/**
 * ui window controller to show the user what solo action token have been selected
 */
public class DisplayIAActionController extends Controller implements Initializable {

    @FXML
    AnchorPane root;


    /**
     * Constructor
     *
     * @param gui   gui object reference
     * @param token the selected token
     */
    //TODO it doesn't work properly
    DisplayIAActionController(GUI gui, SoloActionToken token) {
        super(gui);
        String imagePath = new java.io.File(".").getAbsolutePath() + "/src/main/resources/it/polimi/ingsw/ui/gui/images/";
        if (token instanceof SoloActionTokenDiscard) {
            SoloActionTokenDiscard tok = (SoloActionTokenDiscard) token;
            @SuppressWarnings("OptionalGetWithoutIsPresent")
            CardColor color = tok.getCardsDiscarded().keySet().stream().findFirst().get();
            int n = 0;
            switch (color) {
                case BLUE -> n = 1;
                case GREEN -> n = 2;
                case VIOLET -> n = 3;
                case YELLOW -> n = 4;
            }
            imageUrl = "file:" + imagePath + "token" + n + ".png";
        } else {
            SoloActionTokenMove tok = (SoloActionTokenMove) token;
            imageUrl = "file:" + imagePath + "token" + (tok.reshuffle() ? 7 : 6) + ".png";
        }

        description = token.description();
    }

    String description;
    @FXML
    Label descriptionLabel;
    @FXML
    ImageView tokenImage;
    String imageUrl;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        descriptionLabel.setText(description);
        tokenImage.setImage(new Image(imageUrl));
    }


    /**
     * closes the window
     */
    @FXML
    public void onOK() {
        ((Stage)root.getScene().getWindow()).close();
    }
}
