package it.polimi.ingsw.exceptions;

/**
 * Checked exception thrown when multiple incompatible leader powers are used in some way
 */
public class IncompatiblePowersException extends Exception {
    public IncompatiblePowersException(String s) {
        super(s);
    }
}
