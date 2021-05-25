package it.polimi.ingsw.events.ClientEvents;

import it.polimi.ingsw.model.FaithTrack.PopeFavorCard;

import java.util.HashMap;

/**
 * Event signaling the change of the position of a player in the faith track
 */
public class FaithTrackEvent extends ClientEvent{

    private final int position;
    private final HashMap<String, HashMap<Integer, PopeFavorCard>> popeFavorCards;

    /**
     * Constructor for the class
     * @param playerId The ID of the player that changed position
     * @param position The new position of the player identified by the PlayerID in this event
     * @param popeFavorCards The pope favor cards acquired by each player
     */
    @SuppressWarnings("unchecked")
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
    @SuppressWarnings("unchecked")
    public HashMap<String, HashMap<Integer, PopeFavorCard>> getPopeFavorCards() {
        HashMap<String, HashMap<Integer, PopeFavorCard>> popeFavorCards = new HashMap<>();
        for(String playerID: this.popeFavorCards.keySet())
            popeFavorCards.put(playerID, (HashMap<Integer, PopeFavorCard>) this.popeFavorCards.get(playerID).clone());
        return popeFavorCards;
    }
}
