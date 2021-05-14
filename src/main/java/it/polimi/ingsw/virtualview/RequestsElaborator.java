package it.polimi.ingsw.virtualview;

import it.polimi.ingsw.events.ClientEvents.BadRequestEvent;
import it.polimi.ingsw.events.ControllerEvents.MatchEvents.MatchEvent;
import it.polimi.ingsw.events.Event;
import it.polimi.ingsw.events.ControllerEvents.NewPlayerEvent;
import it.polimi.ingsw.events.ControllerEvents.NewPlayerEventWithNetworkData;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class RequestsElaborator {
    public static final int QUEUE_SIZE = 10;

    private ClientHandlerSender clientHandlerSender;
    private ClientHandlerReceiver clientHandlerReceiver;
    private BlockingQueue<Event> requestsQueue;
    private VirtualView mainEventHandlerRegistry;
    private VirtualView matchEventHandlerRegistry;
    private String ownerUserID;

    public RequestsElaborator(Socket socket, VirtualView mainEventHandlerRegistry) {
        try {
            this.requestsQueue = new ArrayBlockingQueue<>(QUEUE_SIZE);
            this.clientHandlerSender = new ClientHandlerSender(socket.getOutputStream());
            this.clientHandlerReceiver = new ClientHandlerReceiver(socket.getInputStream(), requestsQueue);
            this.mainEventHandlerRegistry = mainEventHandlerRegistry;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("InfiniteLoopStatement")
    public void elaborateRequests(){
        new Thread(()->clientHandlerReceiver.waitForEvent()).start();

        while(true) {
            try {
                boolean done = false;
                while (!done) {
                    Event event = requestsQueue.take();
                    done = true;

                    if(event.getClass() == NewPlayerEvent.class)
                        event = new NewPlayerEventWithNetworkData((NewPlayerEvent) event, this);
                    else if(!ownerUserID.equals(event.getPlayerId()))
                        mainEventHandlerRegistry.sendEvent(new BadRequestEvent(event.getPlayerId(),
                                "The userID given is wrong", event));

                    if(MatchEvent.class.isAssignableFrom(event.getClass())) {
                        if (matchEventHandlerRegistry == null)
                            mainEventHandlerRegistry.sendEvent(new BadRequestEvent(event.getPlayerId(),
                                    "The match has not started yet", event));
                        else
                            matchEventHandlerRegistry.sendEvent(event);
                    }
                    else{
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

    public void setMatchEventHandlerRegistry(VirtualView matchEventHandlerRegistry) {
        this.matchEventHandlerRegistry = matchEventHandlerRegistry;
    }

    public void setOwnerUserID(String ownerUserID) {
        this.ownerUserID = ownerUserID;
    }
}
