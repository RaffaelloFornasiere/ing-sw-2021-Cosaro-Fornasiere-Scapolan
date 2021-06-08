package it.polimi.ingsw.ui.gui;

import com.sun.tools.javac.Main;
import it.polimi.ingsw.client.NetworkAdapter;
import it.polimi.ingsw.events.ClientEvents.DepotState;
import it.polimi.ingsw.events.ControllerEvents.MatchEvents.BuyDevCardsEvent;
import it.polimi.ingsw.events.ControllerEvents.MatchEvents.BuyResourcesEvent;
import it.polimi.ingsw.events.ControllerEvents.MatchEvents.NewResourcesOrganizationEvent;
import it.polimi.ingsw.model.FaithTrack.PopeFavorCard;
import it.polimi.ingsw.model.LeaderCards.LeaderCard;
import it.polimi.ingsw.model.Marble;
import it.polimi.ingsw.model.ProductionPower;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.ui.UI;
import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;

public class GUI extends UI {
    String leaderID;
    String playerID;




    GUI() {
        MainApplication.setGui(this);
        try {
            MainApplication.setScene("splashscreen");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Application.launch(MainApplication.class);

    }

    public void setLoginData(String playerID, String leaderID) {
        this.playerID = playerID;
        this.leaderID = leaderID;
    }

    public InetAddress getServerAddress() {
        return serverAddress;
    }

    public void setServerAddress(InetAddress serverAddress) {
        this.serverAddress = serverAddress;
    }

    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    private InetAddress serverAddress;
    private int serverPort;


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
    public InetAddress askIP() {
        return serverAddress;
    }

    @Override
    public boolean askIfNewLobby() {
        return leaderID==null;
    }

    @Override
    public String askUserID() {
        return playerID;
    }

    @Override
    public String askLeaderID() {
        return leaderID;
    }

    @Override
    public void displayLobbyState(String leaderID, ArrayList<String> otherPLayersID) {

    }

    @Override
    public void displayWaitingForPlayerToSetupState(String playerID) {

    }

    @Override
    public void beginGame() {

    }

    @Override
    public void setUserTurnActive(boolean active) {

    }

    @Override
    public void ack() {

    }

    @Override
    public ArrayList<String> choseInitialLeaderCards(ArrayList<String> leaderCardsIDs, int numberOFLeaderCardsToChose) {
        return null;
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
        return null;
    }

    @Override
    public BuyDevCardsEvent askForDevCard() {
        return null;
    }

    @Override
    public HashMap<Marble, LeaderCard> getLeaderCardMarbleMatching(ArrayList<Marble> marbles, ArrayList<String> leaderCardIDs) {
        return null;
    }

    @Override
    public NewResourcesOrganizationEvent getWarehouseDisplacement(HashMap<Resource, Integer> resources) {
        return null;
    }

    @Override
    public ArrayList<LeaderCard> useLeaderCardPowers(ArrayList<LeaderCard> leaderCards) {
        return null;
    }

    @Override
    public ArrayList<ArrayList<Resource>> getResourcesSelection(ArrayList<Resource> required) {
        return null;
    }
}
