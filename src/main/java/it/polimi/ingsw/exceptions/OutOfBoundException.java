package it.polimi.ingsw.exceptions;

/**
 * Checked exception thrown when the operation tries to go out of bounds of some data structure
 */
public class OutOfBoundException  extends Exception{
public OutOfBoundException(){super();}
public OutOfBoundException(String message){ super(message);}
}
