package it.polimi.ingsw.controller.modelChangeHandlers;

import it.polimi.ingsw.events.ClientEvents.FaithTrackEvent;
import it.polimi.ingsw.model.FaithTrack.FaithTrackData;
import it.polimi.ingsw.model.FaithTrack.PopeFavorCard;
import it.polimi.ingsw.model.MatchState;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.virtualview.RequestsElaborator;

import java.util.HashMap;

public class FaithTrackDataHandler extends MatchObserver{

    private MatchState matchState;

    public FaithTrackDataHandler(HashMap<String, RequestsElaborator> networkData, MatchState matchState) {
        super(networkData);
        this.matchState = matchState;
    }

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
