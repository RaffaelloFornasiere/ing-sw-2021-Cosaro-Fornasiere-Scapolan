package it.polimi.ingsw.model.devCards;

import it.polimi.ingsw.exceptions.NotPresentException;
import it.polimi.ingsw.model.CardColor;

import java.util.ArrayList;


/**
 * Represents the a single deck in the development card grid
 */
public class DevDeck {
    private ArrayList<DevCard> cards;
    int level;
    CardColor color;


    @Override
    public boolean equals(Object o) {
        if (o.getClass() != getClass()) return false;
        if (this == o) return true;

        DevDeck devDeck = (DevDeck) o;

        int res = 1;

        res *= (devDeck.cards.equals(cards)) ? 1 : 0;
        res *= (devDeck.level == level) ? 1 : 0;
        res *= (devDeck.color.equals(color)) ? 1 : 0;
        return res == 1;
    }

    /**
     * construct the deck from a list of cards. check the coherence of the cards
     *
     * @param cards cards to construct the deck
     */
    public DevDeck(ArrayList<DevCard> cards) {
        if (cards.size() != 0) {
            level = cards.get(0).getLevel();
            color = cards.get(0).getColor();
            cards.forEach(x -> {
                if (x.getColor() != color)
                    throw new IllegalArgumentException("Card color does not match deck color");
                if (x.getLevel() != level)
                    throw new IllegalArgumentException("Card level does not match deck level");
            });
        }
        this.cards = cards;
    }

    /**
     * If the card respects the Decks level and color it is added to the deck
     *
     * @param card card to be added
     */
    public void push(DevCard card) {
        if (card.getColor() != color)
            throw new IllegalArgumentException("Card color does not match deck color");
        if (card.getLevel() != level)
            throw new IllegalArgumentException("Card level does not match deck level");

        cards.add(card);
    }

    /**
     * @return pops the card on the deck's top
     */
    public DevCard pop() throws NotPresentException {
        DevCard aux = topCard();
        cards.remove(aux);
        return aux;
    }

    /**
     * @return the card on the first position of the deck (
     * behaves as a LIFO stack)
     */
    public DevCard topCard() throws NotPresentException {
        if (cards.size() == 0) throw new NotPresentException("The deck is empty");
        return cards.get(cards.size() - 1);
    }


    /**
     * @return the size of the deck
     */
    public int size() {
        return cards.size();
    }


    /**
     * getter
     *
     * @return the level of the card
     */
    public int getLevel() {
        return level;
    }

    /**
     * getter
     *
     * @return the color of the card
     */
    public CardColor getColor() {
        return color;
    }

}