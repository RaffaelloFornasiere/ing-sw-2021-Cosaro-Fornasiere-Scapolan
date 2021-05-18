package it.polimi.ingsw.events.ClientEvents;

import it.polimi.ingsw.model.FaithTrack.PopeFavorCard;

import java.util.HashMap;

public class FaithTrackEvent extends ClientEvent{

    private int position;
    private HashMap<String, HashMap<Integer, PopeFavorCard>> popeFavorCards;

    public FaithTrackEvent(String playerId, int position, HashMap<String, HashMap<Integer, PopeFavorCard>> popeFavorCards) {
        super(playerId);
        this.position = position;
        this.popeFavorCards = new HashMap<>();
        for(String playerID: popeFavorCards.keySet())
            this.popeFavorCards.put(playerID, (HashMap<Integer, PopeFavorCard>) popeFavorCards.get(playerID).clone());
    }

    /**
     * Getter for the new position of the player identified by the PlayerID in this event
     * @return the new position of the player identified by the PlayerID in this event
     */
    public int getPosition() {
        return position;
    }

    /**
     * Getter for the pope favor cards acquired by each player
     * @return the pope favor cards acquired by each player
     */
    public HashMap<String, HashMap<Integer, PopeFavorCard>> getPopeFavorCards() {
        HashMap<String, HashMap<Integer, PopeFavorCard>> popeFavorCards = new HashMap<>();
        for(String playerID: this.popeFavorCards.keySet())
            popeFavorCards.put(playerID, (HashMap<Integer, PopeFavorCard>) this.popeFavorCards.get(playerID).clone());
        return popeFavorCards;
    }
}
