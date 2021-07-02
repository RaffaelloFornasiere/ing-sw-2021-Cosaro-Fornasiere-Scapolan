package it.polimi.ingsw.ui.gui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.stream.Collectors;


/**
 * ui window controller to show the lobby and the users logging in the match
 * the match leader can start the game
 */
public class LobbyController extends Controller implements Initializable {
    private ArrayList<String> players;
    private String leader;
    private final boolean isLeader;

    @FXML
    ListView<Label> playersList;
    @FXML
    Button startGameButton;
    @FXML
    AnchorPane root;

    /**
     * controller
     * @param gui object reference to gui
     * @param leader leader id of the
     * @param players players already in the lobby
     * @param isLeader if this player is the leader
     */
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


    /**
     * update the data and the ui
     * @param leader string identifier of the leader
     * @param players all players in lobby
     */
    public void updatePlayerList(String leader, ArrayList<String> players) {
        updateData(leader, players);
        playersList.getItems().clear();
        playersList.getItems().addAll(players.stream().map(Label::new).collect(Collectors.toList()));
        playersList.getItems().stream()
                .filter(l -> l.getText().equals(leader))
                .collect(Collectors.toList())
                .get(0).setStyle("    -fx-text-fill: #6a0a0a;");
    }


    /**
     * update the lobby players
     * @param leader leader identifier
     * @param players players in the lobby
     */
    private void updateData(String leader, ArrayList<String> players) {
        this.leader = leader;
        this.players = players;

        if (!players.contains(leader))
            throw new IllegalArgumentException("no leader present");
    }

    /**
     * @return the players waiting inside the lobby
     */
    public ArrayList<String> getPlayers() {
        return new ArrayList<>(players);
    }

    /**
     * calls a function that will trig an event to start the game
     */
    @FXML
    public void onStartGame() {
        gui.startGame();
    }


}
