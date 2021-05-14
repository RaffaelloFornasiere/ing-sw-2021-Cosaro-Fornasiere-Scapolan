package it.polimi.ingsw.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.events.ControllerEvents.ControllerEvent;
import it.polimi.ingsw.events.Event;
import it.polimi.ingsw.model.LeaderCards.LeaderPower;
import it.polimi.ingsw.model.LeaderCards.Requirement;
import it.polimi.ingsw.utilities.GsonInheritanceAdapter;

import java.io.*;
import java.net.Socket;

public class NetworkHandlerSender {
    private BufferedWriter writer;
    public static final String MESSAGE_START = "#-- BEGIN MESSAGE --#";
    public static final String MESSAGE_END = "#-- END MESSAGE --#";

    public NetworkHandlerSender(Socket server) {
            if (server == null)
                throw new NullPointerException();

            try {
                writer = new BufferedWriter(new OutputStreamWriter(server.getOutputStream()));
            } catch (IOException ignore) {

        }
    }

    public void sendData(String data) throws IOException {

            writer.write(data + "\n");
            writer.flush();

    }

    public synchronized void sendObject(Event e) throws IOException
    {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Event.class, new GsonInheritanceAdapter<Event>());
        Gson gson = builder.create();
        String data = gson.toJson(e, Event.class);

        System.out.println(data);
        sendData(data);
    }
}
