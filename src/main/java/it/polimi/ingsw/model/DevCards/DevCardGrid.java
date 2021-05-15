package it.polimi.ingsw.model.DevCards;
import it.polimi.ingsw.model.CardColor;
import it.polimi.ingsw.utilities.Observable;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class DevCardGrid extends Observable {
    private DevDeck[][] decks;


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

    public DevDeck[][] getDecks() {
        DevDeck[][] returns =  new DevDeck[getColumnsNumber()][getRowsNumber()];
        for(int i=0; i<getColumnsNumber(); i++)
            if (getRowsNumber() >= 0) System.arraycopy(decks[i], 0, returns[i], 0, getRowsNumber());

        return returns;
    }

    public int getColumnsNumber(){
        return decks.length;
    }

    public int getRowsNumber(){
        return decks[0].length;
    }

    public void push(DevCard card, int column, int row) {
        if (column<0 || column>=getColumnsNumber())
            throw new IllegalArgumentException("Column index out of bounds");
        if (row<0 || row>getRowsNumber())
            throw new IllegalArgumentException("Row index out of bounds");

        decks[column][row].push(card);
        notifyObservers();
    }

    public DevCard pop(int column, int row) {
        if (column<0 || column>=getColumnsNumber())
            throw new IllegalArgumentException("Column index out of bounds");
        if (row<0 || row>getRowsNumber())
            throw new IllegalArgumentException("Row index out of bounds");

        DevCard ret = decks[column][row].pop();
        notifyObservers();
        return ret;
    }
}