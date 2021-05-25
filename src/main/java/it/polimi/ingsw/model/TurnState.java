package it.polimi.ingsw.model;

public enum TurnState {
    WAITING_FOR_PLAYER(1),
    START(2),
    AFTER_LEADER_CARD_ACTION(3),
    AFTER_MAIN_ACTION(4),
    END_OF_TURN(5),
    WAITING_FOR_SOMETHING(6),
    MATCH_ENDED(7);

    TurnState(int state) { }
}
