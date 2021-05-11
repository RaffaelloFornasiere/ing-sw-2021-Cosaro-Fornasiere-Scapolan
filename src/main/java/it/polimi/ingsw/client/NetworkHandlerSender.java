package it.polimi.ingsw.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.model.LeaderCards.LeaderPower;
import it.polimi.ingsw.model.LeaderCards.Requirement;
import it.polimi.ingsw.utilities.GsonInheritanceAdapter;

import java.io.*;
import java.net.Socket;

public class NetworkHandlerSender {
    private BufferedWriter writer;

    public NetworkHandlerSender(Socket server) {
            if (server == null)
                throw new NullPointerException();
            synchronized (server){
            try {
                writer = new BufferedWriter(new OutputStreamWriter(server.getOutputStream()));
            } catch (IOException ignore) {
            }
        }
    }

    public void sendData(String data) throws IOException {
        synchronized (writer) {
            writer.write(data);
        };
    }

    public synchronized void sendObject(Object o)
    {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Requirement.class, new GsonInheritanceAdapter<Requirement>());
        builder.registerTypeAdapter(LeaderPower.class, new GsonInheritanceAdapter<LeaderPower>());
        Gson gson = builder.create();
        String data = gson.toJson(o);
        try {
            sendData(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
