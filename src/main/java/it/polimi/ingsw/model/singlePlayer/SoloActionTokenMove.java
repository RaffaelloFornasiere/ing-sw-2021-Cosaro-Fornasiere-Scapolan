package it.polimi.ingsw.model.singlePlayer;

import it.polimi.ingsw.model.faithTrack.FaithTrack;

import java.util.stream.IntStream;

/**
 * Token representing Lorenzo il Magnifico moving along the FaithTrack
 */
public class SoloActionTokenMove extends SoloActionToken{
    private final int steps;
    private final boolean reshuffle;

    /**
     * Constructor for the class
     * @param steps The amount Lorenzo il Magnifico will move after this token is drawn
     * @param reshuffle Whether the stack of tokens should be reshuffled when this one is drawn
     */
    public SoloActionTokenMove(int steps, boolean reshuffle) {
        this.steps = steps;
        this.reshuffle = reshuffle;
    }

    /**
     * Getter for the amount Lorenzo il Magnifico will move after this token is drawn
     * @return The amount Lorenzo il Magnifico will move after this token is drawn
     */
    public int getSteps() {
        return steps;
    }

    /**
     * @return Whether the stack of tokens should be reshuffled when this one is drawn
     */
    public boolean reshuffle() {
        return reshuffle;
    }

    @Override
    public boolean doAction(SinglePlayerMatchState singlePlayerMatchState) {
        int beforePosition = singlePlayerMatchState.getLorenzoPosition();
        singlePlayerMatchState.incrementLorenzoPosition(steps);
        int afterPosition = singlePlayerMatchState.getLorenzoPosition();
        IntStream.range(beforePosition, afterPosition+1).forEach(i -> FaithTrack.getArrayOfCells().get(i).activateEffect(singlePlayerMatchState));
        if(reshuffle) singlePlayerMatchState.shuffleToken();
        return singlePlayerMatchState.getLorenzoPosition() >= FaithTrack.getArrayOfCells().size()-1;
    }

    @Override
    public String description() {
        StringBuilder builder = new StringBuilder();
        if(steps!=0){
            builder.append("Lorenzo il Magnifico will move ");
            if (steps == 1) {
                builder.append(steps).append(" step forward");
            } else if (steps > 1) {
                builder.append(steps).append(" steps forward");
            } else if (steps == -1){
                builder.append(-steps).append(" step backwards");
            } else {
                builder.append(-steps).append(" steps backwards");
            }
        }
        if(reshuffle){
            if(!builder.isEmpty()) builder.append(". ");
            builder.append("The token pile will be reshuffled");
        }
        return builder.toString();
    }
}
