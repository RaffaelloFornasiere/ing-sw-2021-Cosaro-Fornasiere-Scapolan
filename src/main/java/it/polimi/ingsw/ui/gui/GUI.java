package it.polimi.ingsw.ui.gui;

import it.polimi.ingsw.events.ClientEvents.DepotState;
import it.polimi.ingsw.events.ControllerEvents.MatchEvents.*;
import it.polimi.ingsw.events.Event;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.exceptions.NotPresentException;
import it.polimi.ingsw.model.FaithTrack.PopeFavorCard;
import it.polimi.ingsw.model.LeaderCards.LeaderCard;
import it.polimi.ingsw.ui.UI;
import it.polimi.ingsw.utilities.LockWrap;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;


public class GUI extends UI {

    public enum Action {
        MARKET_ACTION,
        DEV_CARD_ACTION,
        PRODUCTION_ACTION
    }

    private final LockWrap<Action> actionPerformed = new LockWrap<>(null);

    private final LockWrap<String> leaderID = new LockWrap<>(null);
    private final LockWrap<String> playerID = new LockWrap<>(null);
    private final LockWrap<InetAddress> serverAddress = new LockWrap<>(null);
    private final LockWrap<Integer> serverPort = new LockWrap<>(null);

    private PlayerInfo playerInfo;


    private String PlayerImage;

    ServerSettingsController serverSettingsController;
    LoginController loginController;
    SplashScreenController splashScreenController;
    LobbyController lobbyController;
    MainViewController mainViewController;


    public GUI() {

        serverSettingsController = new ServerSettingsController(this);
        loginController = new LoginController(this);
        splashScreenController = new SplashScreenController(this);
        lobbyController = new LobbyController(this);
        mainViewController = new MainViewController(this);

        MainApplication.setGui(this);
        MainApplication.setFirstScene("splashscreen", splashScreenController);
        new Thread() {

            @Override
            public void run() {
                super.run();
                Application.launch(MainApplication.class);

            }
        }.start();
    }


    public void openMarketWindow() {

    }

    public String getPlayerImage() {
        return PlayerImage;
    }

    public void setPlayerImage(String playerImage) {
        PlayerImage = playerImage;
    }

    public void setLoginData(String playerID, String leaderID) {
        this.playerID.setItem(playerID);
        this.leaderID.setItem(leaderID);
    }

    public InetAddress getServerAddress() {
        InetAddress res = null;

        res = serverAddress.getWaitIfLocked();

        return res;
    }

    public void setServerAddress(InetAddress serverAddress) {
        this.serverAddress.setItem(serverAddress);
    }


    public int getServerPort() throws InterruptedException {
        return serverPort.getWaitIfLocked();
    }

    public void setServerPort(int serverPort) {
        this.serverPort.setItem(serverPort);
    }


//   #######  ##     ## ######## ########  ########  #### #######   ########  ######
//  ##     ## ##     ## ##       ##     ## ##     ##  ##  ##     ## ##       ##    ##
//  ##     ## ##     ## ##       ##     ## ##     ##  ##  ##     ## ##       ##
//  ##     ## ##     ## ######   ########  ########   ##  ##     ## ######    ######
//  ##     ##  ##   ##  ##       ##   ##   ##   ##    ##  ##     ## ##             ##
//  ##     ##   ## ##   ##       ##    ##  ##    ##   ##  ##     ## ##       ##    ##
//   #######     ###    ######## ##     ## ##     ## #### #######   ########  ######


