package it.polimi.ingsw.ui.gui;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class GUIRaf extends GUI {
    public GUIRaf(){
        playerID.setItem("Raf");
        leaderID.setItem("Raf");
    }

    @Override
    public InetAddress askIP() {
        try {
            return InetAddress.getByName("127.0.0.1");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return null;
    }
}
