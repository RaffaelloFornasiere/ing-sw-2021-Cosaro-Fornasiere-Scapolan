package it.polimi.ingsw.ui.gui;

import it.polimi.ingsw.events.Event;
import it.polimi.ingsw.events.clientEvents.DepotState;
import it.polimi.ingsw.events.clientEvents.FinalPlayerState;
import it.polimi.ingsw.events.controllerEvents.matchEvents.*;
import it.polimi.ingsw.model.Marble;
import it.polimi.ingsw.model.ProductionPower;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.model.TurnState;
import it.polimi.ingsw.model.faithTrack.PopeFavorCard;
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
    protected final LockWrap<String> leaderID = new LockWrap<>(null);
    protected final LockWrap<String> playerID = new LockWrap<>(null);
    protected final LockWrap<InetAddress> serverAddress = new LockWrap<>(null);
    protected final LockWrap<Integer> serverPort = new LockWrap<>(null);
    protected final LockWrap<Boolean> singlePlayer = new LockWrap<>(null);
    protected String playerImage;


    LobbyController lobbyController;
    MainViewController mainViewController;
    HashMap<String, PlayerState> playerStates;


    // primary stage, this mustn't to be deleted
    Stage stage;


    /**
     * @return the reference to the player state of the user using the gui
     */
    public PlayerState thisPlayerState() {
        return playerStates.get(playerID.getItem());
    }

    /**
     *
     */
    public synchronized void close() {
        System.exit(0);
    }


    /**
     * constructor:
     * launches the javafx application and wait it gets ready
     * one the application is read sets the splashscreen scene
     */
    public GUI() {

        GUI aux = this;
        new Thread() {
            @Override
            public void run() {
                super.run();
                MainApplication.setGui(aux);
                Application.launch(MainApplication.class);

            }
        }.start();

        if (MainApplication.isReady())
            stage = MainApplication.getStage();

        playerStates = new HashMap<>();

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


        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    /**
     * method used by controllers to set the next event on queue.
     * In addition, resets the array of actions that can be performed
     * and sets to false the variable that allows controllers to perform actions
     *
     * @param event event to put in queue
     */
    public synchronized void addEvent(Event event) {

        if (event instanceof it.polimi.ingsw.events.controllerEvents.matchEvents.BuyResourcesEvent) {
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
        PlayerState.canPerformActions = false;
        PlayerState.availableActions.clear();
        thisPlayerState().event.waitIfSet();
        thisPlayerState().event.setItem(event);
    }

    /**
     * @return the player image url
     */
    public String getPlayerImage() {
        return playerImage;
    }

    /**
     * sets the player image url
     *
     * @param playerImage image url
     */
    public void setPlayerImage(String playerImage) {
        this.playerImage = playerImage;
    }


    /**
     * setter. if these are wrong the user receives an error window
     *
     * @param playerID string that identifies the user
     * @param leaderID string that identifies the leader
     */
    public void setLoginData(String playerID, String leaderID) {

        this.playerID.setItem(playerID);
        this.leaderID.setItem(leaderID);
        playerStates.put(this.playerID.getItem(), new PlayerState());
    }

    /**
     * setter
     *
     * @param serverAddress the address of the server to connect
     */
    public void setServerAddress(InetAddress serverAddress) {
        this.serverAddress.setItem(serverAddress);
    }

    /**
     * setter
     *
     * @param serverPort the port of the server to connect
     */
    public void setServerPort(int serverPort) {
        this.serverPort.setItem(serverPort);
    }


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


    @Override
    public boolean askSingleplayer() {
        return singlePlayer.getWaitIfLocked();
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

    @Override
    public void displayLobbyState(String leaderID, ArrayList<String> otherPLayersID) {
        otherPLayersID.add(leaderID);
        GUI aux = this;
        Platform.runLater(() -> {
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
        });

    }

    void startGame() {
        client.startMatch();
    }

    @Override
    public void displayWaitingForPlayerToSetupState(String playerID) {

    }

    @Override
    public void initializeMatchObjects() {
        mainViewController = new MainViewController(this);
        Platform.runLater(() -> {
            try {
                Scene scene = MainApplication.createScene("MainView.fxml", mainViewController);
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        //System.out.println("waiting for mainViewController get ready");
        mainViewController.waitForReady();
        System.out.println("ready");

        if (!singlePlayer.getItem())
            for (String playerID : lobbyController.getPlayers()) {
                playerStates.put(playerID, new PlayerState());
            }
    }

    @Override
    public ArrayList<String> choseInitialLeaderCards(ArrayList<String> leaderCardsIDs, int numberOFLeaderCardsToChose) {

        ArrayList<String> res;
        SelectLeaderCardsController controller = new SelectLeaderCardsController(this, leaderCardsIDs, numberOFLeaderCardsToChose);
        Platform.runLater(() -> {
            FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("SelectLeaderCards.fxml"));
            loader.setController(controller);
            try {
                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                Scene scene = new Scene(loader.load());
                stage.setScene(scene);
                stage.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        res = controller.getSelected();
        return res;
    }


    @Override
    public void setPersonalProductionPower(String playerId, ProductionPower personalProductionPower) {
        playerStates.get(playerId).personalProductionPower = personalProductionPower;
    }


    @Override
    public void updateFaithTrack(String playerID, int position,
                                 HashMap<String, HashMap<Integer, PopeFavorCard>> popeFavorCards) {
        PlayerState playerState = playerStates.get(playerID);
        playerState.setFaithTrackPosition(position);
        playerState.setPopeFavorCards(popeFavorCards.get(playerID));

        Platform.runLater(() -> {
            if (playerID.equals(this.playerID.getItem())) {
                mainViewController.setFaithTrackPosition(position);
                mainViewController.faithTrackController.getItem().updateFavorCards(false);
            } else {
                mainViewController.faithTrackController.getItem().updateFavorCards(true);
            }
        });
    }

    @Override
    public void updateDashboard(String playerID, ArrayList<String> topDevCards, HashMap<Resource, Integer> strongBox, ArrayList<DepotState> warehouse) {
        PlayerState playerState = playerStates.get(playerID);
        for (int i = 0; i < Math.min(topDevCards.size(), 3); i++) {
            String devCardID = topDevCards.get(i);
            if (devCardID == null)
                continue;
            ArrayList<String> slot = playerState.ownedCards.get(i);
            if (slot.size() == 0 || !slot.get(slot.size() - 1).equals(devCardID))
                slot.add(devCardID);
        }
        playerState.strongBox = new HashMap<>(strongBox);
        playerState.warehouse = new ArrayList<>(warehouse);
        Platform.runLater(() -> {
            if (playerID.equals(this.playerID.getItem())) {
                mainViewController.updateAll();
            }
        });

    }

    @Override
    public void updateLeaderCardsState(String playerID, HashMap<String, Boolean> leaderCards) {
        PlayerState playerState = playerStates.get(playerID);
        playerState.leaderCards = new HashMap<>(leaderCards);
        for (String lcID : leaderCards.keySet())
            playerState.leaderPowerStates.putIfAbsent(lcID, new ArrayList<>());
        Platform.runLater(() -> {
            if (playerID.equals(this.playerID.getItem())) {
                mainViewController.updateAll();
            }
        });
    }

    @Override
    public void updateMarket(int rows, int cols, Marble[][] marketStatus, Marble marbleLeft) {
        PlayerState.marketStatus = new Pair<>(marketStatus.clone(), marbleLeft);
        Platform.runLater(() -> mainViewController.updateMarket());
    }

    @Override
    public void updateDevCardGrid(String[][] topDevCardIDs) {
        PlayerState.devCardGrid = topDevCardIDs.clone();

    }


    @Override
    public ArrayList<Event> askForNextAction(String playerID, boolean lastRound, TurnState turnState) {
        ArrayList<Event> events = new ArrayList<>();
        PlayerState.availableActions = new ArrayList<>();
        if (!playerID.equals(this.playerID.getItem())) {
            Platform.runLater(() -> mainViewController.setTurnActive(false));
            return events;
        }
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
            case END_OF_TURN -> PlayerState.availableActions.add(Action.END_TURN);
        }
        Platform.runLater(() -> mainViewController.setTurnActive(true));
        PlayerState.canPerformActions = true;

        System.out.println("locking events");
        //null is the lockingState
        thisPlayerState().event.setItem(null);
        events.add(thisPlayerState().event.getWaitIfLocked());
        PlayerState.canPerformActions = false;


        return events;
    }


    @Override
    public void updateLeaderCardDepositState(String playerID, String leaderCardID, int leaderPowerIndex, HashMap<
            Resource, Integer> storedResources) {
        playerStates.get(playerID).updateLeaderCardDepositState(leaderCardID, leaderPowerIndex, storedResources);
        Platform.runLater(() -> {
            if (playerID.equals(this.playerID.getItem())) {
                mainViewController.updateDepots();
            }
        });
    }

    @Override
    public void updateLeaderPowersSelectedState(String playerID, String
            leaderCardID, ArrayList<Boolean> powerSelectedStates) {
        playerStates.get(playerID).leaderPowerStates.put(leaderCardID, new ArrayList<>(powerSelectedStates));

    }


    @Override
    public void displayEndOfGame(ArrayList<FinalPlayerState> finalPlayerStates) {
        System.out.println("gui game end");

        FinalScreenController controller = new FinalScreenController(this, singlePlayer.getItem(), singlePlayer.getItem() ? finalPlayerStates : null);
        Platform.runLater(() -> {
            FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("FinalScreen.fxml"));
            loader.setController(controller);
            try {
                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                Scene scene = new Scene(loader.load());
                stage.setScene(scene);
                stage.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        PlayerState.canPerformActions = true;
    }


    @Override
    public NewResourcesOrganizationEvent getWarehouseDisplacement(HashMap<Resource, Integer> resources) {
        //System.out.println(resources);
        //resources.entrySet().stream().map(n -> new Pair(n.getKey(), n.getValue())).forEach(System.out::print);
        WarehouseController controller = new WarehouseController(this, resources);
        Platform.runLater(() -> {
            FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("Warehouse.fxml"));
            loader.setController(controller);
            try {
                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                Scene scene = new Scene(loader.load());
                stage.setScene(scene);
                stage.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        PlayerState.canPerformActions = true;
        thisPlayerState().event.setItem(null);
        return (NewResourcesOrganizationEvent) thisPlayerState().event.getWaitIfLocked();
    }


    @Override
    public ChosenResourcesEvent askWhereToTakeResourcesFrom(HashMap<Resource, Integer> required,
                                                            int freeChoicesResources) {

        SelectResourcesController controller = new SelectResourcesController(this, required, freeChoicesResources);
        Platform.runLater(() -> {
            FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("SelectResourcesToUse.fxml"));
            loader.setController(controller);
            try {
                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                Scene scene = new Scene(loader.load());
                stage.setScene(scene);
                stage.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        PlayerState.canPerformActions = true;
        return controller.getResult();
    }

    @Override
    public HashMap<Resource, Integer> chooseResources(int requiredResourcesOFChoice, ArrayList<
            Resource> allowedResourcesTypes) {

        HashMap<Resource, Integer> res = new HashMap<>();
        for (Resource r : allowedResourcesTypes) {
            res.put(r, 0);
        }
        if (requiredResourcesOFChoice <= 0) return res;

        ChoseResourcesController controller = new ChoseResourcesController(this, allowedResourcesTypes, requiredResourcesOFChoice);
        Platform.runLater(() -> {
            FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("ChoseResources.fxml"));
            loader.setController(controller);
            try {
                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                Scene scene = new Scene(loader.load());
                stage.setScene(scene);
                stage.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        res = controller.getChosen();
        return res;
    }


    @Override
    public void displaySinglePlayerLost() {
        FinalScreenController controller = new FinalScreenController(this, true, null);

        Platform.runLater(() -> {
            FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("FinalScreen.fxml"));
            loader.setController(controller);
            try {
                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                Scene scene = new Scene(loader.load());
                stage.setScene(scene);
                stage.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        PlayerState.canPerformActions = true;
    }

    @Override
    public void updateLorenzoPosition(int position) {
        //System.out.println(position);
        Platform.runLater(() -> mainViewController.setLorenzoFaithTrackPosition(position));
    }

    @Override
    public void displayIAAction(SoloActionToken action) {
        DisplayIAActionController controller = new DisplayIAActionController(this, action);

        Platform.runLater(() -> {
            FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("DisplayIAAction.fxml"));
            loader.setController(controller);
            try {
                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                Scene scene = new Scene(loader.load());
                stage.setScene(scene);
                stage.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
