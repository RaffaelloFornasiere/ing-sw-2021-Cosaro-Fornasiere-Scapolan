package it.polimi.ingsw.ui.gui;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.events.controllerEvents.matchEvents.ActivateProductionEvent;
import it.polimi.ingsw.model.devCards.DevCard;
import it.polimi.ingsw.model.leaderCards.LeaderCard;
import it.polimi.ingsw.model.leaderCards.LeaderPower;
import it.polimi.ingsw.model.leaderCards.ProductionLeaderPower;
import it.polimi.ingsw.model.leaderCards.Requirement;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.ui.cli.Action;
import it.polimi.ingsw.utilities.GsonInheritanceAdapter;
import it.polimi.ingsw.utilities.GsonPairAdapter;
import it.polimi.ingsw.utilities.Pair;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class ProductionController extends Controller implements Initializable {

    @FXML
    ListView<Label> warehouseList;
    @FXML
    ListView<Label> strongboxList;
    @FXML
    ListView<Label> resourcesToUse;
    @FXML
    ListView<Label> leaderDeportsResourcesList;
    @FXML
    AnchorPane root;
//    @FXML
//    ListView<HBox> resourcesOfChoiceList;
    @FXML
    Label warningLabelOfChoice;

    ArrayList<Pair<ImageView, Region>> selectedProductions;

    private final ArrayList<DevCard> devCards;
    private final ArrayList<String> selectedDevCards;
    private final ArrayList<LeaderCard> leaderCards;
    private final ArrayList<String> selectedLeaderCards;
    boolean personalPower;

    /**
     * Constructor
     * initializes local variables
     *
     * @param gui
     */
    ProductionController(GUI gui) {
        super(gui);
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Requirement.class, new GsonInheritanceAdapter<Requirement>());
        builder.registerTypeAdapter(LeaderPower.class, new GsonInheritanceAdapter<LeaderPower>());
        builder.registerTypeAdapter(Pair.class, new GsonPairAdapter());
        Gson gson = builder.create();
        devCards = new ArrayList<>();
        leaderCards = new ArrayList<>();
        selectedDevCards = new ArrayList<>();
        selectedLeaderCards = new ArrayList<>();
        try {
            for (var deck : gui.thisPlayerState().ownedCards) {
                if (deck.isEmpty())
                    continue;
                var devCard = gson.fromJson(Files.readString(Paths.get("src/main/resources/" + deck.get(deck.size() - 1) + ".json")), DevCard.class);
                devCards.add(devCard);
            }
            if (PlayerState.availableActions.contains(Action.LEADER_ACTION))
                for (var card : gui.thisPlayerState().leaderCards.entrySet().stream().filter(Map.Entry::getValue).map(Map.Entry::getKey).collect(Collectors.toList())) {
                    var leaderCard = gson.fromJson(Files.readString(Paths.get("src/main/resources/" + card + ".json")), LeaderCard.class);
                    leaderCards.add(leaderCard);
                }

        } catch (IOException e) {
            e.printStackTrace();
        }
        selectedProductions = new ArrayList<>();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // ************************************************************************************************** //
        // SETTING CARDS IMAGES
        // ************************************************************************************************** //
        var selectableImages = root.getChildren().stream()
                .filter(n -> n.getStyleClass().contains("selectable") && n instanceof ImageView)
                .map(n -> (ImageView) n)
                .collect(Collectors.toList());
        String imagePath = new java.io.File(".").getAbsolutePath();
        imagePath = imagePath.substring(0, imagePath.length() - 2) + "/src/main/resources/it/polimi/ingsw/ui/gui/images/";
        var devCardsImages = selectableImages.stream().filter(n -> n.getStyleClass().contains("devCard")).collect(Collectors.toList());
        for (int i = 0; i < devCardsImages.size(); i++) {
            if (i >= devCards.size()) {
                selectableImages.remove(devCardsImages.get(i));
                devCardsImages.get(i).setOpacity(0);
                continue;
            }
            devCardsImages.get(i).setOpacity(1);
            devCardsImages.get(i).setImage(new Image("file:/" + imagePath + "front/" +
                    devCards.get(i).getCardID() + ".png"));
        }
        System.out.println(devCardsImages.size());
        var leaderCardImages = selectableImages.stream().filter(n -> n.getStyleClass().contains("leaderCard")).collect(Collectors.toList());
        System.out.println(leaderCardImages.size());


        for (int i = 0; i < leaderCardImages.size(); i++) {
            if (i >= leaderCards.size()) {
                selectableImages.remove(leaderCardImages.get(i));
                leaderCardImages.get(i).getStyleClass().remove("selectable");
                leaderCardImages.get(i).setOpacity(0);
                continue;
            }
            int finalI = i;
            leaderCardImages.get(i).setOpacity(1);
            LeaderCard leaderCard = leaderCards.stream().filter(card -> card.getCardID().equals(leaderCards.get(finalI).getCardID())).findFirst().orElse(null);
            assert leaderCard != null;
            if (leaderCard.getLeaderPowers().stream().noneMatch(n -> n instanceof ProductionLeaderPower)) {
                selectableImages.remove(leaderCardImages.get(i));
            }
            leaderCardImages.get(i).setImage(new Image("file:/" + imagePath + "leaders/" + leaderCards.get(i).getCardID() + ".png"));

        }

        SelectableImage.setSelectable(root);
    }

    public void onProductionClicked(MouseEvent mouseEvent) {
        ImageView image = (ImageView) mouseEvent.getSource();
        String name = image.getImage().getUrl();

        name = name.substring(name.lastIndexOf("/") + 1, name.lastIndexOf("."));
        System.out.println(name);
        ArrayList<String> aux = null;
        if (name.contains("DevCard"))
            aux = selectedDevCards;
        else if (name.contains("LeaderCard"))
            aux = selectedLeaderCards;
        if (aux != null) {
            boolean present = aux.contains(name);
            if (!present)
                aux.add(name);
            else
                aux.remove(name);
        } else
            personalPower = true;
    }


    public void leaderClick(MouseEvent event) {

    }

    public ArrayList<Resource> getResourcesRequired() {
        ArrayList<Resource> resourcesRequired = new ArrayList<>();
        for (var card : selectedDevCards) {
            ArrayList<Resource> required = devCards.stream()
                    .filter(c -> c.getCardID().equals(card))
                    .map(c -> c.getProductionPower().getConsumedResources()).flatMap(p ->
                            p.entrySet().stream().flatMap(entry -> new ArrayList<Resource>() {{
                                for (int i = 0; i < entry.getValue(); i++)
                                    add(entry.getKey());
                            }}.stream())).collect(Collectors.toCollection(ArrayList::new));
            resourcesRequired.addAll(required);
        }
        for (var card : selectedLeaderCards) {
            ArrayList<Resource> required = leaderCards.stream()
                    .filter(c -> c.getCardID().equals(card))
                    .flatMap(c -> c.getLeaderPowers().stream())
                    .map(power -> ((ProductionLeaderPower) power).getEffectPower().getConsumedResources())
                    .flatMap(p -> p.entrySet().stream().flatMap(entry -> new ArrayList<Resource>() {{
                        for (int i = 0; i < entry.getValue(); i++)
                            add(entry.getKey());
                    }}.stream())).collect(Collectors.toCollection(ArrayList::new));
            resourcesRequired.addAll(required);
        }
        return resourcesRequired;
    }


    @FXML
    public void onCancel() {
        Stage stage = (Stage) root.getScene().getWindow();
        stage.close();
    }

//    public void checkResources() {
//        int opacity = (((personalPower ? 1 : 0) + selectedLeaderCards.size()) == resourcesOfChoiceList.getItems().size()) ? 0 : 1;
//        warningLabelOfChoice.setOpacity(opacity);
//    }


    @FXML
    public void onNext() {
        if (getResourcesRequired().size() == 0) {
            ((Stage) root.getScene().getWindow()).close();
            return;
        }
        gui.addEvent(new ActivateProductionEvent(gui.askUserID(), selectedDevCards, personalPower));


        ((Stage) root.getScene().getWindow()).close();
    }
}
