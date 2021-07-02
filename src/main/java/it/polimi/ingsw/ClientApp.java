package it.polimi.ingsw;

import it.polimi.ingsw.messageSenders.LocalSender;
import it.polimi.ingsw.client.NetworkAdapter;
import it.polimi.ingsw.messageSenders.Sender;
import it.polimi.ingsw.events.EventRegistry;
import it.polimi.ingsw.controller.PreGameController;
import it.polimi.ingsw.events.clientEvents.GameStartingEvent;
import it.polimi.ingsw.ui.UI;
import it.polimi.ingsw.ui.cli.CLI;
import it.polimi.ingsw.ui.gui.GUI;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class ClientApp {
    public static void main(String[] args) {
        //UI ui = new GUI();
        UI ui = selectUI();
        if (ui.askSingleplayer()) {
            startSingleplayer(ui);
        } else {
            NetworkAdapter networkAdapter = connectToServer(ui);
            ui.setClient(networkAdapter);

            networkAdapter.joinLobby();
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
        System.out.println("Write C for CLI, G for GUI");
        Scanner in = new Scanner(System.in);
        String input = in.nextLine().toUpperCase();
        while (!input.startsWith("C") && !input.startsWith("G")) {
            System.out.println("Insert a valid input");
            System.out.println("Write C for CLI, G for GUI");
            input = in.nextLine().toUpperCase();
        }
        if (input.startsWith("C"))
            return new CLI();
        else
            return new GUI();
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