    @Override
    // this gets what's wrong
    public void printMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
        alert.showAndWait();
    }

    @Override
    public void printError(String error) {
        Alert alert = new Alert(Alert.AlertType.ERROR, error, ButtonType.OK);
        alert.showAndWait();
    }

    @Override
    public void printWarning(String warning) {
        Alert alert = new Alert(Alert.AlertType.WARNING, warning, ButtonType.OK);
        alert.showAndWait();
    }

    @Override
    public boolean askSingleplayer() {
        return false;
    }

    @Override
    public InetAddress askIP() {
        return serverAddress.getWaitIfLocked();
    }

    @Override
    public boolean askIfNewLobby() {
        return leaderID.getItem() == null;
    }

    @Override
    public String askUserID() {
        return playerID.getWaitIfLocked();
    }

    @Override
    public String askLeaderID() {
        return leaderID.getWaitIfLocked();
    }

    @Override
    public void invalidateUsername() {
        playerID.setItem(null);
    }

    @Override
    public void displayLobbyState(String leaderID, ArrayList<String> otherPLayersID) {
        try {
            MainApplication.setScene("lobby", lobbyController);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void displayWaitingForPlayerToSetupState(String playerID) {

    }

    //TODO deleted from UI
    /*@Override
    public void beginGame() {
        try {
            MainApplication.setScene("mainview", mainViewController);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setUserTurnActive(boolean active) {
        actionPerformed.setItem(null);
    }*/

    @Override
    public ArrayList<String> choseInitialLeaderCards(ArrayList<String> leaderCardsIDs, int numberOFLeaderCardsToChose) {
        Stage popUp = new Stage();
        ArrayList<String> res = new ArrayList<>();
        SelectLeaderCardsController controller = new SelectLeaderCardsController(leaderCardsIDs, numberOFLeaderCardsToChose, res, popUp);
        try {
            FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("selectleadercards.fxml"));
            loader.setController(controller);
            Scene scene = new Scene(loader.load());
            popUp.initModality(Modality.APPLICATION_MODAL);
            popUp.setScene(scene);
            popUp.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }

    @Override
    public HashMap<Resource, Integer> choseResources(ArrayList<Resource> resourceType, int numberOFResources) {
        return null;
    }

    @Override
    public void setPersonalProductionPower(String playerId, ProductionPower personalProductionPower) {

    }

    @Override
    public void updateFaithTrack(String playerID, int position, HashMap<String, HashMap<Integer, PopeFavorCard>> popeFavorCards) {

    }

    @Override
    public void updateDashboard(String playerId, ArrayList<String> topDevCards, HashMap<Resource, Integer> strongBox, ArrayList<DepotState> warehouse) {

    }

    @Override
    public void updateLeaderCardsState(String playerId, HashMap<String, Boolean> leaderCards) {

    }

    @Override
    public void updateMarket(int rows, int cols, Marble[][] marketStatus, Marble marbleLeft) {

    }

    @Override
    public void updateDevCardGrid(String[][] topDevCardIDs) {

    }

    @Override
    public BuyResourcesEvent askForMarketRow() {
        //

        return null;
    }

    @Override
    public BuyDevCardsEvent askForDevCard() {
        //
        return null;
    }

    @Override
    public ActivateProductionEvent askForProductionPowersToUse() {
        //
        return null;
    }

    @Override
    public String askForLeaderCardToDiscard() {

        return null;
    }

    @Override
    public String askForLeaderCardToActivate() {

        return null;
    }

    @Override
    public ArrayList<LeaderPowerSelectStateEvent> askForLeaderCardToSelectOrDeselect() throws NotPresentException {
        return null;
    }

    @Override
    public Event askForNextAction(String PlayerID, boolean lastRound, TurnState turnState) {
        Action a;
        a = actionPerformed.getWaitIfLocked();
        return switch (a) {
            case MARKET_ACTION -> {
                Direction dir = playerInfo.getBoughtResourcesInfo().getKey();
                Integer index = playerInfo.getBoughtResourcesInfo().getValue();
                yield new BuyResourcesEvent(playerInfo.getPlayerID(), dir, index);
            }
            case DEV_CARD_ACTION -> {
                String devCardId = playerInfo.getBuyDevCardInfo().substring(0, playerInfo.getBuyDevCardInfo().indexOf(":"));
                int cardSlot = Integer.parseInt(playerInfo.getBuyDevCardInfo().substring(playerInfo.getBuyDevCardInfo().indexOf(":")));
                yield new BuyDevCardsEvent(playerInfo.getPlayerID(), devCardId, cardSlot);
            }
            case PRODUCTION_ACTION -> {
                var devCards = playerInfo.getProdPowerDevCards();
                var personalPower = playerInfo.isActivatePersonalPower();
                yield new ActivateProductionEvent(playerInfo.getPlayerID(), devCards, personalPower);
            }
        };
    }


    @Override
    public void updateLeaderCardDepositState(String playerID, String leaderCardID, int leaderPowerIndex, HashMap<Resource, Integer> storedResources) {

    }

    @Override
    public void updateLeaderPowersSelectedState(String playerId, String leaderCardID, ArrayList<Boolean> powerSelectedStates) {

    }

    @Override
    public NewResourcesOrganizationEvent getWarehouseDisplacement(HashMap<Resource, Integer> resources) {

        return null;
    }

//    @Override
//    public ArrayList<ArrayList<Resource>> getResourcesSelection(ArrayList<Resource> required) {
//    }

    @Override
    public ChosenResourcesEvent askWhereToTakeResourcesFrom(HashMap<Resource, Integer> required, int freeChoicesResources) {
        return null;
    }

    @Override
    public HashMap<Resource, Integer> chooseResources(int requiredResourcesOFChoice, ArrayList<Resource> allowedResourcesTypes) {
        return null;
    }
}
