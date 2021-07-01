package it.polimi.ingsw.controller.modelChangeHandlers;

import it.polimi.ingsw.messageSenders.Sender;
import it.polimi.ingsw.events.ClientEvents.FaithTrackEvent;
import it.polimi.ingsw.model.FaithTrack.FaithTrackData;
import it.polimi.ingsw.model.FaithTrack.PopeFavorCard;
import it.polimi.ingsw.model.MatchState;
import it.polimi.ingsw.model.Player;

import java.util.HashMap;

/**
 * Observer for the FaithTrackData
 */
public class FaithTrackDataHandler extends MatchObserver{

    private final MatchState matchState;

    /**
     * Constructor for the class
     * @param networkData The Senders of all the player involved into the match
     * @param matchState The match state the FaithTrackData belongs to
     */
    public FaithTrackDataHandler(HashMap<String, Sender> networkData, MatchState matchState) {
        super(networkData);
        this.matchState = matchState;
    }

    /**
     * Sends to all the players the new position of a Player when they move, along with the pope favor card of everyone
     * @param o The FaithTrackData that changed
     */
    @Override
    public void update(Object o) {
        FaithTrackData faithTrackData = (FaithTrackData) o;

        Player movedPlayer = null;
        HashMap<String, HashMap<Integer, PopeFavorCard>> popeFavorCards = new HashMap<>();

        for(Player player: matchState.getPlayers()){
            if(player.getDashBoard().getFaithTrackData() == faithTrackData)
                movedPlayer = player;
            popeFavorCards.put(player.getPlayerId(), player.getDashBoard().getFaithTrackData().getAcquiredPopeFavorCards());
        }
        if(movedPlayer!=null)
            sendToAll(new FaithTrackEvent(movedPlayer.getPlayerId(), movedPlayer.getDashBoard().getFaithTrackData().getPosition(), popeFavorCards));
    }
}
