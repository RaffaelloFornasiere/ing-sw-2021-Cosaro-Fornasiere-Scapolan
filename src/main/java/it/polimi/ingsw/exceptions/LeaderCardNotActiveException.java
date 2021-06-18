package it.polimi.ingsw.exceptions;

/**
 * Checked exception thrown when an operation that can only be done on an active leader card is being done on one that isn't
 */
public class LeaderCardNotActiveException extends Throwable {
    public LeaderCardNotActiveException(String s) {
        super(s);
    }
}
