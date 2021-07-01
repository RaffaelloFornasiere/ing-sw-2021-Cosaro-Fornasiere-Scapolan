package it.polimi.ingsw.ui.gui;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class GUIFar extends GUI{
    public GUIFar(){
        playerID.setItem("far");
        leaderID.setItem("Raf");
        singlePlayer.setItem(false);
    }

    @Override
    public InetAddress askIP() {
        try {
            return InetAddress.getByName("192.168.0.233");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return null;
    }
}
