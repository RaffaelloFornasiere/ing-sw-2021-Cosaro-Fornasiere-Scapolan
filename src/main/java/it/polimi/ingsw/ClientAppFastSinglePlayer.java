package it.polimi.ingsw;

import it.polimi.ingsw.client.LocalSender;
import it.polimi.ingsw.client.NetworkAdapter;
import it.polimi.ingsw.client.Sender;
import it.polimi.ingsw.controller.EventRegistry;
import it.polimi.ingsw.controller.PreGameController;
import it.polimi.ingsw.ui.UI;
import it.polimi.ingsw.ui.cli.CLI;

import java.util.ArrayList;
import java.util.HashMap;

public class ClientAppFastSinglePlayer {
    public static void main(String[] args) {
        UI ui = new CLI();
        String playerID = ui.askUserID();
        EventRegistry toController = new EventRegistry();
        EventRegistry toPlayer = new EventRegistry();
        NetworkAdapter networkAdapter = new NetworkAdapter(ui, toController, toPlayer, playerID);
        ui.setClient(networkAdapter);
        HashMap<String, Sender> clientHandlerSender = new HashMap<>();
        clientHandlerSender.put(playerID, new LocalSender(toPlayer));

        PreGameController.setupMatch(new ArrayList<>() {{
            add(playerID);
        }}, clientHandlerSender, toController);
    }
}
