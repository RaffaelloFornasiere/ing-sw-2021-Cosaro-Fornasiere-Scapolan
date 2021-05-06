

package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.OutOfBoundException;
import it.polimi.ingsw.model.FaithTrack.FaithTrack;
import it.polimi.ingsw.model.FaithTrack.FaithTrackData;
import it.polimi.ingsw.model.MatchState;
import it.polimi.ingsw.model.Player;

import java.util.stream.IntStream;

public class FaithTrackManager {

    public static void incrementFaithTrackPosition(Player p, int n, MatchState matchState) {
        FaithTrackData ft= p.getDashBoard().getFaithTrackData();
        int beforePosition= ft.getPosition();
        try {
            ft.incrementPosition(n);
        } catch (OutOfBoundException e) {  }
        int afterPosition=ft.getPosition();
        IntStream.range(beforePosition, afterPosition+1).forEach(i -> {
            FaithTrack.getArrayOfCells().get(i).activateEffect(matchState);
        });
    }
}
