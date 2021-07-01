package it.polimi.ingsw.ui.gui;

import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.utilities.LockWrap;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ChoseResourcesController extends Controller implements Initializable {


    @FXML
    AnchorPane root;
    @FXML
    ListView<HBox> resourcesOfChoiceList;
    @FXML
    Label warningLabelOfChoice;
    @FXML
    Label title;
    @FXML
    Label explanation;
    @FXML
    Label addLabel;

    LockWrap<Boolean> done = new LockWrap<>(false, false);
    int numberOFResources;
    ArrayList<Resource> allowedResourceType;

    /**
     * Constructor
     * initializes local variables
     *
     * @param gui
     */
    ChoseResourcesController(GUI gui, ArrayList<Resource> resourceType, int numberOFResources) {
        super(gui);
        this.numberOFResources = numberOFResources;
        this.allowedResourceType = resourceType;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> {
            root.getScene().getWindow().setOnCloseRequest(new EventHandler<WindowEvent>() {
                public void handle(WindowEvent we) {
                    onCancel();
                    //dataReady.setItem(false);
                }
            });
        });
        done.setItem(false);
        checkResources();
        if(numberOFResources == 1)
            title.setText("Select one resource");
        else
            title.setText("Select " + String.valueOf(numberOFResources) + " resources");

        StringBuilder sb = new StringBuilder();
        sb.append("You have to chose "+String.valueOf(numberOFResources)+((numberOFResources == 1)?" resource ":" resources ") +"among these:\n");
        for(var res : allowedResourceType)
            sb.append(res).append("\n");
        explanation.setText(sb.toString());
    }

    public HashMap<Resource, Integer> getChosen() {
        done.getWaitIfLocked();
        HashMap<Resource, Integer> res = new HashMap<>();
        resourcesOfChoiceList.getItems().stream().map( n-> Resource.valueOf(((Label)n.getChildren().get(0)).getText())).forEach( r ->
                res.put(r, res.getOrDefault(r, 0) + 1)
        );


        return res;
    }

    @FXML
    public void onCancel() {
        Alert alert = new Alert(Alert.AlertType.ERROR, "You must select " + numberOFResources + " " + ((numberOFResources > 1) ? "resources " : "resource"), ButtonType.OK);
        alert.showAndWait();
    }

    public void checkResources() {
        if (resourcesOfChoiceList.getItems().size() < numberOFResources)
            warningLabelOfChoice.setOpacity(1);
        else if (resourcesOfChoiceList.getItems().size() == numberOFResources)
            addLabel.setVisible(false);
    }


    @FXML
    public void onNext() {
        if (resourcesOfChoiceList.getItems().size() != numberOFResources)
            return;
        done.setItem(true);
        Stage stage = (Stage) root.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void onAddResourcesClicked() {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initStyle(StageStyle.UTILITY);

        AnchorPane root = new AnchorPane();
        dialog.setTitle("Select Resources");
        Scene scene = new Scene(root);

        ListView<Label> list = new ListView<>();
        list.setMaxHeight(100);
        list.setMaxWidth(200);

        list.getItems().addAll(allowedResourceType.stream().map(n -> new Label(n.name())).collect(Collectors.toList()));
        Button confirm = new Button("Add");
        confirm.addEventFilter(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            dialog.close();
        });
        root.getChildren().addAll(list, confirm);
        AnchorPane.setTopAnchor(list, 0.0);
        AnchorPane.setLeftAnchor(list, 0.0);
        AnchorPane.setRightAnchor(list, 0.0);
        AnchorPane.setBottomAnchor(list, 70.0);
        AnchorPane.setRightAnchor(confirm, 20.0);
        AnchorPane.setBottomAnchor(confirm, 10.0);
        String path = new java.io.File(".").getAbsolutePath().replace("\\", "/");
        path = "file:/" + path.substring(0, path.length() - 2) + "/src/main/resources/it/polimi/ingsw/ui/gui/stylesheets/selectresourcedialog.css";
        root.getStylesheets().add(path);
        dialog.setScene(scene);
        dialog.showAndWait();
        if (list.getSelectionModel().getSelectedItem() != null) {
            HBox hBox = new HBox();
            hBox.setAlignment(Pos.CENTER);
            Label label = new Label(list.getSelectionModel().getSelectedItem().getText());
            label.setMaxHeight(10);
            Line buttonLine = new Line();
            buttonLine.setStrokeWidth(4);
            buttonLine.setEndX(hBox.getWidth() - 5);
            buttonLine.setStartX(hBox.getWidth() - 20);
            buttonLine.setStroke(Paint.valueOf("red"));
            buttonLine.addEventFilter(MouseEvent.MOUSE_CLICKED, mouseEvent -> {

                resourcesOfChoiceList.getItems().remove(hBox);
                checkResources();
            });

            label.setMaxWidth(Double.MAX_VALUE);
            HBox.setHgrow(label, Priority.ALWAYS);
            hBox.getChildren().addAll(label, buttonLine);
            resourcesOfChoiceList.getItems().add(hBox);
            System.out.println(list.getSelectionModel().getSelectedItem().getText());
        }
    }

}
