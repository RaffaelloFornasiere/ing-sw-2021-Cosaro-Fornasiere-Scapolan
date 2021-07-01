package it.polimi.ingsw.model.singlePlayer;

import it.polimi.ingsw.exceptions.NotPresentException;
import it.polimi.ingsw.model.CardColor;
import it.polimi.ingsw.model.devCards.DevCardGrid;
import it.polimi.ingsw.model.devCards.DevDeck;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
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
                    devCardGrid.notifyObservers();
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
        devCardGrid.notifyObservers();
        return true;
    }

    @Override
    public String description() {
        if(cardsDiscarded.isEmpty()) return "No card will be discarded";
        StringBuilder builder = new StringBuilder();

        builder.append("The following cards will be discarded:\n");
        for(CardColor color: cardsDiscarded.keySet()){
            int n = cardsDiscarded.get(color);
            if(n>0){
                builder.append(n);
                builder.append(" ");
                builder.append(color);
                builder.append("\n");
            }
        }
        builder.deleteCharAt(builder.lastIndexOf("\n"));

        return builder.toString();
    }
}
