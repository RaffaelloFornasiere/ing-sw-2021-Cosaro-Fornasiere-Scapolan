package it.polimi.ingsw.virtualview;

import it.polimi.ingsw.events.Event;
import it.polimi.ingsw.events.NewPlayerEvent;
import it.polimi.ingsw.events.NewPlayerEventWithNetworkData;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class RequestsElaborator {
    public static final int QUEUE_SIZE = 10;

    private ClientHandlerSender clientHandlerSender;
    private ClientHandlerReceiver clientHandlerReceiver;
    private BlockingQueue<Event> requestsQueue;
    private VirtualView eventHandlerRegistry;

    public RequestsElaborator(Socket socket, VirtualView eventHandlerRegistry) {
        try {
            this.requestsQueue = new ArrayBlockingQueue<>(QUEUE_SIZE);
            this.clientHandlerSender = new ClientHandlerSender(socket.getOutputStream());
            this.clientHandlerReceiver = new ClientHandlerReceiver(socket.getInputStream(), requestsQueue);
            this.eventHandlerRegistry = eventHandlerRegistry;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void elaborateRequests(){
        new Thread(()->clientHandlerReceiver.waitForEvent());

        while(true) {
            try {
                boolean done = false;
                while (!done) {
                    Event event = requestsQueue.take();
                    done = true;
                    if(event.getClass() == NewPlayerEvent.class)
                        event = new NewPlayerEventWithNetworkData((NewPlayerEvent) event, this);
                    eventHandlerRegistry.sendEvent(event);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public ClientHandlerSender getClientHandlerSender() {
        return clientHandlerSender;
    }
}
