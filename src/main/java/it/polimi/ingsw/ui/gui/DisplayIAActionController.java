package it.polimi.ingsw.ui.gui;

import it.polimi.ingsw.model.CardColor;
import it.polimi.ingsw.model.singlePlayer.SoloActionToken;
import it.polimi.ingsw.model.singlePlayer.SoloActionTokenDiscard;
import it.polimi.ingsw.model.singlePlayer.SoloActionTokenMove;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javax.swing.text.html.ImageView;
import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;

public class DisplayIAActionController extends Controller implements Initializable {




    DisplayIAActionController(GUI gui, SoloActionToken token)
    {
        super(gui);
        String imagePath = new java.io.File(".").getAbsolutePath() + "/src/main/resources/it/polimi/ingsw/ui/gui/images/";
        if(token instanceof SoloActionTokenDiscard) {
            SoloActionTokenDiscard tok = (SoloActionTokenDiscard) token;
            CardColor color = tok.getCardsDiscarded().keySet().stream().findFirst().get();
            int n = 0;
            switch (color){
                case BLUE ->  n = 1;
                case GREEN -> n = 2;
                case VIOLET -> n = 3;
                case YELLOW -> n = 4;
            }
            imageUrl = "file:/" + imagePath + "token" + n + ".png";
        }
        else{
            SoloActionTokenMove tok = (SoloActionTokenMove) token;
            imageUrl = "file:/" + imagePath + "token" + (tok.reshuffle()?7:6)  + ".png";
        }

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
    }
}
