package it.polimi.ingsw.client;

import it.polimi.ingsw.events.*;
import it.polimi.ingsw.model.Direction;
import it.polimi.ingsw.utilities.PropertyChangeSubject;
import org.reflections.Reflections;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.*;
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

    public NetworkAdapter(PropertyChangeSubject subject, InetAddress address)
    {
        try
        {
            server = new Socket(address, SERVER_PORT);
        }catch (IOException e)
        {
            e.printStackTrace();
        }
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
                    // view alert of client disconnection
                }
            }
        };
        timer.scheduleAtFixedRate(heartbeat, 0, 1000);



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


    public void accessMatch(String username, String lobbyLeaderID)
    {
        this.playerID = username;
        NewPlayerEvent event = new NewPlayerEvent(username, lobbyLeaderID);
        sender.sendObject(event);
    }

    public void createMatch(String username)
    {
        NewPlayerEvent event = new NewPlayerEvent(username);
        sender.sendObject(event);
    }


    public void buyResources(Direction direction, int index)
    {
        BuyResourcesEvent event = new BuyResourcesEvent(playerID, direction, index);
        sender.sendObject(event);
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
