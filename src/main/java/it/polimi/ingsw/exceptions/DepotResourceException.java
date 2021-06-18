package it.polimi.ingsw.exceptions;

/**
 * Checked exception thrown when there are problems with resources stored in a depot
 */
public class DepotResourceException extends Exception{

    public DepotResourceException(){super();}

    public DepotResourceException(String message){ super(message);}
}
