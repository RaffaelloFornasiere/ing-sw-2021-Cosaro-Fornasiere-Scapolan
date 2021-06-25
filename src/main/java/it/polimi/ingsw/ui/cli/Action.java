package it.polimi.ingsw.ui.cli;

public enum Action {
    TAKE_RESOURCES_FROM_MARKET("TAKE RESOURCES FROM MARKET"),
    BUY_DEVCARD("BUY A DEVELOPMENT CARD"),
    PRODUCE("ACTIVATE PRODUCTION"),
    LEADER_ACTION("LEADER ACTION"),
    SELECT_LEADER_CARD("SELECT/DESELECT LEADER CARD"),
    DISPLAY_SMTH("DISPLAY SOMETHING"),
    END_TURN("END TURN");


    Action(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    private final String description;
}
