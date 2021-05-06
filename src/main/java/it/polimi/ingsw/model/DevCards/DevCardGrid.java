package it.polimi.ingsw.model.DevCards;
import it.polimi.ingsw.model.CardColor;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class DevCardGrid {
    public DevDeck decks[][];


    public DevCardGrid(ArrayList<DevCard> cards)
    {
        int levels = cards.stream().mapToInt(DevCard::getLevel).max().getAsInt();
        decks = new DevDeck[levels+1][CardColor.values().length];
        IntStream.range(0, levels+1).forEach(i -> {
            for(CardColor color:CardColor.values()){
                decks[i][color.getCode()]=
                        new DevDeck(cards.stream().filter(c->c.getLevel()==i && c.getColor()==color).collect(Collectors.toCollection(ArrayList::new)));
            }
        });
    }

    public int getColumnsNumber(){
        return decks.length;
    }

    public int getRowsNumber(){
        return decks[0].length;
    }

}