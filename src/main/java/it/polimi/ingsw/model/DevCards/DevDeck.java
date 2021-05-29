package it.polimi.ingsw.model.DevCards;

import it.polimi.ingsw.model.CardColor;
import it.polimi.ingsw.utilities.Observable;

import java.util.ArrayList;

public class DevDeck{
    private ArrayList<DevCard> cards;
    int level;
    CardColor color;


    /**
     * construct the deck from a list of cards. check the coherence of the cards
     * @param cards cards to construct the deck
     */
    DevDeck(ArrayList<DevCard> cards) {
        if (cards.size() != 0) {
            level = cards.get(0).getLevel();
            color = cards.get(0).getColor();
            cards.forEach(x -> {
                if (x.getColor() != color)
                    throw new IllegalArgumentException("Card color does not match deck color");
                if (x.getLevel() != level)
                    throw new IllegalArgumentException("Card level does not match deck level");
            });
            this.cards = cards;
        }
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
     * @param card removes the card passed by argument
     */
    private void remove(DevCard card) {
        cards.remove(card);
    }

    /**
     * @return pops the card on the deck's top
     */
    public DevCard pop() {
        DevCard aux = topCard();
        cards.remove(aux);
        return aux;
    }

    /**
     * @return the card on the first position of the deck (
     * behaves as a LIFO stack)
     */
    public DevCard topCard() {
        return cards.get(cards.size() - 1);
    }

    public int size(){
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