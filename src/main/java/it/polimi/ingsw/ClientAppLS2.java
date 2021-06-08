package it.polimi.ingsw;

import it.polimi.ingsw.client.NetworkAdapter;
import it.polimi.ingsw.ui.UI;
import it.polimi.ingsw.ui.cli.CLI;

import java.io.IOException;
import java.net.InetAddress;

public class ClientAppLS2 {
    public static void main( String[] args )
    {
        UI ui = new CLI();
        NetworkAdapter networkAdapter = connectToServer(ui);
        ui.setClient(networkAdapter);

        ui.setThisPlayer("Other");
        networkAdapter.enterMatch("Other", "Leader");
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
