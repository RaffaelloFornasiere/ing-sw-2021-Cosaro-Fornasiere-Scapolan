package it.polimi.ingsw.ui.gui;

import it.polimi.ingsw.events.ClientEvents.DepotState;
import it.polimi.ingsw.events.ClientEvents.FinalPlayerState;
import it.polimi.ingsw.events.ControllerEvents.MatchEvents.*;
import it.polimi.ingsw.events.Event;
import it.polimi.ingsw.exceptions.NotPresentException;
import it.polimi.ingsw.model.FaithTrack.PopeFavorCard;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.singlePlayer.SoloActionToken;
import it.polimi.ingsw.ui.UI;
import it.polimi.ingsw.ui.cli.Action;
import it.polimi.ingsw.utilities.LockWrap;
import it.polimi.ingsw.utilities.Pair;
import javafx.application.Application;
import javafx.application.Platform;
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
import java.util.concurrent.TimeUnit;


public class GUI extends UI {

    /*public enum Action {
        MARKET_ACTION,
        DEV_CARD_ACTION,
        PRODUCTION_ACTION
    }*/

    // private final LockWrap<Action> actionPerformed = new LockWrap<>(null);

    private final LockWrap<String> leaderID = new LockWrap<>(null);
    private final LockWrap<String> playerID = new LockWrap<>(null);
    private final LockWrap<InetAddress> serverAddress = new LockWrap<>(null);
    private final LockWrap<Integer> serverPort = new LockWrap<>(null);


    private String PlayerImage;

    ServerSettingsController serverSettingsController;
    LoginController loginController;
    SplashScreenController splashScreenController;
    LobbyController lobbyController;
    MainViewController mainViewController;

    HashMap<String, PlayerState> playerStates;

    public PlayerState thisPlayerState() {
        return playerStates.get(playerID.getItem());
    }


