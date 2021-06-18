package it.polimi.ingsw.exceptions;

/**
 * Checked exception thrown when there are ptentila problems caused by the strongbox being empty
 */
public class EmptyStrongboxException extends Exception{
    public EmptyStrongboxException(){super();}
    public EmptyStrongboxException(String message){ super(message);}
}
