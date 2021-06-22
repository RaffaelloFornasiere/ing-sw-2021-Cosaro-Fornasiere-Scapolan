package it.polimi.ingsw.Server;

import it.polimi.ingsw.events.ClientEvents.BadRequestEvent;
import it.polimi.ingsw.events.ControllerEvents.MatchEvents.MatchEvent;
import it.polimi.ingsw.events.Event;
import it.polimi.ingsw.events.ControllerEvents.NewPlayerEvent;
import it.polimi.ingsw.events.ControllerEvents.NewPlayerEventWithNetworkData;
import it.polimi.ingsw.controller.EventRegistry;
import it.polimi.ingsw.events.HeartbeatEvent;
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
    private Event event;
    private final Object eventLock = new Object();

    /**
     * Constructor for the class
     * @param socket The socket fom where the requests will come from
     * @param mainEventHandlerRegistry The EventRegistry responsible of all the events unrelated to any match
     */
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

    /**
     * Methods that waits for events to be received and elaborates on them
     */
    public void elaborateRequests(){
        new Thread(()->clientHandlerReceiver.waitForEvent()).start();

        while(socket!=null) {
            try {
                event = requestsQueue.take();
                new Thread(this::elaborateEvent).start();
                synchronized (eventLock){
                    eventLock.wait();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Method responsible for and that notifies the controller about the arrival of an event
     */
    private void elaborateEvent(){
        Event event;
        synchronized (eventLock) {
            event = this.event;
            eventLock.notifyAll();
        }
        if (socket != null) {
            System.out.println("Elaborating: " + event.getEventName());
            if(event.getClass() == HeartbeatEvent.class)
                clientHandlerSender.sendObject(event);
            else if (event.getClass() == NewPlayerEvent.class)
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
    }

    /**
     * Getter for the object responsible of sending the events through the socket this request elaborator is associated with
     * @return The object responsible of sending the events through the socket this request elaborator is associated with
     */
    public ClientHandlerSender getClientHandlerSender() {
        return clientHandlerSender;
    }

    /**
     * Getter for the event registry that is responsible of the events for the match that the client this request elaborator is associated with is taking part in
     * @return The event registry that is responsible of the events for the match that the client this request elaborator is associated with is taking part in
     */
    public EventRegistry getMatchEventHandlerRegistry() {
        return matchEventHandlerRegistry;
    }

    /**
     * Setter for the event registry that is responsible of the events for the match that the client this request elaborator is associated with is taking part in
     * @param matchEventHandlerRegistry The event registry that is responsible of the events for the match that the client this request elaborator is associated with is taking part in
     */
    public void setMatchEventHandlerRegistry(EventRegistry matchEventHandlerRegistry) {
        this.matchEventHandlerRegistry = matchEventHandlerRegistry;
    }

    /**
     * Setter for the userID of the client this request elaborator is associated with
     * @param ownerUserID The userID of the client this request elaborator is associated with
     */
    public void setOwnerUserID(String ownerUserID) {
        this.ownerUserID = ownerUserID;
        this.clientHandlerReceiver.setOwnerUserID(ownerUserID);
    }

    /**
     * Method responsible of closing the connection with the socket this request elaborator is associated with
     */
    public void closeConnection() {
        clientHandlerSender.closeConnection();
        clientHandlerReceiver.closeConnection();
        try {
            socket.close();
            socket = null;
            requestsQueue.put(new QueueStopEvent(ownerUserID));
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
