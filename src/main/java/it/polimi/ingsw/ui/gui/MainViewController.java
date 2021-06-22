package it.polimi.ingsw.ui.gui;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainViewController extends Controller implements Initializable {
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public MainViewController(GUI gui) {
        super(gui);
    }


    public void openMarket() throws IOException {
        Stage marketStage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("market.fxml"));
        fxmlLoader.setController(new MarketController(gui, gui.getMarketStatus().getKey(), gui.getMarketStatus().getValue()));
        Scene scene = new Scene(fxmlLoader.load());
        marketStage.initModality(Modality.APPLICATION_MODAL);
        marketStage.setScene(scene);
        marketStage.showAndWait();
    }

    public void openCardGrid() throws IOException {
        Stage gridStage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("devcardgrid.fxml"));
        fxmlLoader.setController(new DevCardGridController(gui, gui.getDevCardGridState()));
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
