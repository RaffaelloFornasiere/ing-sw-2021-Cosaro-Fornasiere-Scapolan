package it.polimi.ingsw.ui.gui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class LobbyController extends Controller implements Initializable {
    private ArrayList<String> players;
    private String leader;
    private boolean isLeader;

    @FXML
    ListView<Label> playersList;
    @FXML
    GridPane playersGrid;
    @FXML
    Button startGameButton;

    public LobbyController(GUI gui, String leader, ArrayList<String> players, boolean isLeader) {
        super(gui);
        updateData(leader, players);
        this.isLeader = isLeader;
    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        updatePlayerList(leader, players);
        if (isLeader) {
            startGameButton.setDisable(false);
            startGameButton.setVisible(true);

        }
    }


    public void updatePlayerList(String leader, ArrayList<String> players) {
        updateData(leader, players);
        playersList.getItems().clear();
        playersList.getItems().addAll(players.stream().map(Label::new).collect(Collectors.toList()));
        playersList.getItems().stream()
                .filter(l -> l.getText().equals(leader))
                .collect(Collectors.toList())
                .get(0).setStyle("    -fx-text-fill: #6a0a0a;");

        var children = playersGrid.getChildren();
        for (int i = 0; i < children.size(); i++) {
            Label playerName =
                    ((Label) ((AnchorPane) children.get(i)).getChildren()
                            .stream().filter(n -> n instanceof Label)
                            .collect(Collectors.toList()).get(0));
            playerName.setText(players.get(i));
            if (players.get(i).equals(leader))
                playerName.setStyle("    -fx-text-fill: #6a0a0a; -fx-background-color: transparent");
        }
    }

    private void updateData(String leader, ArrayList<String> players) {
        this.leader = leader;
        this.players = players;

        if (!players.contains(leader))
            throw new IllegalArgumentException("no leader present");
    }

    public ArrayList<String> getPlayers() {
        return new ArrayList<>(players);
    }

    public void onStartGame() {
        gui.startGame();
    }
}
