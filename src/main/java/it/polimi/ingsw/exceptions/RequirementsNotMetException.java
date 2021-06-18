package it.polimi.ingsw.exceptions;

/**
 * Checked exception thrown when the requirements for something are not met
 */
public class RequirementsNotMetException extends Exception {
    public RequirementsNotMetException(String s) {
        super(s);
    }
}
