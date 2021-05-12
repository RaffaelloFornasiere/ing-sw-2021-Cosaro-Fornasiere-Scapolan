package it.polimi.ingsw.client;

import it.polimi.ingsw.events.*;
import it.polimi.ingsw.model.DevCards.DevCard;
import it.polimi.ingsw.model.Direction;
import it.polimi.ingsw.ui.UI;
import it.polimi.ingsw.utilities.PropertyChangeSubject;
import org.reflections.Reflections;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.*;
import java.util.ArrayList;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class NetworkAdapter {

    public static final int SERVER_PORT = 50885;
    Socket socket;
    NetworkHandlerReceiver receiver;
    NetworkHandlerSender sender;
    Socket server;
    String playerID;
    UI view;

    public NetworkAdapter(PropertyChangeSubject subject, InetAddress address) throws IOException
    {
        connectToServer(address);

        // method-event binding
        Reflections reflections = new Reflections("it.polimi.ingsw.events");
        Set<Class<? extends Event>> events = reflections.getSubTypesOf(Event.class);

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
            } catch (NoSuchMethodException  e) {
                e.printStackTrace();
            }
        }
    }

    public boolean connectToServer(InetAddress address) throws IOException
    {
        server = new Socket(address, SERVER_PORT);
        NetworkHandlerSender sender = new NetworkHandlerSender(server);
        NetworkHandlerReceiver receiver = new NetworkHandlerReceiver(server, this);
        Timer timer = new Timer();
        TimerTask heartbeat;
        heartbeat = new TimerTask(){
            @Override
            public void run(){
                try {
                    sender.sendData("heartbeat");
                }catch (IOException e)
                {
                    view.printError("connection with server error, please check connection");
                    timer.cancel();
                }
            }
        };
        timer.scheduleAtFixedRate(heartbeat, 1000, 1000);
        return true;
    }


    public void enterMatch(String username, String lobbyLeaderID)
    {
        this.playerID = username;
        NewPlayerEvent event = new NewPlayerEvent(username, lobbyLeaderID);
        send(event);
    }

    public void createMatch(String username)
    {
        NewPlayerEvent event = new NewPlayerEvent(username);
        send(event);
    }

    public void buyResources(Direction direction, int index)
    {
        BuyResourcesEvent event = new BuyResourcesEvent(playerID, direction, index);
        send(event);
    }

    public void buyDevCards(int row, int column)
    {
        BuyDevCardsEvent event = new BuyDevCardsEvent(playerID, row, column);
        send(event);
    }

    public void activateProduction(ArrayList<DevCard> devCards)
    {
        ActivateProductionEvent event = new ActivateProductionEvent(playerID, devCards);
        send(event);
    }





    private void send(Object o)
    {
        try {
            sender.sendObject(o);
        }catch (IOException e)
        {
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





}
