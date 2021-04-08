package it.polimi.ingsw.model.DevCards;
import it.polimi.ingsw.model.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;


public class DevCardGrid {
    public DevDeck decks[][];

    DevCardGrid(ArrayList<DevCard> cards)
    {
        int levels = cards.stream().mapToInt(DevCard::getLevel).max().getAsInt();
        decks = new DevDeck[levels][CardColor.values().length];

        // not really safe if codes changes
        cards.forEach(x -> decks[x.getLevel()][x.getColor().getCode()].push(x));
    }

}