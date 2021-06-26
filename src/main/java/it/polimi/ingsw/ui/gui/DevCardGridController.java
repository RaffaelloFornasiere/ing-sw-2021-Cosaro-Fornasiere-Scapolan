package it.polimi.ingsw.ui.gui;

import com.google.gson.Gson;
import it.polimi.ingsw.events.ControllerEvents.MatchEvents.BuyDevCardsEvent;
import it.polimi.ingsw.model.DevCards.DevCard;
import it.polimi.ingsw.model.LeaderCards.ProductionLeaderPower;
import it.polimi.ingsw.model.Resource;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class DevCardGridController extends Controller implements Initializable {

    @FXML
    AnchorPane root;
    @FXML
    GridPane grid;
    String selected;


    int devCardSlot;

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
            ImageView oldCard = grid.getChildren().stream()
                    .flatMap(n -> ((Group) n).getChildren().stream())
                    .filter(n -> n instanceof ImageView)
                    .map(n -> (ImageView) n)
                    .filter(n -> n.getImage().getUrl().contains(selected+".png")).findFirst().orElse(null);
            Event.fireEvent(oldCard, new MouseEvent(MouseEvent.MOUSE_CLICKED, 0,
                    0, 0, 0, MouseButton.PRIMARY, 1, true, true, true, true,
                    true, true, true, true, true, true, null));
            String url = card.getImage().getUrl();
            selected = url.substring(url.lastIndexOf("/") + 1, url.lastIndexOf("."));

        }

    }

    public void onCancel() {
        ((Stage)root.getScene().getWindow()).close();
    }

    public void onNext() {
        if(selected == null)
            return;
        gui.thisPlayerState().events.add(new BuyDevCardsEvent(gui.askUserID(), selected, devCardSlot));
        DevCard devCard = null;
        try {
            devCard = (new Gson()).fromJson(Files.readString(Paths.get("src\\main\\resources\\" + selected + ".json")), DevCard.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        HashMap<Resource, Integer> requiredResources = devCard.getCost();
        requiredResources.entrySet().forEach(n -> System.out.println(n.getKey() + "--" + n.getValue()));
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        SelectResourcesController selectResourcesController = new SelectResourcesController(gui, requiredResources, 0);

        FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("selectresources.fxml"));
        loader.setController(selectResourcesController);
        try {
            stage.setScene(new Scene(loader.load()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.showAndWait();
        gui.addEvent(selectResourcesController.getResult());
    }


}
