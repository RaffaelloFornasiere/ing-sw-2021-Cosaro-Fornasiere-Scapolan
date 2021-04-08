package it.polimi.ingsw.model.singlePlayer;

public class SoloActionTokenMove extends SoloActionToken{
    int steps;
    boolean reshuffle;

    SoloActionTokenMove(int steps, boolean reshuffle)
    {
        this.steps = steps;
        this.reshuffle = reshuffle;
    }

    /**
     * getter
     * @return
     */
    public int getSteps() {
        return steps;
    }

    /**
     * getter
     * @return
     */
    public boolean isReshuffle() {
        return reshuffle;
    }


}
