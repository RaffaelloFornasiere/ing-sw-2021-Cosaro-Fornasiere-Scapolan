package it.polimi.ingsw.virtualview;

import it.polimi.ingsw.controller.PreGameController;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MainServer{
    public static final int SERVER_PORT = 50885;
    private VirtualView eventHandlerRegistry;

    public MainServer(){
        eventHandlerRegistry = new VirtualView();
    }

    public VirtualView getEventHandlerRegistry() {
        return eventHandlerRegistry;
    }

    private void startServer() throws IOException{
        ServerSocket serverSocket = new ServerSocket(SERVER_PORT);
        //serverSocket.setSoTimeout(10*1000);
        while(true){
            Socket socket = serverSocket.accept();
            RequestsElaborator requestsElaborator = new RequestsElaborator(socket, eventHandlerRegistry);
            new Thread(requestsElaborator::elaborateRequests);
        }
    }

    public static void main(String[] args) {
        MainServer ms = new MainServer();
        PreGameController preGameController = new PreGameController(ms.getEventHandlerRegistry());
        try {
            ms.startServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}