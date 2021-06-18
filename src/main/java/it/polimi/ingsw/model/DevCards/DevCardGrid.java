package it.polimi.ingsw.model.DevCards;

import com.google.gson.Gson;
import it.polimi.ingsw.exceptions.NotPresentException;
import it.polimi.ingsw.model.CardColor;
import it.polimi.ingsw.utilities.Config;
import it.polimi.ingsw.utilities.Observable;
import it.polimi.ingsw.utilities.Pair;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * represent the gird of all the cards. It is a wrapper of a 2D array
 * with some methods that helps the managing of the array for the
 * specific purpose
 */
public class DevCardGrid extends Observable {
    private DevDeck[][] decks;

    /**
     * constructor. Takes as input an array of different cards and
     * splits it into different decks with respect to the level and
     * the color of the cards.
     *
     * @param cards all the cards available for the game
     * @throws IllegalArgumentException if the array provided is null or void
     */
    public DevCardGrid(ArrayList<DevCard> cards) throws IllegalArgumentException{
        if(cards == null || cards.size() <= 0)
            throw new IllegalArgumentException("can construct grid from a null or a void array");
        int levels = cards.stream().mapToInt(DevCard::getLevel).max().getAsInt();
        decks = new DevDeck[levels][CardColor.values().length];

        IntStream.range(1, levels + 1).forEach(i -> {
            for (CardColor color : CardColor.values()) {
                decks[i - 1][color.getCode()] =
                        new DevDeck(cards.stream().filter(c -> c.getLevel() == i && c.getColor() == color).collect(Collectors.toCollection(ArrayList::new)));
            }
        });
    }

    /**
     * getter of the decks
     *
     * @return the decks in the grid
     */
    public DevDeck[][] getDecks() {
        DevDeck[][] returns = new DevDeck[getRowsNumber()][getColumnsNumber()];
        for (int i = 0; i < getRowsNumber(); i++)
            if (getColumnsNumber() >= 0) System.arraycopy(decks[i], 0, returns[i], 0, getColumnsNumber());

        return returns;
    }

    /**
     * getter of the number of the columns
     *
     * @return the number of the columns
     */
    public int getColumnsNumber() {
        return decks[0].length;
    }

    /**
     * getter of the number of rows
     * @return the number of rows
     */
    public int getRowsNumber() {
        return decks.length;
    }

    public void push(DevCard card, int row, int column) {
        if (row < 0 || row > getRowsNumber())
            throw new IllegalArgumentException("Row index out of bounds");
        if (column < 0 || column >= getColumnsNumber())
            throw new IllegalArgumentException("Column index out of bounds");

        decks[row][column].push(card);
        notifyObservers();
    }

    public void push(DevCard card, Pair<Integer, Integer> indexes) {
        push(card, indexes.getKey(), indexes.getValue());
    }

    public DevCard pop(int row, int column) throws NotPresentException {
        if (row < 0 || row > getRowsNumber())
            throw new IllegalArgumentException("Row index out of bounds");
        if (column < 0 || column >= getColumnsNumber())
            throw new IllegalArgumentException("Column index out of bounds");

        DevCard ret = decks[row][column].pop();
        notifyObservers();
        return ret;
    }

    public DevCard pop(Pair<Integer, Integer> indexes) throws NotPresentException {
        return pop(indexes.getKey(), indexes.getValue());
    }

    public DevCard topCard(int row, int column) throws NotPresentException {
        if (row < 0 || row > getRowsNumber())
            throw new IllegalArgumentException("Row index out of bounds");
        if (column < 0 || column >= getColumnsNumber())
            throw new IllegalArgumentException("Column index out of bounds");

        return decks[row][column].topCard();
    }

    public DevCard topCard(Pair<Integer, Integer> indexes) throws NotPresentException {
        return topCard(indexes.getKey(), indexes.getValue());
    }

    public Pair<Integer, Integer> getRowColOfCardFromID(String devCardID) throws NotPresentException {
        for (int i = 0; i < getRowsNumber(); i++) {
            for (int j = 0; j < getColumnsNumber(); j++) {
                if (decks[i][j].topCard().getCardID().equals(devCardID))
                    return new Pair<>(i, j);
            }
        }
        throw new NotPresentException("There's no leader card at the top of any deck with the given ID");
    }

    /*public static void main(String[] args) {
        ArrayList<DevCard> devCards = Config.getInstance().getDevCards();

        devCards.removeIf(x->x.getLevel()==2);

        DevCardGrid devCardGrid = new DevCardGrid(devCards);

        for(DevDeck[] devDeckArray: devCardGrid.getDecks()){
            for(DevDeck devDeck: devDeckArray){
                try {
                    System.out.println(new Gson().toJson(devDeck.topCard()));
                } catch (NotPresentException notPresentException) {
                    System.out.println("Not present");
                }
            }
        }
    }*/
}