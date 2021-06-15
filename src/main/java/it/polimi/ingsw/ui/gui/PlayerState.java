package it.polimi.ingsw.ui.gui;

import it.polimi.ingsw.model.DevCards.DevCard;
import it.polimi.ingsw.model.Direction;
import it.polimi.ingsw.model.Marble;
import it.polimi.ingsw.utilities.LockWrap;
import it.polimi.ingsw.utilities.Pair;
import org.reflections.vfs.Vfs;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.locks.Lock;

public class PlayerState {

    HashMap<Marble, Integer> warehouse;
    HashMap<Marble, Integer> strongbox;


    ArrayList<ArrayList<String>> ownedCards;
    ArrayList<String> leaderCards;

    Integer victoryPoints;
    Integer faithTrackPoints;

    Marble[][] marketStatus;


    
    


}
