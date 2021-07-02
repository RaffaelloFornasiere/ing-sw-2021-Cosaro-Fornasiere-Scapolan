package it.polimi.ingsw.ui.cli;

public enum DisplayOptions {
    DISPLAY_DASHBOARD("SOMEONE'S DASHBOARD", ""),
    DISPLAY_DEV_CARD_GRID("DEV_CARD GRID", "THIS IS THE DEVELOPMENT CARD GRID'S CURRENT STATE"),
    DISPLAY_LEADER_CARD("SOMEONE'S LEADER CARDS", ""),
    DISPLAY_FAITH_TRACK("FAITH_TRACK", "THIS IS THE CURRENT STATE OF FAITH TRACK"),
    DISPLAY_MARKET("MARKET", "THIS IS THE CURRENT STATE OF THE MARKET");

    DisplayOptions(String title, String message) {
        this.title = title;
        this.message = message;
    }

    private final String message;
    private final String title;

    public String getMessage() {
        return this.message;
    }

    public String getTitle() {
        return this.title;
    }

    public String personalizeOption(String playerName) {
        return this.message + playerName;
    }

}
