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

    public void waitForEvent(){
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Requirement.class, new GsonInheritanceAdapter<Event>());
        Gson gson = builder.create();

        while(true) {
            String eventJSON = scanner.next(MessageWrapper.getScannerPattern()); //maybe needs a timeOut to check if messages sent are good
            try {
                eventJSON = MessageWrapper.unwrap(eventJSON);
            } catch (IllegalOperation illegalOperation) {
                illegalOperation.printStackTrace();
            }

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
