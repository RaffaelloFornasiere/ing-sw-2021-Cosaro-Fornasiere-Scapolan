package it.polimi.ingsw.Server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import it.polimi.ingsw.events.Event;
import it.polimi.ingsw.utilities.GsonInheritanceAdapter;
import it.polimi.ingsw.utilities.MessageWrapper;

import java.io.InputStream;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;

public class ClientHandlerReceiver {
    Scanner scanner;
    BlockingQueue<Event> requestsQueue;

    public ClientHandlerReceiver(InputStream inputStream, BlockingQueue<Event> requestsQueue) {
        scanner = new Scanner(inputStream);
        this.requestsQueue = requestsQueue;
    }

    @SuppressWarnings("InfiniteLoopStatement")
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
                //TODO disconnect
                System.out.println("Client disconnected");
                break;
            }
        }
    }

    public void closeConnection() {
        scanner.close();
    }
}


