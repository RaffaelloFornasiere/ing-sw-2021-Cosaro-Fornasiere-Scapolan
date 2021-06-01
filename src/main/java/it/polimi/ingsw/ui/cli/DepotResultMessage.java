package it.polimi.ingsw.ui.cli;

public enum DepotResultMessage {
    SUCCESSFUL_LEADER("THE RESOURCES HAS BEEN ADDED SUCCESSFULLY\n TO THE EXTRA DEPOSIT OF LEADER CARD",true),
    REACH_MAX_CAP_LEADER("REACHED MAXIMUM CAPACITY OF POWER DEPOSIT",false),
    INVALID_RES_LEADER("WRONG DESTINATION: THIS CARD DOESN'T CONTAIN ANY DEPOSIT FOR THIS RESOURCES",false),
    SUCCESSFUL_DEPOT("THE RESOURCES HAS BEEN ADDED SUCCESSFULLY\n TO DEPOT", true),
    REACH_MAX_CAP_DEPOT("REACHED MAXIMUM CAPACITY OF DEPOT",false),
    INVALID_RES_DEPOT("WRONG DESTINATION: THIS DEPOT DOESN'T CONTAIN THIS KIND OF RESOURCES",false);



    private final String message;
    private final boolean successfull;

    DepotResultMessage(String  message, boolean successfull) {
        this.message=message;
        this.successfull=successfull;
    }


    public String getMessage() {
        return message;
    }

    public boolean getSuccessfull() {
        return successfull;
    }


}