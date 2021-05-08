package it.polimi.ingsw.virtualview;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.events.Event;
import it.polimi.ingsw.exceptions.IllegalOperation;
import it.polimi.ingsw.model.LeaderCards.Requirement;
import it.polimi.ingsw.utilities.GsonInheritanceAdapter;
import it.polimi.ingsw.utilities.MessageWrapper;

import java.io.OutputStream;
import java.io.PrintWriter;

public class ClientHandlerSender {
    PrintWriter printWriter;

    public ClientHandlerSender(OutputStream outputStream){
        printWriter = new PrintWriter(outputStream);
    }

    public void sendEvent(Event event){
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Requirement.class, new GsonInheritanceAdapter<Event>());
        Gson gson = builder.create();

        String eventJSON = gson.toJson(event, Event.class);

        try {
            printWriter.print(MessageWrapper.wrap(eventJSON));
        } catch (IllegalOperation illegalOperation) {
            System.out.println("The event to send contains the messages delimiters(\"" + MessageWrapper.MESSAGE_START +
                    "\" or \"" + MessageWrapper.MESSAGE_END + "\") somewhere");
        }
    }
}
