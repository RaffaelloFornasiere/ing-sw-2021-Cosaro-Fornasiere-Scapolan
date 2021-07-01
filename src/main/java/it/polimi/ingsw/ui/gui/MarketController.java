package it.polimi.ingsw.ui.gui;

import it.polimi.ingsw.events.controllerEvents.matchEvents.BuyResourcesEvent;
import it.polimi.ingsw.model.Direction;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class MarketController extends Controller implements Initializable {
    @FXML
    GridPane gridPane;

    @FXML
    ImageView marbleLeftImage;

    @FXML
    Button nextButton;

    @FXML
    AnchorPane root;
    Direction dir;
    int index;

    private BuyResourcesEvent data;


    ArrayList<ArrayList<String>> marketStatus;
    String marbleLeft;


    MarketController(GUI gui) {
        super(gui);
        this.marketStatus = new ArrayList<>();
        String imagePath = new java.io.File(".").getAbsolutePath() + "/src/main/resources/it/polimi/ingsw/ui/gui/images/";
        for (int i = 0; i < PlayerState.marketStatus.getKey().length; i++) {
            this.marketStatus.add(new ArrayList<String>());
            for (int j = 0; j < PlayerState.marketStatus.getKey()[0].length; j++) {
                String url = "file:/" + imagePath + PlayerState.marketStatus.getKey()[i][j].toString().toLowerCase() + "-marble.png";
                System.out.println(url);
                this.marketStatus.get(i).add(url);
            }
        }
        this.marbleLeft = "file:/" + imagePath + PlayerState.marketStatus.getValue().toString().toLowerCase() + "-marble.png";


    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (!PlayerState.canPerformActions) {
            SelectableImage.getChildrenOf(root).stream().filter(n -> n.getStyleClass().contains("arrow-button"))
                    .forEach(n -> n.setDisable(true));
            nextButton.setDisable(true);
        }

        var marbles = gridPane.getChildren()
                .stream().filter(n -> n instanceof ImageView)
                .map(i -> (ImageView) i).collect(Collectors.toList());


        for (var marble : marbles) {
            marble.setImage(new Image(marketStatus.get(marble.getId().charAt(2) - '0')
                    .get(marble.getId().charAt(1) - '0')));
        }
        marbleLeftImage.setImage(new Image(marbleLeft));


    }

    public void onButtonPressed(ActionEvent event) {
        String id = ((Button) event.getSource()).getId();
        int index = Integer.parseInt(String.valueOf(id.charAt(1))) - 1;
        String regex = "c"
                + (id.contains("c") ? Integer.toString(index) + "\\d" : "\\d" + Integer.toString(index));
        //System.out.println(regex);
        gridPane.getChildren().stream()
                .filter(n -> n instanceof Circle)
                .forEach(c -> c.setOpacity(0));
        gridPane.getChildren().stream()
                .filter(n -> n instanceof Circle)
                .filter(n -> n.getId().matches(regex))
                .forEach(c -> c.setOpacity(1));
        dir = (id.contains("c") ? Direction.COLUMN : Direction.ROW);
        this.index = index;
    }

    public void onCancel() throws IOException {
        ((Stage) gridPane.getScene().getWindow()).close();
    }

    public void onNext() {

        gui.addEvent(new BuyResourcesEvent(gui.askUserID(), dir, index));

    }


}
