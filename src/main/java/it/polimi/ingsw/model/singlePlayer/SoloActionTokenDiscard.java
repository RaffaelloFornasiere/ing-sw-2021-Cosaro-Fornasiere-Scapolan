package it.polimi.ingsw.model.singlePlayer;

import it.polimi.ingsw.model.CardColor;

import java.util.HashMap;

public class SoloActionTokenDiscard extends SoloActionToken {
    private HashMap<CardColor, Integer> quantitites;

    SoloActionTokenDiscard(HashMap<CardColor, Integer> quantities)
    {
        this.quantitites = quantities;
    }
}
