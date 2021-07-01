package it.polimi.ingsw.ui.gui;

import it.polimi.ingsw.events.ClientEvents.FinalPlayerState;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;


public class FinalScreenController extends Controller implements Initializable {


    @FXML
    Label primaryLabel;
    @FXML
    Label secondaryLabel;
    @FXML
    ListView<HBox> classificationList;
    boolean singlePlayer;
    ArrayList<FinalPlayerState> finalPlayerStates;


    public FinalScreenController(GUI gui, boolean singlePlayer, ArrayList<FinalPlayerState> finalPlayerStates) {
        super(gui);
        this.finalPlayerStates = finalPlayerStates;
        this.singlePlayer = singlePlayer;

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        if (!singlePlayer) {
            if (finalPlayerStates.get(0).getPlayerID().equals(gui.playerID.getItem())) {
                primaryLabel.setText("YOU WON");
                secondaryLabel.setOpacity(0);
            } else {
                primaryLabel.setText("YOU LOST");
                secondaryLabel.setText("the winner is " + finalPlayerStates.get(0).getPlayerID());
            }

            finalPlayerStates.forEach(n -> {
                HBox hBox = new HBox();
                Label playerID = new Label(n.getPlayerID());
                Label victoryPoints = new Label(String.valueOf(n.getVictoryPoints()));
                Label totalResources = new Label(String.valueOf(n.getTotalResources()));
                hBox.setAlignment(Pos.CENTER);
                hBox.getChildren().addAll(playerID, victoryPoints, totalResources);

                playerID.setMaxWidth(Double.MAX_VALUE);
                victoryPoints.setMinWidth(30);
                totalResources.setMinWidth(30);
                HBox.setHgrow(playerID, Priority.ALWAYS);
                classificationList.getItems().add(hBox);
            });
        } else {
            if (finalPlayerStates != null) {
                primaryLabel.setText("YOU WON");
                secondaryLabel.setOpacity(0);
            } else {
                primaryLabel.setText("YOU LOST");
                secondaryLabel.setText("Lorenzo il Magnifico won");
            }
        }

    }


}
