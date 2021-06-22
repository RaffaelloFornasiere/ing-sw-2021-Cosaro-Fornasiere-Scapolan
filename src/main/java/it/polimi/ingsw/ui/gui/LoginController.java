package it.polimi.ingsw.ui.gui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.ResourceBundle;

public class LoginController extends Controller implements Initializable {

    LinkedList<String> playerImages;
    ListIterator<String> currentImage;


    public LoginController(GUI gui) {
        super(gui);
        playerImages = new LinkedList<String>();
        File dir = new File("src/main/resources/it/polimi/ingsw/ui/gui/images/Players/");
        if (!dir.exists())
            throw new NullPointerException("aaa");
        for (File file : dir.listFiles()) {
            if (!file.isDirectory()) {
                String url = "file:/" + file.getAbsolutePath().replace("\\", "/");
                playerImages.add(url);
            }
        }
        currentImage = playerImages.listIterator();


    }

    @FXML
    ImageView playerImage;
    @FXML
    CheckBox createMatchCheckBox;
    @FXML
    Group joinMatchGroup;
    @FXML
    TextField playerIdTextField;
    @FXML
    Label noPlayerIDLabel;
    @FXML
    TextField leaderIdTextField;

    public void onCheckBoxStatusChanged() {
        if (createMatchCheckBox.isSelected())
            joinMatchGroup.setOpacity(1);
        else
            joinMatchGroup.setOpacity(0);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (gui.getPlayerImage() == null)
            playerImage.setImage(new Image(currentImage.next()));
        else
            playerImage.setImage(new Image(gui.getPlayerImage()));
    }

    public void nextImage() {
        String newImage;
        if (currentImage.hasNext())
            newImage = currentImage.next();
        else
            newImage = (currentImage = playerImages.listIterator()).next();

        if (playerImage.getImage().getUrl().equals(newImage)) {
            nextImage();
            return;
        }
        playerImage.setImage(new Image(newImage));
    }

    public void previousImage() {
        String newImage;
        if (currentImage.hasPrevious())
            newImage = currentImage.previous();
        else
            newImage = (currentImage = playerImages.listIterator(playerImages.size() - 1)).next();

        if (playerImage.getImage().getUrl().equals(newImage)) {
            previousImage();
            return;
        }
        playerImage.setImage(new Image(newImage));
    }


    public void onCancel() throws IOException {
        MainApplication.setPreviousScene();
    }

    public void onNext() throws IOException {
        if (playerIdTextField.getText().equals(""))
            noPlayerIDLabel.setOpacity(1);
        else {
            noPlayerIDLabel.setOpacity(0);
            gui.setLoginData(playerIdTextField.getText(), leaderIdTextField.getText());
            gui.setPlayerImage(playerImage.getImage().getUrl());
        }
    }

    public void UsernameAlreadyTaken() {

    }


}
