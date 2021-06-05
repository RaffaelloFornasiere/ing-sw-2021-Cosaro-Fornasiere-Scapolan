package it.polimi.ingsw.Server;

import it.polimi.ingsw.events.ClientEvents.BadRequestEvent;
import it.polimi.ingsw.events.ControllerEvents.MatchEvents.MatchEvent;
import it.polimi.ingsw.events.Event;
import it.polimi.ingsw.events.ControllerEvents.NewPlayerEvent;
import it.polimi.ingsw.events.ControllerEvents.NewPlayerEventWithNetworkData;
import it.polimi.ingsw.controller.EventRegistry;
import it.polimi.ingsw.events.QueueStopEvent;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class RequestsElaborator {
    public static final int QUEUE_SIZE = 10;

    private Socket socket;
    private ClientHandlerSender clientHandlerSender;
    private ClientHandlerReceiver clientHandlerReceiver;
    private BlockingQueue<Event> requestsQueue;
    private EventRegistry mainEventHandlerRegistry;
    private EventRegistry matchEventHandlerRegistry;
    private String ownerUserID;

    public RequestsElaborator(Socket socket, EventRegistry mainEventHandlerRegistry) {
        try {
            this.socket = socket;
            this.requestsQueue = new ArrayBlockingQueue<>(QUEUE_SIZE);
            this.clientHandlerSender = new ClientHandlerSender(socket.getOutputStream());
            this.clientHandlerReceiver = new ClientHandlerReceiver(socket.getInputStream(), requestsQueue);
            this.mainEventHandlerRegistry = mainEventHandlerRegistry;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void elaborateRequests(){
        new Thread(()->clientHandlerReceiver.waitForEvent()).start();

        while(socket!=null) {
            try {
                Event event = requestsQueue.take();
                if(socket!=null) {
                    System.out.println("Elaborating: " + event.getEventName());
                    if (event.getClass() == NewPlayerEvent.class)
                        event = new NewPlayerEventWithNetworkData((NewPlayerEvent) event, this);
                    else if (!ownerUserID.equals(event.getPlayerId()))
                        mainEventHandlerRegistry.sendEvent(new BadRequestEvent(event.getPlayerId(),
                                "The userID given is wrong", event));

                    if (MatchEvent.class.isAssignableFrom(event.getClass())) {
                        if (matchEventHandlerRegistry == null)
                            mainEventHandlerRegistry.sendEvent(new BadRequestEvent(event.getPlayerId(),
                                    "The match has not started yet", event));
                        else
                            matchEventHandlerRegistry.sendEvent(event);
                    } else {
                        mainEventHandlerRegistry.sendEvent(event);
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public ClientHandlerSender getClientHandlerSender() {
        return clientHandlerSender;
    }

    public EventRegistry getMatchEventHandlerRegistry() {
        return matchEventHandlerRegistry;
    }

    public void setMatchEventHandlerRegistry(EventRegistry matchEventHandlerRegistry) {
        this.matchEventHandlerRegistry = matchEventHandlerRegistry;
    }

    public void setOwnerUserID(String ownerUserID) {
        this.ownerUserID = ownerUserID;
        this.clientHandlerReceiver.setOwnerUserID(ownerUserID);
    }

    public void closeConnection() {
        clientHandlerSender.closeConnection();
        clientHandlerReceiver.closeConnection();
        try {
            socket.close();
            socket = null;
            requestsQueue.put(new QueueStopEvent());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
