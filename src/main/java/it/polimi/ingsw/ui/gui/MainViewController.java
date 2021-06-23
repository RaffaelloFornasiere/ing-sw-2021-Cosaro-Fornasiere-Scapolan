package it.polimi.ingsw.ui.gui;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.stream.Collectors;

public class MainViewController extends Controller implements Initializable {
    @FXML
    BorderPane root;

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
    }

    public MainViewController(GUI gui) {
        super(gui);
    }


    public void openMarket() throws IOException {
        Stage marketStage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("market.fxml"));
        fxmlLoader.setController(new MarketController(gui, gui.playerState.marketStatus.getKey(), gui.playerState.marketStatus.getValue()));
        Scene scene = new Scene(fxmlLoader.load());
        marketStage.initModality(Modality.APPLICATION_MODAL);
        marketStage.setScene(scene);
        marketStage.showAndWait();
    }

    public void openCardGrid(MouseEvent event) throws IOException {
        System.out.println(((Node)event.getSource()).getId());
        Stage gridStage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("devcardgrid.fxml"));
        fxmlLoader.setController(new DevCardGridController(gui));
        Scene scene = new Scene(fxmlLoader.load());
        gridStage.initModality(Modality.APPLICATION_MODAL);
        gridStage.setScene(scene);
        gridStage.showAndWait();
    }

    public void openProduction() throws IOException {
        Stage productionStage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("production.fxml"));
        fxmlLoader.setController(new ProductionController(gui));
        Scene scene = new Scene(fxmlLoader.load());
        productionStage.initModality(Modality.APPLICATION_MODAL);
        productionStage.setScene(scene);
        productionStage.showAndWait();
    }


}
