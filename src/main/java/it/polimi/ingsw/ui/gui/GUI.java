package it.polimi.ingsw.ui.gui;

import it.polimi.ingsw.events.ClientEvents.DepotState;
import it.polimi.ingsw.events.ClientEvents.FinalPlayerState;
import it.polimi.ingsw.events.ControllerEvents.MatchEvents.*;
import it.polimi.ingsw.events.Event;
import it.polimi.ingsw.exceptions.NotPresentException;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.DevCards.DevCardGrid;
import it.polimi.ingsw.model.FaithTrack.PopeFavorCard;
import it.polimi.ingsw.model.LeaderCards.LeaderCard;
import it.polimi.ingsw.model.singlePlayer.SoloActionToken;
import it.polimi.ingsw.ui.UI;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;


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


    private String PlayerImage;

    ServerSettingsController serverSettingsController;
    LoginController loginController;
    SplashScreenController splashScreenController;
    LobbyController lobbyController;
    MainViewController mainViewController;

    HashMap<String, PlayerState> playerStates;

    public PlayerState thisPlayerState(){
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
                        add(new DepotState(Resource.ROCK, 3, 3));

                    }};

                    aux.thisPlayerState().ownedCards.get(0).add("DevCard1");
                    aux.thisPlayerState().ownedCards.get(0).add("DevCard23");
                    aux.thisPlayerState().ownedCards.get(1).add("DevCard4");
                    aux.thisPlayerState().ownedCards.get(2).add("DevCard8");
                    aux.thisPlayerState().ownedCards.get(2).add("DevCard26");
                    aux.thisPlayerState().ownedCards.get(2).add("DevCard35");

                    aux.thisPlayerState().leaderCards.add("LeaderCard15");
                    aux.thisPlayerState().leaderDepots = new HashMap<>(){{
                        put(Resource.COIN, 2);
                        put(Resource.SERVANT, 1);
                    }};
                    aux.thisPlayerState().warehouse = warehouse;
                    aux.thisPlayerState().strongBox = strongBox;
                    aux.thisPlayerState().faithTrackPoints = 4;
                    aux.thisPlayerState().victoryPoints = 15;

                    int rows = 3; int cols = 4;
                    PlayerState.devCardGrid = new String[rows][cols];
                    for(int i = 0; i < rows; i++)
                    {
                        for(int j = 0; j < cols; j++)
                        {
                            int level = i*16+1;
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
                    Market market = new Market(4, 3, marbles);
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
        if (event instanceof BuyResourcesEvent) {
            actionPerformed.setItem(Action.MARKET_ACTION);
        } else if (event instanceof BuyDevCardsEvent) {
            actionPerformed.setItem(Action.DEV_CARD_ACTION);
        } else if (event instanceof ActivateProductionEvent) {
            actionPerformed.setItem(Action.PRODUCTION_ACTION);
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

    @Override
    public void displayWaitingForPlayerToSetupState(String playerID) {

    }

    @Override
    public void initializeMatchObjects() {

    }

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
        //Direction dir = playerInfo.getBoughtResourcesInfo().getKey();
        //Integer index = playerInfo.getBoughtResourcesInfo().getValue();
        //return new BuyResourcesEvent(playerID.getItem(), dir, index);

        return null;
    }

    @Override
    public BuyDevCardsEvent askForDevCard() {
        //String devCardId = playerInfo.getBuyDevCardInfo().substring(0, playerInfo.getBuyDevCardInfo().indexOf(":"));
        //int cardSlot = Integer.parseInt(playerInfo.getBuyDevCardInfo().substring(playerInfo.getBuyDevCardInfo().indexOf(":")));
        //return new BuyDevCardsEvent(playerID.getItem(), devCardId, cardSlot);

        return null;
    }

    @Override
    public ActivateProductionEvent askForProductionPowersToUse() {
        //var devCards = playerInfo.getProdPowerDevCards();
        //var personalPower = playerInfo.isActivatePersonalPower();
        //return new ActivateProductionEvent(playerID.getItem(), devCards, personalPower);
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
    public ArrayList<Event> askForNextAction(String playerID, boolean lastRound, TurnState turnState) {

        ArrayList<Event> events = new ArrayList<>();
        if (playerID.equals(this.playerID.getItem()))
            return events;
        Action a;
        a = actionPerformed.getWaitIfLocked();
//        return switch (a) {
//            case MARKET_ACTION -> askForMarketRow();
//            case DEV_CARD_ACTION -> askForDevCard();
//            case PRODUCTION_ACTION -> askForProductionPowersToUse();
//        switch (a) {
//            case MARKET_ACTION -> {
//                Direction dir = playerInfo.getBoughtResourcesInfo().getKey();
//                Integer index = playerInfo.getBoughtResourcesInfo().getValue();
//                events.add(new BuyResourcesEvent(playerInfo.getPlayerID(), dir, index));
//                break;
//            }
//            case DEV_CARD_ACTION -> {
//                String devCardId = playerInfo.getBuyDevCardInfo().substring(0, playerInfo.getBuyDevCardInfo().indexOf(":"));
//                int cardSlot = Integer.parseInt(playerInfo.getBuyDevCardInfo().substring(playerInfo.getBuyDevCardInfo().indexOf(":")));
//                events.add( new BuyDevCardsEvent(playerInfo.getPlayerID(), devCardId, cardSlot));
//                break;
//            }
//            case PRODUCTION_ACTION -> {
//                var devCards = playerInfo.getProdPowerDevCards();
//                var personalPower = playerInfo.isActivatePersonalPower();
//                events.add( new ActivateProductionEvent(playerInfo.getPlayerID(), devCards, personalPower));
//                break;
//            }
//        };
        return events;
    }


    @Override
    public void updateLeaderCardDepositState(String playerID, String leaderCardID, int leaderPowerIndex, HashMap<Resource, Integer> storedResources) {

    }

    @Override
    public void updateLeaderPowersSelectedState(String playerId, String leaderCardID, ArrayList<Boolean> powerSelectedStates) {

    }

    @Override
    public void displayEndOfGame(ArrayList<FinalPlayerState> finalPlayerStates) {

    }

    @Override
    public NewResourcesOrganizationEvent getWarehouseDisplacement(HashMap<Resource, Integer> resources) {

        return null;
    }

    @Override
    public ChosenResourcesEvent askWhereToTakeResourcesFrom(HashMap<Resource, Integer> required, int freeChoicesResources) {
        return null;
    }

    @Override
    public HashMap<Resource, Integer> chooseResources(int requiredResourcesOFChoice, ArrayList<Resource> allowedResourcesTypes) {
        return null;
    }

    @Override
    public void displayIAAction(SoloActionToken action) {

    }

    @Override
    public void displaySinglePlayerLost() {

    }

    @Override
    public void updateLorenzoPosition(int position) {

    }
}
