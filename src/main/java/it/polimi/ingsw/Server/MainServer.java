package it.polimi.ingsw.Server;

import it.polimi.ingsw.controller.PreGameController;
import it.polimi.ingsw.controller.EventRegistry;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class MainServer{
    public static final int SERVER_PORT = 50885;
    private EventRegistry mainEventHandlerRegistry;

    public MainServer(){
        mainEventHandlerRegistry = new EventRegistry();
    }

    public EventRegistry getMainEventHandlerRegistry() {
        return mainEventHandlerRegistry;
    }

    @SuppressWarnings("InfiniteLoopStatement")
    public void startServer() throws IOException{
        ServerSocket serverSocket = new ServerSocket(SERVER_PORT);
        //serverSocket.setSoTimeout(10*1000);
        System.out.println("Beginning to accept clients on " + InetAddress.getLocalHost());
        while(true){
            Socket socket = serverSocket.accept();
            System.out.println("new client");
            RequestsElaborator requestsElaborator = new RequestsElaborator(socket, mainEventHandlerRegistry);

            new Thread(requestsElaborator::elaborateRequests).start();
        }
    }

    public static void main(String[] args) {
        MainServer ms = new MainServer();
        PreGameController preGameController = new PreGameController(ms.getMainEventHandlerRegistry());
        try {
            ms.startServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
