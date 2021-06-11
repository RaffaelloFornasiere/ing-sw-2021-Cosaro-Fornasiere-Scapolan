package it.polimi.ingsw.ui.gui;

import it.polimi.ingsw.events.ClientEvents.DepotState;
import it.polimi.ingsw.events.ControllerEvents.MatchEvents.*;
import it.polimi.ingsw.events.Event;
import it.polimi.ingsw.exceptions.NotPresentException;
import it.polimi.ingsw.model.FaithTrack.PopeFavorCard;
import it.polimi.ingsw.model.LeaderCards.LeaderCard;
import it.polimi.ingsw.model.Marble;
import it.polimi.ingsw.model.ProductionPower;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.model.TurnState;
import it.polimi.ingsw.ui.UI;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;

public class GUI extends UI {
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
    public void printMessage(String message) {

    }

    @Override
    public void printError(String error) {

    }

    @Override
    public void printWarning(String warning) {

    }

    @Override
    public InetAddress askIP() {
        return null;
    }

    @Override
    public boolean askIfNewLobby() {
        return false;
    }

    @Override
    public String askUserID() {
        return null;
    }

    @Override
    public String askLeaderID() {
        return null;
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
    public ActivateProductionEvent askForProductionPowersToUse() {
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
        return null;
    }

    @Override
    public void updateLeaderCardDepositState(String playerID, String leaderCardID, int leaderPowerIndex, HashMap<Resource, Integer> storedResources) {

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
