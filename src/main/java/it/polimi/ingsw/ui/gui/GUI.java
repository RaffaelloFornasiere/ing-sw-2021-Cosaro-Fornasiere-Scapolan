package it.polimi.ingsw.ui.gui;

import it.polimi.ingsw.events.ClientEvents.DepotState;
import it.polimi.ingsw.events.ClientEvents.FinalPlayerState;
import it.polimi.ingsw.events.ControllerEvents.MatchEvents.*;
import it.polimi.ingsw.events.Event;
import it.polimi.ingsw.exceptions.NotPresentException;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.FaithTrack.PopeFavorCard;
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

    public synchronized void close() {
        System.exit(0);
    }

    Stage stage;

    public GUI() {

        serverSettingsController = new ServerSettingsController(this);
        loginController = new LoginController(this);
        splashScreenController = new SplashScreenController(this);
        //


        new Thread() {
            @Override
            public void run() {
                super.run();
                Application.launch(MainApplication.class);

            }
        }.start();

        if (MainApplication.isReady())
            stage = MainApplication.getStage();

        playerStates = new HashMap<>();
        var aux = this;
        try {
            TimeUnit.SECONDS.sleep(1);

            Platform.runLater(() -> {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("Splashscreen.fxml"));
                    Controller controller = new SplashScreenController(aux);//, allowed, 2);
                    fxmlLoader.setController(controller);
                    stage.setScene(new Scene(fxmlLoader.load()));
                    stage.show();

                } catch (IOException e) {
                    e.printStackTrace();
                }

            });



//            playerID.setItem("paolo");
//            playerStates.put(playerID.getItem(), new PlayerState());
//
//            HashMap<Resource, Integer> strongBox = new HashMap<>() {{
//                put(Resource.COIN, 4);
//                put(Resource.SHIELD, 2);
//                put(Resource.SERVANT, 5);
//                put(Resource.ROCK, 3);
//            }};
//            ArrayList<DepotState> warehouse = new ArrayList<>() {{
//                add(new DepotState(Resource.COIN, 1, 1));
//                add(new DepotState(Resource.SHIELD, 2, 2));
//                add(new DepotState(Resource.SERVANT, 3, 3));
//
//            }};
//
//            aux.thisPlayerState().ownedCards.get(0).add("DevCard1");
//            aux.thisPlayerState().ownedCards.get(0).add("DevCard23");
//            aux.thisPlayerState().ownedCards.get(1).add("DevCard4");
//            aux.thisPlayerState().ownedCards.get(2).add("DevCard8");
//            aux.thisPlayerState().ownedCards.get(2).add("DevCard26");
//            aux.thisPlayerState().ownedCards.get(2).add("DevCard35");
//
//            aux.thisPlayerState().leaderCards.put("LeaderCard5", true);
//            aux.thisPlayerState().leaderCards.put("LeaderCard6", false);
//            aux.thisPlayerState().leaderCards.put("LeaderCard7", false);
//            aux.thisPlayerState().leaderCards.put("LeaderCard8", false);
//
//
//
//
//            aux.thisPlayerState().updateLeaderCardDepositState("LeaderCard5", 0,
//                    new HashMap<>() {{
//                        put(Resource.ROCK, 1);
//                    }});
//            aux.thisPlayerState().leaderPowerStates.put("LeaderCard5", new ArrayList<>(){{add(true);}});
//
//            aux.thisPlayerState().warehouse = warehouse;
//            aux.thisPlayerState().strongBox = strongBox;
//            aux.thisPlayerState().faithTrackPoints = 4;
//            aux.thisPlayerState().victoryPoints = 15;
//            ArrayList<ArrayList<Integer>> indexes = new ArrayList<>();
//            for (int i = 0; i < 3; i++) {
//                int finalI = i;
//                indexes.add(new ArrayList<>() {{
//                    int base = (finalI) * 16;
//                    add(1 + base);
//                    add(2 + base);
//                    add(3 + base);
//                    add(4 + base);
//                }});
//            }
//
//            PlayerState.devCardGrid = new String[3][4];
//            for (int i = 0; i < PlayerState.devCardGrid.length; i++) {
//                for (int j = 0; j < PlayerState.devCardGrid[0].length; j++) {
//                    int index = indexes.get(i).get(j);
//                    PlayerState.devCardGrid[i][j] = "DevCard" + index;
//                }
//            }
//
//
//            int rows = 3;
//            int cols = 4;
//            PlayerState.devCardGrid = new String[rows][cols];
//            for (int i = 0; i < rows; i++) {
//                for (int j = 0; j < cols; j++) {
//                    int level = i * 16 + 1;
//                    PlayerState.devCardGrid[i][j] = "DevCard" + (level + j);
//                    //System.out.println(aux.playerState.devCardGrid[i][j]);
//                }
//            }
//            HashMap<Marble, Integer> marbles = new HashMap<>() {{
//                put(Marble.GRAY, 2);
//                put(Marble.YELLOW, 2);
//                put(Marble.PURPLE, 2);
//                put(Marble.BLUE, 2);
//                put(Marble.WHITE, 4);
//                put(Marble.RED, 1);
//            }};
//            Market market = new Market(3, 4, marbles);
//            PlayerState.marketStatus = new Pair<>(market.getMarketStatus(), market.getMarbleLeft());
//            HashMap<Resource, Integer> resources = new HashMap<>(){{
//                put(Resource.COIN, 4);
//                put(Resource.ROCK, 2);
//                put(Resource.SERVANT, 3);
//                put(Resource.SHIELD, 1);
//
//            }};
//
//
//
//            WarehouseController controller = new WarehouseController(this, resources);
//            Platform.runLater(() -> {
//                FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("Warehouse.fxml"));
//                loader.setController(controller);
//                try {
//                    Stage stage = new Stage();
//                    stage.initModality(Modality.APPLICATION_MODAL);
//                    Scene scene = null;
//                    scene = new Scene(loader.load());
//                    stage.setScene(scene);
//                    stage.showAndWait();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            });
//            PlayerState.canPerformActions = true;

