package it.polimi.ingsw;

import it.polimi.ingsw.client.LocalSender;
import it.polimi.ingsw.client.NetworkAdapter;
import it.polimi.ingsw.client.Sender;
import it.polimi.ingsw.controller.EventRegistry;
import it.polimi.ingsw.controller.PreGameController;
import it.polimi.ingsw.events.ClientEvents.GameStartingEvent;
import it.polimi.ingsw.ui.UI;
import it.polimi.ingsw.ui.cli.CLI;
import it.polimi.ingsw.ui.gui.GUI;
import it.polimi.ingsw.ui.gui.GUIFar;
import it.polimi.ingsw.ui.gui.GUIRaf;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Hello world!
 */
public class ClientAppGUI2 {
    public static void main(String[] args) {
        //UI ui = new GUI();
        UI ui = selectUI();
        if (ui.askSingleplayer()) {
            startSingleplayer(ui);
        } else {
            NetworkAdapter networkAdapter = connectToServer(ui);
            ui.setClient(networkAdapter);

            joinLobby(ui, networkAdapter);
        }
    }

    private static void startSingleplayer(UI ui) {
        String playerID = ui.askUserID();
        EventRegistry toController = new EventRegistry();
        EventRegistry toPlayer = new EventRegistry();
        NetworkAdapter networkAdapter = new NetworkAdapter(ui, toController, toPlayer, playerID);
        ui.setClient(networkAdapter);
        HashMap<String, Sender> clientHandlerSender = new HashMap<>();
        clientHandlerSender.put(playerID, new LocalSender(toPlayer));

        ArrayList<String> playerArrayList =  new ArrayList<>();
        playerArrayList.add(playerID);

        toPlayer.sendEvent(new GameStartingEvent(playerID, playerArrayList));
        PreGameController.setupMatch(playerArrayList, clientHandlerSender, toController);
    }

    private static UI selectUI() {
            return new GUIFar();
    }

    public static void joinLobby(UI ui, NetworkAdapter networkAdapter) {
        String playerID;
        String leaderID;
        if (ui.askIfNewLobby()) {
            playerID = ui.askUserID();
            networkAdapter.createMatch(playerID);
        } else {
            playerID = ui.askUserID();
            leaderID = ui.askLeaderID();
            System.out.println(leaderID + " " + playerID);
            networkAdapter.enterMatch(playerID, leaderID);
        }
    }

    private static NetworkAdapter connectToServer(UI ui) {
        NetworkAdapter networkAdapter = null;
        while (networkAdapter == null) {
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
