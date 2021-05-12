package it.polimi.ingsw.ui;

import it.polimi.ingsw.model.LeaderCards.*;
import it.polimi.ingsw.model.Marble;

import java.util.ArrayList;
import java.util.HashMap;

public interface UI {
    void printMessage(String message);
    void printError(String error);

    HashMap<Marble, LeaderCard> getLeaderCardMarbleMatching(ArrayList<Marble> marbles, ArrayList<LeaderCard> leaderCards);
    ArrayList<ArrayList<Marble>> getWarehouseDisplacement(ArrayList<Marble> marbles);
    ArrayList<LeaderCard> useLeaderCardPowers(ArrayList<LeaderCard> leaderCards);


}
