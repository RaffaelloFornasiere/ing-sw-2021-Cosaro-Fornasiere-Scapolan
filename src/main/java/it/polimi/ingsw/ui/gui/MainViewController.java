package it.polimi.ingsw.ui.gui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MainViewController extends Controller implements Initializable {
    @FXML
    BorderPane root;
    @FXML
    GridPane faithTrack;

    FaithTrackController faithTrackController;

    public static ArrayList<Node> getAllNodes(Parent root) {
        ArrayList<Node> nodes = new ArrayList<Node>();
        addAllDescendents(root, nodes);
        return nodes;
    }

    public ArrayList<Node> getChildrenOf(Parent parent) {
        ArrayList<Node> nodes = new ArrayList<>();
        for (Node node : parent.getChildrenUnmodifiable()) {
            nodes.add(node);
            if (node instanceof Parent)
                nodes.addAll(getChildrenOf((Parent) node));
        }
        return nodes;
    }

    private static void addAllDescendents(Parent parent, ArrayList<Node> nodes) {

        for (Node node : parent.getChildrenUnmodifiable()) {
            nodes.add(node);
            if (node instanceof Parent)
                addAllDescendents((Parent) node, nodes);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        SelectableImage.setSelectable(root);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                faithTrackController = new FaithTrackController(faithTrack);
            }
        });

    }

    public MainViewController(GUI gui) {
        super(gui);
    }


    public void openMarket() throws IOException {
        Stage marketStage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("market.fxml"));
        fxmlLoader.setController(new MarketController(gui));
        Scene scene = new Scene(fxmlLoader.load());
        marketStage.initModality(Modality.APPLICATION_MODAL);
        marketStage.setScene(scene);
        marketStage.showAndWait();
    }

    public void openCardGrid(MouseEvent event) throws IOException {
        Stage gridStage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("devcardgrid.fxml"));
        fxmlLoader.setController(new DevCardGridController(gui));
        Scene scene = new Scene(fxmlLoader.load());
        gridStage.initModality(Modality.APPLICATION_MODAL);
        gridStage.setScene(scene);
        gridStage.showAndWait();
    }

    public void openProduction() throws IOException {
        if(!PlayerState.canPerformActions)
            return;

        Stage productionStage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("production.fxml"));
        fxmlLoader.setController(new ProductionController(gui));
        Scene scene = new Scene(fxmlLoader.load());
        productionStage.initModality(Modality.APPLICATION_MODAL);
        productionStage.setScene(scene);
        productionStage.showAndWait();
        if(productionStage.isFullScreen())
            System.out.println("");
    }


    public void decrementPos() {
        System.out.println("decrement");
        faithTrackController.setPosition(0);
    }

    public void incrementPos() {

        System.out.println("increment");
        faithTrackController.setPosition(24);
    }

}
