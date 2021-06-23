package it.polimi.ingsw.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.events.ControllerEvents.ControllerEvent;
import it.polimi.ingsw.events.Event;
import it.polimi.ingsw.events.HeartbeatEvent;
import it.polimi.ingsw.exceptions.IllegalOperation;
import it.polimi.ingsw.model.LeaderCards.LeaderPower;
import it.polimi.ingsw.model.LeaderCards.Requirement;
import it.polimi.ingsw.utilities.GsonInheritanceAdapter;
import it.polimi.ingsw.utilities.MessageWrapper;

import java.io.*;
import java.net.Socket;
import java.util.NoSuchElementException;

public class NetworkHandlerSender implements Sender{
    private PrintWriter writer;

    public NetworkHandlerSender(Socket server) {
            if (server == null)
                throw new NullPointerException();

            try {
                writer = new PrintWriter(new OutputStreamWriter(server.getOutputStream()));
            } catch (IOException ignore) {

        }
    }

    public void sendData(String data){
        try {
            writer.println(MessageWrapper.wrap(data));
            writer.flush();
        } catch (IllegalOperation illegalOperation) {
            System.err.println("Impossible to send:");
            System.err.println(data);
            System.err.println("because it cannot be wrapped");
        }catch (NoSuchElementException | IllegalStateException e){
            System.err.println("Impossible to send because the stream was closed");
        }
    }

    @Override
    public synchronized void sendObject(Event e)
    {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Event.class, new GsonInheritanceAdapter<Event>());
        Gson gson = builder.create();
        String data = gson.toJson(e, Event.class);

        if(e.getClass()!= HeartbeatEvent.class)
            System.out.println(data);
        sendData(data);
    }

    @Override
    public void closeConnection() {
        writer.close();
    }

}
