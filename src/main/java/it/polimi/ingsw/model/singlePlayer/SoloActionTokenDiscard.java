package it.polimi.ingsw.model.singlePlayer;

import it.polimi.ingsw.model.CardColor;
import it.polimi.ingsw.model.DevCards.DevCardGrid;
import it.polimi.ingsw.model.DevCards.DevDeck;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SoloActionTokenDiscard extends SoloActionToken {
    private HashMap<CardColor, Integer> cardsDiscarded;

    public SoloActionTokenDiscard(HashMap<CardColor, Integer> cardsDiscarded)
    {
        this.cardsDiscarded = (HashMap<CardColor, Integer>) cardsDiscarded.clone();
    }

    public HashMap<CardColor, Integer> getCardsDiscarded() {
        return (HashMap<CardColor, Integer>) cardsDiscarded.clone();
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
                if(toDiscard<=size) {
                    for (int i = 0; i < toDiscard; i++) {
                        devDeck.pop();
                    }
                    return true;
                }
                else{
                    for (int i = 0; i < size; i++) {
                        devDeck.pop();
                    }
                    toDiscard -= size;
                }
            }
        }
        return false;
    }
}
