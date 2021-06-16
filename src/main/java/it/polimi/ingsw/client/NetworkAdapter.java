package it.polimi.ingsw.client;

import com.google.gson.GsonBuilder;
import it.polimi.ingsw.ClientApp;
import it.polimi.ingsw.controller.EventRegistry;
import it.polimi.ingsw.events.ClientEvents.*;
import it.polimi.ingsw.events.ControllerEvents.MatchEvents.*;
import it.polimi.ingsw.events.ControllerEvents.NewPlayerEvent;
import it.polimi.ingsw.events.ControllerEvents.StartMatchEvent;
import it.polimi.ingsw.events.Event;
import it.polimi.ingsw.model.Direction;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.ui.UI;
import org.reflections.Reflections;

import java.beans.PropertyChangeEvent;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.Socket;
import java.util.*;
import java.util.stream.Collectors;

public class NetworkAdapter {

    public static final int SERVER_PORT = 50885;
    NetworkHandlerReceiver receiver;
    Sender sender;
    Socket server;
    String playerID;
    UI view;

    public NetworkAdapter(InetAddress address, UI ui) throws IOException {
        connectToServer(address);
        view = ui;

        // method-event binding
        Reflections reflections = new Reflections("it.polimi.ingsw.events");

        Set<Class<? extends Event>> events = new HashSet<>(reflections.getSubTypesOf(ClientEvent.class));
        //events.addAll(reflections.getSubTypesOf(ControllerEvent.class));


        for (var event : events) {
            try {
                Method method = this.getClass().getMethod(event.getSimpleName() + "Handler",
                        PropertyChangeEvent.class);
                receiver.getEventRegistry().addPropertyChangeListener(event.getSimpleName(), x -> {
                    try {
                        method.invoke(this, x);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                });
            } catch (NoSuchMethodException e) {
                System.err.println("Missing handler for " + event.getSimpleName());
                e.printStackTrace();
            }

        }
    }

    public NetworkAdapter(UI ui, EventRegistry toController, EventRegistry toPlayer, String playerID){
        sender = new LocalSender(toController);
        server = null;
        this.playerID = playerID;
        view = ui;

        // method-event binding
        Reflections reflections = new Reflections("it.polimi.ingsw.events");

        Set<Class<? extends Event>> events = new HashSet<>(reflections.getSubTypesOf(ClientEvent.class));
        //events.addAll(reflections.getSubTypesOf(ControllerEvent.class));


        for (var event : events) {
            try {
                Method method = this.getClass().getMethod(event.getSimpleName() + "Handler",
                        PropertyChangeEvent.class);
                toPlayer.addPropertyChangeListener(event.getSimpleName(), x -> {
                    try {
                        method.invoke(this, x);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                });
            } catch (NoSuchMethodException e) {
                System.err.println("Missing handler for " + event.getSimpleName());
                e.printStackTrace();
            }

        }
    }


    public boolean connectToServer(InetAddress address) throws IOException {
        server = new Socket(address, SERVER_PORT);
        //server.setSoTimeout(3000);
        sender = new NetworkHandlerSender(server);
        receiver = new NetworkHandlerReceiver(server);
        new Thread(receiver::receive).start();
        Timer timer = new Timer();
        TimerTask heartbeat;
        heartbeat = new TimerTask() {
            @Override
            public void run() {
                ((NetworkHandlerSender)sender).sendData("heartbeat");
            }
        };
        //timer.scheduleAtFixedRate(heartbeat, 1000, 1000);
        return true;
    }

    private void send(Event e) {
        sender.sendObject(e);
    }


    public void enterMatch(String username, String lobbyLeaderID) {
        this.playerID = username;
        NewPlayerEvent event = new NewPlayerEvent(username, lobbyLeaderID);
        send(event);
    }

    public void createMatch(String username) {
        this.playerID = username;
        NewPlayerEvent event = new NewPlayerEvent(username, username);
        send(event);
    }

    public void startMatch() {
        send(new StartMatchEvent(playerID));
    }

    public void buyResources(Direction direction, int index) {
        BuyResourcesEvent event = new BuyResourcesEvent(playerID, direction, index);
        send(event);
    }

    public void buyDevCards(String cardID, int cardSlot) {
        BuyDevCardsEvent event = new BuyDevCardsEvent(playerID, cardID, cardSlot);
        send(event);
    }

    public void activateProduction(ArrayList<String> devCards, boolean personalPower) {
        ActivateProductionEvent event = new ActivateProductionEvent(playerID, devCards, personalPower);
        send(event);
    }

    public void activateLeaderCard(String leaderCardID) {
        ActivateLeaderCardEvent event = new ActivateLeaderCardEvent(playerID, leaderCardID);
        send(event);
    }

    public void sendSelectedResources(HashMap<Resource, Integer> resources) {

    }


    /*
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     *
     *  __    __       ___      .__   __.  _______   __       _______ .______      _______.
     * |  |  |  |     /   \     |  \ |  | |       \ |  |     |   ____||   _  \    /       |
     * |  |__|  |    /  ^  \    |   \|  | |  .--.  ||  |     |  |__   |  |_)  |  |   (----`
     * |   __   |   /  /_\  \   |  . `  | |  |  |  ||  |     |   __|  |      /    \   \
     * |  |  |  |  /  _____  \  |  |\   | |  '--'  ||  `----.|  |____ |  |\  \--.--)   |
     * |__|  |__| /__/     \__\ |__| \__| |_______/ |_______||_______|| _| `.___|_____/
     *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     */
    public synchronized void BadRequestEventHandler(PropertyChangeEvent evt) {
        BadRequestEvent event = (BadRequestEvent) evt.getNewValue();
        view.printError("Bad request");
        view.printError(event.getDescription());
        System.err.println("Cause: " + new GsonBuilder().setPrettyPrinting().create().toJson(event.getCause()));
    }

    public synchronized void CantAffordErrorHandler(PropertyChangeEvent evt) {
        CantAffordError event = (CantAffordError) evt.getNewValue();
        view.printWarning("You can't afford "+event.getDevCardID());
    }

    public synchronized void ChoseMultipleExtraResourcePowerEventHandler(PropertyChangeEvent evt) {
        ChoseMultipleExtraResourcePowerEvent event = (ChoseMultipleExtraResourcePowerEvent) evt.getNewValue();
        HashMap<Resource, Integer> chosen = view.chooseResources(event.getNumberOfResources(), event.getResourceTypes());
        send(new SimpleChosenResourcesEvent(event.getPlayerId(), chosen));
    }

    public synchronized void ChoseResourcesEventHandler(PropertyChangeEvent evt) {
        ChoseResourcesEvent event = (ChoseResourcesEvent) evt.getNewValue();
        send(view.askWhereToTakeResourcesFrom(event.getRequiredResources(), event.getRequiredResourcesOFChoice()));
    }

    public synchronized void DashBoardStateEventHandler(PropertyChangeEvent evt) {
        DashBoardStateEvent event = (DashBoardStateEvent) evt.getNewValue();
        view.updateDashboard(event.getPlayerId(), event.getTopDevCards(), event.getStrongBox(), event.getWarehouse());
    }

    public synchronized void DepositLeaderPowerStateEventHandler(PropertyChangeEvent evt) {
        DepositLeaderPowerStateEvent event = (DepositLeaderPowerStateEvent) evt.getNewValue();
        view.updateLeaderCardDepositState(event.getPlayerId(), event.getLeaderCardID(), event.getLeaderPowerIndex(), event.getStoredResources());
    }

    public synchronized void DevCardGridStateEventHandler(PropertyChangeEvent evt) {
        DevCardGridStateEvent event = (DevCardGridStateEvent) evt.getNewValue();
        view.updateDevCardGrid(event.getTopDevCardIDs());
    }

    public synchronized void DevCardSlotErrorHandler(PropertyChangeEvent evt) {
        DevCardSlotError event = (DevCardSlotError) evt.getNewValue();
        view.printWarning("You can't insert the card " + event.getDevCardID() + " into slot number " + event.getCardSlot());
    }

    public synchronized void FaithTrackEventHandler(PropertyChangeEvent evt) {
        FaithTrackEvent event = (FaithTrackEvent) evt.getNewValue();
        view.updateFaithTrack(event.getPlayerId(), event.getPosition(), event.getPopeFavorCards());
    }

    public synchronized void GameEndedEventHandler(PropertyChangeEvent evt) {
        System.out.println("Received" + evt.getClass().getSimpleName());
    }

    public synchronized void IAActionEventHandler(PropertyChangeEvent evt){
        IAActionEvent event = (IAActionEvent) evt.getNewValue();
        view.displayIAAction(event.getAction());
    }

    public synchronized void IncompatiblePowersErrorHandler(PropertyChangeEvent evt) {
        IncompatiblePowersError event = (IncompatiblePowersError) evt.getNewValue();
        view.printWarning("The power number " + event.getLeaderPowerIndex() + " of " + event.getLeaderCardID() + " can't be selected because it's in conflict with another power already selected");
    }

    public synchronized void InitialChoicesEventHandler(PropertyChangeEvent evt) {
        InitialChoicesEvent event = (InitialChoicesEvent) evt.getNewValue();
        ArrayList<String> chosenLeaderCards = view.choseInitialLeaderCards(event.getLeaderCards(), event.getNumberOFLeaderCardsToChose());

        ArrayList<Resource> allResources = Arrays.stream(Resource.values()).collect(Collectors.toCollection(ArrayList::new));
        HashMap<Resource, Integer> chosenResources = view.choseResources(allResources, event.getNumberResourcesOfChoice());

        send(new InitialDecisionsEvent(event.getPlayerId(), chosenLeaderCards, chosenResources));
    }

    public synchronized void LeaderCardNotActiveErrorHandler(PropertyChangeEvent evt) {
        LeaderCardNotActiveError event = (LeaderCardNotActiveError) evt.getNewValue();
        view.printWarning(event.getLeaderCardID() + " is not active");
    }

    public synchronized void LeaderCardStateEventHandler(PropertyChangeEvent evt) {
        LeaderCardStateEvent event = (LeaderCardStateEvent) evt.getNewValue();
        view.updateLeaderPowersSelectedState(event.getPlayerId(), event.getLeaderCardID(), event.getPowerSelectedStates());
    }

    public synchronized void LobbyErrorHandler(PropertyChangeEvent evt) {
        LobbyError event = (LobbyError) evt.getNewValue();


        view.printError(event.getErrorMsg());
        ClientApp.joinLobby(view, this);
    }

    public synchronized void LobbyStateEventHandler(PropertyChangeEvent evt) {
        LobbyStateEvent event = (LobbyStateEvent) evt.getNewValue();

        view.displayLobbyState(event.getLeaderID(), event.getOtherPLayersID());
    }

    public synchronized void LorenzoPositionEventHandler(PropertyChangeEvent evt) {
        LorenzoPositionEvent event = (LorenzoPositionEvent) evt.getNewValue();

        view.updateLorenzoPosition(event.getPosition());
    }

    public synchronized void MarketStateEventHandler(PropertyChangeEvent evt) {
        MarketStateEvent event = (MarketStateEvent) evt.getNewValue();
        view.updateMarket(event.getRows(), event.getCols(), event.getMarketStatus(), event.getMarbleLeft());
    }

    public synchronized void MatchStateEventHandler(PropertyChangeEvent evt) {
        MatchStateEvent event = (MatchStateEvent) evt.getNewValue();
        ArrayList<Event> evs = view.askForNextAction(event.getPlayerId(), event.isLastRound(), event.getTurnState());
        evs.forEach(ev -> {
                    send(ev);
                    System.out.println("event sent");
                }
        );
    }

    public synchronized void OrganizeResourcesEventHandler(PropertyChangeEvent evt) {
        OrganizeResourcesEvent event = (OrganizeResourcesEvent) evt.getNewValue();
        NewResourcesOrganizationEvent newOrganization = view.getWarehouseDisplacement(event.getResourcesToOrganize());
        send(newOrganization);
    }

    public synchronized void PersonalProductionPowerStateEventHandler(PropertyChangeEvent evt) {
        PersonalProductionPowerStateEvent event = (PersonalProductionPowerStateEvent) evt.getNewValue();
        view.setPersonalProductionPower(event.getPlayerId(), event.getPersonalProductionPower());
    }

    public synchronized void PlayerStateEventHandler(PropertyChangeEvent evt) {
        PlayerStateEvent event = (PlayerStateEvent) evt.getNewValue();
        view.updateLeaderCardsState(event.getPlayerId(), event.getLeaderCards());
    }

    public synchronized void PlayerActionErrorHandler(PropertyChangeEvent evt) {
        PlayerActionError event = (PlayerActionError) evt.getNewValue();
        view.printWarning(event.getDescription());
    }

    public synchronized void RequirementsNotMetErrorHandler(PropertyChangeEvent evt) {
        RequirementsNotMetError event = (RequirementsNotMetError) evt.getNewValue();
        view.printWarning(event.getLeaderCardID() + " can't be activated because you don't meet the requirements");
    }


    public synchronized void SetupDoneEventHandler(PropertyChangeEvent evt) {
        SetupDoneEvent event = (SetupDoneEvent) evt.getNewValue();

        view.displayWaitingForPlayerToSetupState(event.getPlayerId());
    }

    public synchronized void SimpleChoseResourcesEventHandler(PropertyChangeEvent evt) {
        SimpleChoseResourcesEvent event = (SimpleChoseResourcesEvent) evt.getNewValue();
        ArrayList<Resource> resources = new ArrayList<>();
        Collections.addAll(resources, Resource.values());
        HashMap<Resource, Integer> chosen = view.chooseResources(event.getRequiredResourcesOFChoice(), resources);
        send(new SimpleChosenResourcesEvent(event.getPlayerId(), chosen));
    }

    public synchronized void SinglePlayerLostEventHandler(PropertyChangeEvent evt) {
        view.displaySinglePlayerLost();
    }

    public synchronized void UsernameErrorHandler(PropertyChangeEvent evt) {
        UsernameError event = (UsernameError) evt.getNewValue();

        view.invalidateUsername();
        view.printError(event.getErrorMsg());
        ClientApp.joinLobby(view, this);
    }

    public static void main(String[] args) {
        ClientApp.main(args);
    }
}
