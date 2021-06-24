package it.polimi.ingsw.ui.gui;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.ResourceBundle;

public class DevCardGridController extends Controller implements Initializable {

    @FXML
    AnchorPane root;

    @FXML
    GridPane grid;

    String selected;

    DevCardGridController(GUI gui) {
        super(gui);
        selected = null;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        SelectableImage.setSelectable(root);
    }

    public void onCardClicked(MouseEvent mouseEvent) {
        ImageView card = (ImageView) mouseEvent.getSource();
        if (selected == null) {
            String url = card.getImage().getUrl();
            selected = url.substring(url.lastIndexOf("/") + 1, url.lastIndexOf("."));
        } else if (card.getImage().getUrl().contains(selected+".png")) {
            selected = null;
        } else {

            System.out.println(selected);
            ImageView oldCard = grid.getChildren().stream()
                    .flatMap(n -> ((Group) n).getChildren().stream())
                    .filter(n -> n instanceof ImageView)
                    .map(n -> (ImageView) n)
                    .filter(n -> {
                        System.out.println(n.getImage().getUrl());
                        return n.getImage().getUrl().contains(selected+".png");
                    }).findFirst().orElse(null);
            Event.fireEvent(oldCard, new MouseEvent(MouseEvent.MOUSE_CLICKED, 0,
                    0, 0, 0, MouseButton.PRIMARY, 1, true, true, true, true,
                    true, true, true, true, true, true, null));
            String url = card.getImage().getUrl();
            selected = url.substring(url.lastIndexOf("/") + 1, url.lastIndexOf("."));

        }

    }

    public void onCancel() {

    }

    public void onNext() {

    }
}
