

package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.OutOfBoundException;
import it.polimi.ingsw.model.FaithTrack.FaithTrack;
import it.polimi.ingsw.model.FaithTrack.FaithTrackData;
import it.polimi.ingsw.model.MatchState;
import it.polimi.ingsw.model.Player;

import java.util.stream.IntStream;

public class FaithTrackManager {

    private final MatchState matchState;

    public FaithTrackManager(MatchState matchState) {
        this.matchState = matchState;
    }

    /**
     * Increments of n steps the position of the given player
     *
     * @param p The player whose position gets incremented
     * @param n The number of steps forward
     */
    public void incrementFaithTrackPosition(Player p, int n) {
        FaithTrackData ft = p.getDashBoard().getFaithTrackData();
        int beforePosition = ft.getPosition();
        try {
            ft.incrementPosition(n);
        } catch (OutOfBoundException e) {
           System.out.println(p+" reached the end of faithTrack");
            try {
                ft.setPosition(FaithTrack.size()-1);
                IntStream.range(beforePosition, FaithTrack.size()).forEach(i -> FaithTrack.getArrayOfCells().get(i).activateEffect(this.matchState));
                this.matchState.setLastRound();
            } catch (OutOfBoundException outOfBoundException) {
                outOfBoundException.printStackTrace();
            }
        }
        int afterPosition = ft.getPosition();
        IntStream.range(beforePosition, afterPosition + 1).forEach(i -> FaithTrack.getArrayOfCells().get(i).activateEffect(this.matchState));
    }
}
