package it.polimi.ingsw.ui.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class SelectOthersDashBoard extends Controller implements Initializable {


    SelectOthersDashBoard(GUI gui) {
        super(gui);
    }

    @FXML
    Button player1;
    @FXML
    Button player2;
    @FXML
    Button player3;
    @FXML
    Button player4;
    @FXML
    AnchorPane root;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ArrayList<Button> list = new ArrayList<>() {{
            add(player1);
            add(player2);
            add(player3);
            add(player4);
        }};
        for (int i = 0; i < gui.playerStates.size(); i++) {
            list.get(i).setText(new ArrayList<>(gui.playerStates.keySet()).get(i));
        }
        for (int i = gui.playerStates.size(); i < 4; i++) {
            list.get(i).setVisible(false);
        }
    }



    @FXML
    public void onClicked(MouseEvent event) throws IOException {
        Button source = (Button) event.getSource();
        String playerId = source.getText();
        Stage watchOthersStage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("OthersDashBoard.fxml"));
        fxmlLoader.setController(new OthersDashBoardController(gui, gui.playerStates.get(playerId), playerId));
        Scene scene = new Scene(fxmlLoader.load());
        watchOthersStage.initModality(Modality.APPLICATION_MODAL);
        watchOthersStage.setScene(scene);
        watchOthersStage.showAndWait();
        ((Stage) root.getScene().getWindow()).close();
    }
}
