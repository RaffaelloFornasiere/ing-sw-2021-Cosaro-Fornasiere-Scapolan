package it.polimi.ingsw.client;

import it.polimi.ingsw.events.*;
import it.polimi.ingsw.events.ControllerEvents.MatchEvents.*;
import it.polimi.ingsw.events.ControllerEvents.*;
import it.polimi.ingsw.events.ClientEvents.*;
import it.polimi.ingsw.model.DevCards.DevCard;
import it.polimi.ingsw.model.Direction;
import it.polimi.ingsw.model.Marble;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.ui.UI;
import it.polimi.ingsw.utilities.PropertyChangeSubject;
import org.reflections.Reflections;

import java.beans.PropertyChangeEvent;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.Socket;
import java.util.*;

public class NetworkAdapter {

    public static final int SERVER_PORT = 50885;
    Socket socket;
    NetworkHandlerReceiver receiver;
    NetworkHandlerSender sender;
    Socket server;
    String playerID;
    UI view;

    public NetworkAdapter(InetAddress address) throws IOException {
        connectToServer(address);

        // method-event binding
        Reflections reflections = new Reflections("it.polimi.ingsw.events");

        Set<Class<? extends Event>> events = new HashSet<>(reflections.getSubTypesOf(ClientEvent.class));
        events.addAll(reflections.getSubTypesOf(ControllerEvent.class));


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
        server.setSoTimeout(3000);
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

    public static void main(String[] args) {
        try {
            NetworkAdapter nt = new NetworkAdapter(InetAddress.getByName("127.0.0.1"));
            nt.createMatch("raffaello");
            //nt.createMatch("raffaello");

            System.out.println("aaa");

        } catch (IOException e) {
            e.printStackTrace();
        }
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
        NewPlayerEvent event = new NewPlayerEvent(username, username);
        send(event);
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

    public void displaceWarehouseMarbles(HashMap<Resource, Integer> resources) {
        OrganizeWarehouseResEvent event = new OrganizeWarehouseResEvent(playerID, resources);
        send(event);
    }

    public void sendSelectedResources(HashMap<Resource, Integer> resources)
    {

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
        System.out.println("Received" + evt.getClass().getSimpleName());
    }

    public void ClientEventHandler(PropertyChangeEvent evt) {
        System.out.println("Received" + evt.getClass().getSimpleName());
    }

    public void IncompatiblePowersErrorHandler(PropertyChangeEvent evt) {
        System.out.println("Received" + evt.getClass().getSimpleName());
    }

    public void InitialChoicesEventHandler(PropertyChangeEvent evt) {
        System.out.println("Received" + evt.getClass().getSimpleName());
    }

    public void LeaderCardNotActiveErrorHandler(PropertyChangeEvent evt) {
        System.out.println("Received" + evt.getClass().getSimpleName());
    }

    public void LobbyStateEventHandler(PropertyChangeEvent evt) {
        System.out.println("Received" + evt.getClass().getSimpleName());
    }

    public void RequirementsNotMetErrorHandler(PropertyChangeEvent evt) {
        System.out.println("Received" + evt.getClass().getSimpleName());
    }

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

    public void LeaderPowerSelectStateEventHandler(PropertyChangeEvent evt) {
        System.out.println("Received" + evt.getClass().getSimpleName());
    }

    public void MatchEventHandler(PropertyChangeEvent evt) {
        System.out.println("Received" + evt.getClass().getSimpleName());
    }

    public void OrganizeWarehouseResEventHandler(PropertyChangeEvent evt) {
        System.out.println("Received" + evt.getClass().getSimpleName());
        OrganizeWarehouseResEvent event = (OrganizeWarehouseResEvent) evt.getNewValue();
        var displacement = view.getWarehouseDisplacement(event.getResources());
        send(new OrganizeWarehouseResEvent(playerID, displacement));
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

    public void MarketStateEventHandler(PropertyChangeEvent evt) {
        System.out.println("Received" + evt.getClass().getSimpleName());
    }

    public void ResourceSelectionEventHandler(PropertyChangeEvent evt) {
        System.out.println("Received" + evt.getClass().getSimpleName());
        ResourceSelectionEvent event = (ResourceSelectionEvent)evt.getNewValue();
        var selection = view.getResourcesSelection(event.getRequired());
        send(new ResourceSelectionEvent(playerID, null, selection.get(0), selection.get(1)));
    }
}
