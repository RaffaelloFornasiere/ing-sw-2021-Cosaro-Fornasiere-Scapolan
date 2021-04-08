package it.polimi.ingsw.model;

import it.polimi.ingsw.model.DevCards.DevCard;
import it.polimi.ingsw.model.DevCards.DevCardGrid;

import java.util.ArrayList;
import java.util.HashMap;

public class MatchState {
    private int currentPlayerIndex;
    private boolean lastRound;
    private ArrayList<Player> players;
    private DevCardGrid devCardGrid;
    private Market market;

    //constructor from JSON?
    MatchState() {
        //get data from JSON
        ArrayList<DevCard> cards = new ArrayList<>(1);
        devCardGrid = new DevCardGrid(cards);

        HashMap<Marbel, Integer> marbels = new HashMap<Marbel, Integer>() {{
                    put(Marbel.WHITE, 4);
                    put(Marbel.BLUE, 2);
                    put(Marbel.GRAY, 2);
                    put(Marbel.YELLOW, 2);
                    put(Marbel.PURPLE, 2);
                    put(Marbel.RED, 1);
        }};
        market = new Market(3,4, marbels);

    }
}
