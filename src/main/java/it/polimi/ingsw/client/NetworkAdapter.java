package it.polimi.ingsw.client;

import com.google.gson.GsonBuilder;
import it.polimi.ingsw.ClientApp;
import it.polimi.ingsw.events.EventRegistry;
import it.polimi.ingsw.events.clientEvents.*;
import it.polimi.ingsw.events.controllerEvents.matchEvents.*;
import it.polimi.ingsw.events.controllerEvents.NewPlayerEvent;
import it.polimi.ingsw.events.controllerEvents.StartMatchEvent;
import it.polimi.ingsw.events.Event;
import it.polimi.ingsw.events.HeartbeatEvent;
import it.polimi.ingsw.messageSenders.NetworkHandlerSender;
import it.polimi.ingsw.messageSenders.LocalSender;
import it.polimi.ingsw.messageSenders.Sender;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.ui.UI;
import it.polimi.ingsw.utilities.NetworkConfiguration;
import org.reflections.Reflections;

import java.beans.PropertyChangeEvent;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.Socket;
import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class NetworkAdapter {

    public static final int SERVER_PORT = NetworkConfiguration.getInstance().getPORT();
    private NetworkHandlerReceiver receiver;
    private Sender sender;
    private Socket server;
    private String playerID;
    private final UI view;
    private Timer heartbeatTimer;

    /**
     * Constructor for the class when the client should connect to a server
     * @param address The internet address of the server
     * @param ui The ui tha is in use
     * @throws IOException If it's unable to connect to the server
     */
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
                        System.out.println(event.getSimpleName());
                        e.printStackTrace();
                    }
                });
            } catch (NoSuchMethodException e) {
                System.err.println("Missing handler for " + event.getSimpleName());
                e.printStackTrace();
            }

        }
    }

    /**
     * Constructor for the class when the process will do both the client and the server functionality
     * @param ui The ui in use
     * @param toController The event registry for the events that should be handled by the server
     * @param toPlayer The event registry for the events that should be handled by the client
     * @param playerID The ID of the player
     */
    public NetworkAdapter(UI ui, EventRegistry toController, EventRegistry toPlayer, String playerID) {
        sender = new LocalSender(toController);
        server = null;
        receiver = null;
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

    boolean stopThread = false;

    /**
     * Connects to the server
     * @param address The internet address of the server
     * @return Whether the connection was successful
     * @throws IOException If the connection to the server failed
     */
    public boolean connectToServer(InetAddress address) throws IOException {
        server = new Socket(address, SERVER_PORT);
        server.setSoTimeout(10*1000);
        sender = new NetworkHandlerSender(server.getOutputStream());
        receiver = new NetworkHandlerReceiver(server);

        new Thread(() -> {
            while (!stopThread) {
                receiver.receive();
            }
            System.out.println("Stopped thread");

        }).start();



        heartbeatTimer = new Timer();
        TimerTask heartbeat;
        heartbeat = new TimerTask() {
            @Override
            public void run() {
                sender.sendObject(new HeartbeatEvent(playerID));
            }
        };
        heartbeatTimer.scheduleAtFixedRate(heartbeat, 1000, 1000);
        return true;
    }

    /**
     * Methods that stops the thread that continuously reads from the socket
     */
    public void stopThread(){
        if(receiver!=null)
            receiver.closeConnection();
    }

    /**
     * Sends an event through the sender
     * @param e The event to send
     */
    private void send(Event e) {
        sender.sendObject(e);
    }


    /**
     * Enters a lobby
     * @param username The ID of the user
     * @param lobbyLeaderID The ID of the leader
     */
    public void enterMatch(String username, String lobbyLeaderID) {
        this.playerID = username;
        NewPlayerEvent event = new NewPlayerEvent(username, lobbyLeaderID);
        send(event);
    }

    /**
     * Creates a lobby
     * @param username The ID of the user
     */
    public void createMatch(String username) {
        this.playerID = username;
        NewPlayerEvent event = new NewPlayerEvent(username, username);
        send(event);
    }

    /**
     * Signals the server to start the match. Can be used only if the player is the leader of it's lobby
     */
    public void startMatch() {
        send(new StartMatchEvent(playerID));
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

    /**
     * Handler for BadRequestEvent
     */
    public synchronized void BadRequestEventHandler(PropertyChangeEvent evt) {
        BadRequestEvent event = (BadRequestEvent) evt.getNewValue();
        view.printError("Bad request");
        view.printError(event.getDescription());
        System.err.println("Cause: " + new GsonBuilder().setPrettyPrinting().create().toJson(event.getCause()));
    }

    /**
     * Handler for CantAffordError
     */
    public synchronized void CantAffordErrorHandler(PropertyChangeEvent evt) {
        CantAffordError event = (CantAffordError) evt.getNewValue();
        view.printWarning("You can't afford " + event.getDevCardID());
    }

    /**
     * Handler for ChoseMultipleExtraResourcePowerEvent
     */
    public synchronized void ChoseMultipleExtraResourcePowerEventHandler(PropertyChangeEvent evt) {
        ChoseMultipleExtraResourcePowerEvent event = (ChoseMultipleExtraResourcePowerEvent) evt.getNewValue();
        HashMap<Resource, Integer> chosen = view.chooseResources(event.getNumberOfResources(), event.getResourceTypes());
        send(new SimpleChosenResourcesEvent(event.getPlayerId(), chosen));
    }

    /**
     * Handler for ChoseResourcesEvent
     */
    public synchronized void ChoseResourcesEventHandler(PropertyChangeEvent evt) {
        ChoseResourcesEvent event = (ChoseResourcesEvent) evt.getNewValue();
        send(view.askWhereToTakeResourcesFrom(event.getRequiredResources(), event.getRequiredResourcesOFChoice()));
    }

    /**
     * Handler for DashBoardStateEvent
     */
    public synchronized void DashBoardStateEventHandler(PropertyChangeEvent evt) {
        DashBoardStateEvent event = (DashBoardStateEvent) evt.getNewValue();
        view.updateDashboard(event.getPlayerId(), event.getTopDevCards(), event.getStrongBox(), event.getWarehouse());
    }

    /**
     * Handler for DepositLeaderPowerStateEvent
     */
    public synchronized void DepositLeaderPowerStateEventHandler(PropertyChangeEvent evt) {
        DepositLeaderPowerStateEvent event = (DepositLeaderPowerStateEvent) evt.getNewValue();
        view.updateLeaderCardDepositState(event.getPlayerId(), event.getLeaderCardID(), event.getLeaderPowerIndex(), event.getStoredResources());
    }

    /**
     * Handler for DevCardGridStateEvent
     */
    public synchronized void DevCardGridStateEventHandler(PropertyChangeEvent evt) {
        DevCardGridStateEvent event = (DevCardGridStateEvent) evt.getNewValue();
        view.updateDevCardGrid(event.getTopDevCardIDs());
    }

    /**
     * Handler for DevCardSlotError
     */
    public synchronized void DevCardSlotErrorHandler(PropertyChangeEvent evt) {
        DevCardSlotError event = (DevCardSlotError) evt.getNewValue();
        view.printWarning("You can't insert the card " + event.getDevCardID() + " into slot number " + event.getCardSlot());
    }

    /**
     * Handler for FaithTrackEvent
     */
    public synchronized void FaithTrackEventHandler(PropertyChangeEvent evt) {
        FaithTrackEvent event = (FaithTrackEvent) evt.getNewValue();
        view.updateFaithTrack(event.getPlayerId(), event.getPosition(), event.getPopeFavorCards());
    }

    /**
     * Handler for GameStartingEvent
     */
    public synchronized void GameStartingEventHandler(PropertyChangeEvent evt) {
        GameStartingEvent event = (GameStartingEvent) evt.getNewValue();
        view.initializeMatchObjects();
    }

    /**
     * Handler for GameEndedEvent
     */
    public synchronized void GameEndedEventHandler(PropertyChangeEvent evt) {
        GameEndedEvent event = (GameEndedEvent) evt.getNewValue();
        view.displayEndOfGame(event.getFinalPlayerStates());
        stopThread();
    }

    /**
     * Handler for IAActionEvent
     */
    public synchronized void IAActionEventHandler(PropertyChangeEvent evt) {
        IAActionEvent event = (IAActionEvent) evt.getNewValue();
        view.displayIAAction(event.getAction());
    }

    /**
     * Handler for IncompatiblePowersError
     */
    public synchronized void IncompatiblePowersErrorHandler(PropertyChangeEvent evt) {
        IncompatiblePowersError event = (IncompatiblePowersError) evt.getNewValue();
        view.printWarning("The power number " + event.getLeaderPowerIndex() + " of " + event.getLeaderCardID() + " can't be selected because it's in conflict with another power already selected");
    }

    /**
     * Handler for InitialChoicesEvent
     */
    public synchronized void InitialChoicesEventHandler(PropertyChangeEvent evt) {
        InitialChoicesEvent event = (InitialChoicesEvent) evt.getNewValue();
        ArrayList<String> chosenLeaderCards = view.choseInitialLeaderCards(event.getLeaderCards(), event.getNumberOFLeaderCardsToChose());

        ArrayList<Resource> allResources = Arrays.stream(Resource.values()).collect(Collectors.toCollection(ArrayList::new));
        HashMap<Resource, Integer> chosenResources = view.chooseResources(event.getNumberResourcesOfChoice(), allResources);

        send(new InitialDecisionsEvent(event.getPlayerId(), chosenLeaderCards, chosenResources));
    }

    /**
     * Handler for LeaderCardNotActiveError
     */
    public synchronized void LeaderCardNotActiveErrorHandler(PropertyChangeEvent evt) {
        LeaderCardNotActiveError event = (LeaderCardNotActiveError) evt.getNewValue();
        view.printWarning(event.getLeaderCardID() + " is not active");
    }

    /**
     * Handler for LeaderCardStateEvent
     */
    public synchronized void LeaderCardStateEventHandler(PropertyChangeEvent evt) {
        LeaderCardStateEvent event = (LeaderCardStateEvent) evt.getNewValue();
        view.updateLeaderPowersSelectedState(event.getPlayerId(), event.getLeaderCardID(), event.getPowerSelectedStates());
    }

    /**
     * Handler for LobbyError
     */
    public synchronized void LobbyErrorHandler(PropertyChangeEvent evt) {
        LobbyError event = (LobbyError) evt.getNewValue();

        view.printError(event.getErrorMsg());
        ClientApp.joinLobby(view, this);
    }

    /**
     * Handler for LobbyStateEvent
     */
    public synchronized void LobbyStateEventHandler(PropertyChangeEvent evt) {
        LobbyStateEvent event = (LobbyStateEvent) evt.getNewValue();

        view.displayLobbyState(event.getLeaderID(), event.getOtherPLayersID());
    }

    /**
     * Handler for LorenzoPositionEvent
     */
    public synchronized void LorenzoPositionEventHandler(PropertyChangeEvent evt) {
        LorenzoPositionEvent event = (LorenzoPositionEvent) evt.getNewValue();
        System.out.println("LorenzoPositionEventHandler");
        view.updateLorenzoPosition(event.getPosition());
    }

    /**
     * Handler for MarketStateEvent
     */
    public synchronized void MarketStateEventHandler(PropertyChangeEvent evt) {
        MarketStateEvent event = (MarketStateEvent) evt.getNewValue();
        view.updateMarket(event.getRows(), event.getCols(), event.getMarketStatus(), event.getMarbleLeft());
    }

    /**
     * Handler for MatchStateEvent
     */
    public synchronized void MatchStateEventHandler(PropertyChangeEvent evt) {
        MatchStateEvent event = (MatchStateEvent) evt.getNewValue();
        ArrayList<Event> evs = view.askForNextAction(event.getPlayerId(), event.isLastRound(), event.getTurnState());
        evs.forEach(ev -> {
                    send(ev);
                    System.out.println("event sent");
                }
        );
    }

    /**
     * Handler for OrganizeResourcesEvent
     */
    public synchronized void OrganizeResourcesEventHandler(PropertyChangeEvent evt) {
        OrganizeResourcesEvent event = (OrganizeResourcesEvent) evt.getNewValue();
        NewResourcesOrganizationEvent newOrganization = view.getWarehouseDisplacement(event.getResourcesToOrganize());
        send(newOrganization);
    }

    /**
     * Handler for PersonalProductionPowerStateEvent
     */
    public synchronized void PersonalProductionPowerStateEventHandler(PropertyChangeEvent evt) {
        PersonalProductionPowerStateEvent event = (PersonalProductionPowerStateEvent) evt.getNewValue();
        view.setPersonalProductionPower(event.getPlayerId(), event.getPersonalProductionPower());
    }

    /**
     * Handler for PlayerStateEvent
     */
    public synchronized void PlayerStateEventHandler(PropertyChangeEvent evt) {
        PlayerStateEvent event = (PlayerStateEvent) evt.getNewValue();
        view.updateLeaderCardsState(event.getPlayerId(), event.getLeaderCards());
    }

    /**
     * Handler for PlayerActionError
     */
    public synchronized void PlayerActionErrorHandler(PropertyChangeEvent evt) {
        PlayerActionError event = (PlayerActionError) evt.getNewValue();
        view.printWarning(event.getDescription());
    }

    /**
     * Handler for RequirementsNotMetError
     */
    public synchronized void RequirementsNotMetErrorHandler(PropertyChangeEvent evt) {
        RequirementsNotMetError event = (RequirementsNotMetError) evt.getNewValue();
        view.printWarning(event.getLeaderCardID() + " can't be activated because you don't meet the requirements");
    }

    /**
     * Handler for ServerDisconnectionEvent
     */
    public synchronized void ServerDisconnectionEventHandler(PropertyChangeEvent evt) {
        ServerDisconnectionEvent event = (ServerDisconnectionEvent) evt.getNewValue();

        view.printError("The connection with the server was closed. Shutting down the application");
        heartbeatTimer.cancel();
        sender.closeConnection();
        receiver.closeConnection();
        try {
            server.close();
        } catch (IOException e) {
            System.err.println("Error closing the socket");
        }
    }

    /**
     * Handler for SetupDoneEvent
     */
    public synchronized void SetupDoneEventHandler(PropertyChangeEvent evt) {
        SetupDoneEvent event = (SetupDoneEvent) evt.getNewValue();

        view.displayWaitingForPlayerToSetupState(event.getPlayerId());
    }

    /**
     * Handler for SimpleChoseResourcesEvent
     */
    public synchronized void SimpleChoseResourcesEventHandler(PropertyChangeEvent evt) {
        SimpleChoseResourcesEvent event = (SimpleChoseResourcesEvent) evt.getNewValue();
        ArrayList<Resource> resources = new ArrayList<>();
        Collections.addAll(resources, Resource.values());
        HashMap<Resource, Integer> chosen = view.chooseResources(event.getRequiredResourcesOFChoice(), resources);
        send(new SimpleChosenResourcesEvent(event.getPlayerId(), chosen));
    }

    /**
     * Handler for SinglePlayerLostEvent
     */
    public synchronized void SinglePlayerLostEventHandler(PropertyChangeEvent evt) {
        view.displaySinglePlayerLost();
    }

    /**
     * Handler for UsernameError
     */
    public synchronized void UsernameErrorHandler(PropertyChangeEvent evt) {
        UsernameError event = (UsernameError) evt.getNewValue();

        view.invalidateUsername();
        view.printError(event.getErrorMsg());
        ClientApp.joinLobby(view, this);
    }
}
