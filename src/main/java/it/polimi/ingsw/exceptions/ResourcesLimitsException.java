package it.polimi.ingsw.exceptions;

/**
 * Checked exception thrown when an operation tries to exceed a limit on the number of resources
 */
public class ResourcesLimitsException extends Exception {
    public ResourcesLimitsException() {
        super();
    }

    public ResourcesLimitsException(String message) {
        super(message);
    }
}
