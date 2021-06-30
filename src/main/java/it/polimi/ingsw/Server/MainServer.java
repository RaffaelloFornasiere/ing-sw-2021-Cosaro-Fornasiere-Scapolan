package it.polimi.ingsw.Server;

import it.polimi.ingsw.controller.PreGameController;
import it.polimi.ingsw.controller.EventRegistry;
import it.polimi.ingsw.utilities.NetworkConfiguration;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * The class containing the logic to start the server
 */
public class MainServer{
    public static final int SERVER_PORT = NetworkConfiguration.getInstance().getPORT();
    private final EventRegistry mainEventHandlerRegistry;

    /**
     * Constructor for the class
     */
    public MainServer(){
        mainEventHandlerRegistry = new EventRegistry();
    }

    /**
     * Getter for the EventRegistry responsible of all the events unrelated to any match
     * @return The EventRegistry responsible of all the events unrelated to any match
     */
    public EventRegistry getMainEventHandlerRegistry() {
        return mainEventHandlerRegistry;
    }

    /**
     * Function to start the server
     * @throws IOException if the server cannot start
     */
    @SuppressWarnings("InfiniteLoopStatement")
    public void startServer() throws IOException{
        ServerSocket serverSocket = new ServerSocket(SERVER_PORT);
        System.out.println("Beginning to accept clients on " + InetAddress.getLocalHost());
        while(true){
            Socket socket = serverSocket.accept();
            socket.setSoTimeout(10*1000);
            System.out.println("New client");
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
            System.err.println("Unable to start the server");
            e.printStackTrace();
            System.err.println("Shutting down");
        }
    }

}
