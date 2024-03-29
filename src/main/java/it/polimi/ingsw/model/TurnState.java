package it.polimi.ingsw.model;

public enum TurnState {
    WAITING_FOR_PLAYER(1, "is waiting for another player"),
    START(2, "is starting the turn "),
    AFTER_LEADER_CARD_ACTION(3, "has just performed a leader card action, but can still do a main action"),
    AFTER_MAIN_ACTION(4, " has just done its action, but can still do the leader action"),
    END_OF_TURN(5, "has ended their turn"),
    WAITING_FOR_SOMETHING(6, "is waiting for something"),
    MATCH_ENDED(7, "has ended the match");

    TurnState(int state,String description) {
        stateCode=state;
        this.description= description;
    }

    private final String description;
    private final int stateCode;

    /**
     * Getter for the description of the turn state
     * @return The description of the turn state
     */
    public String getDescription(){
        return description;
    }

    /**
     * Getter for the code corresponding to the turn state
     * @return The code corresponding to the turn state
     */
    @SuppressWarnings("unused")
    public int getStateCode(){
        return stateCode;
    }
}