    public GUI() {

        serverSettingsController = new ServerSettingsController(this);
        loginController = new LoginController(this);
        splashScreenController = new SplashScreenController(this);
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
        playerStates = new HashMap<>();
        var aux = this;
        try {
            TimeUnit.SECONDS.sleep(1);

            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    playerID.setItem("paolo");
                    playerStates.put(playerID.getItem(), new PlayerState());

                    HashMap<Resource, Integer> strongBox = new HashMap<>() {{
                        put(Resource.COIN, 4);
                        put(Resource.SHIELD, 2);
                        put(Resource.SERVANT, 5);
                        put(Resource.ROCK, 3);
                    }};
                    ArrayList<DepotState> warehouse = new ArrayList<>() {{
                        add(new DepotState(Resource.COIN, 1, 1));
                        add(new DepotState(Resource.SHIELD, 2, 2));
                        add(new DepotState(Resource.SERVANT, 3, 3));

                    }};

                    aux.thisPlayerState().ownedCards.get(0).add("DevCard1");
                    aux.thisPlayerState().ownedCards.get(0).add("DevCard23");
                    aux.thisPlayerState().ownedCards.get(1).add("DevCard4");
                    aux.thisPlayerState().ownedCards.get(2).add("DevCard8");
                    aux.thisPlayerState().ownedCards.get(2).add("DevCard26");
                    aux.thisPlayerState().ownedCards.get(2).add("DevCard35");

                    aux.thisPlayerState().leaderCards.put("LeaderCard5", true);
                    aux.thisPlayerState().leaderCards.put("LeaderCard6", false);
                    aux.thisPlayerState().leaderCards.put("LeaderCard7", false);
                    aux.thisPlayerState().leaderCards.put("LeaderCard8", false);


                    aux.thisPlayerState().updateLeaderCardDepositState("LeaderCard15", 0,
                            new HashMap<>() {{
                                put(Resource.COIN, 2);
                                put(Resource.SERVANT, 1);
                            }});
                    aux.thisPlayerState().warehouse = warehouse;
                    aux.thisPlayerState().strongBox = strongBox;
                    aux.thisPlayerState().faithTrackPoints = 4;
                    aux.thisPlayerState().victoryPoints = 15;
                    ArrayList<ArrayList<Integer>> indexes = new ArrayList<>();
                    for (int i = 0; i < 3; i++) {
                        int finalI = i;
                        indexes.add(new ArrayList<>() {{
                            int base = (finalI) * 16;
                            add(1 + base);
                            add(2 + base);
                            add(3 + base);
                            add(4 + base);
                        }});
                    }

                    PlayerState.devCardGrid = new String[3][4];
                    for (int i = 0; i < PlayerState.devCardGrid.length; i++) {
                        for (int j = 0; j < PlayerState.devCardGrid[0].length; j++) {
                            int index = indexes.get(i).get(j);
                            PlayerState.devCardGrid[i][j] = "DevCard" + index;
                        }
                    }


                    int rows = 3;
                    int cols = 4;
                    PlayerState.devCardGrid = new String[rows][cols];
                    for (int i = 0; i < rows; i++) {
                        for (int j = 0; j < cols; j++) {
                            int level = i * 16 + 1;
                            PlayerState.devCardGrid[i][j] = "DevCard" + (level + j);
                            //System.out.println(aux.playerState.devCardGrid[i][j]);
                        }
                    }
                    HashMap<Marble, Integer> marbles = new HashMap<>() {{
                        put(Marble.GRAY, 2);
                        put(Marble.YELLOW, 2);
                        put(Marble.PURPLE, 2);
                        put(Marble.BLUE, 2);
                        put(Marble.WHITE, 4);
                        put(Marble.RED, 1);
                    }};
                    Market market = new Market(3, 4, marbles);
                    PlayerState.marketStatus = new Pair<>(market.getMarketStatus(), market.getMarbleLeft());

                    Stage stage = new Stage();
                    FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("mainview.fxml"));
                    MainViewController mainViewController = new MainViewController(aux);
                    fxmlLoader.setController(mainViewController);
                    try {
                        stage.setScene(new Scene(fxmlLoader.load()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    stage.show();


                }
            });

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public Pair<Marble[][], Marble> getMarketStatus() {
        return new Pair<>(PlayerState.marketStatus);
    }

    public String[][] getDevCardGridState() {
        return PlayerState.devCardGrid.clone();
    }

    public void addEvent(Event event) {
        /*if (event instanceof BuyResourcesEvent) {
            actionPerformed.setItem(Action.MARKET_ACTION);
        } else if (event instanceof BuyDevCardsEvent) {
            actionPerformed.setItem(Action.DEV_CARD_ACTION);
        } else if (event instanceof ActivateProductionEvent) {
            actionPerformed.setItem(Action.PRODUCTION_ACTION);
        }*/
        thisPlayerState().events.add(event);
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
        return serverAddress.getWaitIfLocked();
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

    //TODO
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

    //TODO?
    @Override
    public void displayLobbyState(String leaderID, ArrayList<String> otherPLayersID) {
        try {
            if (lobbyController != null)
                lobbyController.updatePlayerList(leaderID, otherPLayersID);
            else {
                LobbyController lobbyController = new LobbyController(this, leaderID, otherPLayersID,
                        this.playerID.getItem().equals(leaderID));
                MainApplication.setScene("lobby", lobbyController);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void startGame() {
        client.startMatch();
    }

    //TODO
    @Override
    public void displayWaitingForPlayerToSetupState(String playerID) {

    }

    @Override
    public void initializeMatchObjects() {
        for (String playerID : lobbyController.getPlayers()) {
            playerStates.put(playerID, new PlayerState());
        }
    }

    //TODO?
    @Override
    public ArrayList<String> choseInitialLeaderCards(ArrayList<String> leaderCardsIDs, int numberOFLeaderCardsToChose) {
        Stage popUp = new Stage();
        ArrayList<String> res = new ArrayList<>();
        SelectLeaderCardsController controller = new SelectLeaderCardsController(leaderCardsIDs, numberOFLeaderCardsToChose);
        try {
            FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("selectleadercards.fxml"));
            loader.setController(controller);
            Scene scene = new Scene(loader.load());
            popUp.initModality(Modality.APPLICATION_MODAL);
            popUp.setScene(scene);
            popUp.showAndWait();
            res = controller.getSelected();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }

    //TODO
    @Override
    public HashMap<Resource, Integer> choseResources(ArrayList<Resource> resourceType, int numberOFResources) {
        return null;
    }

    @Override
    public void setPersonalProductionPower(String playerId, ProductionPower personalProductionPower) {
        playerStates.get(playerId).personalProductionPower = personalProductionPower;
    }

    @Override
    public void updateFaithTrack(String playerID, int position, HashMap<String, HashMap<Integer, PopeFavorCard>> popeFavorCards) {
        PlayerState playerState = playerStates.get(playerID);
        playerState.setFaithTrackPosition(position);
        playerState.setPopeFavorCards(popeFavorCards.get(playerID));

        if (playerID.equals(this.playerID.getItem())) {
            mainViewController.faithTrackController.setPosition(position);
            mainViewController.faithTrackController.setPopeFavorCards(popeFavorCards);
        }
    }

    @Override
    public void updateDashboard(String playerID, ArrayList<String> topDevCards, HashMap<Resource, Integer> strongBox, ArrayList<DepotState> warehouse) {
        PlayerState playerState = playerStates.get(playerID);

        for (int i = 0; i < Math.min(topDevCards.size(), 3); i++) {
            String dcID = topDevCards.get(i);
            ArrayList<String> slot = playerState.ownedCards.get(i);
            if (!slot.get(slot.size() - 1).equals(dcID))
                slot.add(dcID);
        }

        playerState.strongBox = new HashMap<>(strongBox);

        playerState.warehouse = new ArrayList<>(warehouse);

    }

    @Override
    public void updateLeaderCardsState(String playerID, HashMap<String, Boolean> leaderCards) {
        PlayerState playerState = playerStates.get(playerID);
        playerState.leaderCards = new HashMap<>(leaderCards);
        for (String lcID : leaderCards.keySet())
            playerState.leaderPowerStates.putIfAbsent(lcID, new ArrayList<>());
    }

    @Override
    public void updateMarket(int rows, int cols, Marble[][] marketStatus, Marble marbleLeft) {
        PlayerState.marketStatus = new Pair<>(marketStatus.clone(), marbleLeft);
    }

    @Override
    public void updateDevCardGrid(String[][] topDevCardIDs) {
        PlayerState.devCardGrid = topDevCardIDs.clone();
    }

    @Override
    public BuyResourcesEvent askForMarketRow() {
        PlayerState.availableActions = new ArrayList<>();

        PlayerState.availableActions.add(Action.TAKE_RESOURCES_FROM_MARKET);

        PlayerState.canPerformActions = true;
        BuyResourcesEvent e = (BuyResourcesEvent) thisPlayerState().event.getWaitIfLocked();
        thisPlayerState().event.setItem(null);
        //TODO this is problematic because potentially a user could do more than one action if he's fast enough. Should be done by each controller before setting event
        PlayerState.canPerformActions = false;

        return e;
    }

    @Override
    public BuyDevCardsEvent askForDevCard() {
        PlayerState.availableActions = new ArrayList<>();
        PlayerState.availableActions.add(Action.BUY_DEVCARD);

        PlayerState.canPerformActions = true;
        BuyDevCardsEvent e = (BuyDevCardsEvent) thisPlayerState().event.getWaitIfLocked();
        thisPlayerState().event.setItem(null);
        //TODO this is problematic because potentially a user could do more than one action if he's fast enough. Should be done by each controller before setting event
        PlayerState.canPerformActions = false;

        return e;
    }

    @Override
    public ActivateProductionEvent askForProductionPowersToUse() {
        PlayerState.availableActions = new ArrayList<>();
        PlayerState.availableActions.add(Action.PRODUCE);

        PlayerState.canPerformActions = true;
        ActivateProductionEvent e = (ActivateProductionEvent) thisPlayerState().event.getWaitIfLocked();
        thisPlayerState().event.setItem(null);
        //TODO this is problematic because potentially a user could do more than one action if he's fast enough. Should be done by each controller before setting event
        PlayerState.canPerformActions = false;

        return e;
    }

    //TODO missing a way to differentiate it with activate
    @Override
    public String askForLeaderCardToDiscard() throws NotPresentException {
        if (!thisPlayerState().leaderCards.containsValue(false))
            throw new NotPresentException("No leader card can be discarded");
        PlayerState.availableActions = new ArrayList<>();
        PlayerState.availableActions.add(Action.LEADER_ACTION);

        PlayerState.canPerformActions = true;
        String leaderCardID = ((DiscardLeaderCardEvent) thisPlayerState().event.getWaitIfLocked()).getLeaderCardID();
        thisPlayerState().event.setItem(null);
        //TODO this is problematic because potentially a user could do more than one action if he's fast enough. Should be done by each controller before setting event
        PlayerState.canPerformActions = false;

        return leaderCardID;
    }

    //TODO missing a way to differentiate it with discard
    @Override
    public String askForLeaderCardToActivate() throws NotPresentException {
        if (!thisPlayerState().leaderCards.containsValue(false))
            throw new NotPresentException("No leader card can be activated");
        PlayerState.availableActions = new ArrayList<>();
        PlayerState.availableActions.add(Action.LEADER_ACTION);

        PlayerState.canPerformActions = true;
        String leaderCardID = ((ActivateLeaderCardEvent) thisPlayerState().event.getWaitIfLocked()).getLeaderCardID();
        thisPlayerState().event.setItem(null);
        //TODO this is problematic because potentially a user could do more than one action if he's fast enough. Should be done by each controller before setting event
        PlayerState.canPerformActions = false;

        return leaderCardID;
    }

    @Override
    public ArrayList<LeaderPowerSelectStateEvent> askForLeaderCardToSelectOrDeselect() throws NotPresentException {
        ArrayList<LeaderPowerSelectStateEvent> events = new ArrayList<>();
        if (!thisPlayerState().leaderCards.containsValue(true))
            throw new NotPresentException("No leader card is active");
        PlayerState.availableActions = new ArrayList<>();
        PlayerState.availableActions.add(Action.SELECT_LEADER_CARD);

        PlayerState.canPerformActions = true;
        events.add((LeaderPowerSelectStateEvent) thisPlayerState().event.getWaitIfLocked());
        thisPlayerState().event.setItem(null);
        //TODO this is problematic because potentially a user could do more than one action if he's fast enough. Should be done by each controller before setting event
        PlayerState.canPerformActions = false;
        return events;
    }

    @Override
    public ArrayList<Event> askForNextAction(String playerID, boolean lastRound, TurnState turnState) {
        ArrayList<Event> events = new ArrayList<>();
        PlayerState.availableActions = new ArrayList<>();
        if (playerID.equals(this.playerID.getItem()))
            return events;
        switch (turnState) {
            case START -> {
                PlayerState.availableActions.add(Action.TAKE_RESOURCES_FROM_MARKET);
                PlayerState.availableActions.add(Action.BUY_DEVCARD);
                PlayerState.availableActions.add(Action.PRODUCE);
                PlayerState.availableActions.add(Action.LEADER_ACTION);
                PlayerState.availableActions.add(Action.SELECT_LEADER_CARD);
            }
            case AFTER_LEADER_CARD_ACTION -> {
                PlayerState.availableActions.add(Action.TAKE_RESOURCES_FROM_MARKET);
                PlayerState.availableActions.add(Action.BUY_DEVCARD);
                PlayerState.availableActions.add(Action.PRODUCE);
                PlayerState.availableActions.add(Action.SELECT_LEADER_CARD);
            }
            case AFTER_MAIN_ACTION -> {
                PlayerState.availableActions.add(Action.LEADER_ACTION);
                PlayerState.availableActions.add(Action.END_TURN);
            }
            case END_OF_TURN -> {
                PlayerState.availableActions.add(Action.END_TURN);
            }
        }

        PlayerState.canPerformActions = true;
        //null is the lockingState
        events.add(thisPlayerState().event.getWaitIfLocked());
        thisPlayerState().event.setItem(null);
        //TODO this is problematic because potentially a user could do more than one action if he's fast enough. Should be done by each controller before setting event
        PlayerState.canPerformActions = false;


        return events;
    }


    @Override
    public void updateLeaderCardDepositState(String playerID, String leaderCardID, int leaderPowerIndex, HashMap<Resource, Integer> storedResources) {
        playerStates.get(playerID).updateLeaderCardDepositState(leaderCardID, leaderPowerIndex, storedResources);
    }

    @Override
    public void updateLeaderPowersSelectedState(String playerID, String leaderCardID, ArrayList<Boolean> powerSelectedStates) {
        playerStates.get(playerID).leaderPowerStates.put(leaderCardID, new ArrayList<>(powerSelectedStates));
    }

    //TODO
    @Override
    public void displayEndOfGame(ArrayList<FinalPlayerState> finalPlayerStates) {

    }

    //TODO
    @Override
    public NewResourcesOrganizationEvent getWarehouseDisplacement(HashMap<Resource, Integer> resources) {

        return null;
    }

    //TODO
    @Override
    public ChosenResourcesEvent askWhereToTakeResourcesFrom(HashMap<Resource, Integer> required, int freeChoicesResources) {
        return null;
    }

    //TODO
    @Override
    public HashMap<Resource, Integer> chooseResources(int requiredResourcesOFChoice, ArrayList<Resource> allowedResourcesTypes) {
        return null;
    }

    //TODO
    @Override
    public void displayIAAction(SoloActionToken action) {

    }

    //TODO
    @Override
    public void displaySinglePlayerLost() {

    }

    //TODO
    @Override
    public void updateLorenzoPosition(int position) {

    }
}
