package it.polimi.ingsw.Server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.client.Sender;
import it.polimi.ingsw.events.Event;
import it.polimi.ingsw.exceptions.IllegalOperation;
import it.polimi.ingsw.utilities.GsonInheritanceAdapter;
import it.polimi.ingsw.utilities.MessageWrapper;

import java.io.OutputStream;
import java.io.PrintWriter;

public class ClientHandlerSender implements Sender {
    PrintWriter printWriter;

    public ClientHandlerSender(OutputStream outputStream) {
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
            illegalOperation.printStackTrace();
        }
    }

    public void closeConnection() {
        printWriter.close();
    }
}
