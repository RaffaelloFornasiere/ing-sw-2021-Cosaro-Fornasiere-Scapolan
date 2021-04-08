package it.polimi.ingsw.model.DevCards;

import it.polimi.ingsw.model.CardColor;

import java.util.ArrayList;

public class DevDeck {
    private ArrayList<DevCard> cards;
    int level;
    CardColor color;


    /**
     * construct the deck from a list of cards. check the coherence of the cards
     * @param cards cards to construct the deck
     */
    DevDeck(ArrayList<DevCard> cards) {
        level = cards.get(0).getLevel();
        color = cards.get(0).getColor();
        cards.forEach(x -> {
            if (x.getColor() != color || x.getLevel() != level)
                throw new IllegalStateException();
        });
        this.cards = cards;
    }

    /**
     * If the card respects the Decks level and color it is added to the deck
     *
     * @param card card to be added
     */
    public void push(DevCard card) {
        if (card.getLevel() != level || card.getColor() != color) ;
        //throw something
        cards.add(card);
    }

    /**
     * @param card removes the card passed by argument
     */
    public void remove(DevCard card) {
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