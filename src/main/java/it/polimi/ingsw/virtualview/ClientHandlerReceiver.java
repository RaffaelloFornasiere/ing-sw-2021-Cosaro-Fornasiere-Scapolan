package it.polimi.ingsw.virtualview;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.events.Event;
import it.polimi.ingsw.exceptions.IllegalOperation;
import it.polimi.ingsw.model.LeaderCards.Requirement;
import it.polimi.ingsw.utilities.GsonInheritanceAdapter;
import it.polimi.ingsw.utilities.MessageWrapper;

import java.io.InputStream;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;

public class ClientHandlerReceiver {
    Scanner scanner;
    BlockingQueue<Event> requestsQueue;

    public ClientHandlerReceiver(InputStream inputStream, BlockingQueue<Event> requestsQueue) {
        scanner = new Scanner(inputStream);
        this.requestsQueue = requestsQueue;
    }

    //TODO use a better read/write mechanism and check validity of arrivals and empty the buffer from junk
    @SuppressWarnings("InfiniteLoopStatement")
    public void waitForEvent(){
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Event.class, new GsonInheritanceAdapter<Event>());
        Gson gson = builder.create();


        while(true) {
            String eventJSON = MessageWrapper.unwrap(scanner.nextLine());
            /*
            String eventJSON = scanner.next(MessageWrapper.getScannerPattern());
             maybe needs a timeOut to check if messages sent are good
             System.out.println(eventJSON);
             try {
                 eventJSON = MessageWrapper.unwrap(eventJSON);
             } catch (IllegalOperation illegalOperation) {
                 illegalOperation.printStackTrace();
             }
            */
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
        }
    }
}
