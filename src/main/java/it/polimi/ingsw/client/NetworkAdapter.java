package it.polimi.ingsw.client;

import it.polimi.ingsw.events.*;
import it.polimi.ingsw.events.ControllerEvents.MatchEvents.*;
import it.polimi.ingsw.events.ControllerEvents.*;
import it.polimi.ingsw.events.ClientEvents.*;
import it.polimi.ingsw.model.DevCards.DevCard;
import it.polimi.ingsw.model.Direction;
import it.polimi.ingsw.ui.UI;
import it.polimi.ingsw.utilities.PropertyChangeSubject;
import org.reflections.Reflections;

import java.beans.PropertyChangeEvent;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.*;
import java.util.*;

public class NetworkAdapter {

    public static final int SERVER_PORT = 50885;
    Socket socket;
    NetworkHandlerReceiver receiver;
    NetworkHandlerSender sender;
    Socket server;
    String playerID;
    UI view;

    public NetworkAdapter(PropertyChangeSubject subject, InetAddress address) throws IOException {
        connectToServer(address);

        // method-event binding
        Reflections reflections = new Reflections("it.polimi.ingsw.events");

        Set<Class<? extends Event>> events = new HashSet<>(reflections.getSubTypesOf(ClientEvent.class));
        events.addAll(reflections.getSubTypesOf(ControllerEvent.class));

        for (var event : events) {
            try {
                Method method = this.getClass().getMethod(event.getSimpleName() + "Handler",
                        PropertyChangeEvent.class);
                subject.addPropertyChangeListener(event.getSimpleName(), x -> {
                    try {
                        method.invoke(this, x);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                });
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }

        }
    }

    public boolean connectToServer(InetAddress address) throws IOException {
        server = new Socket(address, SERVER_PORT);
        server.setSoTimeout(3000);
        NetworkHandlerSender sender = new NetworkHandlerSender(server);
        NetworkHandlerReceiver receiver = new NetworkHandlerReceiver(server, this);
        Timer timer = new Timer();
        TimerTask heartbeat;
        heartbeat = new TimerTask() {
            @Override
            public void run() {
                try {
                    sender.sendData("heartbeat");
                } catch (IOException e) {
                    view.printError("connection with server error, please check connection");
                    timer.cancel();
                }
            }
        };
        timer.scheduleAtFixedRate(heartbeat, 1000, 1000);
        return true;
    }


    public void enterMatch(String username, String lobbyLeaderID) {
        this.playerID = username;
        NewPlayerEvent event = new NewPlayerEvent(username, lobbyLeaderID);
        send(event);
    }

    public void createMatch(String username) {
        NewPlayerEvent event = new NewPlayerEvent(username);
        send(event);
    }

    public void buyResources(Direction direction, int index) {
        BuyResourcesEvent event = new BuyResourcesEvent(playerID, direction, index);
        send(event);
    }

    public void buyDevCards(int row, int column) {
        BuyDevCardsEvent event = new BuyDevCardsEvent(playerID, row, column);
        send(event);
    }

    public void activateProduction(ArrayList<Integer> devCards) {
    {
        ActivateProductionEvent event = new ActivateProductionEvent(playerID, devCards);
        send(event);
    }

    public void activateLeaderCard(int index) {
        ActivateLeaderCardEvent event = new ActivateLeaderCardEvent(playerID, index);
        send(event);
    }

    private void send(Object o) {
        try {
            sender.sendObject(o);
        } catch (IOException e) {
            view.printError("connection with server error, please check connection");
        }
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

    public void ControllerEventHandler(PropertyChangeEvent evt){}
    public void MatchEventsHandler(PropertyChangeEvent evt){}
    public void ActivateLeaderCardEventHandler(PropertyChangeEvent evt){}
    public void ActivateProductionEventHandler(PropertyChangeEvent evt){}
    public void AddedNewPopeFavorCardEventHandler(PropertyChangeEvent evt){}
    public void BuyDevCardsEventHandler(PropertyChangeEvent evt){}
    public void BuyResourcesEventHandler(PropertyChangeEvent evt){}
    public void IncrementedFaithTrackPositionEventHandler(PropertyChangeEvent evt){}
    public void LeaderPowerSelectStateEventHandler(PropertyChangeEvent evt){}
    public void MatchEventHandler(PropertyChangeEvent evt){}
    public void OrganizeWarehouseResEventHandler(PropertyChangeEvent evt){}
    public void SelectMultiLPowersEventHandler(PropertyChangeEvent evt){}
    public void NewPlayerEventHandler(PropertyChangeEvent evt){}
    public void NewPlayerEventWithNetworkDataHandler(PropertyChangeEvent evt){}
    public void StartMatchEventHandler(PropertyChangeEvent evt){}


}
