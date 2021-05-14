package it.polimi.ingsw.virtualview;

import it.polimi.ingsw.controller.PreGameController;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MainServer{
    public static final int SERVER_PORT = 50885;
    private VirtualView mainEventHandlerRegistry;

    public MainServer(){
        mainEventHandlerRegistry = new VirtualView();
    }

    public VirtualView getMainEventHandlerRegistry() {
        return mainEventHandlerRegistry;
    }

    @SuppressWarnings("InfiniteLoopStatement")
    private void startServer() throws IOException{
        ServerSocket serverSocket = new ServerSocket(SERVER_PORT);
        //serverSocket.setSoTimeout(10*1000);
        while(true){
            Socket socket = serverSocket.accept();
            System.out.println("new request");
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
