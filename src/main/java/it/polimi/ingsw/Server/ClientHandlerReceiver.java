package it.polimi.ingsw.Server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import it.polimi.ingsw.events.ControllerEvents.QuitGameEvent;
import it.polimi.ingsw.events.Event;
import it.polimi.ingsw.utilities.GsonInheritanceAdapter;
import it.polimi.ingsw.utilities.MessageWrapper;

import java.io.InputStream;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;

public class ClientHandlerReceiver {
    private final Scanner scanner;
    private final BlockingQueue<Event> requestsQueue;
    private String ownerUserID;

    public ClientHandlerReceiver(InputStream inputStream, BlockingQueue<Event> requestsQueue) {
        scanner = new Scanner(inputStream);
        this.requestsQueue = requestsQueue;
        ownerUserID = null;
    }

    public void setOwnerUserID(String ownerUserID) {
        this.ownerUserID = ownerUserID;
    }

    public void waitForEvent(){
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Event.class, new GsonInheritanceAdapter<Event>());
        Gson gson = builder.create();


        while(true) {
            String eventJSON = "";
            try {
            eventJSON = MessageWrapper.unwrap(scanner.nextLine());
            System.out.println("Received: "+ eventJSON);
                Event event = gson.fromJson(eventJSON, Event.class);
                boolean done = false;
                while (!done) {
                    try {
                        requestsQueue.put(event);
                        done = true;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } catch (JsonSyntaxException e) {
                System.err.println("Bad message received");
                System.err.println(eventJSON);
            }catch (NoSuchElementException | IllegalStateException e){
                if(ownerUserID ==null) {
                    System.out.println("Client disconnected");
                }
                else{
                    System.out.println("Client of " + ownerUserID + " disconnected");
                    try {
                        requestsQueue.put(new QuitGameEvent(ownerUserID));
                    } catch (InterruptedException interruptedException) {
                        interruptedException.printStackTrace();
                    }
                }
                break;
            }
        }
    }

    public void closeConnection() {
        scanner.close();
    }
}


