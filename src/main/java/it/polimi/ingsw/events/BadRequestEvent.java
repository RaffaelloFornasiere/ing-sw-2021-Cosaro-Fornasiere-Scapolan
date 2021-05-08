package it.polimi.ingsw.events;

public class BadRequestEvent extends Event{
    private String description;
    private Event cause;

    /**
     * constructor for the class
     * @param playerID the player that generated(directly or indirectly) this event
     * @param description description of why the request is bad
     * @param cause the event that caused this
     */
    public BadRequestEvent(String playerID, String description, Event cause){
        super(playerID);
        this.description = description;
        this.cause = cause;
    }

    /**
     * getter for the description that explains why the request is bad
     * @return the description that explains why the request is bad
     */
    public String getDescription() {
        return description;
    }

    /**
     * getter for the event that caused the bad request
     * @return the event that caused the bad request
     */
    public Event getCause(){
        return cause;
    }
}
