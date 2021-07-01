package it.polimi.ingsw.messageSenders;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.messageSenders.Sender;
import it.polimi.ingsw.events.Event;
import it.polimi.ingsw.exceptions.IllegalOperation;
import it.polimi.ingsw.utilities.GsonInheritanceAdapter;
import it.polimi.ingsw.utilities.MessageWrapper;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.NoSuchElementException;

public class NetworkHandlerSender implements Sender {
    PrintWriter printWriter;

    /**
     * Constructor for the class
     * @param outputStream the stream where to write the messages to send
     */
    public NetworkHandlerSender(OutputStream outputStream) {
        printWriter = new PrintWriter(outputStream);
    }

    @Override
    public void sendObject(Event event) {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Event.class, new GsonInheritanceAdapter<Event>());
        Gson gson = builder.create();

        String eventJSON = gson.toJson(event, Event.class);

        try {
            printWriter.println(MessageWrapper.wrap(eventJSON));
            printWriter.flush();
        } catch (IllegalOperation illegalOperation) {
            System.err.println("Impossible to send:");
            System.err.println(eventJSON);
            System.err.println("because it cannot be wrapped");
        }catch (NoSuchElementException | IllegalStateException e){
            System.err.println("Impossible to send because the stream was closed");
        }
    }

    @Override
    public void closeConnection() {
        printWriter.close();
    }
}
