package it.polimi.ingsw.model.DevCards;

import it.polimi.ingsw.exceptions.NotPresentException;
import it.polimi.ingsw.model.CardColor;
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
    public DevCardGrid(ArrayList<DevCard> cards) throws IllegalArgumentException {
        if (cards == null || cards.size() <= 0)
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
     *
     * @return the number of rows
     */
    public int getRowsNumber() {
        return decks.length;
    }


    /**
     * pushes a card on top the deck specified by row and column
     *
     * @param card   Card to be pushed
     * @param row    index of the row in the grid
     * @param column index of the column in the gird
     */
    public void push(DevCard card, int row, int column) {
        if (row < 0 || row > getRowsNumber())
            throw new IllegalArgumentException("Row index out of bounds");
        if (column < 0 || column >= getColumnsNumber())
            throw new IllegalArgumentException("Column index out of bounds");

        decks[row][column].push(card);
        notifyObservers();
    }

    /**
     * pushes a card on top the deck specified by row and column
     *
     * @param card    Card to be pushed
     * @param indexes pair containing the indices of the position in the grid
     */
    public void push(DevCard card, Pair<Integer, Integer> indexes) {
        push(card, indexes.getKey(), indexes.getValue());
    }

    /**
     * pops out the card on the top of the deck specified by row and column
     * After popping notifies all observers.
     *
     * @param row    row index in the grid
     * @param column column index in the grid
     * @return the card on top of the deck pointed
     * @throws NotPresentException id row or column are out of bound or if there is no card
     *                             in the deck pointed
     */
    public DevCard pop(int row, int column) throws NotPresentException {
        if (row < 0 || row > getRowsNumber())
            throw new IllegalArgumentException("Row index out of bounds");
        if (column < 0 || column >= getColumnsNumber())
            throw new IllegalArgumentException("Column index out of bounds");

        DevCard ret = decks[row][column].pop();
        notifyObservers();
        return ret;
    }

    /**
     * pops out the card on the top of the deck specified by row and column
     * After popping notifies all observers.
     *
     * @param indexes indexes pointing a deck in the grid
     * @return the card on top of the deck pointed
     * @throws NotPresentException id row or column are out of bound or if there is no card
     *                             in the deck pointed
     */
    public DevCard pop(Pair<Integer, Integer> indexes) throws NotPresentException {
        return pop(indexes.getKey(), indexes.getValue());
    }

    /**
     * Returns the card on the top of the deck specified by row and column
     *
     * @param row    row index in the grid
     * @param column column index in the grid
     * @return the card on top of the deck pointed
     * @throws NotPresentException id row or column are out of bound or if there is no card
     *                             in the deck pointed
     */
    public DevCard topCard(int row, int column) throws NotPresentException {
        if (row < 0 || row > getRowsNumber())
            throw new IllegalArgumentException("Row index out of bounds");
        if (column < 0 || column >= getColumnsNumber())
            throw new IllegalArgumentException("Column index out of bounds");

        return decks[row][column].topCard();
    }


    /**
     * Returns the card on the top of the deck specified by row and column
     *
     * @param indexes indexes pointing a deck in the grid
     * @return the card on top of the deck pointed
     * @throws NotPresentException id row or column are out of bound or if there is no card
     *                             in the deck pointed
     */
    public DevCard topCard(Pair<Integer, Integer> indexes) throws NotPresentException {
        return topCard(indexes.getKey(), indexes.getValue());
    }

    /**
     * Provides the row and the column indexes of the card identified by the string passed.
     * Searches in all decks of the grid
     *
     * @param devCardID card string identifier
     * @return the row and the column indexes of the card identified by the string passed.
     * @throws NotPresentException if there's no card at the top of any deck with the given ID
     */
    public Pair<Integer, Integer> getRowColOfCardFromID(String devCardID) throws NotPresentException {
        for (int i = 0; i < getRowsNumber(); i++) {
            for (int j = 0; j < getColumnsNumber(); j++) {
                try {
                    if (decks[i][j].topCard().getCardID().equals(devCardID))
                        return new Pair<>(i, j);
                } catch (NotPresentException ignored) {}
            }
        }
        throw new NotPresentException("There's no card at the top of any deck with the given ID");
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