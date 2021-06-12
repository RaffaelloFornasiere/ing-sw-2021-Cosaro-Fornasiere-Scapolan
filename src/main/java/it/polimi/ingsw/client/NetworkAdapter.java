package it.polimi.ingsw.client;

import com.google.gson.Gson;
import it.polimi.ingsw.ClientApp;
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
    Socket socket;
    NetworkHandlerReceiver receiver;
    NetworkHandlerSender sender;
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
                receiver.addPropertyChangeListener(event.getSimpleName(), x -> {
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


    @SuppressWarnings("unused")
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
                try {
                    sender.sendData("heartbeat");
                    //System.out.println("heartbeat");
                } catch (IOException e) {
                    view.printError("connection with server error, please check connection");
                    timer.cancel();
                }
            }
        };
        //timer.scheduleAtFixedRate(heartbeat, 1000, 1000);
        return true;
    }

    private void send(Event e) {
        try {
            sender.sendObject(e);
        } catch (IOException err) {
            view.printError("connection with server error, please check connection");
        }
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
    public void BadRequestEventHandler(PropertyChangeEvent evt) {
        System.err.println(new Gson().toJson(evt.getNewValue()));
    }

    public void CantAffordErrorHandler(PropertyChangeEvent evt) {
        System.out.println("Received" + evt.getClass().getSimpleName());
    }

    public void ChoseMultipleExtraResourcePowerEventHandler(PropertyChangeEvent evt) {
        System.out.println("Received" + evt.getClass().getSimpleName());
    }

    public void ChoseResourcesEventHandler(PropertyChangeEvent evt) {
        System.out.println("Received" + evt.getClass().getSimpleName());
    }

    public void ClientEventHandler(PropertyChangeEvent evt) {
        System.out.println("Received" + evt.getClass().getSimpleName());
    }

    public void DashBoardStateEventHandler(PropertyChangeEvent evt) {
        DashBoardStateEvent event = (DashBoardStateEvent) evt.getNewValue();
        view.updateDashboard(event.getPlayerId(), event.getTopDevCards(), event.getStrongBox(), event.getWarehouse());
    }

    public void DepositLeaderPowerStateEventHandler(PropertyChangeEvent evt) {
        DepositLeaderPowerStateEvent event = (DepositLeaderPowerStateEvent) evt.getNewValue();
        view.updateLeaderCardDepositState(event.getPlayerId(), event.getLeaderCardID(), event.getLeaderPowerIndex(), event.getStoredResources());
    }

    public void DevCardGridStateEventHandler(PropertyChangeEvent evt) {
        DevCardGridStateEvent event = (DevCardGridStateEvent) evt.getNewValue();
        view.updateDevCardGrid(event.getTopDevCardIDs());
    }

    public void DevCardSlotErrorHandler(PropertyChangeEvent evt) {
        System.out.println("Received" + evt.getClass().getSimpleName());
    }

    public void FaithTrackEventHandler(PropertyChangeEvent evt) {
        FaithTrackEvent event = (FaithTrackEvent) evt.getNewValue();
        view.updateFaithTrack(event.getPlayerId(), event.getPosition(), event.getPopeFavorCards());
    }

    public void GameEndedEventHandler(PropertyChangeEvent evt) {
        System.out.println("Received" + evt.getClass().getSimpleName());
    }

    public void IncompatiblePowersErrorHandler(PropertyChangeEvent evt) {
        System.out.println("Received" + evt.getClass().getSimpleName());
    }

    public void InitialChoicesEventHandler(PropertyChangeEvent evt) {
        InitialChoicesEvent event = (InitialChoicesEvent) evt.getNewValue();
        ArrayList<String> chosenLeaderCards = view.choseInitialLeaderCards(event.getLeaderCards(), event.getNumberOFLeaderCardsToChose());

        ArrayList<Resource> allResources = Arrays.stream(Resource.values()).collect(Collectors.toCollection(ArrayList::new));
        HashMap<Resource, Integer> chosenResources = view.choseResources(allResources, event.getNumberResourcesOfChoice());

        send(new InitialDecisionsEvent(event.getPlayerId(), chosenLeaderCards, chosenResources));
    }

    public void LeaderCardNotActiveErrorHandler(PropertyChangeEvent evt) {
        System.out.println("Received" + evt.getClass().getSimpleName());
    }

    public void LeaderCardStateEventHandler(PropertyChangeEvent evt) {
        LeaderCardStateEvent event = (LeaderCardStateEvent) evt.getNewValue();
        view.updateLeaderPowersSelectedState(event.getPlayerId(), event.getLeaderCardID(), event.getPowerSelectedStates());
    }

    public void LobbyErrorHandler(PropertyChangeEvent evt) {
        LobbyError event = (LobbyError) evt.getNewValue();


        view.printError(event.getErrorMsg());
        ClientApp.joinLobby(view, this);
    }

    public void LobbyStateEventHandler(PropertyChangeEvent evt) {
        LobbyStateEvent event = (LobbyStateEvent) evt.getNewValue();

        view.displayLobbyState(event.getLeaderID(), event.getOtherPLayersID());
    }

    public void MarketStateEventHandler(PropertyChangeEvent evt) {
        MarketStateEvent event = (MarketStateEvent) evt.getNewValue();
        view.updateMarket(event.getRows(), event.getCols(), event.getMarketStatus(), event.getMarbleLeft());
    }

    public void MatchStateEventHandler(PropertyChangeEvent evt) {
        MatchStateEvent event = (MatchStateEvent) evt.getNewValue();
        Event ev = view.askForNextAction(event.getPlayerId(), event.isLastRound(), event.getTurnState());
        if(ev!=null) send(ev);
    }

    public void OrganizeResourcesEventHandler(PropertyChangeEvent evt) {
        OrganizeResourcesEvent event = (OrganizeResourcesEvent) evt.getNewValue();
        NewResourcesOrganizationEvent newOrganization = view.getWarehouseDisplacement(event.getResourcesToOrganize());
        send(newOrganization);
    }

    public void PersonalProductionPowerStateEventHandler(PropertyChangeEvent evt) {
        PersonalProductionPowerStateEvent event = (PersonalProductionPowerStateEvent) evt.getNewValue();
        view.setPersonalProductionPower(event.getPlayerId(), event.getPersonalProductionPower());
    }

    public void PlayerStateEventHandler(PropertyChangeEvent evt) {
        PlayerStateEvent event = (PlayerStateEvent) evt.getNewValue();
        view.updateLeaderCardsState(event.getPlayerId(), event.getLeaderCards());
    }

    public void RequirementsNotMetErrorHandler(PropertyChangeEvent evt) {
        System.out.println("Received" + evt.getClass().getSimpleName());
    }

    public void ResourceSelectionEventHandler(PropertyChangeEvent evt) {
        ResourceSelectionEvent event = (ResourceSelectionEvent) evt.getNewValue();
        var selection = view.getResourcesSelection(event.getRequired());
        send(new ResourceSelectionEvent(playerID, null, selection.get(0), selection.get(1)));
    }

    public void SetupDoneEventHandler(PropertyChangeEvent evt) {
        SetupDoneEvent event = (SetupDoneEvent) evt.getNewValue();

        view.displayWaitingForPlayerToSetupState(event.getPlayerId());
    }

    public void SimpleChoseResourcesEventHandler(PropertyChangeEvent evt) {
        System.out.println("Received" + evt.getClass().getSimpleName());
    }

    public void SinglePlayerLostEventHandler(PropertyChangeEvent evt) {
        System.out.println("Received" + evt.getClass().getSimpleName());
    }

    public void UsernameErrorHandler(PropertyChangeEvent evt) {
        UsernameError event = (UsernameError) evt.getNewValue();
        System.out.println("Received" + event.getEventName());

        view.invalidateUsername();
        view.printError(event.getErrorMsg());
        ClientApp.joinLobby(view, this);
    }

    /*
    public void ControllerEventHandler(PropertyChangeEvent evt) {
        System.out.println("Received" + evt.getClass().getSimpleName());
    }

    public void MatchEventsHandler(PropertyChangeEvent evt) {
        System.out.println("Received" + evt.getClass().getSimpleName());
    }

    public void ActivateLeaderCardEventHandler(PropertyChangeEvent evt) {
        System.out.println("Received" + evt.getClass().getSimpleName());
    }

    public void ActivateProductionEventHandler(PropertyChangeEvent evt) {
        System.out.println("Received" + evt.getClass().getSimpleName());
    }

    public void AddedNewPopeFavorCardEventHandler(PropertyChangeEvent evt) {
        System.out.println("Received" + evt.getClass().getSimpleName());
    }

    public void BuyDevCardsEventHandler(PropertyChangeEvent evt) {
        System.out.println("Received" + evt.getClass().getSimpleName());
    }

    public void BuyResourcesEventHandler(PropertyChangeEvent evt) {
        System.out.println("Received" + evt.getClass().getSimpleName());
    }

    public void IncrementedFaithTrackPositionEventHandler(PropertyChangeEvent evt) {
        System.out.println("Received" + evt.getClass().getSimpleName());
    }

    public void MatchEventHandler(PropertyChangeEvent evt) {
        System.out.println("Received" + evt.getClass().getSimpleName());
    }

    public void OrganizeWarehouseResEventHandler(PropertyChangeEvent evt) {
        System.out.println("Received" + evt.getClass().getSimpleName());
        OrganizeWarehouseResEvent event = (OrganizeWarehouseResEvent) evt.getNewValue();
        //var displacement = view.getWarehouseDisplacement(event.getResources());
        //send(new OrganizeWarehouseResEvent(playerID, displacement));
    }

    public void SelectMultiLPowersEventHandler(PropertyChangeEvent evt) {
        System.out.println("Received" + evt.getClass().getSimpleName());
    }

    public void NewPlayerEventHandler(PropertyChangeEvent evt) {
        System.out.println("Received" + evt.getClass().getSimpleName());
    }

    public void NewPlayerEventWithNetworkDataHandler(PropertyChangeEvent evt) {
        System.out.println("Received" + evt.getClass().getSimpleName());
    }

    public void StartMatchEventHandler(PropertyChangeEvent evt) {
        System.out.println("Received" + evt.getClass().getSimpleName());
    }
    */

    public static void main(String[] args) {
        ClientApp.main(args);
    }
}
