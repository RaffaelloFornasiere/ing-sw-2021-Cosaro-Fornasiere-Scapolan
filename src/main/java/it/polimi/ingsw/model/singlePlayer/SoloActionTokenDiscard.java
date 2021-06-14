package it.polimi.ingsw.model.singlePlayer;

import it.polimi.ingsw.exceptions.NotPresentException;
import it.polimi.ingsw.model.CardColor;
import it.polimi.ingsw.model.DevCards.DevCardGrid;
import it.polimi.ingsw.model.DevCards.DevDeck;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Token representing the action to discard certain devCards from the grid
 */
public class SoloActionTokenDiscard extends SoloActionToken {
    private final HashMap<CardColor, Integer> cardsDiscarded;

    /**
     * Constructor for the class
     * @param cardsDiscarded The cards that will get discarded when the token is drawn
     */
    public SoloActionTokenDiscard(HashMap<CardColor, Integer> cardsDiscarded) {
        this.cardsDiscarded = new HashMap<>(cardsDiscarded);
    }

    /**
     * Getter for the cards that will get discarded when the token is drawn
     * @return The cards that will get discarded when the token is drawn
     */
    public HashMap<CardColor, Integer> getCardsDiscarded() {
        return new HashMap<>(cardsDiscarded);
    }

    @Override
    public boolean doAction(SinglePlayerMatchState singlePlayerMatchState) {
        DevCardGrid devCardGrid = singlePlayerMatchState.getDevCardGrid();
        DevDeck[][] decks = devCardGrid.getDecks();
        Stream<DevDeck> devDeckStream = Arrays.stream(decks).flatMap(Arrays::stream);
        for(CardColor color: cardsDiscarded.keySet()){
            int toDiscard = cardsDiscarded.get(color);
            List<DevDeck> devDecks = devDeckStream
                    .filter(x -> color == x.getColor())
                    .sorted(Comparator.comparingInt(DevDeck::getLevel)).collect(Collectors.toList());

            for(DevDeck devDeck: devDecks){
                int size = devDeck.size();
                if(toDiscard<size) {
                    for (int i = 0; i < toDiscard; i++) {
                        try {
                            devDeck.pop();
                        } catch (NotPresentException ignore) { }
                    }
                    return false;
                }
                else{
                    for (int i = 0; i < size; i++) {
                        try {
                            devDeck.pop();
                        } catch (NotPresentException ignore) {}
                    }
                    toDiscard -= size;
                }
            }
        }
        return true;
    }
}
