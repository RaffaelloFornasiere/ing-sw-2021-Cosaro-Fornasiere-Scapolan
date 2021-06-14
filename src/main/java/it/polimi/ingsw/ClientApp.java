package it.polimi.ingsw;

import it.polimi.ingsw.client.NetworkAdapter;
import it.polimi.ingsw.ui.UI;
import it.polimi.ingsw.ui.cli.CLI;
import it.polimi.ingsw.ui.gui.GUI;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Hello world!
 *
 */
public class ClientApp
{
    public static void main( String[] args )
    {
        UI ui = new GUI();

        NetworkAdapter networkAdapter = connectToServer(ui);
        ui.setClient(networkAdapter);

        joinLobby(ui, networkAdapter);
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