//
//            ArrayList<String> res;
//            ArrayList<String> leaderCardsIDs = new ArrayList<>() {{
//                add("LeaderCard1");
//                add("LeaderCard6");
//                add("LeaderCard11");
//                add("LeaderCard12");
//            }};
//            SelectLeaderCardsController controller = new SelectLeaderCardsController(this, leaderCardsIDs, 2);
//            Platform.runLater(new Runnable() {
//                @Override
//                public void run() {
//                    FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("SelectLeaderCards.fxml"));
//                    loader.setController(controller);
//                    Scene scene = null;
//                    try {
//                        scene = new Scene(loader.load());
//                        stage.setScene(scene);
//                        stage.show();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            });
//            System.out.println("wating for res");
//            res = controller.getSelected();
//            System.out.println("Res arrived: ");
//            res.forEach(System.out::print);
//

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
        if (event instanceof BuyResourcesEvent) {
            PlayerState.availableActions.remove(Action.TAKE_RESOURCES_FROM_MARKET);
        } else if (event instanceof BuyDevCardsEvent) {
            PlayerState.availableActions.remove(Action.BUY_DEVCARD);
        } else if (event instanceof LeaderPowerSelectStateEvent) {
            PlayerState.availableActions.remove(Action.SELECT_LEADER_CARD);
        } else if (event instanceof ActivateProductionEvent) {
            PlayerState.availableActions.remove(Action.PRODUCE);
        } else if (event instanceof ActivateLeaderCardEvent) {
            PlayerState.availableActions.remove(Action.LEADER_ACTION);
        } else if (event instanceof DiscardLeaderCardEvent) {
            PlayerState.availableActions.remove(Action.LEADER_ACTION);
        } else if (event instanceof EndTurnEvent) {
            PlayerState.availableActions.remove(Action.END_TURN);
        }
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
        playerStates.put(this.playerID.getItem(), new PlayerState());
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
        GUI aux = this;
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
            if (message.contains("IP")) {
                aux.serverAddress.setItem(null);
                Controller controller = new ServerSettingsController(aux);
                try {
                    stage.setScene(MainApplication.createScene("ServerSettings.fxml", controller));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            alert.showAndWait();
        });
    }

    @Override
    public void printError(String error) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR, error, ButtonType.OK);
            alert.showAndWait();
        });
    }

    @Override
    public void printWarning(String warning) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.WARNING, warning, ButtonType.OK);
            alert.showAndWait();
        });
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
        return leaderID.getWaitIfLocked().equals("");
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
        otherPLayersID.add(leaderID);
        GUI aux = this;
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if (lobbyController != null) {
                    lobbyController.updatePlayerList(leaderID, otherPLayersID);
                } else {
                    lobbyController = new LobbyController(aux, leaderID, otherPLayersID,
                            aux.playerID.getItem().equals(leaderID));
                    try {
                        Scene scene = MainApplication.createScene("Lobby.fxml", lobbyController);
                        stage.setScene(scene);
                        stage.show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }


                for (
                        var player : otherPLayersID) {
                    playerStates.put(player, new PlayerState());
                }
            }
        });

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
        mainViewController = new MainViewController(this);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                try {
                    Scene scene = MainApplication.createScene("MainView.fxml", mainViewController);
                    stage.setScene(scene);
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        });
        mainViewController.waitForReady();
        System.out.println("ready");


        for (String playerID : lobbyController.getPlayers()) {
            playerStates.put(playerID, new PlayerState());
        }
    }

    //TODO?
    @Override
    public ArrayList<String> choseInitialLeaderCards(ArrayList<String> leaderCardsIDs, int numberOFLeaderCardsToChose) {

        ArrayList<String> res;
        SelectLeaderCardsController controller = new SelectLeaderCardsController(this, leaderCardsIDs, numberOFLeaderCardsToChose);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("SelectLeaderCards.fxml"));
                loader.setController(controller);
                Scene scene = null;
                try {
                    Stage stage = new Stage();
                    stage.initModality(Modality.APPLICATION_MODAL);
                    scene = new Scene(loader.load());
                    stage.setScene(scene);
                    stage.showAndWait();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        System.out.println("wating for res");
        res = controller.getSelected();
        System.out.println("Res arrived: ");
        res.forEach(System.out::print);
        return res;
    }

    //TODO
    @Override
    public HashMap<Resource, Integer> choseResources(ArrayList<Resource> resourceType, int numberOFResources) {

        HashMap<Resource, Integer> res = new HashMap<>();
        for (Resource r : resourceType) {
            res.put(r, 0);
        }
        if (numberOFResources <= 0) return res;

        ChoseResourcesController controller = new ChoseResourcesController(this, resourceType, numberOFResources);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("ChoseResources.fxml"));
                loader.setController(controller);
                try {
                    Stage stage = new Stage();
                    stage.initModality(Modality.APPLICATION_MODAL);
                    Scene scene = null;
                    scene = new Scene(loader.load());
                    stage.setScene(scene);
                    stage.showAndWait();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        System.out.println("wating for res");
        res = controller.getChosen();
        System.out.println("Res arrived: ");
        res.entrySet().stream().map(n -> new Pair(n.getKey(), n.getValue())).forEach(System.out::print);
        return res;
    }

    @Override
    public void setPersonalProductionPower(String playerId, ProductionPower personalProductionPower) {
        playerStates.get(playerId).personalProductionPower = personalProductionPower;
    }

    @Override
    public void updateFaithTrack(String playerID, int position, HashMap<
            String, HashMap<Integer, PopeFavorCard>> popeFavorCards) {
        PlayerState playerState = playerStates.get(playerID);
        playerState.setFaithTrackPosition(position);
        playerState.setPopeFavorCards(popeFavorCards.get(playerID));

        if (playerID.equals(this.playerID.getItem())) {
            mainViewController.setFaithTrackPosition(position);
        }
    }

    @Override
    public void updateDashboard(String playerID, ArrayList<String> topDevCards, HashMap<Resource, Integer> strongBox, ArrayList<DepotState> warehouse) {
        PlayerState playerState = playerStates.get(playerID);
        for (int i = 0; i < Math.min(topDevCards.size(), 3); i++) {
            String devCardID = topDevCards.get(i);
            if (devCardID == null)
                continue;
            ArrayList<String> slot = playerState.ownedCards.get(i);
            if (!slot.get(slot.size() - 1).equals(devCardID))
                slot.add(devCardID);
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
        if (!playerID.equals(this.playerID.getItem()))
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
    public void updateLeaderCardDepositState(String playerID, String leaderCardID, int leaderPowerIndex, HashMap<
            Resource, Integer> storedResources) {
        playerStates.get(playerID).updateLeaderCardDepositState(leaderCardID, leaderPowerIndex, storedResources);
    }

    @Override
    public void updateLeaderPowersSelectedState(String playerID, String
            leaderCardID, ArrayList<Boolean> powerSelectedStates) {
        playerStates.get(playerID).leaderPowerStates.put(leaderCardID, new ArrayList<>(powerSelectedStates));
    }

    //TODO
    @Override
    public void displayEndOfGame(ArrayList<FinalPlayerState> finalPlayerStates) {

    }

    //TODO
    @Override
    public NewResourcesOrganizationEvent getWarehouseDisplacement(HashMap<Resource, Integer> resources) {
        WarehouseController controller = new WarehouseController(this, resources);
        Platform.runLater(() -> {
            FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("Warehouse.fxml"));
            loader.setController(controller);
            try {
                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                Scene scene = null;
                scene = new Scene(loader.load());
                stage.setScene(scene);
                stage.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        PlayerState.canPerformActions = true;
        return (NewResourcesOrganizationEvent) thisPlayerState().event.getWaitIfLocked();
    }

    //TODO
    @Override
    public ChosenResourcesEvent askWhereToTakeResourcesFrom(HashMap<Resource, Integer> required,
                                                            int freeChoicesResources) {

        return null;
    }

    //TODO
    @Override
    public HashMap<Resource, Integer> chooseResources(int requiredResourcesOFChoice, ArrayList<
            Resource> allowedResourcesTypes) {
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
