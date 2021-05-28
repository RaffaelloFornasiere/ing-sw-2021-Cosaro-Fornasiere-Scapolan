package it.polimi.ingsw.model.singlePlayer;

import it.polimi.ingsw.model.FaithTrack.FaithTrack;

import java.util.stream.IntStream;

public class SoloActionTokenMove extends SoloActionToken{
    int steps;
    boolean reshuffle;

    SoloActionTokenMove(int steps, boolean reshuffle)
    {
        this.steps = steps;
        this.reshuffle = reshuffle;
    }

    public int getSteps() {
        return steps;
    }

    public boolean reshuffle() {
        return reshuffle;
    }

    @Override
    public boolean doAction(SinglePlayerMatchState singlePlayerMatchState) {
        int beforePosition = singlePlayerMatchState.getLorenzoPosition();
        singlePlayerMatchState.incrementLorenzoPosition(steps);
        int afterPosition = singlePlayerMatchState.getLorenzoPosition();
        IntStream.range(beforePosition, afterPosition+1).forEach(i -> {
            FaithTrack.getArrayOfCells().get(i).activateEffect(singlePlayerMatchState);
        });
        if(reshuffle) singlePlayerMatchState.shuffleToken();
        return singlePlayerMatchState.getLorenzoPosition() >= FaithTrack.getArrayOfCells().size()-1;
    }
}
