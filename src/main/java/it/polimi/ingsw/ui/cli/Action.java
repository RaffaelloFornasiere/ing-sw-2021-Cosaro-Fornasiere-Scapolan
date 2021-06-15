package it.polimi.ingsw.ui.cli;

public enum Action {
    TAKE_RESOURCES_FROM_MARKET(2, "TAKE RESOURCES FROM MARKET"),
    BUY_DEVCARD(1, "BUY A DEVELOPMENT CARD"),
    PRODUCE(3, "ACTIVATE PRODUCTION"),
    LEADER_ACTION(4, "LEADER ACTION"),
    SELECT_LEADER_CARD(6, "SELECT/DESELECT LEADER CARD"),
    DISPLAY_SMTH(5, "DISPLAY SOMETHING");



    Action(int code, String description) {
        this.description = description;
        actionCode = code;
    }

    public String getDescription() {
        return description;
    }

    public int getActionCode() {
        return actionCode;
    }

    private String description;
    private int actionCode;
}
