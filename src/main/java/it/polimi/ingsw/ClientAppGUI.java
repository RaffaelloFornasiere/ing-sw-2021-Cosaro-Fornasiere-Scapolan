package it.polimi.ingsw;

import it.polimi.ingsw.Server.ClientHandlerSender;
import it.polimi.ingsw.client.LocalSender;
import it.polimi.ingsw.client.NetworkAdapter;
import it.polimi.ingsw.client.Sender;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.EventRegistry;
import it.polimi.ingsw.controller.FaithTrackManager;
import it.polimi.ingsw.controller.PreGameController;
import it.polimi.ingsw.events.ClientEvents.InitialChoicesEvent;
import it.polimi.ingsw.model.DashBoard;
import it.polimi.ingsw.model.FaithTrack.FaithTrack;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.singlePlayer.SinglePlayerMatchState;
import it.polimi.ingsw.ui.UI;
import it.polimi.ingsw.ui.cli.CLI;
import it.polimi.ingsw.ui.gui.GUI;
import it.polimi.ingsw.utilities.Config;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Hello world!
 *
 */
public class ClientAppGUI
{
    public static void main( String[] args )
    {
        //UI ui = new GUI();
        UI ui = selectUI();
        /*if(ui.askSingleplayer()){
            startSingleplayer(ui);
        }*/

            NetworkAdapter networkAdapter = connectToServer(ui);
            ui.setClient(networkAdapter);

            joinLobby(ui, networkAdapter);

    }

    /*private static void startSingleplayer(UI ui) {
        String playerID = ui.askUserID();
        EventRegistry toController = new EventRegistry();
        EventRegistry toPlayer = new EventRegistry();
        NetworkAdapter networkAdapter = new NetworkAdapter(ui, toController, toPlayer, playerID);
        ui.setClient(networkAdapter);
        HashMap<String, Sender> clientHandlerSender = new HashMap<>();
        clientHandlerSender.put(playerID, new LocalSender(toPlayer));

        PreGameController.setupMatch(new ArrayList<>(){{add(playerID);}}, clientHandlerSender, toController);
    }*/

    private static UI selectUI(){
        return new GUI();
    }

    public static void joinLobby(UI ui, NetworkAdapter networkAdapter) {
        String playerID;
        String leaderID;
        if(ui.askIfNewLobby()){
            playerID = ui.askUserID();
            networkAdapter.createMatch(playerID);
        }
        else{
            playerID = ui.askUserID();
            leaderID = ui.askLeaderID();
            networkAdapter.enterMatch(playerID, leaderID);
        }
    }

    private static NetworkAdapter connectToServer(UI ui) {
        NetworkAdapter networkAdapter = null;
        while(networkAdapter==null){
            InetAddress inetAddress = ui.askIP();
            try {
                networkAdapter = new NetworkAdapter(inetAddress, ui);
            } catch (IOException e) {
                ui.printError("Cannot connect to the server. Is it the right IP?");
            }
        }
        return networkAdapter;
    }
}
